package com.kabasoft.iws.client

import com.kabasoft.iws.gui.AppRouter._
import com.kabasoft.iws.gui.services.{RootModel, SPACircuit}
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

  val v1=  MenuItem("001","Masterfile", "#Masterfile" ,
             List(MenuItem("002","Account","#acc"),
                  MenuItem("003","Article","#art"),
                  MenuItem("004","Customer","#cust"),
                  MenuItem("005","Supplier","#sup"),
                  MenuItem("006","Article group","#cat"),
                  MenuItem("007","Quantity unit","#qty"),
                  MenuItem("010","Cost center","#cost"),
                  MenuItem("011","Store","#sto")
  ))

  val v2=MenuItem("008","Purchasing", "#Purchasing" ,
          List(MenuItem("009","Purchase order","#ord"),
               MenuItem("030","CGoodreceiving","#good")
    ))
  val v3=MenuItem("020","Dashboard", "#Dashboard" ,
    List(MenuItem("021","Dashboard","#scalacss"))

    )
  val vm=MenuItem("000","IWS", "#IWS" ,List(v1,v2,v3))


  val z1 = BusinessPartnerUIMacro.makeUI(Supplier())
  val z4 = BackendMacro.makeBackend(QuantityUnit())
  val z5 = BackendMacro.makeBackend(Vat())
  val z6 = BackendMacro.makeBackend(CostCenter())
  val z8 = BackendMacro.makeBackend(ArticleGroup())

  // configure the router
  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    val x1 =  SPACircuit.connect(_.store.get.models.get(1).get)
    val x2 =  SPACircuit.connect(_.store.get.models.get(2).get)
    val x3 =  SPACircuit.connect(_.store.get.models.get(3).get)
    val x4 =  SPACircuit.connect(_.store.get.models.get(4).get)
    val x5 =  SPACircuit.connect(_.store.get.models.get(5).get)
    val x6 =  SPACircuit.connect(_.store.get.models.get(6).get)
    val x7 =  SPACircuit.connect(_.store.get.models.get(7).get,"Article")
    val x8 =  SPACircuit.connect(_.store.get.models.get(8).get)
    val x9 =  SPACircuit.connect(_.store.get.models.get(9).get)
    val x101 = SPACircuit.connect(_.store.get.models.get(101).get, "POrder")
    val x104 = SPACircuit.connect(_.store.get.models.get(104).get, "Goodreceiving")
    val x4711 = SPACircuit.connect(_.store.get.models.get(4711).get)


    //val x2 =  SPACircuit.connect(_.store.get.models.getOrElse(2,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
     (staticRoute(root, DashboardPage$) ~> renderR(ctl => x4(proxy => Dashboard(ctl, proxy.asInstanceOf[ModelProxy[Pot[Data]]],QuantityUnitPage$)))
      | staticRoute("#art", ArticlePage$) ~> renderR(ctl => x7(p7 => ARTICLE(p7.asInstanceOf[ModelProxy[Pot[Data]]])))
       | staticRoute("#sto", StorePage$) ~> renderR(ctl => x2(p2 => STORE(p2.asInstanceOf[ModelProxy[Pot[Data]]])))
      |staticRoute("#ord", POrderPage$) ~> renderR(ctl => x101(proxy101 => PURCHASEORDER(proxy101.asInstanceOf[ModelProxy[Pot[Data]]])))
       |staticRoute("#good", GoodreceivingPage$) ~> renderR(ctl => x104(proxy104 => GOODRECEIVING(proxy104.asInstanceOf[ModelProxy[Pot[Data]]])))
      | staticRoute("#qty", QuantityUnitPage$) ~> renderR(ctl => x4(p4=>(z4(p4.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#vat", VatPage$) ~> renderR(ctl => x5(p5=>(z5(p5.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#acc", AccountPage$) ~> renderR(ctl => x9(p9 =>(ACCOUNT(p9.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#todo", TodoPage$) ~> renderR(ctl => x4711(p4711 =>(Todo(p4711.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cust", CustomerPage$) ~> renderR(ctl => x3(p3 =>(CUSTOMER(p3.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cat", CategoryPage$) ~> renderR(ctl => x8(p8 => (z8(p8.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#sup", SupplierPage$) ~> renderR(ctl => x1(p1 =>(z1(p1.asInstanceOf[ModelProxy[Pot[Data]]]))))
      | staticRoute("#cost", CostCenterPage$) ~> renderR(ctl => x6(p6 =>(z6(p6.asInstanceOf[ModelProxy[Pot[Data]]]))))
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
