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
        IWSCircuit.dispatch(Refresh(Article()))
        IWSCircuit.dispatch(Refresh(QuantityUnit()))
        IWSCircuit.dispatch(Refresh(Vat()))
      }

    def edit(line:LineVendorInvoice) = {
      //log.debug(s" order to edit Line is ${line}")
      //$.modState(s => s.copy(item = Some(line)).copy(edit = true))
       $.modState(s => s.copy(item = Some(line)))
    }
//    def updateAccount(id: String) = {
//      val r =id.split(":")
//      val accountId = r(0)
//      //val qttyUnitId = r(2)
//     // val  vatId = r(3)
//      log.debug(s"ItemId Key is ${r}  itemid  ${accountId}  ")
//      //$.modState(s => s.copy(item = s.item.map(_.copy(unit=Some(qttyUnitId)))))>>
//      //  $.modState(s => s.copy(item = s.item.map(_.copy(vat=Some(vatId)))))>>
//        $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))>>setModfied
//    }
    def updateAccount(id:String) = {
      val oaccountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>>
        setModfied
    }

    def updateOAccount(id:String) = {
      val oaccountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>>
        setModfied
    }

    def updateSide(id:String) = {
      val sideId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(side = sideId))))>>
        setModfied
    }

    def updateDuedate(e: ReactEventI) = {
      log.debug(s" Duedate is  mm${e.target.value}")
      val l = e.target.value
      Moment.locale("de_DE")
      val m = Moment(l)
      val _date=m.toDate()
      val _helsenkiOffset = 2*60*60000 //maybe 3 [h*60*60000 = ms]
      val _userOffset = _date.getTimezoneOffset()*60000  // [min*60000 = ms]
      val _helsenkiTime = new Date((_date.getTime()+_helsenkiOffset+_userOffset).toLong)

       $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(_helsenkiTime)))))>>
        setModfied
    }
    def updateText(e: ReactEventI) = {
      val l =e.target.value
      $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))>>
        setModfied
    }


    def updateAmount(e: ReactEventI, s1:State) = {
      val l =e.target.value.toDouble
      //log.debug(s"Item price is ${l} State:${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(amount = l))))>>
      setModfied
    }


    def delete(line:LineVendorInvoice, deleteLineCallback:LineVendorInvoice =>Callback) =
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)
    def save(line:LineVendorInvoice, saveLineCB:LineVendorInvoice =>Callback) = {
      saveLineCB(line) >> $.modState(s => s.copy(item = None).copy(edit = false))
        //>> resetState
    }


    def postProcess (item:VendorInvoice[LineVendorInvoice]) =  Callback {
      val ro = item.getLines.filter(_.created ==true).map( e => item.replaceLine( e.copy(created = false).copy(modified =false)))
      val setLineID = ro.head.copy(lines = Some( item.getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
      // $.modState(s => s.copy(item = Some(setLineID)))
    }

  def resetState =   $.modState(s => s.copy(item = None).copy(edit = false))
    def setModfied = Callback {$.modState(s => s.copy(item = s.item.map(_.copy(modified = true)))).runNow() }
    def newLine(line:LineVendorInvoice, newLineCallback:LineVendorInvoice =>Callback) = {
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LineVendorInvoice])
      def items =  IWSCircuit.zoom(_.store.get.models.get(7)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]
      def qttyUnit =  IWSCircuit.zoom(_.store.get.models.get(4)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[QuantityUnit]]
      def vat =  IWSCircuit.zoom(_.store.get.models.get(5)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Vat]]

      def saveButton = Button(Button.Props(save(s.item.getOrElse(LineVendorInvoice()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LineVendorInvoice(created = true), p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list map (iws =>(iws.id+"|"+iws.name))
      //def buildArticleList [A<:Article](list: List[A]): List[String]= list map (iws =>(iws.id+":"+iws.name +":"+iws.qttyUnit  +":"+iws.vat.getOrElse("0")))

      def editFormLine : Seq [TagMod] = List(
          <.div(^.cls :="row",
            buildSItemN("Account", itemsx = buildIdNameList(items), defValue = "0001", evt = updateAccount, "col-xs-2"),
            buildSItemN("Side", List("S","H"), defValue = "S", evt = updateSide, "col-xs-2"),
            buildSItemN("OAccount", itemsx = buildIdNameList(items), defValue = "7", evt = updateOAccount, "col-xs-2"),
            buildWItemN[BigDecimal]("Amount", s.item.map(_.amount), 0.0, updateAmount(_, s),"col-xs-2"),

            buildDateN("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(), updateDuedate,"col-xs-2"),
            //buildWItemN[BigDecimal]("Quantity", s.item.map(_.quantity), 0.0, updateQuantity,"col-xs-2"),
              saveButton, newButton)
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
       val style = bss.listGroup
       val defaultL = LinePurchaseOrder()
       val cond = item.tid==s.item.getOrElse(defaultL).tid
       //log.debug(s"  Condition  ${cond}")
       val stylex = if(cond) style.itemOpt(CommonStyle.warning) else style.itemOpt(CommonStyle.success)

      <.li( stylex,
        ^.fontSize:=12.px, ^.fontWeight:=50.px, ^.maxHeight:=30.px, ^.height:=30.px, ^.alignContent:="center", ^.tableLayout:="fixed",
        <.span(deleteButton , ^.alignContent:="left"),
        <.span(item.id,^.paddingLeft:=10.px),
        <.span(item.account ,  ^.minWidth := 80, ^.padding:=30.px),
        <.span(item.side ,^.paddingLeft:=10.px),
        <.span(item.oaccount ,^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.amount.bigDecimal),^.paddingLeft:=10.px),
        <.span( Moment(item.duedate.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10),
        <.span(item.text ,^.paddingLeft:=10.px),
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
