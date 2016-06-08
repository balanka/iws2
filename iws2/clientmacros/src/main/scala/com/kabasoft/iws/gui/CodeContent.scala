package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.document
import org.scalajs.dom.ext.PimpedNodeList

/**
 * Created by weiyin on 16/03/15.
 */
object CodeContent {

  case class Content(scalaSource: String, el: ReactNode, exampleClasses: String = "") {
    def apply() = component(this)
  }

  case class State(showCode: Boolean = false)
  class Backend(scope: BackendScope[ Content, State]) {

    def toggleCodeMode(e: ReactEvent) = {
      e.preventDefault()
      scope.modState(s => s.copy(showCode = !s.showCode))
    }

    def render(p: Content, state:State) = {
      <.div(^.className := "playground",
        <.div(^.className := s"bs-example ${p.exampleClasses}",
          <.div(p.el)),
        if (state.showCode)
          <.div(
            <.pre(<.code(p.scalaSource.trim)),
            <.a(^.className := "code-toggle", ^.onClick ==> toggleCodeMode, ^.href := "#", "Hide code")
          )
        else
          <.a(^.className := "code-toggle", ^.onClick ==> toggleCodeMode, ^.href := "#", "Show code")

      )
    }
  }

  def applySyntaxHighlight() = {
    import scala.scalajs.js.Dynamic.{global => g}
    val nodeList = document.querySelectorAll("pre code").toArray
    nodeList.foreach(n => g.hljs.highlightBlock(n))
  }

  val component = ReactComponentB[Content]("CodeContent")
    .initialState(State())
    .renderBackend[Backend]
    .build


    //.componentDidMount(_ => applySyntaxHighlight())
    //.componentDidUpdate((P, C, S) => applySyntaxHighlight())
    //.build
    def apply(scalaSource: String, el: ReactNode, exampleClasses: String = "") = component(Content(scalaSource,el,exampleClasses))
}

