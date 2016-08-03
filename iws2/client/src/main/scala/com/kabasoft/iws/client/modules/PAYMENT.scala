package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{LinePaymentList, LineVendorInvoiceList, PaymentList, VendorInvoiceList}
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

import scalacss.ScalaCssReact._

object PAYMENT {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Payment[LinePayment]] = None)
  @volatile var gitems = Set.empty[Payment[LinePayment]]

  class Backend($: BackendScope[Props, State]) {

    implicit def orderingById[A <: Payment[LinePayment]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}
    def mounted(props: Props) = {

      Callback {
        IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
        IWSCircuit.dispatch(Refresh(Payment[LinePayment]()))
      }
    }
   def edit(item:Option[Payment[LinePayment]]) = {
      val d =item.getOrElse(Payment[LinePayment]())
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(idx:String) = {
      // val oId = idx.substring(0, idx.indexOf("|"))
      //  log.debug(s"oid is "+oId)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))
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
    def updateText(e: ReactEventI) = {
      val txt = e.target.value
      log.debug(s"txt is ${txt}")
      $.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))  >>setModified
    }
    def edited(order:Payment[LinePayment]) = {
     // $.modState(s => s.copy(item =Some(order)))
      Callback {IWSCircuit.dispatch(Update(order))}
      //$.props >>= (_.proxy.dispatch(Update(order)))
    }
    def saveLine(linex:LinePayment) = {
      val line = linex.copy(modified=true)
      val k = $.state.runNow().item.getOrElse(Payment[LinePayment]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
     // log.debug(s"purchaseOrder saved is ZZZZZZZZZZ" + k2)
      runCB(k2)

    }
    def setModified  = $.modState(s => s.copy(item = s.item.map(_.copy(modified = true))))
    def runCB (item:Payment[LinePayment]) = Callback {
      IWSCircuit.dispatch(Update(item))
      val ro = item.getLines.filter(_.created ==true).map( e => item.replaceLine( e.copy(created = false).copy(modified =false)))
      val setLineID = ro.head.copy(lines = Some( item.getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
      $.modState(s => s.copy(item =Some(setLineID))).runNow()
    }

    def delete(item:Payment[LinePayment]) = {
      Callback.log("Payment deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def runDelete(item1:Payment[LinePayment]) =   {
      log.debug(s"  Payment to delete line from  >>>>>  ${item1}")
      IWSCircuit.dispatch(Update(item1))
      Callback {
        $.modState(s => s.copy(item = Some(item1))).runNow()
      }
    }
    def deleteLine(line1:LinePayment) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(Payment[LinePayment]())
      val k2 = k.replaceLine(deleted)
      edited(k2)
      //runDelete(k2)
    }

    def AddNewLine(line:LinePayment) = {
      val  created =line.copy(created = true)
      log.debug(s"New  LinePayment  created>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=
                s.item.getOrElse(Payment[LineVendorInvoice]()).tid)))))
    }

    def filterWith(line:LinePayment, search:String) =
      line.account.getOrElse("").contains(search)

    def buildFormTab(p: Props, s: State, items:List[Payment[LinePayment]]): Seq[ReactElement] =
      List(<.div(bss.formGroup,
        TabComponent(Seq(
          TabItem("vtab1", "List", "#vtab1", true,
             PaymentList( items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
          TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items))
        ))
      )
      )

    def buildFormTab2(p: Props, s: State, items:List[Payment[LinePayment]], header:Seq[ReactElement]): ReactElement =
      <.div(bss.formGroup,
        TabComponent2("Payment", Seq(
          TabItem("Payment_vtab1", "List", "Payment_#vtab1", true,
            PaymentList(items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
          TabItem("Payment_vtab1", "Form", "Payment_#vtab2", false, buildForm(p, s, items))),
          header)
      )

    def render(p: Props, s: State) = {

      def saveButton = Button(Button.Props(edited(s.item.getOrElse(Payment[LinePayment]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(Payment[LinePayment]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")

      if( gitems.filter(_.tid != 0).size <=1) {
        gitems = IWSCircuit.zoom(_.store.get.models.get(114)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Payment[LinePayment]]].toSet
      }
      val items = gitems.toList.sorted
       BasePanel("Payment", buildFormTab(p, s, items), List(saveButton, newButton))
     // buildFormTab(p, s, items, List(saveButton, newButton))
    }

    def buildForm (p: Props, s:State, items:List[Payment[LinePayment]]) = {
      val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
      val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
      val porder = s.item.getOrElse(Payment[LinePayment]().add(LinePayment(account = Some("4711"))))
      val  storeList=store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
      val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
        <.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("Id", s.item.map(_.id), "id"),
              //buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildSItem("oid", itemsx = buildTransIdList(items) , defValue = "001", evt = updateStore),
              buildSItem("Store", itemsx=storeList,defValue = "0001", evt = updateStore),
              buildSItem("Supplier", itemsx = supplierList, defValue = "KG", evt = updateAccount),
              buildWItem("Text", s.item.map(_.text), defValue = "txt", evt = updateText)
            )
          )
        ),
          LinePaymentList(porder, AddNewLine, saveLine, deleteLine)
      )
    }
  }

  val component = ReactComponentB[Props]("VendorInvoice")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
