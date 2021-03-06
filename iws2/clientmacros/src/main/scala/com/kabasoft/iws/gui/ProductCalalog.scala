package com.kabasoft.iws.gui

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object ProductCalalog {

  case class Product(name: String, price: Double, category: String, stocked: Boolean)
  case class State(filterText: String, inStockOnly: Boolean)

  class Backend($: BackendScope[_, State])  {
    def onTextChange(e: ReactEventI) =
      e.extract(_.target.value)(value =>
        $.modState(_.copy(filterText = value)))

    def onCheckBox(e: ReactEvent) =
      $.modState(s => s.copy(inStockOnly = !s.inStockOnly))
  }

  val ProductCategoryRow = ReactComponentB[String]("ProductCategoryRow")
    .render_P(category => <.tr(<.th(^.colSpan := 2, category)))
    .build

  val ProductRow = ReactComponentB[Product]("ProductRow")
    .render_P(p =>
      <.tr(
        <.td(<.span(!p.stocked ?= ^.color.red, p.name)),
        <.td(p.price))
    )
    .build

  def productFilter(s: State)(p: Product): Boolean =
    p.name.contains(s.filterText) &&
      (!s.inStockOnly || p.stocked)

  val ProductTable = ReactComponentB[(List[Product], State)]("ProductTable")
    .render_P { case (products, state) =>
      val rows = products.filter(productFilter(state))
        .groupBy(_.category).toList
        .flatMap{ case (cat, ps) =>
          ProductCategoryRow.withKey(cat)(cat) :: ps.map(p => ProductRow.withKey(p.name)(p))
        }
      <.table(
        <.thead(
          <.tr(
            <.th("Name"),
            <.th("Price"))),
        <.tbody(
          rows))
    }
    .build

  val SearchBar = ReactComponentB[(State, Backend)]("SearchBar")
    .render_P { case (s, b) =>
      <.form(
        <.input.text(
          ^.placeholder := "Search Bar ...",
          ^.value       := s.filterText,
          ^.onChange   ==> b.onTextChange),
        <.p(
          <.input.checkbox(
            ^.onClick ==> b.onCheckBox),
          "Only show products in stock"))
    }
    .build

  val FilterableProductTable = ReactComponentB[List[Product]]("FilterableProductTable")
    .initialState(State("", false))
    .backend(new Backend(_))
    .renderPS(($, p, s) =>
      <.div(
        SearchBar((s,$.backend)),
        ProductTable((p, s))
      )
    ).build

  val PRODUCTS = List(
    Product("FootBall", 49.99, "Sporting Goods", true),
    Product("Baseball", 9.99, "Sporting Goods", true),
    Product("basketball", 29.99, "Sporting Goods", false),
    Product("ipod touch", 99.99, "Electronics", true),
    Product("iphone 5", 499.99, "Electronics", true),
    Product("Nexus 7", 199.99, "Electronics", true),
    Product("Ipad 8", 299.99, "Healf care", false))
}
