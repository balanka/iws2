package com.kabasoft.iws.client

import com.kabasoft.iws.gui.AppRouter._
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.services.{IWSCircuit, RootModel}
import diode.ModelR
import diode.react.ModelProxy

import scala.scalajs.js.annotation.JSExport
import com.kabasoft.iws.client.modules._
import com.kabasoft.iws.gui.MenuItem
import com.kabasoft.iws.gui._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.{BackendMacro, _}
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.shared.{FDocument, VendorInvoice, _}
import diode.data.{Pot, Ready}
import diode.react.ReactPot._
import japgolly.scalajs.react.{ReactDOM, _}
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom

import scala.language.reflectiveCalls
import scala.scalajs.js
import scala.scalajs.js.Dynamic.{global => g}
import scalacss.Defaults._
import scalacss.ScalaCssReact._

@JSExport("SPAMain")
object IWSMain extends js.JSApp {

  val v1 =  MenuItem("001","Masterfile", "#Masterfile" ,
             List(MenuItem("002","Account","#acc"),
                  MenuItem("003","Article","#art"),
                  MenuItem("004","Customer","#cust"),
                  MenuItem("005","Supplier","#sup"),
                  MenuItem("006","Bank","#bank"),
                  MenuItem("007","BankAccount","#bacc"),
                  MenuItem("008","Quantity unit","#qty"),
                  MenuItem("009","Cost center","#cost"),
                  MenuItem("010","Store","#sto"),
                  MenuItem("011","Company","#comp")
  ))
  val v2 = MenuItem("020","Purchasing", "#Purchasing" ,
    List(MenuItem("021","Purchase order","#ord"),
        MenuItem("022","Goods receipt","#good"),
        MenuItem("023","Inventory Invoice","#iinv")

    ))
  val v3 = MenuItem("030","Accounting", "#Accounting" ,
          List(MenuItem("031","Customer Invoice","#cinv"),
              MenuItem("024","Vendor Invoice","#vinv"),
              MenuItem("025","Payment","#pay"),
              MenuItem("032","Settlement","#setl")))

  val v4 = MenuItem("040","Dashboard", "#Dashboard" ,
          List(MenuItem("041","Dashboard","#scalacss"),
               MenuItem("042","Catalog","#catalog"))

    )
  val vm=MenuItem("000","IWS", "#IWS" ,List(v1,v2,v3,v4))


  val z1 = BusinessPartnerUIMacro.makeUI(Supplier())
  val z4 = BackendMacro.makeBackend(QuantityUnit())
  val z5 = BackendMacro.makeBackend(Vat())
  val z6 = BackendMacro.makeBackend(CostCenter())
  val vendorInvoice = FDocument(VendorInvoice[LineVendorInvoice]())
  val lineVendorInvoice:LineFDocument = LineFDocument(LineVendorInvoice())
  val customerInvoice = FDocument(CustomerInvoice[LineCustomerInvoice]())
  val lineCustomerInvoice:LineFDocument = LineFDocument(LineCustomerInvoice())
  val payment= FDocument(Payment[LinePayment]())
  val linePayment:LineFDocument = LineFDocument(LinePayment())
  val settlement= FDocument(Settlement[LineSettlement]())
  val lineSettlement:LineFDocument = LineFDocument(LineSettlement())
  val store = Store()
  val supplier=Supplier()
  val customer = Customer()
  // configure the router
  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    val x1 =  IWSCircuit.connect(_.store.get.models.get(1).get)
    val x2 =  IWSCircuit.connect(_.store.get.models.get(2).get)
    val x3 =  IWSCircuit.connect(_.store.get.models.get(3).get)
    val x4 =  IWSCircuit.connect(_.store.get.models.get(4).get)
    val x5 =  IWSCircuit.connect(_.store.get.models.get(5).get)
    val x6 =  IWSCircuit.connect(_.store.get.models.get(6).get)
    val x7 =  IWSCircuit.connect(_.store.get.models.get(7).get,"Article")
    //val x8 =  IWSCircuit.connect(_.store.get.models.get(8).get)
    val x9 =  IWSCircuit.connect(_.store.get.models.get(9).get)
    val x10 =  IWSCircuit.connect(_.store.get.models.get(10).get)
    val x11 =  IWSCircuit.connect(_.store.get.models.get(11).get)
    val x12 =  IWSCircuit.connect(_.store.get.models.get(12).get)
    val x101 = IWSCircuit.connect(_.store.get.models.get(101).get, "POrder")
    val x104 = IWSCircuit.connect(_.store.get.models.get(104).get, "Good receipt")
    val x110 = IWSCircuit.connect(_.store.get.models.get(110).get, "Inventory Invoice")
    val x112 = IWSCircuit.connect(_.store.get.models.get(112).get, "Vendor Invoice")
    val x114 = IWSCircuit.connect(_.store.get.models.get(114).get, "Payment")
    val x122 = IWSCircuit.connect(_.store.get.models.get(122).get, "Customer Invoice")
    val x124 = IWSCircuit.connect(_.store.get.models.get(124).get, "Settlement")

