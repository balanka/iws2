package com.kabasoft.iws.client.modules


import com.kabasoft.iws.client.components.{LinePurchaseOrderList, PurchaseOrderList}
import com.kabasoft.iws.gui.AccordionPanel
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.SPACircuit

import scala.scalajs.js
import scalacss.ScalaCssReact._

object PURCHASEORDER {

  @inline private def bss = GlobalStyles.bootstrapStyles
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[PurchaseOrder[LinePurchaseOrder]] = None)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback {
        SPACircuit.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]()))
      }




    def edit(item:Option[PurchaseOrder[LinePurchaseOrder]]) = {
      val d =item.getOrElse(PurchaseOrder[LinePurchaseOrder]())

      //log.debug(s"purchaseOrder is xxxxxx ${d}")
      $.modState(s => s.copy(item = Some(d)))
      // $.modState(s => s.copy(item = s.item))
    }

    def updateOid(e: ReactEventI) = {
      val l =e.target.value.toLong
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
      log.debug(s"purchaseOrder is zzzzzzzzz"+currentValue)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = currentValue))))
    }


    def edited(order:PurchaseOrder[LinePurchaseOrder]) = {
      //$.modState(s => s.copy(item =Some(order)))
      $.props >>= (_.proxy.dispatch(Update(order)))
      //LinePurchaseOrderList.apply($.props.runNow().proxy, item, AddNewLine, editLine, saveLine, deleteLine)
      // $.modState(s => s.copy(item =Some(order)))
    }

    def saveLine(line:LinePurchaseOrder) = {
      log.debug(s"purchaseOrder is yyyyyyyyyy"+line)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      def cond(line1:LinePurchaseOrder, line2:LinePurchaseOrder ) =(line.tid == line2.tid)  &&(line1.created ==true)
      val k2 = k.replaceLine(k.getLines.filter(cond(_,line)).headOption.getOrElse(LinePurchaseOrder()), line.copy(transid = k.tid))
      log.debug(s"purchaseOrder k2 is ${k2} ")
      //$.modState(s => s.copy(item =Some(k2)))>> edited(k2)
      edited(k2)
      //val k3 = k.replaceLine(k.getLines.filter(cond(_,line)).headOption.getOrElse(LinePurchaseOrder()), line.copy(created = false))
      //log.debug(s"purchaseOrder K3 ${k3} ")
      //$.modState(s => s.copy(item =Some(k3)))

    }

    def delete(item:PurchaseOrder[LinePurchaseOrder]) = {
      // val s = $.state.runNow().item
      Callback.log("PurchaseOrder deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def deleteLine(line1:LinePurchaseOrder) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine(k.getLines.filter(_.tid == deleted.tid).headOption.getOrElse(LinePurchaseOrder()), deleted)
      edited(k2)
    }
    def AddNewLine(line:LinePurchaseOrder) = {
      val  created =line.copy(created = true)
      log.debug(s"New Line Purchase order before  edit>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()).tid)))))
      //editLine()
    }

    def filterWith(line:LinePurchaseOrder, search:String) =
      line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      Panel(Panel.Props("Purchase Order"), <.div(^.className := "panel-heading"),
        <.div(^.padding := 0,
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          AccordionPanel("Edit", buildForm(p, s), List(saveButton, newButton)),
          p.proxy().render( all  => AccordionPanel("Display", List(PurchaseOrderList(all.items.asInstanceOf[Seq[PurchaseOrder[LinePurchaseOrder]]],
            item => edit(Some(item)), item => p.proxy.dispatch(Delete(item))))))
        )
      )


    }

    def buildForm (p: Props, s:State): Seq[ReactElement] = {

      val porder = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]().add(LinePurchaseOrder(item = Some("4711"))))
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("id", s.item.map(_.id), "id"),
              buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildWItem[String]("store", s.item.map(_.store.getOrElse("store")), "store", updateStore),
              buildWItem[String]("account", s.item.map(_.account.getOrElse("account")), "account", updateAccount)
            )
          )
        ),
        LinePurchaseOrderList(porder, AddNewLine, saveLine, deleteLine)
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
