package com.kabasoft.iws.client.modules


import com.kabasoft.iws.client.components.{LinePurchaseOrderList, PurchaseOrderList}
import com.kabasoft.iws.gui.AccordionPanel
import diode.react.ReactPot._
import diode.react._
import diode.data.{Pot, Ready}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit

import scala.scalajs.js
import scalacss.ScalaCssReact._

object PURCHASEORDER {

  @inline private def bss = GlobalStyles.bootstrapStyles
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[PurchaseOrder[LinePurchaseOrder]] = None)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback {
         IWSCircuit.dispatch(Refresh(Store()))
         IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
        IWSCircuit.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]()))
    }


    def edit(item:Option[PurchaseOrder[LinePurchaseOrder]]) = {
      val d =item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      //log.debug(s"purchaseOrder is xxxxxx ${d}")
      Callback {
        $.modState(s => s.copy(item = Some(d))).runNow()
      }
    }

    def updateOid(e: ReactEventI) = {
      val l =e.target.value.toLong
      log.debug(s"Oid is "+l)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
    }

    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
      log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
    }

    def updateSupplier(idx:String) = {
      val accountId =  idx.substring(0, idx.indexOf("|"))
      log.debug(s"accountId is "+accountId)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))
    }


//    def updateOid = (e: ReactEventI) => {val l =e.target.value.toLong; $.modState(s => s.copy(item = s.item.map(_.copy(oid =l))))}
//    def updateStore = (id:String) =>  $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(id)))))
//    def updateSupplier = (id:String) =>  $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(id)))))

    def edited(order:PurchaseOrder[LinePurchaseOrder]) = {
      //$.modState(s => s.copy(item =Some(order)))
      //$.props >>= (_.proxy.dispatch(Update(order)))
      Callback {
        IWSCircuit.dispatch(Update(order))
      }
      //LinePurchaseOrderList.apply($.props.runNow().proxy, item, AddNewLine, editLine, saveLine, deleteLine)
     // $.modState(s => s.copy(item = None))
    }

    def saveLine(line:LinePurchaseOrder) = {
      log.debug(s"purchaseOrder is yyyyyyyyyy" + line)
      val k = $.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
      edited(k2)
    }

    def delete(item:PurchaseOrder[LinePurchaseOrder]) = {
      Callback.log("PurchaseOrder deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def deleteLine(line1:LinePurchaseOrder) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine(deleted)
      log.debug(s"purchaseOrder is k2k2k2k2k2" + k2)
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
          p.proxy().render( all  => AccordionPanel("Display",
            List(PurchaseOrderList(all.items.asInstanceOf[Seq[PurchaseOrder[LinePurchaseOrder]]],
            item => edit(Some(item)),
            item => p.proxy.dispatch(Delete(item))))))
        )
      )
    }

    def buildForm (p: Props, s:State): Seq[ReactElement] = {
      val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]]
      val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]]
      log.debug(s"Supplier>>>>${supplier}")
      val porder = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]().add(LinePurchaseOrder(item = Some("4711"))))
      val  storeList = store map (iws =>(iws.id+"|"+iws.name))
      val  supplierList=supplier map (iws =>(iws.id+"|"+iws.name))
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("id", s.item.map(_.id), "id"),
              buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildSItem("store", itemsx = storeList , defValue = "001", evt = updateStore),
              buildSItem("supplier", itemsx = supplierList , defValue = "70000", evt = updateSupplier)
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
