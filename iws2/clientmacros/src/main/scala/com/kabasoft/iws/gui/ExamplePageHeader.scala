package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

/**
 * Created by weiyin on 14/03/15.
 */
object ExamplePageHeader {

  case class Props(title: String, subTitle: String) {
    def apply() = component(this)
  }

  val component = ReactComponentB[Props]("ExamplePageHeader")
    .stateless
   // .render_P(Props)
    .render_P((P) => {
    <.div(^.className := "bs-doc-header", ^.id := "content",
      <.div(^.className := "container",
        <.h1(P.title),
        <.p(P.subTitle)
      )
    )
  }).build

}
