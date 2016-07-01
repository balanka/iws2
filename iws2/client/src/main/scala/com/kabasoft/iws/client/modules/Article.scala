package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.ArticleList
import com.kabasoft.iws.gui._
import com.kabasoft.iws.shared.{Article, Data}
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.IWSCircuit

import scalacss.ScalaCssReact._

object ARTICLE {

  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Article] = None, name:String)
  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback {
        IWSCircuit.dispatch(Refresh(Article()))
      }

    def edit(itemx:Option[Article]) = {
      $.modState(s => s.copy(item = itemx))
    }
    def edited(itemx:Article) = {
      $.props >>= (_.proxy.dispatch(Update(itemx)))
    }

    def delete(itemx:Article) = {
      $.props >>= (_.proxy.dispatch(Delete(itemx)))
    }

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(name = r))))
    }
    def updateDescription(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(description = r))))
    }
    def updateGroupId(e: ReactEventI) = {
      val id= e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(groupId = Some(id)))))
    }


    def buildFormTab(p: Props, s: State): Seq[ReactElement] = {
      val subArticles = s.item.getOrElse(Article()).articles.getOrElse(List.empty[Article])
      List(<.div(bss.formGroup,
        TabComponent(Seq(
          TabItem("vtab1", "Form", "#vtab1", true,buildFormTable(s)),
          TabItem("vtab2", "sub account", "#vtab2", false, ArticleList(subArticles))))
      )
      )
    }
    def buildFormTable(s: State) =
      <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
        <.tbody(
          <.tr(bss.formGroup, ^.height := 20.px,
            buildWItem("id", s.item.map(_.id), updateId),
            buildWItem("name", s.item.map(_.name), updateName),
            buildWItem("description", s.item.map(_.description), updateDescription),
            buildWItem("group", s.item.map(_.groupId.getOrElse("groupId")), updateGroupId)
          )
        )
      )


    def buildWItem[A](id:String , value:Option[String],evt:ReactEventI=> Callback) =
      List( <.td(<.label(^.`for` := id, id)),
        <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
          ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10.px))

    def buildItem(id:String , value:String) =
      List( <.td(<.label(^.`for` := id, id)),
        <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
          ^.placeholder := id),^.paddingLeft := 1))

    def render(p: Props, s: State) ={
      val items =  IWSCircuit.zoom(_.store.get.models.get(7)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Article]]
      val saveButton = Button(Button.Props(edited(s.item.getOrElse(Article())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      val newButton=Button(Button.Props(edit(Some(Article())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      Panel(Panel.Props("Account"), <.div(^.className := "panel-heading",^.padding :=0.px), <.div(^.padding :=0.px,
        p.proxy().renderFailed(ex => "Error loading"),
        p.proxy().renderPending(_ > 500, _ => "Loading..."),
        AccordionPanel("Edit", buildFormTab(p,s), List(saveButton, newButton)),
        AccordionPanel("List",
          List(ArticleList(items,
            Some(item => edit(Some(item))),
            Some(item => p.proxy.dispatch(Delete[Article](item))))))
      ))
    }
  }

  val component = ReactComponentB[Props]("Article")
    .initialState(State(name="Article"))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
