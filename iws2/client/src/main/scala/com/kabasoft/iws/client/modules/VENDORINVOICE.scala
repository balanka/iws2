package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{VendorInvoiceList, LineVendorInvoiceList}
import com.kabasoft.iws.gui.BasePanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import diode.data.{Pot, Ready}
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scalacss.ScalaCssReact._

object VENDORINVOICE {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[VendorInvoice[LineVendorInvoice]] = None)
  @volatile var gitems = Set.empty[VendorInvoice[LineVendorInvoice]]
  def updateOid1(idx:String,  bs: BackendScope[Props,State]) = {
    // val oId = idx.substring(0, idx.indexOf("|"))
    //  log.debug(s"oid is "+oId)
    bs.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))
  }
  def updateStore(idx:String, bs: BackendScope[Props,State]) = {
    val storeId = idx.substring(0, idx.indexOf("|"))
    log.debug(s"store is "+storeId)
    bs.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
  }
  def updateAccount(idx: String, bs: BackendScope[Props,State]) = {
    val supplierId=idx.substring(0, idx.indexOf("|"))
    log.debug(s"ItemId Key is ${supplierId}  ")
    bs.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId)))))
  }
  def updateText(e: ReactEventI, bs: BackendScope[Props,State]) = {
    val txt = e.target.value
    log.debug(s"txt is ${txt}")
    bs.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))
  }
  class Backend($: BackendScope[Props, State]) {

    implicit def orderingById[A <: VendorInvoice[LineVendorInvoice]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}

    val r = List( updateOid1 _, updateStore _, updateAccount _, updateText _, edit _,  edited _)

    def mounted(props: Props) = {

      Callback {
        IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
        IWSCircuit.dispatch(Refresh(VendorInvoice[LineVendorInvoice]()))
      }
    }
   def edit(item:Option[VendorInvoice[LineVendorInvoice]]) = {
      val d =item.getOrElse(VendorInvoice[LineVendorInvoice]())
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(idx:String) = {
      // val oId = idx.substring(0, idx.indexOf("|"))
      //  log.debug(s"oid is "+oId)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
    }
    def updateOid1(idx:String,  bs: BackendScope[Props,State]) = {
      // val oId = idx.substring(0, idx.indexOf("|"))
      //  log.debug(s"oid is "+oId)
      bs.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
    }
    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
      log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId))))) >>setModified
    }

    def updateAccount(idx: String) = {
      val supplierId=idx.substring(0, idx.indexOf("|"))
      log.debug(s"ItemId Key is ${supplierId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId))))) >>setModified
    }
    def updateText(e: ReactEventI) = {
      val txt = e.target.value
      log.debug(s"txt is ${txt}")
      $.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))  >>setModified
    }
    def edited(order:VendorInvoice[LineVendorInvoice]) = {
      $.modState(s => s.copy(item =Some(order)))
      Callback {IWSCircuit.dispatch(Update(order))}
    }
    def runIt = $.state.runNow().item.getOrElse(VendorInvoice[LineVendorInvoice]())
    def saveLine(linex:LineVendorInvoice) =  {
      val k = runIt
      val k2 = k.replaceLine( linex.copy(transid = k.tid))

      runCB(k2)

    }
    def setModified  = $.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )
    def runCB (itemx:VendorInvoice[LineVendorInvoice]) = Callback {
      IWSCircuit.dispatch(Update(itemx))

      val ro = itemx.getLines.filter(_.created == true).map(e => itemx.replaceLine(e.copy(created = false).copy(modified = false)))
      if (!ro.isEmpty) {
        val setLineID = ro.head.copy(lines = Some(itemx.getLines map (e => if (e.tid <= 0) e.copy(tid = e.tid - 1) else e)))
        $.modState(s => s.copy(item = Some(setLineID))).runNow()
      }else {
        $.modState(s => s.copy(item = Some(itemx.copy(modified = false)))).runNow()
      }

    }

    def delete(item:VendorInvoice[LineVendorInvoice]) = {
      Callback.log("VendorInvoice deleted>>>>> ${item}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
    }
    def runDelete(item1:VendorInvoice[LineVendorInvoice]) =   {
      log.debug(s"  VendorInvoice to delete line from  >>>>>  ${item1}")
      IWSCircuit.dispatch(Update(item1))
      Callback {
        $.modState(s => s.copy(item = Some(item1))).runNow()
      }
    }
    def deleteLine(line1:LineVendorInvoice) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(VendorInvoice[LineVendorInvoice]())
      val k2 = k.replaceLine(deleted)
      edited(k2)
    }

    def AddNewLine(line:LineVendorInvoice) = {
      val  created =line.copy(created = true)
      log.debug(s"New Line VendorInvoice  created>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(created.copy(transid=
                s.item.getOrElse(VendorInvoice[LineVendorInvoice]()).tid)))))
    }

    def filterWith(line:LineVendorInvoice, search:String) =
      line.account.getOrElse("").contains(search)

    def buildFormTab(p: Props, s: State, items:List[VendorInvoice[LineVendorInvoice]],header:Seq[ReactElement]): ReactElement =
        TabComponent(Seq(
          TabItem("vtab1", "List", "#vtab1", true,
            VendorInvoiceList( items, "VendorInvoice", item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
          TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items,header))
        ))

    def render(p: Props, s: State) = {

      def saveButton = Button(Button.Props(edited(s.item.getOrElse(VendorInvoice[LineVendorInvoice]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(VendorInvoice[LineVendorInvoice]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
        gitems = IWSCircuit.zoom(_.store.get.models.get(112)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[VendorInvoice[LineVendorInvoice]]].toSet
      val items = gitems.toList.sorted
      val header =  List(saveButton, newButton)
      buildFormTab(p, s, items, header)
    }

    def buildForm (p: Props, s:State, items:List[VendorInvoice[LineVendorInvoice]], header:Seq[ReactElement]) = {
      val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
      val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
      val porder = s.item.getOrElse(VendorInvoice[LineVendorInvoice]().add(LineVendorInvoice(account = Some("4711"))))
      val  storeList=store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
      val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))

      BasePanel("Vendor Invoice",
       List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("Id", s.item.map(_.id), "id"),
              //buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildSItem("oid", itemsx = buildTransIdList(items) , defValue = "001", evt = updateStore),
              buildSItem("Store", itemsx = storeList,defValue = "0001", evt = updateStore),
              buildSItem("Supplier", itemsx = supplierList, defValue = "KG", evt = updateAccount),
              buildWItem("Text", s.item.map(_.text), defValue = "txt", evt = updateText)
            )
          )
        ),
          LineVendorInvoiceList(porder, AddNewLine, saveLine, deleteLine)
      )),header)
    }
  }

  val component = ReactComponentB[Props]("VendorInvoice")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