     (staticRoute(root, DashboardPage$) ~> renderR(ctl => x4(proxy => Dashboard(ctl, proxy.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#art", ArticlePage$) ~> renderR(ctl => x7(p7 => ARTICLE(p7.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#sto", StorePage$) ~> renderR(ctl => x2(p2 => STORE(p2.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#ord", POrderPage$) ~> renderR(ctl => x101(proxy101 => PURCHASEORDER(proxy101.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#good", GoodreceivingPage$) ~> renderR(ctl => x104(proxy104 => GOODRECEIVING("GOODRECEIVING",  {"104"},proxy104.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#qty", QuantityUnitPage$) ~> renderR(ctl => x4(p4=>(z4(p4.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#vat", VatPage$) ~> renderR(ctl => x5(p5=>(z5(p5.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#acc", AccountPage$) ~> renderR(ctl => x9(p9 =>(ACCOUNT(p9.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cust", CustomerPage$) ~> renderR(ctl => x3(p3 =>(CUSTOMER(p3.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#comp", CompanyPage$) ~> renderR(ctl => x10(p10 => (COMPANY(p10.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#sup", SupplierPage$) ~> renderR(ctl => x1(p1 =>(z1(p1.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cost", CostCenterPage$) ~> renderR(ctl => x6(p6 =>(z6(p6.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#bank", BankPage$) ~> renderR(ctl => x11(p11 =>(BANK(p11.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#bacc", BankAccountPage$) ~> renderR(ctl => x12(p12 =>(BANKACCOUNT(p12.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#iinv", InventoryInvoicePage$) ~> renderR(ctl => x110(proxy110 => INVENTORYINVOICE("INVENTORYINVOICE",  {"110"},proxy110.asInstanceOf[ModelProxy[Pot[Data]]])))

       | staticRoute("#cinv", CustomerInvoicePage$) ~> renderR(ctl => x122(proxy122 => SALES(proxy122.asInstanceOf[ModelProxy[Pot[Data]]],122,122,2,3, customerInvoice, lineCustomerInvoice, store, customer,"Sales Invoice")))
       | staticRoute("#vinv", VendorInvoicePage$) ~> renderR(ctl => x112(proxy112 => FDOCUMENT(proxy112.asInstanceOf[ModelProxy[Pot[Data]]],112,110,2,1,vendorInvoice,lineVendorInvoice, store, supplier,"Vendor Invoice")))
       | staticRoute("#setl", SettlementPage$) ~> renderR(ctl => x124(proxy124 => SALES(proxy124.asInstanceOf[ModelProxy[Pot[Data]]],124,122,2,3, settlement,lineSettlement, store, customer,"Settlement")))

       | staticRoute("#pay", PaymentPage$) ~> renderR(ctl => x114(proxy114 => FDOCUMENT(proxy114.asInstanceOf[ModelProxy[Pot[Data]]],114,112,2,1,payment,linePayment, store, supplier,"Payment")))
      | staticRoute("#catalog", CatalogPage$) ~> renderR(ctl => ProductCalalog.FilterableProductTable(ProductCalalog.PRODUCTS))
       ).notFound(redirectToPage(DashboardPage$)(Redirect.Replace))
  }.renderWith(layout)

  // base layout for all pages
    def layout(c: RouterCtl[Page], r: Resolution[Page]) = {
     <.header(
         <.meta(^.name :="viewport", ^.contentAttr:="width=device-width, initial-scale=1, maximum-scale=1")
     )
      <.div(
        <.div(^.cls := "navbar navbar navbar-fixed-right",
           <.div(^.cls := "collapse navbar-collapse",
                <.div( ^.cls := "col-sm-10 col-xs-10 col-md-10", r.render(),^.paddingTop :=0, ^.paddingRight:=0.px),
                <.div( ^.cls := "col-sm-10 col-xs-2 col-md-2", TabAccordionMenu(vm),  ^.paddingTop :=0)
               ),
           <.div(^.textAlign := "center", ^.key := "footer")(
           <.p("KABA Soft GmbH All rights reserved", ^.paddingBottom := 1))
        )
      )
  }


  //@JSExport
  def main(): Unit = {
    log.warn("Application starting")
    log.enableServerLogging("/logging")
    log.info("This message goes to server as well")
    // create stylesheet
    GlobalStyles.addToDocument()
    // create the router
    log.info("This message before  RouterConfigDsl")
    val router = Router(BaseUrl.until_#, routerConfig)
    // tell React to render the router in the document body
    ReactDOM.render(router(), dom.document.getElementById("root"))
  }
}
