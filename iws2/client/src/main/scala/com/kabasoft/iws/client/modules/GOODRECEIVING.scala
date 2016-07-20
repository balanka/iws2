package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{GoodreceivingList, LineGoodreceivingList}
import com.kabasoft.iws.gui.AccordionPanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.{IWSCircuit, RootModel}
import com.kabasoft.iws.shared._
import diode.ModelR
import diode.data.{Pot, Ready}
import diode.react.ReactPot._
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scalacss.ScalaCssReact._

object GOODRECEIVING {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Goodreceiving[LineGoodreceiving]] = None)
  @volatile var gitems = Set.empty[Goodreceiving[LineGoodreceiving]]

  class Backend($: BackendScope[Props, State]) {
   // @volatile var poitems = Set.empty[PurchaseOrder[LinePurchaseOrder]]
    implicit def orderingById[A <: Goodreceiving[LineGoodreceiving]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}
    def mounted(props: Props) = {
//      def listener(cursor: ModelR[RootModel[IWS,IWS], Pot[ContainerT[IWS,IWS]]]): Unit = {
//       gitems = collection.immutable.SortedSet [Goodreceiving[LineGoodreceiving]]() ++
//        IWSCircuit.zoom(_.store.get.models.get(104)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[Seq[Goodreceiving[LineGoodreceiving]]].toSet
//      log.debug(s" Goodreceiving Listener ${gitems}")
//      render(props,$.state.runNow())
//    }
//    IWSCircuit.subscribe(IWSCircuit.zoom(_.store.get.models.get(101).get)) (listener)
      Callback {
        IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
        IWSCircuit.dispatch(Refresh(Goodreceiving[LineGoodreceiving]()))
      }
    }
   def edit(item:Option[Goodreceiving[LineGoodreceiving]]) = {
      val d =item.getOrElse(Goodreceiving[LineGoodreceiving]())
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(e: ReactEventI) = {
      e.preventDefault()
      val l =e.target.value.toLong
      log.debug(s"Oid is "+l)

      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
    }

    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
      log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
    }

    def updateAccount(idx: String) = {
      val supplierId=idx.substring(0, idx.indexOf("|"))
      log.debug(s"ItemId Key is ${supplierId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId)))))
    }

    def edited(order:Goodreceiving[LineGoodreceiving]) = {
      $.modState(s => s.copy(item =Some(order)))
      Callback {IWSCircuit.dispatch(Update(order))}
      //$.props >>= (_.proxy.dispatch(Update(order)))
    }
    def saveLine(linex:LineGoodreceiving) = {
      val line = linex.copy(modified=true)
      val k = $.state.runNow().item.getOrElse(Goodreceiving[LineGoodreceiving]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
     // log.debug(s"purchaseOrder saved is ZZZZZZZZZZ" + k2)
      runCB(k2)

    }
    def runCB (item:Goodreceiving[LineGoodreceiving]) = Callback {
      IWSCircuit.dispatch(Update(item))
      val ro = item.getLines.filter(_.created ==true).map( e => item.replaceLine( e.copy(created = false).copy(modified =false)))
      val setLineID = ro.head.copy(lines = Some( item.getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
      $.modState(s => s.copy(item =Some(setLineID))).runNow()
    }

    def delete(item:Goodreceiving[LineGoodreceiving]) = {
      Callback.log("Goodreceiving deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def runDelete(item1:Goodreceiving[LineGoodreceiving]) =   {
      log.debug(s"  Goodreceiving to delete line from  >>>>>  ${item1}")
      IWSCircuit.dispatch(Update(item1))
      Callback {
        $.modState(s => s.copy(item = Some(item1))).runNow()
      }
    }
    def deleteLine(line1:LineGoodreceiving) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(Goodreceiving[LineGoodreceiving]())
      val k2 = k.replaceLine(deleted)
      edited(k2)
      //runDelete(k2)
    }

    def AddNewLine(line:LineGoodreceiving) = {
      val  created =line.copy(created = true)
      log.debug(s"New Line Goodreceiving  created>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=
                s.item.getOrElse(Goodreceiving[LineGoodreceiving]()).tid)))))
    }

    def filterWith(line:LineGoodreceiving, search:String) =
      line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {

      def saveButton = Button(Button.Props(edited(s.item.getOrElse(Goodreceiving[LineGoodreceiving]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(Goodreceiving[LineGoodreceiving]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")

      if( gitems.filter(_.tid != 0).size <=1) {
        gitems = IWSCircuit.zoom(_.store.get.models.get(104)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Goodreceiving[LineGoodreceiving]]].toSet
      }
      val items = gitems.toList.sorted
     // val items = IWSCircuit.zoom(_.store.get.models.get(104)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Goodreceiving[LineGoodreceiving]]].toSet
      Panel(Panel.Props("Goodreceiving"), <.div(^.className := "panel-heading"),
        <.div(^.padding := 0,
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          AccordionPanel("Edit", buildForm(p, s), List(saveButton, newButton)),
          AccordionPanel("Display",
            List(GoodreceivingList(  "GOODRECEIVING",  {"104"},items.toList,
            item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))))
        )
      )
    }

    def buildForm (p: Props, s:State): Seq[ReactElement] = {
      val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
      val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
      val porder = s.item.getOrElse(Goodreceiving[LineGoodreceiving]().add(LineGoodreceiving(item = Some("4711"))))
      val  storeList=store.toList.filter(_.id !="-1") map (iws =>(iws.id+"|"+iws.name))
      val  supplierList=supplier.toList.filter(_.id !="-1") map (iws =>(iws.id+"|"+iws.name))
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("id", s.item.map(_.id), "id"),
              buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildSItem("store", itemsx=storeList,defValue = "0001", evt = updateStore),
              buildSItem("supplier", itemsx = supplierList, defValue = "KG", evt = updateAccount)
            )
          )
        ),
        LineGoodreceivingList("GOODRECEIVING", {"104"},porder, AddNewLine, saveLine, deleteLine)
      )
      )
    }
  }

  val component = ReactComponentB[Props]("PURCHASEORDER")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ref: js.UndefOr[String] = "GOODRECEIVING", key: js.Any = {"104"}, proxy: ModelProxy[Pot[Data]]) = component.set(key, ref)(Props(proxy))
}
