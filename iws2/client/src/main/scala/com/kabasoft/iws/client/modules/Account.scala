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
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.IWSCircuit

import scalacss.ScalaCssReact._

object ACCOUNT {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(selectedItem: Option[Account] = None, name:String)
  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback {
        IWSCircuit.dispatch(Refresh(Account()))
      }

    def edit(item:Option[Account]) = {
      $.modState(s => s.copy(selectedItem = item))
    }
    def edited(item:Account) = {
      $.props >>= (_.proxy.dispatch(Update[Account](item)))
     }

    def delete(item:Account) = {
        $.props >>= (_.proxy.dispatch(Delete(item)))
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
    def updateGroupId(e: ReactEventI) = {
      val id= e.target.value
      $.modState(s => s.copy(s.selectedItem.map( z => z.copy(groupId = Some(id)))))
    }
    val NameChanger = ReactComponentB[ExternalVar[String]]("Name changer")
      .render_P { evar =>
        def updateName = (event: ReactEventI) => evar.set(event.target.value)
        <.input.text(
          ^.value     := evar.value,
          ^.onChange ==> updateName)
      }
      .build

    def buildFormTab(p: Props, s: State): Seq[ReactElement] = {
      val subAccounts = s.selectedItem.getOrElse(Account()).accounts.getOrElse(List.empty[Account])
      List(<.div(bss.formGroup,
        TabComponent(Seq(
          TabItem("vtab1", "Form", "#vtab1", true,buildFormTable(s)),
          TabItem("vtab2", "sub account", "#vtab2", false, AccountList(subAccounts))))
       )
     )
    }
    def buildFormTable(s: State) =
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 20,
              buildWItem("id", s.selectedItem.map(_.id), updateId),
              buildWItem("name", s.selectedItem.map(_.name), updateName),
              buildWItem("description", s.selectedItem.map(_.description), updateDescription),
              buildWItem("group", s.selectedItem.map(_.groupId.getOrElse("groupId")), updateGroupId)
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

    def render(p: Props, s: State) ={
    val items =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]
    val saveButton = Button(Button.Props(edited(s.selectedItem.getOrElse(Account())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
    val newButton=Button(Button.Props(edit(Some(Account())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      Panel(Panel.Props("Account"), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        AccordionPanel("Edit", buildFormTab(p,s), List(saveButton, newButton)),
        AccordionPanel("List",
          List(AccountList(items,
            Some(item => edit(Some(item))),
            Some(item => p.proxy.dispatch(Delete[Account](item))))))
      ))
     }
  }

  val component = ReactComponentB[Props]("Account")
    .initialState(State(name="Account"))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
