package com.kabasoft.iws.client.modules


import com.kabasoft.iws.client.components.{LinePurchaseOrderList, PurchaseOrderList}
import com.kabasoft.iws.gui.BasePanel
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
import com.kabasoft.iws.gui.services.{IWSCircuit, RootModel}
import diode.ModelR

import scala.scalajs.js
import scalacss.ScalaCssReact._


object PURCHASEORDER {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[PurchaseOrder[LinePurchaseOrder]] = None)
  @volatile var poitems = Set.empty[PurchaseOrder[LinePurchaseOrder]]

  class Backend($: BackendScope[Props, State]) {
     implicit def orderingById[A <: PurchaseOrder[LinePurchaseOrder]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}
    def mounted(props: Props) = {
      Callback {
        IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
        IWSCircuit.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]()))
      }
    }

    def edit(item:Option[PurchaseOrder[LinePurchaseOrder]]) = {
      val d =item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(idx:String) = {
     // val oId = idx.substring(0, idx.indexOf("|"))
     //  log.debug(s"oid is "+oId)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))>>setModified
    }
    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
     // log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))>>setModified
    }

    def updateSupplier(idx:String) = {
      val accountId =  idx.substring(0, idx.indexOf("|"))
     // log.debug(s"accountId is "+accountId)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId))))) >>setModified
    }
    def updateText(e: ReactEventI) = {
      val txt = e.target.value
      log.debug(s"txt is ${txt}")
      $.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))  >>setModified
    }
    def setModified  = $.modState(s => s.copy(item = s.item.map(_.copy(modified = true))))
    def edited(order:PurchaseOrder[LinePurchaseOrder]) =   dispatch(order)

  def dispatch (order:PurchaseOrder[LinePurchaseOrder], reset:Boolean = false) =  Callback {
    val m: String = order.account.getOrElse("").concat(order.store.getOrElse(""))
    //log.debug(s"dispatch mmmmmmmmmmmmmmmmmm is ${m}  order ${order}  con: ${ ((!m.isEmpty) && (order.created || order.modified))}")
    if ((!m.isEmpty) && (order.created || order.modified)) {
      IWSCircuit.dispatch(Update(order))
      $.modState(s => s.copy(item = Some(order.copy(created = false).copy(modified = false)))).runNow()
      if( reset) $.modState(s => s.copy(item = Some(resetId(order)))).runNow()
    }
  }

    def saveLine(line:LinePurchaseOrder) = {
      val k = $.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
      // runCB(k2)
      dispatch(k2.copy(modified = true), true)

    }

    def resetId(item:PurchaseOrder[LinePurchaseOrder]): PurchaseOrder[LinePurchaseOrder] = {
      val ro = item.getLines.filter(_.created == true).map(e => item.replaceLine(e.copy(created = false).copy(modified = false)))
      val setLineID = ro.head.copy(lines = Some(item.getLines map (e => if (e.tid != 0) e else e.copy(tid = -1))))
      setLineID.copy(created =false).copy(modified =false)
    }

    def delete(item:PurchaseOrder[LinePurchaseOrder]) = {
      //Callback.log("PurchaseOrder deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def deleteLine(line1:LinePurchaseOrder) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine(deleted)
      edited(k2)
    }

    def AddNewLine(line:LinePurchaseOrder) = {
      val  created =line.copy(created = true)
      log.debug(s"New  LinePurchaseOrder  created>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()).tid)))))
    }
    def filterWith(line:LinePurchaseOrder, search:String) = line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(PurchaseOrder[LinePurchaseOrder](created = true))),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")


      def buildFormTab(p: Props, s: State, items:List[PurchaseOrder[LinePurchaseOrder]]): Seq[ReactElement] =
        List(<.div(bss.formGroup,
          TabComponent(Seq(
            TabItem("vtab1", "List", "#vtab1", true,
              PurchaseOrderList( items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
            TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items))
          ))
        )
        )


      def buildFormTab2(p: Props, s: State, items:List[PurchaseOrder[LinePurchaseOrder]], header:Seq[ReactElement]): ReactElement =
         <.div(bss.formGroup,
         TabComponent2("Purchase Order", Seq(
           TabItem("vtab1", "List", "#vtab1", true,
             PurchaseOrderList(items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
           TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items))),
         header)
       )

      def buildForm (p: Props, s:State, items:List[PurchaseOrder[LinePurchaseOrder]]) = {
        val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
        val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
        val porder = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]().add(LinePurchaseOrder(item = Some("4711"))))
        val  storeList = store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
        val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>  (iws.id+"|"+iws.name))

          <.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(bss.formGroup, ^.height := 10.px,
                  buildItem[String]("Id", s.item.map(_.id), "id"),
                  buildSItem("oid", itemsx = buildTransIdList(items) , defValue = "001", evt = updateOid),
                  buildSItem("Store", itemsx = storeList , defValue = "001", evt = updateStore),
                  buildSItem("Supplier", itemsx = supplierList , defValue = "70000", evt = updateSupplier),
                  buildWItem("Text", s.item.map(_.text), defValue = "txt", evt = updateText)
              )
            )
          ),
          LinePurchaseOrderList(porder, AddNewLine, saveLine, deleteLine)
        )
      }

      if( poitems.filter(_.tid != 0).size <=1) {
        poitems = IWSCircuit.zoom(_.store.get.models.get(101)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[PurchaseOrder[LinePurchaseOrder]]].toSet
      }
      val items = poitems.toList.sorted
      BasePanel("Purchase Order", buildFormTab(p, s, items), List(saveButton, newButton))
      //buildFormTab2(p, s, items, List(saveButton, newButton))
    }
  }

  val component = ReactComponentB[Props]("PURCHASEORDER")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
