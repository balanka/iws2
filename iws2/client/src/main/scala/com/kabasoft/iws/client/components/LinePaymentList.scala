package com.kabasoft.iws.client.components

import java.util.Date

import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.widok.moment.Moment

import scalacss.ScalaCssReact._

object LinePaymentList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  case class State(item: Option[LinePayment]= None,  search:String="", edit:Boolean = false)
  case class Props(porder: Payment[LinePayment],
                   newLine:LinePayment =>Callback,
                   saveLine:LinePayment =>Callback,
                   deleteLine:LinePayment =>Callback)


  class Backend($: BackendScope[Props, State]) {

    def mounted(props: Props) = Callback {
        IWSCircuit.dispatch(Refresh(Account()))
      }

    def edit(line:LinePayment) = $.modState(s => s.copy(item = Some(line)))

    def updateAccount(id:String) = {
      val accountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))>>
        setModfied
    }

    def updateOAccount(id:String) = {
      val oaccountId = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>>
        setModfied
    }

    def updateSide(e: ReactEventI) = {
      val sideId = e.target.checked
      //log.debug(s" sideId ${sideId}")
      $.modState(s => s.copy(item = s.item.map(_.copy(side = sideId)))).runNow() //>>
        setModfied
    }

    def updateDuedate(e: ReactEventI) = {
     // log.debug(s" Duedate is  mm${e.target.value}")
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

    def delete(line:LinePayment, deleteLineCB:LinePayment =>Callback) = deleteLineCB(line)
     // Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)
    def save(line:LinePayment, saveLineCB:LinePayment =>Callback) = {
      saveLineCB(line) >> $.modState(s => s.copy(item = None).copy(edit = false))
        //>> resetState
    }

    def postProcess (item:Payment[LinePayment]) =  Callback {
      val ro = item.getLines.filter(_.created ==true).map( e => item.replaceLine( e.copy(created = false).copy(modified =false)))
      val setLineID = ro.head.copy(lines = Some( item.getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
      // $.modState(s => s.copy(item = Some(setLineID)))
    }

  def resetState =   $.modState(s => s.copy(item = None).copy(edit = false))
    def setModfied = Callback {$.modState(s => s.copy(item = s.item.map(_.copy(modified = true)))).runNow() }
    def newLine(line:LinePayment, newLineCallback:LinePayment =>Callback) = {
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LinePayment])
      def items =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]

      def saveButton = Button(Button.Props(save(s.item.getOrElse(LinePayment()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LinePayment(created = true), p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list.filter(_.id != "-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))

      def editFormLine : Seq [TagMod] = List(
          <.div(^.cls :="row",
            buildSItemN("Account", itemsx = buildIdNameList(items), defValue = "0001", evt = updateAccount, "col-sm-3 col-xs-3"),
            buildBItem("D/C", s.item.map(_.side), true, evt = updateSide, "col-sm-1 col-xs-1" ),
            buildSItemN("OAccount", itemsx = buildIdNameList(items), defValue = "7", evt = updateOAccount, "col-sm-3 col-xs-3"),
            buildWItemN[BigDecimal]("Amount", s.item.map(_.amount), 0.0, updateAmount(_, s),"col-xs-2"),
            //buildDateN("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(), updateDuedate,"col-sm-3 col-xs-2"),
            buildField("Duedate", updateDuedate,"col-sm-3 col-xs-2"),
              saveButton, newButton),
          <.div(^.cls :="row", buildAreaItem("Text", s.item.map(_.text), "", updateText,"col-sm-3 col-xs-12"))
         )

      <.div(bss.formGroup,
        <.ul(style.listGroup)(renderHeader(LineFin_Trans_Headers))(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p, s))),
            if(its.size>0) editFormLine else List(saveButton, newButton)
     )
    }

    def renderItem(item:LinePayment, p: Props, s:State) = {
      def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
       val defaultL = LinePayment()
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
        <.span( Moment(item.duedate.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10.px, ^.paddingRight:=30.px),
        <.span(item.text, ^.paddingRight:=5.px),
        <.span(editButton,^.alignContent:="center")
      )
    }
  }

  val component = ReactComponentB[Props]("LinePaymentList")
      .initialState(State( search ="0"))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:Payment[LinePayment],
             newLine:LinePayment =>Callback,
             saveLine:LinePayment =>Callback,
             deleteLine:LinePayment => Callback) =
             component(Props(porder, newLine, saveLine, deleteLine))
}
