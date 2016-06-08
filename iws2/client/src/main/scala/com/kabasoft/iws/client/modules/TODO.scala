package com.kabasoft.iws.client.modules



import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.client.components.TodoList
import com.kabasoft.iws.client.logger._

import com.kabasoft.iws.shared._
import scala.scalajs.js
import scala.scalajs.js.UndefOr
import scalacss.ScalaCssReact._
import com.kabasoft.iws.gui.macros._
//import com.kabasoft.iws.gui.macros.macros._

object Todo {

  //@ComponentNamespace("ReactBootstrap")
  //abstract class ReactBootstrapComponent extends ReactBridgeComponent

  import japgolly.scalajs.react._
  import org.scalajs.dom.html

  import scala.scalajs.js


  case class Props(proxy: ModelProxy[Pot[Data]])

  case class State(selectedItem: Option[TodoItem] = None, showTodoForm: Boolean = false)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      // dispatch a message to refresh the todos, which will cause TodoStore to fetch todos from the server
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh[TodoItem] (TodoItem())))

    def editTodo(item: Option[TodoItem]) =
      // activate the edit dialog
      $.modState(s => s.copy(selectedItem = item, showTodoForm = true))

    def todoEdited(item: TodoItem, cancelled: Boolean) = {
      val cb = if (cancelled) {
        // nothing to do here
        Callback.log("Todo editing cancelled")
      } else {
        Callback.log(s"Todo edited: $item") >>
          $.props >>= (_.proxy.dispatch(Update[TodoItem](item)))
      }
      // hide the edit dialog, chain callbacks
      cb >> $.modState(s => s.copy(showTodoForm = false))
    }

    def render(p: Props, s: State) =
      Panel(Panel.Props("What needs to be done"), <.div(
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(todos => TodoList(todos.items, item => p.proxy.dispatch(Update[TodoItem](item)),
          item => editTodo(Some(item)), item => p.proxy.dispatch(Delete[TodoItem](item)))),
        Button(Button.Props(editTodo(None)), Icon.plusSquare, " New")),
        // if the dialog is open, add it to the panel
        if (s.showTodoForm) TodoForm(TodoForm.Props(s.selectedItem, todoEdited))
        else // otherwise add an empty placeholder
          Seq.empty[ReactElement])
  }

  // create the React component for To Do management
  val component = ReactComponentB[Props]("TODO")
    .initialState(State()) // initial state from TodoStore
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  /** Returns a function compatible with router location system while using our own props */
  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}

object TodoForm {
  // shorthand for styles
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(item: Option[TodoItem], submitHandler: (TodoItem, Boolean) => Callback)

  case class State(item: TodoItem, cancelled: Boolean = true)

  class Backend(t: BackendScope[Props, State]) {
    def submitForm(): Callback = {
      // mark it as NOT cancelled (which is the default)
      t.modState(s => s.copy(cancelled = false))
    }

    def formClosed(state: State, props: Props): Callback =
      // call parent handler with the new item and whether form was OK or cancelled
      props.submitHandler(state.item, state.cancelled)

    def updateDescription(e: ReactEventI) =
      // update TodoItem content
      t.modState(s => s.copy(item = s.item.copy(content = e.target.value)))

    def updatePriority(e: ReactEventI) = {
      // update TodoItem priority
      val newPri = e.currentTarget.value match {
        case p if p == TodoHigh.toString => TodoHigh
        case p if p == TodoNormal.toString => TodoNormal
        case p if p == TodoLow.toString => TodoLow
      }
      t.modState(s => s.copy(item = s.item.copy(priority = newPri)))
    }
    def printSequence(value: js.Array[String]) : Unit = {
      println(s"Value is ${value.mkString(",")}")
    }

    def render(p: Props, s: State) = {
      log.debug(s"User is ${if (s.item.id == "") "adding" else "editing"} a todo")
      val headerText = if (s.item.id == "") "Add new todo" else "Edit todo"
      Modal(Modal.Props(
        // header contains a cancel button (X)
        header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.h4(headerText)),
        // footer has the OK button that submits the form before hiding it
        footer = hide => <.span(Button(Button.Props(submitForm() >> hide), "OK")),
        // this is called after the modal has been hidden (animation is completed)
        closed = formClosed(s, p)),

        <.div(bss.formGroup,
          <.label(^.`for` := "description", "Description"),
          <.input.text(bss.formControl, ^.id := "description", ^.value := s.item.content,
            ^.placeholder := "write description", ^.onChange ==> updateDescription)),
        //<.div(Input(defaultValue = "foo")()),
       // <.div(TagsInput(defaultValue = Seq("foo","bar"))()),
        <.div(bss.formGroup,
          <.label(^.`for` := "priority", "Priority"),
          // using defaultValue = "Normal" instead of option/selected due to React
          <.select(bss.formControl, ^.id := "priority", ^.value := s.item.priority.toString, ^.onChange ==> updatePriority,
            <.option(^.value := TodoHigh.toString, "High"),
            <.option(^.value := TodoNormal.toString, "Normal"),
            <.option(^.value := TodoLow.toString, "Low")
          )
        )
      )
    }
  }
  import japgolly.scalajs.react.vdom.all._
  /*import com.kabasoft.iws.client.modules.Todo.{Button1, ReactMediumEditor, Input, TagsInput}

  def printSequence(value: js.Array[String]) : Unit = {
    println(s"Value is ${value.mkString(",")}")
  }

  val component1 = ReactComponentB[Unit]("ShowcaseApp").render { _ =>
    div(className := "col-sm-10 col-sm-offset-1")(
      h1()(
        "Example components created with ",
        a(href := "https://github.com/payalabs/scalajs-react-bridge")("scalajs-react-bridge")
      ),
      div(className := "well")(
        b("Tags input (open Inspect Element to see callback being called as you make changes)"),
        TagsInput(defaultValue = Seq("some", "default", "values"), onChange = printSequence _)()
      ),
      div(className := "well")(
        ReactMediumEditor(text =
          """
            | <h1>Medium Editor</h1>
            |
            | <p>Click here and start editing.</p>
            |
            | <b>Select some text to see the editor toolbar pop up.</b>
          """.stripMargin, options = Map("buttons" -> js.Array("bold", "italic", "underline", "anchor", "header1", "header2", "quote", "orderedlist", "unorderedlist")))()
      ),
      div(className := "well")(
        b("Bootstrap"),
        Input(placeholder = "This is a bootstrap input", `type` = "text")(),
        Button1()("Bootstrap Button")
      )
    )
  }.build(())
  */

  val component = ReactComponentB[Props]("TodoForm")
    .initialState_P(p => State(p.item.getOrElse(TodoItem("", 0, "", 4711,TodoNormal, false))))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
  //def apply(props: Props) = component1
}