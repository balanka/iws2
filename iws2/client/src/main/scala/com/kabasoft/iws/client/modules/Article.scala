package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.ArticleList
import com.kabasoft.iws.shared.Article
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.client.logger._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._

import scalacss.ScalaCssReact._

object ARTICLE {

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(selectedItem: Option[Article] = None, showForm: Boolean = false)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      // dispatch a message to refresh the todos, which will cause TodoStore to fetch todos from the server
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(Article())))

    def edit(item:Option[Article]) =
      // activate the edit dialog
      $.modState(s => s.copy(selectedItem = item, showForm = true))

    def edited(item:Article, cancelled: Boolean) = {
      val cb = if (cancelled) {
        // nothing to do here
        Callback.log("Article editing cancelled")
      } else {
        Callback.log(s"Article edited: $item") >>
          $.props >>= (_.proxy.dispatch(Update[Article](item)))
      }
      // hide the edit dialog, chain callbacks
      cb >> $.modState(s => s.copy(showForm = false))
    }

    def render(p: Props, s: State) =
      Panel(Panel.Props("What needs to be done"), <.div(
        p.proxy().renderFailed(ex => "Error loading"),
        //p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(todos => ArticleList(todos.items, item => p.proxy.dispatch(Update[Article](item.asInstanceOf[Article])),
          item => edit(Some(item.asInstanceOf[Article])), item => p.proxy.dispatch(Delete[Article](item.asInstanceOf[Article])))),
        Button(Button.Props(edit(None)), Icon.plusSquare, " New")),
        // if the dialog is open, add it to the panel
        if (s.showForm) ArticleForm(ArticleForm.Props(s.selectedItem, edited))
        else // otherwise add an empty placeholder
          Seq.empty[ReactElement])
  }

  // create the React component for To Do management
  val component = ReactComponentB[Props]("Article")
    .initialState(State()) // initial state from TodoStore
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  /** Returns a function compatible with router location system while using our own props */
  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}

object ArticleForm {
  // shorthand for styles
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(item: Option[Article], submitHandler: (Article, Boolean) => Callback)
  case class State(item: Article, cancelled: Boolean = true)

  class Backend(t: BackendScope[Props, State]) {
    def submitForm(): Callback = {
      t.modState(s => s.copy(cancelled = false))
    }

    def formClosed(state: State, props: Props): Callback =
      props.submitHandler(state.item, state.cancelled)

    def updateId(e: ReactEventI) = {
      val r= e.target.value
      t.modState(s => s.copy(item = s.item.copy(id = r)))
    }
    def updateName(e: ReactEventI) ={
      val r= e.target.value
      log.debug(s"Name is ${e.target.value}")
      t.modState(s => s.copy(item = s.item.copy(name = r)))
    }

    def updateDescription(e: ReactEventI) = {
      val r= e.target.value
      log.debug(s"Description is ${e.target.value}")
      t.modState(s => s.copy(item = s.item.copy(description =r)))
    }

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
      log.debug(s"User is ${if (s.item.id == "") "adding" else "editing"} a Article")
      val headerText = if (s.item.id == "") "Add new Article" else "Edit Article"

      Modal(Modal.Props(
        header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.h4(headerText)),
        footer = hide => <.span(Button(Button.Props(submitForm() >> hide), "OK")),
        closed = formClosed(s, p)),
        body(s)

      )
    }
  }

  val component = ReactComponentB[Props]("ArticleForm")
    .initialState_P(p => State(p.item.getOrElse(Article())))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}