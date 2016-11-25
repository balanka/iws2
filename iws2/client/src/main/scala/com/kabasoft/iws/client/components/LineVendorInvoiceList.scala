package com.kabasoft.iws.client.components

import java.util.Date
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.widok.moment.Moment
import scalacss.ScalaCssReact._

object LineVendorInvoiceList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  case class State(item: Option[LineVendorInvoice]= None,  search:String="", edit:Boolean = false)
  case class Props(porder: VendorInvoice[LineVendorInvoice],
                   newLine:LineVendorInvoice =>Callback,
                   saveLine:LineVendorInvoice =>Callback,
                   deleteLine:LineVendorInvoice =>Callback)


  class Backend($: BackendScope[Props, State]) {

    def mounted(props: Props) = Callback {
        IWSCircuit.dispatch(Refresh(Account()))
      }

    def edit(line:LineVendorInvoice) = $.modState(s => s.copy(item = Some(line)))

    def updateAccount(id:String) = {
      val accountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))>>
        setModified
    }

    def updateOAccount(id:String) = {
      val oaccountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>>
        setModified
    }

    def updateSide(e: ReactEventI) = {
      e.preventDefault()
      val sideId = e.target.checked
      //log.debug(s" sideId ${sideId}")
      $.modState(s => s.copy(item = s.item.map(_.copy(side = sideId)))).runNow() //>>
        setModified
    }

    def updateDuedate(e: ReactEventI) = {
      e.preventDefault()
     // log.debug(s" Duedate is  mm${e.target.value}")
      val l = e.target.value
      Moment.locale("de_DE")
      val m = Moment(l)
      val _date=m.toDate()
      val _helsenkiOffset = 2*60*60000 //maybe 3 [h*60*60000 = ms]
      val _userOffset = _date.getTimezoneOffset()*60000  // [min*60000 = ms]
      val _helsenkiTime = new Date((_date.getTime()+_helsenkiOffset+_userOffset).toLong)

       $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(_helsenkiTime)))))>>
        setModified
    }
    def updateText(e: ReactEventI) = {
      e.preventDefault()
      val l =e.target.value
      $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))>>
        setModified
    }

    def updateAmount(e: ReactEventI, s1:State) = {
      e.preventDefault()
      val l =e.target.value.toDouble
      //log.debug(s"Item price is ${l} State:${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(amount = l))))>>
        setModified
    }

    def delete(line:LineVendorInvoice, deleteLineCallback:LineVendorInvoice =>Callback) = deleteLineCallback(line)
     // Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)
    def save(line:LineVendorInvoice, saveLineCB:LineVendorInvoice =>Callback) = {
      saveLineCB(line)   >> Callback {resetState }//>> $.modState(s => s.copy(item = None).copy(edit = false))

    }



   def resetState =   $.modState(s => s.copy(item = None).copy(edit = false)).runNow()
    //def setModfied = Callback {$.modState(s => s.copy(item = s.item.map(_.copy(modified = true)))).runNow() }
    def setModified  = $.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )

    def newLine(line:LineVendorInvoice, newLineCallback:LineVendorInvoice =>Callback) = {
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LineVendorInvoice])
      def items =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]

      def saveButton = Button(Button.Props(save(s.item.getOrElse(LineVendorInvoice()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LineVendorInvoice(created = true), p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list.filter(_.id != "-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))

      def editFormLine : Seq [TagMod] = List(
          <.div(^.cls :="row",
            buildSItemN("Account", itemsx = buildIdNameList(items), defValue = "0001", evt = updateAccount, "col-xs-3"),
            buildBItem("D/C", s.item.map(_.side), true, evt = updateSide, "col-xs-1" ),
            buildSItemN("OAccount", itemsx = buildIdNameList(items), defValue = "7", evt = updateOAccount, "col-xs-3"),
            buildWItemN[BigDecimal]("Amount", s.item.map(_.amount), 0.0, updateAmount(_, s),"col-xs-2"),
            buildDateN("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(), updateDuedate,"col-xs-2"),
              saveButton, newButton),
          <.div(^.cls :="row",buildAreaItem("Text", s.item.map(_.text), "", updateText,"col-xs-12"))
         )

      <.div(bss.formGroup,
        //<.ul(style.listGroup)(all.filter(p.predicate (_,s.search)).sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.ul(style.listGroup)(renderHeader(LineFin_Trans_Headers))(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p, s))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=30.px, ^.maxHeight:=30.px,
               if(its.size>0) editFormLine else List(saveButton, newButton)
             )
          )
        )
     )
    }

    def renderItem(item:LineVendorInvoice, p: Props, s:State) = {
      def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
       val defaultL = LinePurchaseOrder()
       val cond = item.tid==s.item.getOrElse(defaultL).tid
       val style = if(cond) bss.listGroup.itemOpt(CommonStyle.warning) else bss.listGroup.itemOpt(CommonStyle.success)

      <.li( style,
        ^.fontSize:=12.px, ^.fontWeight:=50.px, ^.maxHeight:=30.px, ^.height:=30.px, ^.alignContent:="center", ^.tableLayout:="fixed",
        <.span(deleteButton , ^.alignContent:="left"),
        <.span(item.id, ^.padding:=10.px),
        <.span(item.account, ^.padding:=10.px),
        <.input.checkbox(^.checked := item.side, ^.padding:=10.px),
        <.span(item.oaccount ,^.padding:=10.px),
        <.span("%06.2f".format(item.amount.bigDecimal),^.padding:=10.px),
        <.span( Moment(item.duedate.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10, ^.paddingRight:=30),
        <.span(item.text, ^.paddingRight:=5),
        <.span(editButton,^.alignContent:="center")
      )
    }
  }

  val component = ReactComponentB[Props]("LineVendorInvoiceList")
      .initialState(State( search ="0"))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:VendorInvoice[LineVendorInvoice],
             newLine:LineVendorInvoice =>Callback,
             saveLine:LineVendorInvoice =>Callback,
             deleteLine:LineVendorInvoice => Callback) =
             component(Props(porder, newLine, saveLine, deleteLine))
}
