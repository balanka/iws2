package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.{AjaxClient, SPACircuit}
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared._
import diode.data.{Pot, Ready}
import diode.react.ModelProxy
import java.util.Date

import autowire._
import com.kabasoft.iws.shared.{Store => MStore, _}

import scala.util.{Failure, Success}
import scalacss.ScalaCssReact._

object LinePurchaseOrderList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  case class State(item: Option[LinePurchaseOrder]= None, articles:Option[List[String]] =None, search:String="")
  case class Props(proxy1: ModelProxy[Pot[Data]], items:List[Article], porder: PurchaseOrder[LinePurchaseOrder],
                   newLine:LinePurchaseOrder =>Callback,
                   saveLine:LinePurchaseOrder =>Callback,
                   deleteLine:LinePurchaseOrder =>Callback)


  class Backend($: BackendScope[Props, State]) {


    def mounted(props: Props) = {
//      IWSCircuit.connect(IWSCircuit.zoom(_.data.get.items.filter(_.modelId == Article().modelId)))
//      val xx =IWSCircuit.zoom(_.data.get.items.filter(_.modelId == Article().modelId))
//      log.debug(s"Dispatching Refresh >>>>>>>>>>>>>>>>>>>>>>>> ${xx}")
//      val ar = Article()
//      val zz =IWSCircuit.zoom(_.data.get.items.filter(_.modelId == Article().modelId))
//      log.debug(s"Dispatching Refresh >>>>>>>>>>>>>>>>>>>>>>>> ${zz}")
//      val proxy = props.proxy1()
//      log.debug(s"KEY pressed >>>>>>>>>>>>>>>>>>>>>>>> ${proxy.isPending}  == ${proxy.isReady} +++${proxy.isStale} ---${proxy.isFailed} ****${proxy.isUnavailable}")
//      Callback.log(s"KEY pressed >>>>>>>>>>>>>>>>>>>>>>>> ${proxy.get.items}")
//
      // Callback.log(s"KEY pressed12345 >>>>>>>>>>>>>>>>>>>>>>>> ${props.items}")
       props.proxy1.dispatch(Refresh (Article()))
    }
     // .componentWillMount(scope => scope.backend.willMount)

    def edit(line:LinePurchaseOrder) = {
      log.debug(s" order to edit Line is ${line}")
       $.modState(s => s.copy(item = Some(line)))
    }

    def updateItem1(l: String) = {
     // def updateItem1(e: ReactEventI) = {
     // val l =Some(e.target.value)
      log.debug(s"ItemId Key is ${l}  ")
      //Callback.log(s"KEY pressed >>>>>>>>>>>>>>>>>>>>>>>> ${l}")>>
      $.modState(s => s.copy(item = s.item.map(_.copy(item = Some(l))))) //>>$.modState(s => s.copy(search =s.search+l))
      //$.modState(s => s.copy(item =  s.item))
    }

    def updateItem(e: ReactEventI, s1:State) = {
      val l =Some(e.target.value)
     log.debug(s"ItemId is ${l}  State: ${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(item = l))))
      //$.modState(s => s.copy(porder = Some(order)))>>
    }
    def updateUnit(e: ReactEventI) = {
      val l =e.target.value
       log.debug(s" Unit is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(unit = Some(l)))))
    }
    def updateVat(e: ReactEventI) = {
      val l =e.target.value
       log.debug(s"Vat is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(vat = Some(l)))))
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

    def delete(line:LinePurchaseOrder, deleteLineCallback:LinePurchaseOrder =>Callback) =
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)

    def save(line:LinePurchaseOrder, saveLineCallback:LinePurchaseOrder =>Callback) = {
      //log.debug(s" saved  Line order is ${line}")
      //$.modState(s => s.copy(item = Some(line)))>> saveLineCallback(line)
      saveLineCallback(line)

    }
    def newLine(line:LinePurchaseOrder, newLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" newLine called with   ${line}")
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LinePurchaseOrder])
      log.debug(s" KKKKKKKKKKKKKKKKKK ${p.items}")

      def saveButton = Button(Button.Props(save(s.item.getOrElse(LinePurchaseOrder()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
      def newButton = Button(Button.Props( newLine(LinePurchaseOrder( created = true),p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
      def buildIdNameList (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+" "+iws.name))
      def editFormLine (items:List[Masterfile]):Seq [TagMod]=List(
              //buildWItem[String]("item", s.item.map(_.item.getOrElse("item")), "item", updateItem(_, s)),
              buildSItem[String]("item", itemsx = buildIdNameList(items), defValue = "0001", evt = updateItem1),
              //ArticleList2( p.items, updateItem1),
              buildWItem[BigDecimal]("price", s.item.map(_.price), 0.0, updatePrice(_, s)),
              buildWItem[BigDecimal]("quantity", s.item.map(_.quantity), 0.0, updateQuantity),
              buildWItem[String]("unit", s.item.map(_.unit.getOrElse("unit")), "unit", updateUnit),
              buildWItem[String]("vat", s.item.map(_.vat.getOrElse("vat")), "19", updateVat),
              buildWItem[Date]("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(),
                updateDuedate), saveButton, newButton)
      <.div(bss.formGroup,
        //<.ul(style.listGroup)(all.filter(p.predicate (_,s.search)).sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.ul(style.listGroup)(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=5.px, ^.maxHeight:=8.px,
               if(its.size>0) editFormLine(p.items) else List(saveButton, newButton)
             )
          )
        )
     )
    }

    def renderItem(item:LinePurchaseOrder, p: Props) = {
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

  def apply( proxy1: ModelProxy[Pot[Data]],
             items:List[Article],
             porder:PurchaseOrder[LinePurchaseOrder],
             newLine:LinePurchaseOrder =>Callback,
             saveLine:LinePurchaseOrder =>Callback,
             deleteLine:LinePurchaseOrder => Callback) =
             component(Props(proxy1, items, porder, newLine, saveLine, deleteLine))
}
