package com.kabasoft.iws.client

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.AppRouter._
import com.kabasoft.iws.gui.macros.IWSList.{customerList, supplierList}
import com.kabasoft.iws.gui.macros._
//import com.kabasoft.iws.gui.Tab.TabItem
import com.kabasoft.iws.gui.AccordionMenu.MenuItem
import com.kabasoft.iws.gui._
import com.kabasoft.iws.gui.macros.BackendMacro
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.client.modules._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.SPACircuit
import com.kabasoft.iws.gui.macros._
import japgolly.scalajs.react._
import japgolly.scalajs.react.ReactDOM
import japgolly.scalajs.react.extra.router._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import scala.scalajs.js
import scalacss.Defaults._
import scalacss.ScalaCssReact._
import diode.data.{Pot, Ready}
import diode.react.ReactPot._
import diode.react.ModelProxy
import com.kabasoft.iws.gui.macros.Bootstrap._
import scala.scalajs.js.Dynamic.{global => g}
import com.kabasoft.iws.client.components.Motd




import scala.language.reflectiveCalls

//@JSExport("SPAMain")
object SPAMain extends js.JSApp {

  //just do val x = SPACircuit.connect(_.messages) and then renderR(r => x(p => SignIn(r, p)))

  val v1=  MenuItem("001","Masterfile", "#Masterfile" ,
             List(MenuItem("002","Account","#acc"),
                  MenuItem("003","Article","#art"),
                  MenuItem("004","Customer","#cust"),
                  MenuItem("005","Supplier","#sup"),
                  MenuItem("006","Article group","#cat"),
                  MenuItem("007","Quantity unit","#qty")
  ))

  val v2=MenuItem("008","Purchasing", "#Purchasing" ,
          List(MenuItem("009","Purchase order","#ord"),
               MenuItem("010","Cost center","#cost")
    ))
  val v3=MenuItem("020","Dashboard", "#Dashboard" ,
    List(MenuItem("021","Dashboard","#scalacss"))

    )
  val vm=MenuItem("000","IWS", "#IWS" ,List(v1,v2,v3))
  val CIRCUIT=SPACircuit.zoom(_.store.get).zoom(_.models(6))

