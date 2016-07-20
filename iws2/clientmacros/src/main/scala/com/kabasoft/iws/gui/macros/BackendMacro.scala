package com.kabasoft.iws.gui.macros

import com.kabasoft.iws.gui.Utils
import com.kabasoft.iws.shared.IWS
import japgolly.scalajs.react._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot


import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context


object BackendMacro {
  import scala.language.experimental.macros
  def makeBackend[T<:IWS](instance:T):IWSBackendTrait[T] = macro makeBackendImpl[T]
  def makeBackendImpl[T:c.WeakTypeTag] (c: Context)  (instance:c.Expr[T]): c.Tree = {
    val t = c.weakTypeOf[T]
    val n = c.weakTypeOf[T].typeSymbol.name.toTermName.toString
    val a = c.weakTypeOf[T].typeSymbol.name.toTypeName

    import c.universe._
    import diode.react.ReactPot._
    import diode.react._
    import diode.data.Pot
    import diode.react.ModelProxy
    import japgolly.scalajs.react._
    import japgolly.scalajs.react.vdom.prefix_<^._
    import scalacss.ScalaCssReact._
    import com.kabasoft.iws.gui.macros.Bootstrap._
    import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle, Modal, Panel}

    q"""
    object IWSMasterfileForm  {

      @inline private def bss = GlobalStyles.bootstrapStyles
      case class Props(item: Option[$t], submitHandler: ($t, Boolean) => Callback)

      case class State(item: $t, cancelled: Boolean = true)

      class Backend(t: BackendScope[Props, State]) {
        def submitForm(): Callback = {
          t.modState(s => s.copy(cancelled = false))
        }

        def formClosed(state: State, props: Props): Callback =
          props.submitHandler(state.item, state.cancelled)

        def updateId(e: ReactEventI) = {
          val r = e.target.value
          t.modState(s => s.copy(item = s.item.copy(id = r)))
         }
        def updateName(e: ReactEventI) ={
          val r = e.target.value
          t.modState(s => s.copy(item = s.item.copy(name = r)))
        }
        def updateDescription(e: ReactEventI) = {
           val r = e.target.value
          t.modState(s => s.copy(item = s.item.copy(description = r)))
        }
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
          val headerText = if (s.item.id == "") "Add new"  else "Edit "

          Modal(Modal.Props(
            header = hide => <.span(<.button(^.tpe := "button", bss.close, ^.onClick --> hide, Icon.close), <.h4(headerText.concat($n))),
            footer = hide => <.span(Button(Button.Props(submitForm() >> hide), "OK")),
            closed = formClosed(s, p)),
            body(s)

          )
        }
      }

      val component = ReactComponentB[Props]("IWSMasterfileForm")
        .initialState_P(p => State(p.item.getOrElse($instance)))
        .renderBackend[Backend]
        .build

      def apply(props: Props) = component(props)
    }

   object MasterfileList {
           @inline private def bss = GlobalStyles.bootstrapStyles

     case class MasterfileListProps(
       items: Seq[$t],
       stateChange: $t => Callback,
       editItem: $t => Callback,
       deleteItem: $t => Callback)

    private val MasterfileList = ReactComponentB[MasterfileListProps]("MasterfileList")
       .render_P(p => {
       val style = bss.listGroup
       def renderItem(item: $t) = {
          <.li(style.itemOpt(CommonStyle.info), ^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,
          <.span(" "),
          <.span(item.id),
          <.span(" "),
          <.span(item.name),
          <.span(" "),
          <.span(item.description),
          Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), "Edit"),
          Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), "Delete")
          )
         }
        <.ul(style.listGroup)(p.items.asInstanceOf[Seq[$t]].sortBy(_.id) map renderItem)
        })
       .build

     def apply(items: Seq[$t], stateChange: $t => Callback, editItem: $t => Callback, deleteItem: $t => Callback) =
             MasterfileList(MasterfileListProps(items, stateChange, editItem, deleteItem))
       }


      object MasterfileBackend extends IWSBackendTrait [$t]{

       // case class Props(proxy: ModelProxy[Pot[Data]],instance:t)
        case class State(selectedItem: Option[$t] = None, showForm: Boolean = false)
        class Backend($$: BackendScope[Props, State]) {
          def mounted(props: Props) =
            Callback.when(props.proxy().isEmpty) (props.proxy.dispatch(Refresh[$t](props.iws)))

          def edit(item: Option[$t]) =
            $$.modState(s => s.copy(selectedItem = item, showForm = true))

          def edited(item: $t, cancelled: Boolean) = {
            val cb = if (cancelled) {
              Callback.log("CostCenter editing cancelled")
            } else {
              //Callback.log(s"CostCenter edited: item") >>
                $$.props >>= (_.proxy.dispatch(Update [$t](item)))
            }
            cb >> $$.modState(s => s.copy(showForm = false))
          }

          def render(p: Props, s: State) =
            Panel(Panel.Props("What needs to be done"), <.div(
              p.proxy().renderFailed(ex => "Error loading"),
              p.proxy().renderPending(_ > 500, _ => "Loading..."),
              p.proxy().render(todos => MasterfileList(todos.items.asInstanceOf[Seq[$t]], item => p.proxy.dispatch(Update[$t](item.asInstanceOf[$t])),
                item => edit(Some(item)), item => p.proxy.dispatch(Delete[$t](item.asInstanceOf[$t])))),
              Button(Button.Props(edit(None)), Icon.plusSquare, " New")),

              if (s.showForm) IWSMasterfileForm(IWSMasterfileForm.Props(s.selectedItem, edited))
              else
                Seq.empty[ReactElement])
        }

        val component = ReactComponentB[Props]("MasterfileBackend")
          .initialState(State())
          .renderBackend[Backend]
          .componentDidMount(scope => scope.backend.mounted(scope.props))
          .build

        def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy,$instance))
      }

    MasterfileBackend
   """

  }
}

