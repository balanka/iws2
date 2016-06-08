package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.AccountList
import com.kabasoft.iws.gui._
import com.kabasoft.iws.shared.Account
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.ExternalVar
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._
import scalacss.ScalaCssReact._

object ACCOUNT {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(selectedItem: Option[Account] = None, name:String)


  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh[Account](Account())))

    def edit(item:Option[Account]) = {
      $.modState(s => s.copy(selectedItem = item))
    }
    def edited(item:Account) = {
      Callback.log("Account edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update[Account](item)))
     }

    def delete(item:Account) = {
      val cb = Callback.log("Account deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
      cb >> $.modState(s => s.copy(name = "Account"))
    }

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(name = r))))
    }
    def updateDescription(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(description = r))))
    }

    val NameChanger = ReactComponentB[ExternalVar[String]]("Name changer")
      .render_P { evar =>
        def updateName = (event: ReactEventI) => evar.set(event.target.value)
        <.input.text(
          ^.value     := evar.value,
          ^.onChange ==> updateName)
      }
      .build

    case class TabItem[A](id: String, title: String, route:String, active:Boolean,body: A => ReactElement, tabItems:Seq[TabItem[A]]=List.empty[TabItem[A]])
    case class Address(id:String, street:String ="", zipCode:String ="", city:String ="", country:String="DE", route:String, title:String)
    // tabbable tabs-left
    def buildAddressTab: ReactElement  =
      <.div(^.cls:="container",
        <.div(^.cls:="row",
          <.div(^.cls:="col-sm-2",
            <.ul(^.id:="nav-tabs-wrapper", ^.cls:="nav nav-tabs nav-pills nav-stacked well",
            //<.ul( ^.cls:="nav nav-pills well",
            <.li(^.cls:="active", <.a(^.href:="#vtab1", "data-toggle".reactAttr:="tab","Home")),
            <.li(<.a(^.href:="#vtab2", "data-toggle".reactAttr:="tab", "Delivery")),
            <.li(<.a(^.href:="#vtab3", "data-toggle".reactAttr:="tab", "Billing"))
          )
        ),
          <.div(^.cls:="col-sm-9",
            <.div(^.cls:="tab-content",
              buildAddress(Address("vtab1","Meisen Str 96","33967","Bielefeld","DE","#vtab1","Home Address"),true),
              buildAddress(Address("vtab2","Meisen Str2 96","33967","Bielefeld2","DE","#vtab2","Delivery Address")),
              buildAddress(Address("vtab3","Meisen Str3 96","33967","Bielefeld3","DE","#vtab3","Billing Address"))
          )
        )
      )
    )

    def buildAddress(adr: Address , active:Boolean=false): ReactElement  = {
      var titleTag = "tab-pane fade"
     if (active) titleTag = "tab-pane fade in active"
      <.div(^.role := "tabpanel", ^.cls := titleTag, ^.id := adr.id,
        <.table(^.cls := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 20,
              buildItem("street",adr.street),
              buildItem("zipCode",adr.zipCode)),
            <.tr(bss.formGroup, ^.height := 20,
              buildItem("city", adr.city),
              buildItem("country",adr.country))
           ))
      )
    }
 def get(a:Address) :String = a.city

    def buildForm(s: State): Seq[ReactElement] =
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 20,
              buildWItem("id", s.selectedItem.map(_.id), updateId),
              buildWItem("name", s.selectedItem.map(_.name), updateName),
              buildWItem("description", s.selectedItem.map(_.description), updateDescription))
           )
         )
        )
      )

    def buildWItem[A](id:String , value:Option[String],evt:ReactEventI=> Callback) =
     List( <.td(<.label(^.`for` := id, id)),
           <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
                   ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10))

    def buildItem(id:String , value:String) =
      List( <.td(<.label(^.`for` := id, id)),
            <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
                               ^.placeholder := id),^.paddingLeft := 1))

    def render(p: Props, s: State) =
      Panel(Panel.Props("Account"), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        p.proxy().render(_ => AccordionPanel("Edit", buildForm(s),
                        List(Button(Button.Props(edited(s.selectedItem.getOrElse(Account())),
                             addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save"),
                             Button(Button.Props(edit(Some(Account())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")))),
          p.proxy().render(
          all => AccountList(all.items.asInstanceOf[List[Account]],
          item => p.proxy.dispatch(Update(item)),
          item => edit(Some(item)), item => p.proxy.dispatch(Delete[Account](item)))),
        buildAddressTab
      ))
        //,<.div(^.className := "panel-footer","panel-footer"))
  }

  // create the React component for To Do management
  val component = ReactComponentB[Props]("Account")
    .initialState(State(name="Account"))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