  // configure the router
  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    val z0 = BackendMacro.makeBackend(ArticleGroup())
    val z1 = BusinessPartnerUIMacro.makeUI(Supplier())
    val z6 = BackendMacro.makeBackend(CostCenter())
    //val z3 = BusinessPartnerUIMacro.makeUI(Customer())
    val z4 = BackendMacro.makeBackend(QuantityUnit())
   // val z8 = BackendMacro.makeBackend(ArticleGroup())
    //val z5 =  BackendMacro.makeBackend(Account())
    /*var t1=  TabItem("Masterfile","Masterfile","#Masterfile", true,<.div(),
      List(TabItem("acc","Account","#acc",true, <.div(Components2())),
        TabItem("art","Article" ,"#art",false, <.div(Components2())
        )))
        */
    //val muiRoutes: Rule =
    //  MuiRouteModule.routes.prefixPath_/("#materialui")
     //   .pmap[Page](MuiPages) { case MuiPages(p) => p }
    //val bb = SPACircuit.connect(SPACircuit.store.get.models.get(8).get.asInstanceOf[Pot[Data]])
    //log.info("/////////////////////////////////////"+SPACircuit.store.get.models.get(4))
    val x0 = SPACircuit.connect(_.motd)
    val x1 =  SPACircuit.connect(_.store.get.models.getOrElse(1,Ready(Data(List(Supplier())))).asInstanceOf[Pot[Data]])
    //val x2 =  SPACircuit.connect(_.store.get.models.getOrElse(2,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
    val x3 =  SPACircuit.connect(_.store.get.models.getOrElse(3,Ready(Data(List(Customer())))).asInstanceOf[Pot[Data]])
    val x4 =  SPACircuit.connect(_.store.get.models.getOrElse(4,Ready(Data(List(QuantityUnit())))).asInstanceOf[Pot[Data]])
    val x6 =  SPACircuit.connect(_.store.get.models.getOrElse(6,Ready(Data(List(CostCenter())))).asInstanceOf[Pot[Data]])
    val x7 =  SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
    val x71 =  SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
    val x8 =  SPACircuit.connect(_.store.get.models.getOrElse(8,Ready(Data(List(ArticleGroup())))).asInstanceOf[Pot[Data]])
    val x9 =  SPACircuit.connect(_.store.get.models.getOrElse(9,Ready(Data(List(Account())))).asInstanceOf[Pot[Data]])
    val x101 =  SPACircuit.connect(_.store.get.models.getOrElse(101,Ready(Data(List(PurchaseOrder[LinePurchaseOrder]())))).asInstanceOf[Pot[Data]])
    val x4711 =  SPACircuit.connect(_.store.get.models.getOrElse(4711,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])
    (staticRoute(root, DashboardPage$) ~> renderR(ctl => x0(proxy => Dashboard(ctl, proxy,QuantityUnitPage$)))
    | staticRoute("#art", ArticlePage$) ~> renderR(ctl =>x71(p71 =>(ARTICLE(p71))))

     // | staticRoute("#art", ArticlePage$) ~> renderR(ctl => SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).asInstanceOf[Pot[Data]])(ARTICLE(_:ModelProxy[Pot[Data]])))

      |staticRoute("#ord", POrderPage$) ~> renderR(ctl => x101 ( proxy101 => (x7 ( proxy7=> (PURCHASEORDER(proxy101,proxy7))))))


      | staticRoute("#qty", QuantityUnitPage$) ~> renderR(ctl => x4(p4=>(z4(p4))))
     // | staticRoute("#acc", AccountPage$) ~> renderR(ctl => SPACircuit.connect(_.store.get.models.getOrElse(9,Ready(Data(List(Account())))).asInstanceOf[Pot[Data]])(ACCOUNT(_)))

      | staticRoute("#acc", AccountPage$) ~> renderR(ctl => x9(p9 =>(ACCOUNT(p9))))
      | staticRoute("#todo", TodoPage$) ~> renderR(ctl => x4711(p4711 =>(Todo(p4711))))
      | staticRoute("#cust", CustomerPage$) ~> renderR(ctl => x3(p3 =>(CUSTOMER(p3))))

      //| staticRoute("#cust", CustomerPage$) ~> renderR(ctl => SPACircuit.connect(_.store.get.models.getOrElse(3,Ready(Data(List(Customer())))).asInstanceOf[Pot[Data]])(CUSTOMER(_)))
      | staticRoute("#cat", CategoryPage$) ~> renderR(ctl => x8(p8 => (z0(p8))))
     // | staticRoute("#ord", POrderPage$) ~> renderR(ctl => SPACircuit.connect(_.store.get.models.getOrElse(101,Ready(Data(List(PurchaseOrder[LinePurchaseOrder]())))).asInstanceOf[Pot[Data]])(PURCHASEORDER(_)))
      | staticRoute("#sup", SupplierPage$) ~> renderR(ctl => x1(p1 =>(z1(p1))))

      | staticRoute("#cost", CostCenterPage$) ~> renderR(ctl => x6(p6 =>(z6(p6))))
      ).notFound(redirectToPage(DashboardPage$)(Redirect.Replace))
   // ).notFound(redirectToPage(Home)(Redirect.Replace))
  }.renderWith(layout)

  // base layout for all pages
    def layout(c: RouterCtl[Page], r: Resolution[Page]) = {
   // val x4 =  SPACircuit.connect(_.store.get.models.getOrElse(4,
    //                 Ready(Data(List(QuantityUnit())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(_)
    /*
    <.div(
            SPACircuit.connect(_.store.get.models.getOrElse(1,Ready(Data(List(Supplier())))))(proxy => {
               SPACircuit.connect(_.store.get.models.getOrElse(3,Ready(Data(List(Customer())))))(proxy1 => {
                // SPACircuit.connect(_.store.get.models.getOrElse(9,Ready(Data(List(Account())))))(proxy2 => {
                   type T = (Boolean, String, String)
                   val menu1 = AccordionMenuItem[T]("collapseOne", "Content", "#collapseOne", true, Seq.empty[AccordionTabItem[T]])
                   val accItem1 = AccordionTabItem[T]("tab2", "Supplier", "#tab2", false,  IWSList.supplierList.buildLines,
                     proxy.asInstanceOf[ModelProxy[Pot[Data]]])
                   val accItem = AccordionTabItem[T]("tab1", "Customer", "#tab1", true,IWSList.customerList.buildLines,
                   proxy1.asInstanceOf[ModelProxy[Pot[Data]]])
                   //val accItem2 = AccordionTabItem[T]("tab3", "Account", "#tab3", false,IWSList.accountList.buildLines,
                  //   proxy2.asInstanceOf[ModelProxy[Pot[Data]]])
                   val menu1x: AccordionMenuItem[T] = menu1.copy(title = "Masterfile")
                  // val menu = menu1x.add(accItem).add(accItem1)
                   //IWSList.customerList(menu, proxy1.asInstanceOf[ModelProxy[Pot[Data]]], Seq(Customer()))
                  customerComponent(menu1x.add(accItem).add(accItem1))
                 //supplierComponent(menu1x.add(accItem).add(accItem1))
               //})

               })
             })
        ) */
      <.div(
     /* <.nav(^.className := "navbar navbar-inverse navbar-fixed-bottom",
        <.div(^.className := "container-fluid",
          //<.div(^.className := "navbar-header", <.span(^.className := "navbar-brand", "SPA Tutorial")),
           <.div(^.className := "collapse navbar-collapse",
           //<.div(^.className := "nav nav-tabs",
            // connect menu to model, because it needs to update when the number of open todos changes
             //val x = SPACircuit.connect(_.messages) and then renderR(r => x(p => SignIn(r, p)))

             //x4(proxy => MainMenu(c, r.page,2,"QuantityUnit", proxy,QuantityUnitPage$,QuantityUnit())),
            // SPACircuit.connect(_.store.get.models.getOrElse(4,Ready(Data(List(QuantityUnit())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,2,"QuantityUnit", proxy,QuantityUnitPage$,QuantityUnit())),

             SPACircuit.connect(_.store.get.models.getOrElse(4,Ready(Data(List(QuantityUnit())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,2,"QuantityUnit", proxy,QuantityUnitPage$,QuantityUnit())),

             SPACircuit.connect(_.store.get.models.getOrElse(9,Ready(Data(List(Account())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,3,"Account", proxy,AccountPage$,Account())),

             SPACircuit.connect(_.store.get.models.getOrElse(3,Ready(Data(List(Customer())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,8,"Customer", proxy,CustomerPage$,Customer())),

             SPACircuit.connect(_.store.get.models.getOrElse(4711,Ready(Data(List(TodoItem())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,1,"Todo", proxy,TodoPage$,TodoItem())),
             SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,4,"Article", proxy,ArticlePage$,Article())),
             SPACircuit.connect(_.store.get.models.getOrElse(8,Ready(Data(List(ArticleGroup())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,5,"Category", proxy,CategoryPage$,ArticleGroup())),
             //SPACircuit.connect(_.store.get.models.getOrElse(101,Ready(Data(List(PurchaseOrder[LinePurchaseOrder]())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,6,"PurchaseOrder", proxy,POrderPage$,PurchaseOrder[LinePurchaseOrder]())),
            /* SPACircuit.connect(_.store.get.models.getOrElse(101,Ready(Data(List(PurchaseOrder[LinePurchaseOrder]())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)
             (proxy =>
               SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)
               (proxy1 =>  PURCHASEORDER(proxy,proxy1))
             ),
             */
             SPACircuit.connect(_.store.get.models.getOrElse(6,Ready(Data(List(CostCenter())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy => MainMenu(c, r.page,7,"CostCenter", proxy,CostCenterPage$,CostCenter()))

          )
        )
     ), */

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
    // send log messages also to the server
    log.enableServerLogging("/logging")
    log.info("This message goes to server as well")


    // create stylesheet
    GlobalStyles.addToDocument()
    // create the router
    log.info("This message before  RouterConfigDsl")
    //val router = Router(BaseUrl.fromWindowOrigin , routerConfig)
    //val router = Router(BaseUrl.until_#, routerConfig)
    val router = Router(BaseUrl.until_#, routerConfig)


    // tell React to render the router in the document body
    ReactDOM.render(router(), dom.document.getElementById("root"))

    //AppRouter.router().render(dom.document.getElementById("root"))
  }
}
