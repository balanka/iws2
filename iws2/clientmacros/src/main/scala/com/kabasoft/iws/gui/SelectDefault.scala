package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scala.scalajs.js

object IWSSelect {

  case class Props(label: String, value:String, onChange: String => Callback, items:List[String])

  def onChange(P: Props)(e: ReactEventI) = P.onChange(e.target.value)
  def render(P: Props) =
     <.div(
         <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := P.value,
           ^.onChange ==> onChange(P))(
           P.items.map(item => (<.option(item)))
         )
      )

  val component = ReactComponentB[Props]("Select")
    .stateless
    .render_P (P =>render(P))
    .build

  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}, label: String,
            value: String, onChange: String => Callback,
            items:List[String]) = component.set(key, ref)(Props(label, value, onChange,items))
}