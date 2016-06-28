package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{CustomerList, StoreList}
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

object STORE {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(selectedItem: Option[Store] = None, name:String="Store")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(Store())))

      def edit(item:Option[Store]) = {
      $.modState(s => s.copy(selectedItem = item))
    }

    def edit1(item:IWS) = {
      $.modState(s => s.copy(selectedItem = Some(item.asInstanceOf[Store])))
    }

    def edited(item:Store) = {
      Callback.log("Customer edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:Store) = {
      val cb = Callback.log("Customer deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "Customer"))
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

     def render(p: Props, s: State) ={
       def saveButton = Button(Button.Props(edited(s.selectedItem.getOrElse(Store())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Store())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
       Panel(Panel.Props("Customer"), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
         p.proxy().renderFailed(ex => "Error loading"),
         p.proxy().renderPending(_ > 500, _ => "Loading..."),
         AccordionPanel("Edit", Seq(buildForm(s)), List(newButton,saveButton)),
         p.proxy().render(
           all => StoreList(all.items.asInstanceOf[List[Store]],
             //item => p.proxy.dispatch(Update(item.asInstanceOf[Customer])),
             item => edit(Some(item.asInstanceOf[Store])), item => p.proxy.dispatch(Delete[Store](item.asInstanceOf[Store]))))
         //TabComponent(Seq(item1, item2, item3)))
       ))
     }

  }

  val component = ReactComponentB[Props]("Customer")
    //.initialState(State(name="Customer"))
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
