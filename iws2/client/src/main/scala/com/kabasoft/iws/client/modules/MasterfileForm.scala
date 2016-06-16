package com.kabasoft.iws.client.modules


import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros.{BootstrapStyles,GlobalStyles,Icon}
import com.kabasoft.iws.client.components._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services._
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.shared._

import scalacss.ScalaCssReact._

  object MasterfileForm  {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(item: Option[IWS], submitHandler: (QuantityUnit, Boolean) => Callback)
  case class State(item: QuantityUnit, cancelled: Boolean = true)

  class Backend(t: BackendScope[Props, State]) {
    def submitForm(): Callback = {
      t.modState(s => s.copy(cancelled = false))
    }

    def formClosed(state: State, props: Props): Callback =
      props.submitHandler(state.item, state.cancelled)

    def updateId(e: ReactEventI) =
      t.modState(s => s.copy(item = s.item.copy(id = e.target.value)))

    def updateName(e: ReactEventI) =
      t.modState(s => s.copy(item = s.item.copy(name = e.target.value)))

    def updateDescription(e: ReactEventI) =
      t.modState(s => s.copy(item = s.item.copy(description = e.target.value)))

    def body (s:State): ReactElement = {
      <.div(bss.formGroup,
        <.label(^.`for` := "id", "id"),
        <.input.text(bss.formControl, ^.id := "id", ^.value := s.item.id,
          ^.placeholder := "write Id", ^.onChange ==> updateId),
        <.label(^.`for` := "name", "Name"),
        <.input.text(bss.formControl, ^.id := "name", ^.value := s.item.name,
          ^.placeholder := "write name", ^.onChange ==> updateName),
        <.label(^.`for` := "description", "Description"),
        <.input.text(bss.formControl, ^.id := "description", ^.value := s.item.description,
          ^.placeholder := "write description", ^.onChange ==> updateDescription))
    }

    def render(p: Props, s: State) = {
      log.debug(s"User is ${if (s.item.id == "") "adding" else "editing"} a QuantityUnit")
      val headerText = if (s.item.id == "") "Add new QuantityUnit" else "Edit QuantityUnit"

      Modal(Modal.Props(
        // header contains a cancel button (X)
        header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.h4(headerText)),
        // footer has the OK button that submits the form before hiding it
        footer = hide => <.span(Button(Button.Props(submitForm() >> hide), "OK")),
        // this is called after the modal has been hidden (animation is completed)
        closed = formClosed(s, p)),
        body(s)

      )
    }
  }

  val component = ReactComponentB[Props]("CostCenterForm")
    .initialState_P(p => State(p.item.getOrElse(QuantityUnit("", "", 4, "")).asInstanceOf[QuantityUnit]))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}