package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js

object CodeExample {

  object Style {

    val pageBodyContent = Seq(^.borderRadius := "2px",
      ^.boxShadow := "0 1px 4px rgba(223, 228, 228, 0.79)",
      ^.maxWidth := "1024px")

    val contentDemo = Seq(^.padding := "30px")

    val contentCode = Seq(^.borderTop := "solid 1px #e0e0e0"
    )

    val title = Seq(
    ^.paddingBottom := "15px")

  }
  case class Backend($: BackendScope[Props, _]){
    def render(P: Props, C: PropsChildren) = {
      <.div(
        P.title.nonEmpty ?= <.h3(P.title,Style.title),
        <.div(Style.pageBodyContent)(
          <.div(Style.contentDemo, ^.key := "dan")(
            C
          ),
          <.pre(Style.contentCode, ^.key := "code")(
            CodeHighlight(P.code)
          )
        )
      )
    }
  }

  val component = ReactComponentB[Props]("codeexample")
    .renderBackend[Backend]
    .build

  case class Props(code: String,title: String)

  def apply(code:     String,
            title:    String,
            ref:      js.UndefOr[String] = "",
            key:      js.Any = {})
           (children: ReactNode*) =
    component.set(key, ref)(Props(code,title), if (children.size == 1) children.head else children)

}
