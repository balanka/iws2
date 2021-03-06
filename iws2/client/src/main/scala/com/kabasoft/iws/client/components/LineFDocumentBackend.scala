package com.kabasoft.iws.client.components

import java.util.Date

//import com.kabasoft.iws.client.modules.Utilities.Props
import com.kabasoft.iws.client.modules.Utilities.{PropsL, StateL => State}
import com.kabasoft.iws.gui.BasePanel2
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared.{LineFDocument, _}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.widok.moment.Moment

import scalacss.ScalaCssReact._


class LineFDocumentBackend($: BackendScope[PropsL[FDocument[LineFDocument], LineFDocument],
  State[LineFDocument]]) extends  BackendLinex [FDocument[LineFDocument], LineFDocument, PropsL[FDocument[LineFDocument],LineFDocument], State[LineFDocument]] ($){

  @inline private def bss = GlobalStyles.bootstrapStyles


  def mounted(props: PropsL[FDocument[LineFDocument], LineFDocument]) = Callback {
    IWSCircuit.dispatch(Refresh(Account()))
  }

  def edit(line:LineFDocument) = $.modState(s => s.copy(item = Some(line)))

  def updateAccount(id:String) = {
    log.debug(s"Account  id is ${id}")
    val accountId = id.substring(0, id.indexOf("|"))
    log.debug(s"accountId   is ${accountId}")
    $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))>>
      setModified
  }

  def updateOAccount(id:String) = {
    log.debug(s"Account  id is ${id}")
    val oaccountId = id.substring(0, id.indexOf("|"))
    log.debug(s"accountId   is ${oaccountId}")
    $.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>> setModified
  }

  def updateSide(e: ReactEventI) = {
    e.preventDefault()
    val sideId = e.target.checked
    $.modState(s => s.copy(item = s.item.map(_.copy(side = sideId)))) >> setModified
  }

  def updateDuedate(e: ReactEventI) = {
    e.preventDefault()
    val l = e.target.value
    Moment.locale("de_DE")
    val m = Moment(l)
    val _date=m.toDate()
    val _helsenkiOffset = 2*60*60000 //maybe 3 [h*60*60000 = ms]
    val _userOffset = _date.getTimezoneOffset()*60000  // [min*60000 = ms]
    val _helsenkiTime = new Date((_date.getTime()+_helsenkiOffset+_userOffset).toLong)

    $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(_helsenkiTime)))))>> setModified
  }
  def updateText(e: ReactEventI) = {
    e.preventDefault()
    val l =e.target.value
    $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))>> setModified
  }

  def updateAmount(e: ReactEventI, s1:State[LineFDocument]) = {
    e.preventDefault()
    val l =e.target.value.toDouble
    $.modState(s => s.copy(item = s.item.map(_.copy(amount = l))))>> setModified
  }

  def delete(item:FDocument[LineFDocument],line:LineFDocument, deleteLineCallback:(FDocument[LineFDocument],LineFDocument) =>Callback) = deleteLineCallback(item,line)
  def save(item:FDocument[LineFDocument],line:LineFDocument, saveLineCB:(FDocument[LineFDocument],LineFDocument) =>Callback) = saveLineCB(item,line)   >> Callback {resetState }
  //def resetState =   $.modState(s => s.copy(item = None).copy(edit = false)).runNow()
  def resetState() =   $.modState(s => s.copy(item = s.item).copy(edit = false).copy(item =None)).runNow()
  def setModified  = $.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )
  def newLine(item:FDocument[LineFDocument], line:LineFDocument, newLineCallback:(FDocument[LineFDocument],LineFDocument )=>Callback) = newLineCallback(item,line)>> edit(line)


  def render(p:PropsL[FDocument[LineFDocument],LineFDocument], s: State[LineFDocument]) = {
    val lines  = p.porder.lines.getOrElse(Seq.empty[LineFDocument])
    def items =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]
    def saveButton = Button(Button.Props(save(p.porder, s.item.getOrElse(p.instance),p.saveLine),
      addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
    def newButton = Button(Button.Props(newLine(p.porder,p.instance, p.newLine),
      addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
    def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list.filter(_.id != "-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))

    def editLine : Seq [TagMod] = List(
      <.div(^.cls :="row",
        ComboBox("Account", itemsx = buildIdNameList(items), defValue = s.item.map(_.account.getOrElse("")).getOrElse(""), updateAccount _, "col-xs-3"),
        buildWItemN[Boolean]("D/C", s.item.map(_.side), true, evt = updateSide, "col-xs-1" ),
        ComboBox("OAccount", buildIdNameList(items),  defValue =s.item.map(_.oaccount.getOrElse("")).getOrElse(""), updateOAccount _, "col-xs-3"),
        buildWItemN[BigDecimal]("Amount", s.item.map(_.amount), 0.0, updateAmount(_, s),"col-xs-2"),
        buildField("Duedate", updateDuedate,"col-xs-2")),
      <.div(^.cls :="row",buildAreaItem("Text", s.item.map(_.text), "", updateText,"col-xs-12"))
    )

    renderBase(p, s, lines, saveButton _, newButton _, editLine _)
  }

  private def renderBase(p: PropsL[FDocument[LineFDocument], LineFDocument], s: State[LineFDocument], lines: Seq[LineFDocument],
                         savex: ()   => ReactComponentU[Button.Props, Unit, Unit, TopNode],
                         newx: ()    => ReactComponentU[Button.Props, Unit, Unit, TopNode],
                         editLine: () => Seq[TagMod]): ReactElement = {
    <.div(bss.formGroup,
      BasePanel2(renderHeader2(LineFin_Trans_Headers,List(savex(), newx()), 15), lines.sortBy(_.tid)(Ordering[Long].reverse) map (e => renderItem(e, p, s))),
      <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
        <.tbody(
          <.tr(bss.formGroup, ^.height := 30.px, ^.maxHeight := 30.px,
            if (lines.size > 0) editLine() else  Seq.empty[ReactElement]
          )
        )
      )
    )
  }

  def renderItem(item:LineFDocument, p: PropsL[FDocument[LineFDocument], LineFDocument], s:State[LineFDocument]) = {
    def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
      bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
    def deleteButton = Button(Button.Props(delete (p.porder,item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
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
