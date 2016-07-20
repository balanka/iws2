package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared._
import java.util.Date
import scalacss.ScalaCssReact._
import org.widok.moment.{Moment, _}

object LinePurchaseOrderList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  case class State(item: Option[LinePurchaseOrder]= None,  search:String="", vatList:Option[List[String]] = None)
  case class Props(porder: PurchaseOrder[LinePurchaseOrder],
                   newLine:LinePurchaseOrder =>Callback,
                   saveLine:LinePurchaseOrder =>Callback,
                   deleteLine:LinePurchaseOrder =>Callback)


  class Backend($: BackendScope[Props, State]) {

    def mounted(props: Props) = Callback {
        IWSCircuit.dispatch(Refresh(Article()))
        IWSCircuit.dispatch(Refresh(QuantityUnit()))
        IWSCircuit.dispatch(Refresh(Vat()))
      }

    def edit(line:LinePurchaseOrder) = {
      //log.debug(s" order to edit Line is ${line}")
       $.modState(s => s.copy(item = Some(line)))
    }
    def updateItem(id: String) = {
      val r =id.split(":")
      val itemId = r(0)
      val qttyUnitId = r(2)
      val  vatId = r(3)
      log.debug(s"ItemId Key is ${r}  itemid  ${itemId} qttyUnitId ${qttyUnitId} vatId ${vatId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(unit=Some(qttyUnitId)))))>>
        $.modState(s => s.copy(item = s.item.map(_.copy(vat=Some(vatId)))))>>
        $.modState(s => s.copy(item = s.item.map(_.copy(item = Some(itemId)))))>>setModfied
    }

    def updateVat(id: String) = {
      val vatId = id.substring(0, id.indexOf("|"))
      log.debug(s"ItemId Key is ${vatId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(vat = Some(vatId)))))>>
        setModfied
    }

    def updateUnit(id:String) = {
      val qtyUnit = id.substring(0, id.indexOf("|"))
      $.modState(s => s.copy(item = s.item.map(_.copy(unit = Some(qtyUnit)))))>>
        setModfied
    }

    def updateDuedate(e: ReactEventI) = {
      val l = e.target.value.toLong
      $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(new Date(l))))))>>
        setModfied
    }
    def updateText(e: ReactEventI) = {
      val l =e.target.value
      $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))>>
        setModfied
    }
    def updatePrice(e: ReactEventI, s1:State) = {
      val l =e.target.value.toDouble
      //log.debug(s"Item price is ${l} State:${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(price = l))))>>
      setModfied
    }
    def updateQuantity(e: ReactEventI) = {
      val l =e.target.value
      log.debug(s"Item quantity is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(quantity = BigDecimal(l.toDouble)))))>>
      setModfied
    }

    def delete(line:LinePurchaseOrder, deleteLineCallback:LinePurchaseOrder =>Callback) =
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)
    def save(line:LinePurchaseOrder, saveLineCB:LinePurchaseOrder =>Callback) = saveLineCB(line.copy(modified = true))

    def setModfied = Callback {$.modState(s => s.copy(item = s.item.map(_.copy(modified = true)))).runNow() }
    def newLine(line:LinePurchaseOrder, newLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" newLine called with   ${line}")
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LinePurchaseOrder])
      def items =  IWSCircuit.zoom(_.store.get.models.get(7)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Article]]
      def qttyUnit =  IWSCircuit.zoom(_.store.get.models.get(4)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[QuantityUnit]]
      def vat =  IWSCircuit.zoom(_.store.get.models.get(5)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Vat]]

      def saveButton = Button(Button.Props(save(s.item.getOrElse(LinePurchaseOrder()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LinePurchaseOrder( created = true),p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list map (iws =>(iws.id+"|"+iws.name))
      def buildArticleList [A<:Article](list: List[A]): List[String]= list map (iws =>(iws.id+":"+iws.name +":"+iws.qttyUnit  +":"+iws.vat.getOrElse("0")))

      def editFormLine : Seq [TagMod]=List(
          <.tr(
              buildSItem("item", itemsx = buildArticleList(items), defValue = "0001", evt = updateItem),
              buildSItem("q.unit", itemsx = buildIdNameList(qttyUnit), defValue = "KG", evt = updateUnit),
              buildSItem("Vat", itemsx = buildIdNameList(vat), defValue = "7", evt = updateVat) ),
            <.tr( bss.formGroup, ^.height := 10,
              buildWItem[BigDecimal]("price", s.item.map(_.price), 0.0, updatePrice(_, s)),
              buildWItem[BigDecimal]("quantity", s.item.map(_.quantity), 0.0, updateQuantity),
              buildWItem[String]("duedate", s.item.map( e =>(fmt(e.duedate.getOrElse(new Date())))),
                fmt(new Date()), updateDuedate), saveButton, newButton)
         )

      <.div(bss.formGroup,
        //<.ul(style.listGroup)(all.filter(p.predicate (_,s.search)).sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.ul(style.listGroup)(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p, s))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=30.px, ^.maxHeight:=30.px,
               if(its.size>0) editFormLine else List(saveButton, newButton)
             )
          )
        )
     )
    }

    def renderItem(item:LinePurchaseOrder, p: Props, s:State) = {
      def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
      val style = bss.listGroup
      <.li(style.itemOpt(CommonStyle.warning),
        ^.fontSize:=12.px, ^.fontWeight:=50.px,
        ^.maxHeight:=30.px, ^.height:=30.px,
        ^.alignContent:="center", ^.tableLayout:="fixed",
        <.span(deleteButton , ^.alignContent:="left"),
        <.span(item.id,^.paddingLeft:=10.px),
        <.span(item.item ,^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.price.bigDecimal),^.paddingLeft:=10.px),
        <.span("%6.2f".format(item.quantity.bigDecimal),^.paddingLeft:=10.px),
        <.span(item.unit ,^.paddingLeft:=10.px),
        <.span(item.vat ,^.paddingLeft:=10.px),
        <.span( Moment(item.duedate.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10),
        <.span(editButton,^.alignContent:="center")
      )
    }
  }

  val component = ReactComponentB[Props]("LinePurchaseOrderList")
      .initialState(State( search ="0"))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:PurchaseOrder[LinePurchaseOrder],
             newLine:LinePurchaseOrder =>Callback,
             saveLine:LinePurchaseOrder =>Callback,
             deleteLine:LinePurchaseOrder => Callback) =
             component(Props(porder, newLine, saveLine, deleteLine))
}
