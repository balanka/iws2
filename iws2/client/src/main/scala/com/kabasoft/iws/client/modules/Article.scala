package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.ArticleList
import com.kabasoft.iws.gui._
import com.kabasoft.iws.shared.{Vat, _}
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.IWSCircuit
import scalacss.ScalaCssReact._


object ARTICLE {

  @inline private def bss = GlobalStyles.bootstrapStyles

  implicit def orderingById[A <: Article]: Ordering[A] = {Ordering.by(e => (e.id, e.id))}
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Article] = None, name:String)
  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) = Callback {
        IWSCircuit.dispatch(Refresh(QuantityUnit()))
        IWSCircuit.dispatch(Refresh(Vat()))
        IWSCircuit.dispatch(Refresh(Article()))
     }

    def edit(itemx:Option[Article]) =
      $.modState(s => s.copy(item = itemx))

    def edited(itemx:Article) = {
      log.debug(s" Current state: ${$.state.runNow()} item ${itemx} ")
      //$.props >>= (_.proxy.dispatch(Update(itemx)))
      Callback {
        IWSCircuit.dispatch(Update(itemx))
      }
    }

    def delete(itemx:Article) =
      $.props >>= (_.proxy.dispatch(Delete(itemx)))

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      e.preventDefaultCB >>$.modState(s => s.copy(s.item.map( z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      e.preventDefaultCB >>$.modState(s => s.copy(s.item.map( z => z.copy(name = r))))
    }
    def updateDescription(e: ReactEventI) = {
      val r = e.target.value
      e.preventDefaultCB >>$.modState(s => s.copy(s.item.map( z => z.copy(description = r))))
    }
    def updateGroupId1(e: ReactEventI) = {
      val id= e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(groupId = Some(id)))))
    }
    def updateGroupId(id: String) = {
      val groupId = id.substring(0, id.indexOf(":"))
      log.debug(s"groupId  is ${groupId}  ")
      $.modState(s => s.copy(s.item.map( z => z.copy(groupId = Some(groupId)))))
    }
    def updatePrice(e: ReactEventI) = {
      val price= e.target.value
      e.preventDefaultCB >>$.modState(s => s.copy(s.item.map( z => z.copy(price =BigDecimal(price)))))
    }
    def updateVat(id: String) = {
      val vatId = id.substring(0, id.indexOf(":"))
      log.debug(s"VatId  is ${vatId}  ")
      $.modState(s => s.copy(s.item.map( z => z.copy(vat = Some(vatId)))))
    }

    def updateUnit(id:String) = {
      val qtyUnit = id.substring(0, id.indexOf(":"))
      log.debug(s" Unit is ${qtyUnit}")
      $.modState(s => s.copy(s.item.map( z => z.copy(qttyUnit = qtyUnit))))
    }

    def updatePackUnit(id:String) = {
      val packUnit1 = id.substring(0, id.indexOf(":"))
      log.debug(s" Unit is ${packUnit1}")
      $.modState(s => s.copy(s.item.map( z => z.copy(packUnit = packUnit1))))
    }

    def buildFormTab(p: Props, s: State, items:List[Article]): Seq[ReactElement] = {
     val subArticles = s.item.getOrElse(Article()).articles.getOrElse(List.empty[Article])

      List(<.div(bss.formGroup,
        TabComponent(Seq(
          TabItem("vtab1", "List", "#vtab1", true,ArticleList(items, Some(item => edit(Some(item))), Some(item => p.proxy.dispatch(Delete[Article](item))))),
          TabItem("vtab2", "Form", "#vtab2", false,buildFormTable(s,items)),
          TabItem("vtab3", "group", "#vtab3", false, ArticleList(subArticles))))
        )
      )
    }
    def buildFormTable(s: State, items:List[Article]) = {
      val qttyUnit =  IWSCircuit.zoom(_.store.get.models.get(4)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[QuantityUnit]]
      val vat =       IWSCircuit.zoom(_.store.get.models.get(5)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Vat]]

       <.table(^.className := "table-responsive table-condensed", //^.tableLayout := "fixed",
        <.tbody(
          <.tr(bss.formGroup, ^.height := 10.px,
              buildWItem("id", s.item.map(_.id), "id" , updateId),
              buildWItem1("name", s.item.map(_.name), "name", updateName),
              buildWItem("description", s.item.map(_.description), "description", updateDescription)),
            <.tr(bss.formGroup, ^.height := 10.px,
              buildWItem("price", s.item.map( x => fmt(x.price.bigDecimal)), "0.0", updatePrice),
              buildWItem1("avg price", s.item.map( x => fmt(x.avgPrice.bigDecimal)), "0.0", updatePrice),
              buildWItem("sales price", s.item.map( x => fmt(x.salesPrice.bigDecimal)), "0.0", updatePrice)),
            <.tr(bss.formGroup, ^.height := 10.px,
              buildWItem("group", s.item.map(_.groupId.getOrElse("groupId")), "groupId", evt = noAction ),
              buildWItem("Vat", s.item.map(_.vat.getOrElse("Mwst")), "Mwst", evt = noAction),
              buildWItem("packUnit", s.item.map(_.packUnit), "packUnit", evt = noAction)),
             <.tr(bss.formGroup, ^.height := 10.px,
              buildSItem("group", itemsx = buildIdNameList(items), defValue = "xxxx", evt = updateGroupId),
              buildSItem("q.unit", itemsx = buildIdNameList(qttyUnit), defValue = "KG", evt = updateUnit),
              buildSItem("Vat", itemsx = buildIdNameList(vat), defValue = "7", evt = updateVat),
              buildSItem("packUnit", itemsx = buildIdNameList(qttyUnit), defValue = "KG", evt = updatePackUnit))
          )
      )
    }

    def render(p: Props, s: State) ={
         val article_items =  IWSCircuit.zoom(_.store.get.models.get(7)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Article]].toSet
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(Article())), addStyles = Seq(bss.pullRight, bss.buttonXS,
        bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
      def newButton =  Button(Button.Props(edit(Some(Article())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
      val items = article_items.toList.sorted
        BasePanel("Article", buildFormTab(p,s, items), List(saveButton, newButton))

    }
  }

  val component = ReactComponentB[Props]("Article")
    .initialState(State(name="Article"))
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
