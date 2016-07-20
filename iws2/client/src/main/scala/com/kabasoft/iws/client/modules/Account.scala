package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.AccountList
import com.kabasoft.iws.gui.Utils._
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
  @volatile var itemsx = Set.empty[Account]
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Account] = None, name:String)
  class Backend($: BackendScope[Props, State]) {
    implicit def orderingById[A <: Account]: Ordering[A] = {Ordering.by(e => (e.id, e.id))}
    def mounted(props: Props) = Callback {
        IWSCircuit.dispatch(Refresh(Account()))
      }

    def edit(item:Option[Account]) = {
      $.modState(s => s.copy(item = item))
    }
    def edited(item:Account) = {
     // $.props >>= (_.proxy.dispatch(Update[Account](item)))
      Callback {IWSCircuit.dispatch(Update(item))}
     }

    def delete(item:Account) = {
        $.props >>= (_.proxy.dispatch(Delete(item)))
    }

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map(z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map(z => z.copy(name = r))))
    }
    def updateDescription(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map(z => z.copy(description = r))))
    }

    def updateGroupId(id: String) = {
      val groupId = id.substring(0, id.indexOf(":"))
      log.debug(s"groupId  is ${groupId}  ")
      $.modState(s => s.copy(s.item.map( z => z.copy(groupId = Some(groupId)))))
    }
    val NameChanger = ReactComponentB[ExternalVar[String]]("Name changer")
      .render_P { evar =>
        def updateName = (event: ReactEventI) => evar.set(event.target.value)
        <.input.text(
          ^.value     := evar.value,
          ^.onChange ==> updateName)
      }
      .build

    def buildFormTab(p: Props, s: State, items:List[Account]): Seq[ReactElement] = {
      val subAccounts = s.item.getOrElse(Account()).accounts.getOrElse(List.empty[Account])
      List(<.div(bss.formGroup,
        TabComponent(Seq(
          TabItem("vtab1", "List", "#vtab1", true,
            AccountList(items, Some(item => edit(Some(item))), Some(item => p.proxy.dispatch(Delete[Account](item))))),
          TabItem("vtab2", "Form", "#vtab2", false,buildFormTable(s,items)),
          TabItem("vtab3", "sub", "#vtab3", false, AccountList(subAccounts))

        ))
       )
     )
    }
    def buildFormTable(s: State, items:List[Account]) =
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 20,
              buildWItem("id", s.item.map(_.id), "", updateId),
              buildWItem("name", s.item.map(_.name), "", updateName),
              buildWItem("description", s.item.map(_.description),"", updateDescription)),
            <.tr(bss.formGroup, ^.height := 20,
              buildWItem("group", s.item.map(_.groupId.getOrElse("groupId")), "groupId", noAction),
              buildSItem("group", itemsx = buildIdNameList(items), defValue = "-1",  evt = updateGroupId)
            )
           )
         )

    def render(p: Props, s: State) ={
    val itemsx =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]].toSet
    val saveButton = Button(Button.Props(edited(s.item.getOrElse(Account())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
    val newButton=Button(Button.Props(edit(Some(Account())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
    val items = itemsx.toList.sorted
     BasePanel("Account", buildFormTab(p,s,items), List(saveButton, newButton))

     }
  }

  val component = ReactComponentB[Props]("Account")
    .initialState(State(name="Account"))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
