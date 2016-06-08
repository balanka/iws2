package com.kabasoft.iws.client.modules


import com.kabasoft.iws.client.components.{LinePurchaseOrderList, PurchaseOrderList}
import com.kabasoft.iws.gui.AccordionPanel
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.client.logger._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._
import scalacss.ScalaCssReact._

object PURCHASEORDER {

  @inline private def bss = GlobalStyles.bootstrapStyles
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[PurchaseOrder[LinePurchaseOrder]] = None, line: Option[LinePurchaseOrder] = None)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh (PurchaseOrder[LinePurchaseOrder]())))

    def edit(item:Option[PurchaseOrder[LinePurchaseOrder]],proxy: ModelProxy[Pot[Data]]) = {
      val d =item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      log.debug(s" current purchaseOrder is ${d}")
      LinePurchaseOrderList.apply(proxy, d, AddNewLine, editLine, saveLine)
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(e: ReactEventI) = {
      val l =e.target.value.toLong //currentTarget.value.toLong
      log.debug(s"Oid is "+l)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
    }

    def updateStore(e: ReactEventI) = {
      val r = Some(e.target.value)
      log.debug(s"store is "+r)
      $.modState(s => s.copy(item = s.item.map(_.copy(store =r ))))
    }

    def updateAccount(e: ReactEventI) = {
      val currentValue = Some(e.target.value)
      log.debug(s"purchaseOrder is "+currentValue)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = currentValue))))
    }

    def edited(item:PurchaseOrder[LinePurchaseOrder]) = {
      Callback.log("Purchase order edited>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Update(item)))
       // $.props >>= ($.modState(s => s.copy(item = item)))
        //$.props >>= edit(Some(PurchaseOrder[LinePurchaseOrder]()))
    }
    def saveLine(line:LinePurchaseOrder) = {
      //getLines.filter(_.tid == 0)(0))

      Callback.log("Line Purchase order edited>>>>> " +line)  >>
        $.modState( s => s.copy(item = {  val k =s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]());
          log.debug(s"purchaseOrder is xzczzxxzxz ${k}  line0 ${k.getLines.filter(_.tid == 0L).headOption.getOrElse(LinePurchaseOrder(1111L))}");
          Some(k.m(k.getLines.filter(_.tid == 0L).headOption.getOrElse(LinePurchaseOrder()),line))})) >>
          $.modState( s => s.copy(item = { val k1 =s.item;log.debug(s"purchaseOrder is 234565 ${k1}"); edited(k1.getOrElse(PurchaseOrder[LinePurchaseOrder]())); k1}))

    }

    def editLine(lineOrder:LinePurchaseOrder) = {

        // $.props >>= (_.proxy.dispatch(Update(item)))

      //LinePurchaseOrderList.State.apply(Some(lineOrder))
      Callback.log("Purchase  Line p order edited>>>>> " +lineOrder)  >>
      $.modState(s => s.copy(line = Some(lineOrder)))
    }
    def delete(item:PurchaseOrder[LinePurchaseOrder]) = {
      Callback.log("PurchaseOrder deleted>>>>> " +item)  //>>
      $.props >>= (_.proxy.dispatch(Delete(item)))
    }
    def deleteLine(item:LinePurchaseOrder) = {
      Callback.log("LinePurchaseOrder deleted>>>>> " +item)  //>>
       $.props >>= (_.proxy.dispatch(Delete(item)))
    }
    def AddNewLine(line:LinePurchaseOrder) = {
       Callback.log(s"New Line Purchase order before  edit>>>>>  ${line}")  >>
        $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()).tid)))))>>
        Callback.log(s"RUNNING State>>>>>  ${ $.modState(s =>s)}")

    }

    def render(p: Props, s: State) = {
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]())),
                       addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(PurchaseOrder[LinePurchaseOrder]()),p.proxy),
                      addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      Panel(Panel.Props("Purchase Order"), <.div(^.className := "panel-heading"), <.div(^.padding := 0,
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(_ => AccordionPanel("Edit",  buildForm(p, s), List(saveButton, newButton))),
        p.proxy().render(all => AccordionPanel("Display", List(PurchaseOrderList(all.items.asInstanceOf[Seq[PurchaseOrder[LinePurchaseOrder]]],
          item => edit(Some(item),p.proxy), item => p.proxy.dispatch(Delete(item))))))))
    }

    def buildWItem[A](id:String , value:Option[A], defValue:A, evt:ReactEventI=> Callback) = {
      val m = value getOrElse defValue
      List(<.td(<.label(^.`for` := id, id)),
           <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
                ^.placeholder := id), ^.onChange ==> evt, ^.paddingLeft := 10))
    }

    def buildItem[A](id:String , value:Option[A], defValue:A) = {
      val m = value getOrElse  defValue
      List(<.td(<.label(^.`for` := id, id)),
           <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
                                     ^.placeholder := id), ^.paddingLeft := 1))
    }

      def buildForm (p: Props, s:State): Seq[ReactElement] = {
        List(<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(bss.formGroup,^.height := 20,
               // <.div(bss.formGroup,
                   buildItem[String]("id", s.item.map(_.id),"id"),
                   //buildIdItemString("id", s.item.map(_.id)),
                   //buildWItemLong("oid", s.item.map(_.oid),updateOid),
                   buildWItem[Long]("oid", s.item.map(_.oid), 1L,updateOid),
                   buildWItem[String]("store", s.item.map(_.store.getOrElse("store")),"store",updateStore),
                   buildWItem[String]("account", s.item.map(_.account.getOrElse("account")),"account",updateAccount)
                )
               )
            ),
          LinePurchaseOrderList(p.proxy, s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()), AddNewLine,editLine, saveLine)
        )
        )
      }
  }

  val component = ReactComponentB[Props]("PURCHASEORDER")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
