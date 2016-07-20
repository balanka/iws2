package com.kabasoft.iws.client

import com.kabasoft.iws.gui.AppRouter._
import com.kabasoft.iws.gui.services.{RootModel, IWSCircuit}
import diode.ModelR
import diode.react.ModelProxy

import scala.scalajs.js.annotation.JSExport
import com.kabasoft.iws.client.modules._
import com.kabasoft.iws.gui.AccordionMenu.MenuItem
import com.kabasoft.iws.gui._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.{BackendMacro, _}
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.shared._
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
               MenuItem("022","Goodreceiving","#good")
    ))
  val v3 = MenuItem("030","Dashboard", "#Dashboard" ,
    List(MenuItem("031","Dashboard","#scalacss"))

    )
  val vm=MenuItem("000","IWS", "#IWS" ,List(v1,v2,v3))


  val z1 = BusinessPartnerUIMacro.makeUI(Supplier())
  val z4 = BackendMacro.makeBackend(QuantityUnit())
  val z5 = BackendMacro.makeBackend(Vat())
  val z6 = BackendMacro.makeBackend(CostCenter())
  //val z8 = BackendMacro.makeBackend(ArticleGroup())

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
    val x104 = IWSCircuit.connect(_.store.get.models.get(104).get, "Goodreceiving")


    //val x2 =  SPACircuit.connect(_.store.get.models.getOrElse(2,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
     (staticRoute(root, DashboardPage$) ~> renderR(ctl => x4(proxy => Dashboard(ctl, proxy.asInstanceOf[ModelProxy[Pot[Data]]],QuantityUnitPage$)))
      | staticRoute("#art", ArticlePage$) ~> renderR(ctl => x7(p7 => ARTICLE(p7.asInstanceOf[ModelProxy[Pot[Data]]])))
       | staticRoute("#sto", StorePage$) ~> renderR(ctl => x2(p2 => STORE(p2.asInstanceOf[ModelProxy[Pot[Data]]])))
      |staticRoute("#ord", POrderPage$) ~> renderR(ctl => x101(proxy101 => PURCHASEORDER(proxy101.asInstanceOf[ModelProxy[Pot[Data]]])))
       |staticRoute("#good", GoodreceivingPage$) ~> renderR(ctl => x104(proxy104 => GOODRECEIVING("GOODRECEIVING",  {"104"},proxy104.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#qty", QuantityUnitPage$) ~> renderR(ctl => x4(p4=>(z4(p4.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#vat", VatPage$) ~> renderR(ctl => x5(p5=>(z5(p5.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#acc", AccountPage$) ~> renderR(ctl => x9(p9 =>(ACCOUNT(p9.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cust", CustomerPage$) ~> renderR(ctl => x3(p3 =>(CUSTOMER(p3.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#comp", CompanyPage$) ~> renderR(ctl => x10(p10 => (COMPANY(p10.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#sup", SupplierPage$) ~> renderR(ctl => x1(p1 =>(z1(p1.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cost", CostCenterPage$) ~> renderR(ctl => x6(p6 =>(z6(p6.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#bank", BankPage$) ~> renderR(ctl => x11(p11 =>(BANK(p11.asInstanceOf[ModelProxy[Pot[Data]]]))))
       | staticRoute("#bacc", BankAccountPage$) ~> renderR(ctl => x12(p12 =>(BANKACCOUNT(p12.asInstanceOf[ModelProxy[Pot[Data]]]))))
      ).notFound(redirectToPage(DashboardPage$)(Redirect.Replace))
   // ).notFound(redirectToPage(Home)(Redirect.Replace))
  }.renderWith(layout)

  // base layout for all pages
    def layout(c: RouterCtl[Page], r: Resolution[Page]) = {
      <.div(
      <.div(^.className := "navbar navbar navbar-fixed-right",
         <.div(^.className := "collapse navbar-collapse",
         <.div( ^.className := "col-xs-8", r.render(),^.paddingTop :=0)
         //<.div( ^.className := "col-xs-2", AccordionMenu(vm),^.paddingTop :=1)
        , <.div( ^.className := "col-xs-2", TabAccordionMenu(vm),^.paddingTop :=0)
        // ,<.div( ^.className := "col-xs-2", Tab(t1),^.paddingTop :=1)
        )
      ,
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

    //AppRouter.router().render(dom.document.getElementById("root"))
  }
}
