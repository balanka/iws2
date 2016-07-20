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
            def listener(cursor: ModelR[RootModel[IWS,IWS], Pot[ContainerT[IWS,IWS]]]): Unit = {
              poitems = collection.immutable.SortedSet[PurchaseOrder[LinePurchaseOrder]]() ++
                IWSCircuit.zoom(_.store.get.models.get(101)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[Seq[PurchaseOrder[LinePurchaseOrder]]].toSet
              log.debug(s" POrder Listener ${poitems}")
              render(props,$.state.runNow())
            }
            IWSCircuit.subscribe(IWSCircuit.zoom(_.store.get.models.get(101).get)) (listener)

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

//    def updateOid(e: ReactEventI) = {
//      val l =e.target.value.toLong
//      log.debug(s"Oid is "+l)
//      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
//    }
    def updateOid(idx:String) = {
     // val oId = idx.substring(0, idx.indexOf("|"))
     //  log.debug(s"oid is "+oId)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))
    }
    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
     // log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
    }

    def updateSupplier(idx:String) = {
      val accountId =  idx.substring(0, idx.indexOf("|"))
     // log.debug(s"accountId is "+accountId)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))
    }
    def edited(order:PurchaseOrder[LinePurchaseOrder]) = //{
      //$.modState(s => s.copy(item =Some(order)))
     // $.props >>= (_.proxy.dispatch(Update(order)))
      Callback {
        IWSCircuit.dispatch(Update(order))
        $.modState(s => s.copy(item =Some(order))).runNow()
      }

    def saveLine(linex:LinePurchaseOrder) = {
      val line = linex.copy(modified=true)
      val k = $.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
      runCB(k2)

    }
    def runCB (item:PurchaseOrder[LinePurchaseOrder]) = Callback {
      IWSCircuit.dispatch(Update(item))
      //val line =item.getLines.filter(_.created ==true)
      val ro = item.getLines.filter(_.created ==true).map( e => item.replaceLine( e.copy(created = false).copy(modified =false)))
      val setLineID = ro.head.copy(lines = Some( item.getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
      $.modState(s => s.copy(item =Some(setLineID))).runNow()
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
      edited(k2)
    }
    def AddNewLine(line:LinePurchaseOrder) = $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid =
                                                     s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()).tid)))))


    def filterWith(line:LinePurchaseOrder, search:String) = line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
     def buildFormTab(p: Props, s: State, items:List[PurchaseOrder[LinePurchaseOrder]]): Seq[ReactElement] =
       List(<.div(bss.formGroup,
         TabComponent(Seq(
           TabItem("vtab1", "List", "#vtab1", true,
             PurchaseOrderList(items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
           TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items))
         ))
       )
      )


      def buildForm (p: Props, s:State, items:List[PurchaseOrder[LinePurchaseOrder]]) = {
        val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
        val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
        val porder = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]().add(LinePurchaseOrder(item = Some("4711"))))
        val  storeList = store.toList.filter(_.id !="-1") map (iws =>(iws.id+"|"+iws.name))
        val  supplierList=supplier.toList.filter(_.id !="-1") map (iws =>  (iws.id+"|"+iws.name))

       // List(
          <.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(bss.formGroup, ^.height := 10.px,
                 buildItem[String]("id", s.item.map(_.id), "id"),
                 //buildLabel("id", s.item.map(_.id), "id"),
                //buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
                buildSItem("oid", itemsx = buildTransIdList(items) , defValue = "001", evt = updateStore),
                buildSItem("store", itemsx = storeList , defValue = "001", evt = updateStore),
                buildSItem("supplier", itemsx = supplierList , defValue = "70000", evt = updateSupplier)
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
      Panel(Panel.Props("Purchase Order"), <.div(^.className := "panel-heading"),
        <.div(^.padding := 0,
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          AccordionPanel("Edit", buildFormTab(p, s, items), List(saveButton, newButton))
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
