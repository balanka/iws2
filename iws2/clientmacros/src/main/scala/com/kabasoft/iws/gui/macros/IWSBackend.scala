package com.kabasoft.iws.gui.macros

import com.kabasoft.iws.gui.macros.Bootstrap.{Panel, CommonStyle, Modal, Button}
import com.kabasoft.iws.shared._
import diode.react.ModelProxy
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.ScalaCssReact._

object IWSMasterfileForm  {

  @inline private def bss = GlobalStyles.bootstrapStyles
  case class Props(item: Option[ArticleGroup], submitHandler: (ArticleGroup, Boolean) => Callback)

  case class State(item: ArticleGroup, cancelled: Boolean = true)

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

    def body(s: State): ReactElement = {
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
     // log.debug(s"User is $${if (s.item.id == "") "adding" else "editing"} a QuantityUnit")
      val headerText = if (s.item.id == "") "Add new QuantityUnit" else "Edit QuantityUnit"

      Modal(Modal.Props(
        header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.h4(headerText)),
        footer = hide => <.span(Button(Button.Props(submitForm() >> hide), "OK")),
        closed = formClosed(s, p)),
        body(s)

      )
    }
  }

  val component = ReactComponentB[Props]("IWSMasterfileForm")
    .initialState_P(p => State(p.item.getOrElse(ArticleGroup())))
    .renderBackend[Backend]
    .build

  def apply(props: Props) = component(props)
}

object MasterfileList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class MasterfileListProps(
              items: Seq[ArticleGroup],
              stateChange: ArticleGroup => Callback,
              editItem: ArticleGroup => Callback,
              deleteItem: ArticleGroup => Callback)

  private val MasterfileList = ReactComponentB[MasterfileListProps]("MasterfileList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: ArticleGroup) = {
        <.li(style.itemOpt(CommonStyle.info),
          <.span(" "),
          <.s(item.id),
          <.span(" "),
          <.span(item.name),
          <.span(" "),
          <.s(item.description),
          Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Edit"),
          Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Delete")
        )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[ArticleGroup]] map renderItem)
    })
    .build

  def apply(items: Seq[ArticleGroup], stateChange: ArticleGroup => Callback, editItem: ArticleGroup => Callback, deleteItem: ArticleGroup => Callback) =
    MasterfileList(MasterfileListProps(items, stateChange, editItem, deleteItem))
}


object IWSBackend extends IWSBackendTrait [ArticleGroup]{

  // case class Props(proxy: ModelProxy[Pot[Data]],instance:t)
   case class State(selectedItem: Option[ArticleGroup] = None, showForm: Boolean = false)
  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback.when(props.proxy().isEmpty)(props.proxy.dispatch(Refresh[ArticleGroup](props.iws)))

    def edit(item: Option[ArticleGroup]) =
      $.modState(s => s.copy(selectedItem = item, showForm = true))

    def edited(item: ArticleGroup, cancelled: Boolean) = {
      val cb = if (cancelled) {
        Callback.log("CostCenter editing cancelled")
      } else {
        Callback.log(s"CostCenter edited: $$item") >>
          $.props >>= (_.proxy.dispatch(Update [ArticleGroup](item)))
      }
      cb >> $.modState(s => s.copy(showForm = false))
    }

    def render(p: Props, s: State) =
      Panel(Panel.Props("What needs to be done"), <.div(
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(todos => MasterfileList(todos.items.asInstanceOf[Seq[ArticleGroup]], item => p.proxy.dispatch(Update[ArticleGroup](item)),
          item => edit(Some(item)), item => p.proxy.dispatch(Delete[ArticleGroup](item)))),
        Button(Button.Props(edit(None)), Icon.plusSquare, " New")),

        if (s.showForm) IWSMasterfileForm(IWSMasterfileForm.Props(s.selectedItem, edited))
        else
          Seq.empty[ReactElement])
  }

  val component = ReactComponentB[Props]("TODO")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy,ArticleGroup()))
}




