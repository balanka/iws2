package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.CustomerList
import com.kabasoft.iws.gui.macros.IWSList.customerList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.IWSList
//import com.kabasoft.iws.gui.macros.MasterDetails
import com.kabasoft.iws.shared._
//import com.kabasoft.iws.shared.Lenses._
import com.kabasoft.iws.gui.macros.{TabComponent, _}
import com.kabasoft.iws.gui.macros.TabItem
import scalacss.ScalaCssReact._
//import monocle.macros.GenLens

object CUSTOMER {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(selectedItem: Option[Customer] = None, name:String="Customer")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(Customer())))

      def edit(item:Option[Customer]) = {
      $.modState(s => s.copy(selectedItem = item))
    }

    def edit1(item:IWS) = {
      $.modState(s => s.copy(selectedItem = Some(item.asInstanceOf[Customer])))
    }

    def edited(item:Customer) = {
      Callback.log("Customer edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:Customer) = {
      val cb = Callback.log("Customer deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "Customer"))
    }



    //def f [S, A]: (S,A) => S

    // def get: Customer => String
   // def set: String => CUSTOMER.State =>  CUSTOMER.State
    //def set1x: (String, CUSTOMER.State ) =>  CUSTOMER.State

    def updateIt(e: ReactEventI, set: (String, CUSTOMER.State ) =>  CUSTOMER.State) = {
      val r = e.target.value
      $.modState(s=>(set (r,s) ))
    }

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(name = r))))
    }
    def updateStreet(e: ReactEventI) = {
      val r1 = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(street = r1))))
    }
    def updateCity(e: ReactEventI) = {
      val r2 = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(city = r2))))
    }
    def updateState(e: ReactEventI) = {
      val r3 = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(state = r3))))
    }
    def updateZip(e: ReactEventI) = {
      val r4 = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(zip = r4))))
    }
    
    
    case class Address(id:String, street:String ="", zipCode:String ="", city:String ="", country:String="DE", route:String, title:String)
     def renderItem(item: Customer) : ReactElement =
        <.li(bss.listGroup.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.street ,^.paddingLeft:=5),
          <.span(item.city ,^.paddingLeft:=5),
          <.span(item.zip ,^.paddingLeft:=5)
        )

     def  m :(String , CUSTOMER.State) =>  CUSTOMER.State = (p, s) => s.copy(s.selectedItem.map( z => z.copy(id = p)))

      def buildForm(s:State): ReactElement =
        //<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(^.height := 20,
                   buildWItem[String]("id", s.selectedItem.map(_.id),"id",updateId),
                   buildWItem[String]("name", s.selectedItem.map(_.name), "", updateName),
                   buildWItem[String]("street", s.selectedItem.map(_.street), "", updateStreet)
                 ),
              <.tr(^.height := 20,
                    buildWItem[String]("city", s.selectedItem.map(_.city), "", updateCity),
                    buildWItem[String]("state", s.selectedItem.map(_.state), "", updateState),
                    buildWItem[String]("zip", s.selectedItem.map(_.zip), "", updateZip)
                 )
            )
          )

    /*type T = (Boolean, String, String)
  type R = MasterDetails.customerComponent.Props


    val l1 = Seq(AccordionTabItem[T, R]("tab1", "Customer", "#tab1", true,MasterDetails.customerComponent.buildTabContent),
                  AccordionTabItem[T,R]("tab2", "News", "#tab2", false,MasterDetails.customerComponent.buildTabContent),
                  AccordionTabItem[T,R]("tab3", "Newsletters", "#tab3", false,MasterDetails.customerComponent.buildTabContent))
    //val l2= Seq( AccordionTabItem[AccordionTabItem]("tab4","Orders" ,"#tab4", false,buildTabContent2), AccordionTabItem("tab5","Invoices","#tab5", false, buildTabContent2),AccordionTabItem ("tab6","Shipments","#tab6", false,buildTabContent2))
    val menu1 = AccordionMenuItem("collapseOne", "Content", "#collapseOne", true, l1)
    //val menu2 = AccordionMenuItem ("collapseTwo", "Modules","#collapseTwo", false, l2)
    val menu = Seq(menu1)
    */

     def render(p: Props, s: State) ={
       def saveButton = Button(Button.Props(edited(s.selectedItem.getOrElse(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
       Panel(Panel.Props("Customer"), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
         p.proxy().renderFailed(ex => "Error loading"),
         p.proxy().renderPending(_ > 500, _ => "Loading..."),
         AccordionPanel("Edit", Seq(buildForm(s)), List(newButton,saveButton)),
         p.proxy().render(
           all => CustomerList(all.items.asInstanceOf[List[Customer]],
             //item => p.proxy.dispatch(Update(item.asInstanceOf[Customer])),
             item => edit(Some(item.asInstanceOf[Customer])), item => p.proxy.dispatch(Delete[Customer](item.asInstanceOf[Customer]))))
         //TabComponent(Seq(item1, item2, item3)))
       ))
     }



   /* def render(p: Props, s: State) = {
      type T = (Boolean, String, String)

    val accItem = AccordionTabItem[T]("tab1", "Customer", "#tab1", true, MasterDetails.customerComponent.buildTabContent, IWSList.customerList.buildLines, p.proxy)
    val menu1x = AccordionMenuItem("collapseOne", "Content", "#collapseOne", true, Seq(accItem))
      MasterDetails.customerComponent(menu1x)
     //MasterDetails.customerComponent(menu1x, customerList.buildLines, p.proxy)
  } */
 
  }

  val component = ReactComponentB[Props]("Customer")
    //.initialState(State(name="Customer"))
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
