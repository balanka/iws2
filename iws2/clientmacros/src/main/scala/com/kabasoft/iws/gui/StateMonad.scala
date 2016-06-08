package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.ScalazReact._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.language.implicitConversions
import scala.scalajs.js


object StateMonad {


  val XList = ReactComponentB[List[String]]("XList")
    .render_P(items =>
      <.ul(items map (<.li(_)))
    ).build

  case class State(items: List[String], text: String)

  //def set [A,B] :A => B => B

  //def ab [A,B] : A => B => ReactEvent=> Callback
  //def f [S]: S => S

  val ST = ReactS.Fix[State]
  //   state type everywhere.
 /* def zz[A](a:A, e: ReactEventI)=
  {

  }
  def acceptChange1(e: ReactEventI,) =
    ST.mod(_.copy(x = e.target.value))

  def acceptChange2(e: ReactEventI,) =
    ST.mod(_.copy(x = e.target.value))
  */
  def acceptChange(e: ReactEventI) =
    ST.mod(_.copy(text = e.target.value))


  def handleSubmit(e: ReactEventI) = (
    ST.retM(e.preventDefaultCB)
      //   with state modification.
      >>
      ST.mod(s => State(s.items :+ s.text, "")).liftCB
    )

  val component = ReactComponentB[Unit]("TodoApp")
    .initialState(State(Nil, ""))
    .renderS(($, s) =>
      <.div(
        <.h3("TODO"),
         XList(s.items),
        <.form(^.onSubmit ==> $._runState(handleSubmit),
          <.input(
            ^.onChange ==> $._runState(acceptChange),
            ^.value := s.text),
          <.button("Add #", s.items.length + 1)))
    ).buildU

  def apply() = component()
}