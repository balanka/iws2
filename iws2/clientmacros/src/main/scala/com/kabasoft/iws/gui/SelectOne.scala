package com.kabasoft.iws.gui
/*import japgolly.scalajs.react._, vdom.prefix_<^._
import ScalazReact._
import org.scalajs.dom.raw.HTMLSelectElement
import scala.scalajs.js
import scalaz.Equal
import scalaz.effect.IO

object SelectOne {

  object ParseInt {
    def unapply(s: String): Option[Int] =
      try {
        Some(s.toInt)
      } catch {
        case _: java.lang.NumberFormatException => None
      }
  }

  case class Choice[A](value   : A,
                       label   : String,
                       disabled: Boolean) {
    def map[B](f: A => B): Choice[B] = copy(value = f(value))
  }

  case class Props[A](selected: A,
                      choices : Seq[Choice[A]],
                      select  : Option[A => IO[Unit]])

  def Component[A: Equal] =
    ReactComponentB[Props[A]]("SelectOne")
      .render_P(render(_))
      .domType[HTMLSelectElement]
      .build

  def render[A](props: Props[A])(implicit E: Equal[A]): ReactTag = {

    val (options, selectedValue) = {
      var sel = -1
      var i = 0
      var j = js.Array[ReactNode]()
      props.choices.foreach { v =>
        j.push(
          <.option(
            ^.value    := i,
            ^.key      := i,
            ^.disabled := v.disabled,
            v.label))
        if (sel == -1 && E.equal(v.value, props.selected))
          sel = i
        i += 1
      }
      (j, sel)
    }

    def onChange: SyntheticEvent[HTMLSelectElement] => Option[IO[Unit]] =
      e => for {
        i  ← ParseInt unapply e.target.value
        v  = props.choices(i).value
        io ← props.select
      } yield io(v)

    <.select(
      ^.value      := selectedValue,
      ^.disabled   := props.select.isEmpty,
      ^.onChange ~~>? onChange,
     // ^.onChange ==>? onChange,
      options)
  }

  // ===================================================================================================================

  def optional[A](choices  : Seq[Choice[A]],
                  nopLabel : String = ""): Seq[Choice[Option[A]]] = {
    val nop = Choice[Option[A]](None, nopLabel, false)
    choices.foldLeft(Vector(nop))(_ :+ _.map[Option[A]](Some.apply))
  }
} */