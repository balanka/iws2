package com.kabasoft.iws.gui

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.SPACircuit
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

class Select[A] {
  case class Props(label: String, value: A, onChange: A => Callback, items:List[A])
}

object IWSSelect {

  case class Props(label: String, value:String, onChange: String => Callback, items:List[String])

  def onChange(P: Props)(e: ReactEventI) =
      P.onChange(e.target.value)

 def render(P: Props) = {
   log.debug(s" WWWWWWWWWWWWWWWWW ${SPACircuit.zoom(_.store.get.models.get(7).size)}")
   val  m = SPACircuit.zoom(_.store.get.models.get(7).get.get.items.size)

   log.debug(s" WWWWWWWWWWWWWWWWW ${m}")
   log.debug(s" WWWWWWWWWWWWWWWWW ${P.items}")

   <.div(
         <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := P.value,
           ^.onChange ==> onChange(P))(
           P.items.map(item => (<.option(item)))
           //buildIdNames(all.items.asInstanceOf[List[Masterfile]]).map(item => (<.option(item)))
         )
   )
 }


  val component = ReactComponentB[Props]("Select")
    .stateless
   //.renderBackend[Backend]
      //.componentDidMount(scope => scope.backend.mounted(scope.props))
      .render_P (P =>render(P))
      .build

  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}, label: String,
            value: String, onChange: String => Callback,
            items:List[String]) = component.set(key, ref)(Props(label, value, onChange,items))
}