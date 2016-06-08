package com.kabasoft.iws.client.modules

//import com.kabasoft.iws.gui.macros.{IWSList, MasterDetails}
import com.kabasoft.iws.shared._
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
//import com.kabasoft.iws.shared.Lenses._
import com.kabasoft.iws.gui.macros._
//import monocle.macros.GenLens

object SUPPLIER {

  /*@inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(selectedItem: Option[Customer] = None)
  class Backend($: BackendScope[Props, State]) {
   // def mounted(props: Props) =
   //   Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(Supplier())))

    def render(p: Props, s: State) = {
      type T = (Boolean, String, String)

    val accItem = AccordionTabItem[T]("tab2", "Supplier", "#tab2", true, MasterDetails.supplierComponent.buildTabContent, IWSList.supplierList.buildLines, p.proxy)
    val menu1x = AccordionMenuItem("collapseOne", "Content", "#collapseOne", true, Seq(accItem))
      MasterDetails.supplierComponent(menu1x)
     //MasterDetails.customerComponent(menu1x, customerList.buildLines, p.proxy)
  }
 
  }

  val component = ReactComponentB[Props]("Supplier")
    //.initialState(State(name="Supplier"))
    .initialState(State())
    .renderBackend[Backend]
    //.componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
  */
}
