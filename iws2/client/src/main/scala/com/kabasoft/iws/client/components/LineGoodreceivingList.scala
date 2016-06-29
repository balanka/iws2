package com.kabasoft.iws.client.components

import java.util.Date

import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.SPACircuit
import com.kabasoft.iws.shared.{Store => MStore, _}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object LineGoodreceivingList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  case class State(item: Option[LineGoodreceiving]= None,  search:String="")
  case class Props(porder: Goodreceiving[LineGoodreceiving],
                   newLine:LineGoodreceiving =>Callback,
                   saveLine:LineGoodreceiving =>Callback,
                   deleteLine:LineGoodreceiving =>Callback)


  class Backend($: BackendScope[Props, State]) {


    def mounted(props: Props) =
      Callback {
        SPACircuit.dispatch(Refresh(Article()))
        SPACircuit.dispatch(Refresh(QuantityUnit()))
        SPACircuit.dispatch(Refresh(Vat()))
      }
      // props.proxy1.dispatch(Refresh (Article()))>> props.proxy1.dispatch(Refresh (Account()))


    def edit(line:LineGoodreceiving) = {
      log.debug(s" order to edit Line is ${line}")
       $.modState(s => s.copy(item = Some(line)))
    }
    def updateItem1(id: String) = {
      val itemId = id.substring(0, id.indexOf("|"))
      log.debug(s"ItemId Key is ${itemId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(item = Some(itemId)))))
    }

    def updateVat1(id: String) = {
      val vatId = id.substring(0, id.indexOf("|"))
      log.debug(s"ItemId Key is ${vatId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(vat = Some(vatId)))))
    }

    def updateUnit1(id:String) = {
      val qtyUnit = id.substring(0, id.indexOf("|"))
      log.debug(s" Unit is ${qtyUnit}")
      $.modState(s => s.copy(item = s.item.map(_.copy(unit = Some(qtyUnit)))))
    }

    def updateDuedate(e: ReactEventI) = {
      val l =e.target.value.toLong
      $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(new Date(l))))))
    }
    def updateText(e: ReactEventI) = {
      val l =e.target.value
      $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))
    }
    def updatePrice(e: ReactEventI, s1:State) = {
      val l =e.target.value.toDouble
      log.debug(s"Item price is ${l} State:${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(price = l))))
    }
    def updateQuantity(e: ReactEventI) = {
      val l =e.target.value
      log.debug(s"Item quantity is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(quantity = BigDecimal(l)))))
    }

    def delete(line:LineGoodreceiving, deleteLineCallback:LineGoodreceiving =>Callback) =
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)

    def save(line:LineGoodreceiving, saveLineCallback:LineGoodreceiving =>Callback) = {
      //log.debug(s" saved  Line order is ${line}")
      saveLineCallback(line)>>$.modState(s => s.copy(item = Some(line)))
     // saveLineCallback(line)

    }
    def newLine(line:LineGoodreceiving, newLineCallback:LineGoodreceiving =>Callback) = {
      log.debug(s" newLine called with   ${line}")
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LineGoodreceiving])
      val items =  SPACircuit.zoom(_.store.get.models.get(7)).eval(SPACircuit.getRootModel).get.get.items.asInstanceOf[List[Article]]
      val qttyUnit =  SPACircuit.zoom(_.store.get.models.get(4)).eval(SPACircuit.getRootModel).get.get.items.asInstanceOf[List[QuantityUnit]]
      val vat =  SPACircuit.zoom(_.store.get.models.get(5)).eval(SPACircuit.getRootModel).get.get.items.asInstanceOf[List[Vat]]

      //log.debug(s" ARTICLESSSSS ${items} VAAAAAAAAT ${vat} QttyUnit ${qttyUnit}")
      def saveButton = Button(Button.Props(save(s.item.getOrElse(LineGoodreceiving()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LineGoodreceiving( created = true),p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+"|"+iws.name))
      def editFormLine : Seq [TagMod]=List(
              //buildWItem[String]("item", s.item.map(_.item.getOrElse("item")), "item", updateItem(_, s)),
              buildSItem("item", itemsx = buildIdNameList(items), defValue = "0001", evt = updateItem1),
              buildWItem[BigDecimal]("price", s.item.map(_.price), 0.0, updatePrice(_, s)),
              buildWItem[BigDecimal]("quantity", s.item.map(_.quantity), 0.0, updateQuantity),
              //buildWItem[String]("unit", s.item.map(_.unit.getOrElse("unit")), "unit", updateUnit),
              //buildWItem[String]("vat", s.item.map(_.vat.getOrElse("vat")), "19", updateVat),
              buildSItem("q.unit", itemsx = buildIdNameList(qttyUnit), defValue = "KG", evt = updateUnit1),
              buildSItem("Vat", itemsx = buildIdNameList(vat), defValue = "7", evt = updateVat1),
              buildWItem[Date]("duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(),
                updateDuedate), saveButton, newButton)
      <.div(bss.formGroup,
        //<.ul(style.listGroup)(all.filter(p.predicate (_,s.search)).sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.ul(style.listGroup)(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=30.px, ^.maxHeight:=30.px,
               if(its.size>0) editFormLine else List(saveButton, newButton)
             )
          )
        )
     )
    }

    def renderItem(item:LineGoodreceiving, p: Props) = {
      def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
      val style = bss.listGroup
      <.li(style.itemOpt(CommonStyle.warning), ^.fontSize:=12.px, ^.fontWeight:=50.px, ^.maxHeight:=30.px,
                                               ^.height:=30.px, ^.alignContent:="center", ^.tableLayout:="fixed",
        <.span(item.id),
        <.span(item.item ,^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.price.bigDecimal),^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.quantity.bigDecimal),^.paddingLeft:=10.px),
        <.span(item.unit ,^.paddingLeft:=10.px),
        <.span(item.vat ,^.paddingLeft:=10.px),
        <.span(editButton, deleteButton,^.alignContent:="center")
      )
    }
  }

  val component = ReactComponentB[Props]("LinePurchaseOrderList")
      .initialState(State( search ="0"))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:Goodreceiving[LineGoodreceiving],
             newLine:LineGoodreceiving =>Callback,
             saveLine:LineGoodreceiving =>Callback,
             deleteLine:LineGoodreceiving => Callback) =
             component(Props(porder, newLine, saveLine, deleteLine))
}
