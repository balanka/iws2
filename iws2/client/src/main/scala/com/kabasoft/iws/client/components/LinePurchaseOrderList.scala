package com.kabasoft.iws.client.components

import com.kabasoft.iws.client.logger._
import com.kabasoft.iws.gui.AccordionPanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{Delete, GlobalStyles, Icon, Update}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared._
import diode.data.Pot
import diode.react.ModelProxy
import java.util.Date

import scalacss.ScalaCssReact._

object LinePurchaseOrderList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  //case class Persona(serviceName:String,serviceId:String,sentMessages:Option[List[String]]){def r( m:String) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[String])++List(m))); def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity) }
  //case class Persona[T](serviceName:String,serviceId:String,sentMessages:Option[List[T]]){def r( m:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T])++List(m))); def rr [T] (old:T, newr:T) = sentMessages.getOrElse(List.empty[T])++ sentMessages map replace((old, newr));  def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity) }
  //defined class Persona

  //case class Persona[T](serviceName:String,serviceId:String,sentMessages:Option[List[T]]){def r( m:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T])++List(m))); def rr [T] (old:T, newr:T) = sentMessages map replace((old, newr));  def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity); def m  (old:T, newr:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T]) map replace((old,newr)))) }
  //defined class Persona
  //scala> Persona("ABC","1",Some(List("1","2")))
 // res73: Persona[String] = Persona(ABC,1,Some(List(1, 2)))

  //scala> res73.m("1","3")
 // res74: Persona[String] = Persona(ABC,1,Some(List(3, 2)))
  //case class State(item: Option[LinePurchaseOrder]= None, porder: Option[PurchaseOrder[LinePurchaseOrder]]= None)
  case class State(item: Option[LinePurchaseOrder]= None)
  case class Props(proxy: ModelProxy[Pot[Data]], porder: PurchaseOrder[LinePurchaseOrder],
                   newLine:LinePurchaseOrder =>Callback,
                   editLine:LinePurchaseOrder =>Callback,
                   saveLine:LinePurchaseOrder =>Callback,
                   deleteLine:LinePurchaseOrder =>Callback)


  class Backend($: BackendScope[Props, State]) {

    def edit(line:LinePurchaseOrder, editLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" order to edit Line is ${line}")
       $.modState(s => s.copy(item = Some(line)))>> editLineCallback(line)
        //$.modState(s => s.copy(porder = Some(s.porder.getOrElse(PurchaseOrder[LinePurchaseOrder]()).add(line.getOrElse(LinePurchaseOrder())))))
        //$.modState(s => s.copy(porder = Some(order.)))>>
        // $.modState(s => s.copy(porder = Some(order.m(s.item.getOrElse(LinePurchaseOrder()), line.getOrElse(LinePurchaseOrder())))))
        //$.modState(s => s.copy(porder = Some(s.porder.getOrElse(PurchaseOrder[LinePurchaseOrder]()).m(s.item.getOrElse(LinePurchaseOrder()), line.getOrElse(LinePurchaseOrder())))))
        //Callback.log(s" Added  Line  to  Order  >>>>> " + $.state.logResult("XXX"))
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

    def delete(line:LinePurchaseOrder, deleteLineCallback:LinePurchaseOrder =>Callback) = {
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)
       // $.modState(s => s.copy(item = Some(line)))>> deleteLineCallback(line)
       //$.props >>= (_.proxy.dispatch(Delete(item)))
    }
    def save(line:LinePurchaseOrder, saveLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" saved  Line order is ${line}")
      //$.modState(s => s.copy(item = Some(line)))>> saveLineCallback(line)
      saveLineCallback(line)
    }


    def render(p:Props, s: State) = {
      val style = bss.listGroup
      val its  = p.porder.lines.getOrElse(Seq.empty[LinePurchaseOrder])
      def saveButton = Button(Button.Props(save(s.item.getOrElse(LinePurchaseOrder()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " SaveLine")
      def newButton = Button(Button.Props( p.newLine(LinePurchaseOrder()),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " NewLine")
      val editFormLine:Seq [TagMod]=List(
              buildWItem[String]("item", s.item.map(_.item.getOrElse("item")), "item", updateItem(_, s)),
              buildWItem[BigDecimal]("price", s.item.map(_.price), 0.0, updatePrice(_, s)),
              buildWItem[BigDecimal]("quantity", s.item.map(_.quantity), 0.0, updateQuantity),
              buildWItem[String]("unit", s.item.map(_.unit.getOrElse("unit")), "unit", updateUnit),
              buildWItem[String]("vat", s.item.map(_.vat.getOrElse("vat")), "19", updateVat),
              buildWItem[Date]("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(), updateDuedate),
              saveButton, newButton)

      <.div(bss.formGroup,
        <.ul(style.listGroup)(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=5.px, ^.maxHeight:=8.px, ^.visibility:=false,
               if(its.size>0) editFormLine else List(saveButton, newButton)
            )
          )
        )
      )
    }

    def renderItem(item:LinePurchaseOrder, p: Props) = {
      def editButton =  Button(Button.Props(edit(item, p.editLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
      val style = bss.listGroup
      <.li(style.itemOpt(CommonStyle.warning),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px,
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
 /*
      def renderHeader = {
        <.li(style.itemOpt(CommonStyle.warning))(
          <.span("  "),
          <.span("ID"),
          <.span(" "),
          <.span("Transid"),
          <.span("    "),
          <.span("item"),
          <.span("    "),
          <.span("Price"),
          <.span("    "),
          <.span("Quantity")

        )
      }

     */

  val component = ReactComponentB[Props]("LinePurchaseOrderList")
      .initialState(State())
      .renderBackend[Backend]
      //.componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]], porder:PurchaseOrder[LinePurchaseOrder], newLine:LinePurchaseOrder =>Callback,
             editLine:LinePurchaseOrder =>Callback, saveLine:LinePurchaseOrder =>Callback,
             deleteLine:LinePurchaseOrder => Callback) =  component(Props(proxy,porder, newLine, editLine, saveLine,deleteLine))

}
