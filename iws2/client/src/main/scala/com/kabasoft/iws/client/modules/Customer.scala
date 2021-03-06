package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.CustomerList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._

import com.kabasoft.iws.gui.services.IWSCircuit

import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros.{TabComponent, _}
import com.kabasoft.iws.gui.macros.TabItem
import scalacss.ScalaCssReact._


object CUSTOMER {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(selectedItem: Option[Customer] = None, name:String="Customer")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
       Callback {
         IWSCircuit.dispatch(Refresh(Customer()))
       }
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

     def buildFormTab(p: Props, s: State, items:List[Customer]): Seq[ReactElement] =
       List(<.div(bss.formGroup,
         TabComponent(Seq(
           TabItem("vtab1", "List", "#vtab1", true,CustomerList(items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
           TabItem("vtab2", "Form", "#vtab2", false,buildForm(s))))
       ))

     def render(p: Props, s: State) ={
       val items =  IWSCircuit.zoom(_.store.get.models.get(3)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Customer]]
       def saveButton = Button(Button.Props(edited(s.selectedItem.getOrElse(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
         BasePanel("Customer", buildFormTab(p,s, items), List(newButton,saveButton))
     }

  }

  val component = ReactComponentB[Props]("Customer")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
