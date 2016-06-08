package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.QuantityUnitList
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._

object IWSBase  extends IWSBackendTrait [QuantityUnit]{

 // case class Props(proxy: ModelProxy[Pot[Data]],iws:IWS)
  case class State(selectedItem: Option[QuantityUnit] = None, showForm: Boolean = false)
  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh[QuantityUnit](props.iws.asInstanceOf[QuantityUnit])))

    def edit(item: Option[QuantityUnit]) =
      $.modState(s => s.copy(selectedItem = item, showForm = true))

    def edited(item: QuantityUnit, cancelled: Boolean) = {
      val cb = if (cancelled) {
        Callback.log("CostCenter editing cancelled")
      } else {
        Callback.log(s"CostCenter edited: $item") >>
          $.props >>= (_.proxy.dispatch(Update(item)))
      }
      // hide the edit dialog, chain callbacks
      cb >> $.modState(s => s.copy(showForm = false))
    }

    def render(p: Props, s: State) =
      Panel(Panel.Props("What needs to be done"), <.div(
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(todos => QuantityUnitList(todos.items, item => p.proxy.dispatch(Update[QuantityUnit](item.asInstanceOf[QuantityUnit])),
          item => edit(Some(item.asInstanceOf[QuantityUnit])), item => p.proxy.dispatch(Delete[QuantityUnit](item.asInstanceOf[QuantityUnit])))),
        Button(Button.Props(edit(None)), Icon.plusSquare, " New")),

        if (s.showForm) MasterfileForm(MasterfileForm.Props(s.selectedItem, edited))
        else
          Seq.empty[ReactElement])
  }

  // create the React component for To Do management
  val component = ReactComponentB[Props]("TODO")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy,QuantityUnit()))






}

