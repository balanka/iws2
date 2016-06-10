package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.Defaults._
import scalacss.ScalaCssReact._
import scalacss.mutable.StyleSheet

object PurchaseOrderList {
  @inline private def bss = GlobalStyles.bootstrapStyles


  case class PurchaseOrderListProps(
    items: Seq[PurchaseOrder[LinePurchaseOrder]],
    //change: PurchaseOrder[LinePurchaseOrder] => Callback,
    edit: PurchaseOrder[LinePurchaseOrder] => Callback,
    delete: PurchaseOrder[LinePurchaseOrder] => Callback
  )
  case class PurchaseOrderListProps1(items: Seq[PurchaseOrder[LinePurchaseOrder]])

  private val PurchaseOrderList = ReactComponentB[PurchaseOrderListProps]("PurchaseOrderList")
    .render_P(p => {
      val style = bss.listGroup
      def renderHeader = {
        <.li(style.itemOpt(CommonStyle.info),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30)(
          <.span("  "),
          <.span("ID"),
          <.span(" "),
          <.span("Transid"),
          <.span("    "),
          <.span("Store"),
          <.span("    "),
          <.span("Account")

        )
      }
      def renderItem(trans:PurchaseOrder[LinePurchaseOrder]) = {
        def editButton =  Button(Button.Props(p.edit(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.delete(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30)(
          <.span("  "),
          <.span(trans.id),
          <.span(" "),
          <.span(trans.oid),
          <.span("    "),
          <.span(trans.store),
          <.span("    "),
          <.span(trans.account),
          editButton,deleteButton
         // <.span("    "),
         // <.span(item.quantity.toDouble)
        )
      }
      <.ul(style.listGroup)(renderHeader)(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build
  private val PurchaseOrderList1 = ReactComponentB[PurchaseOrderListProps1]("PurchaseOrderList")
    .render_P(p => {
      val style = bss.listGroup
      def renderHeader = {
        <.li(style.itemOpt(CommonStyle.warning))(
          <.span("  "),
          <.span("ID"),
          <.span(" "),
          <.span("Transid"),
          <.span("    "),
          <.span("Store"),
          <.span("    "),
          <.span("Account")
        )
      }
      def renderItem(trans:PurchaseOrder[LinePurchaseOrder]) = {
        <.li(style.itemOpt(CommonStyle.warning))(
          <.span("  "),
          <.span(trans.id),
          <.span(" "),
          <.s(trans.oid),
          <.span("    "),
          <.span(trans.store),
          <.span("    "),
          <.span(trans.account)
          // <.span("    "),
          // <.span(item.quantity.toDouble)
        )
      }
      <.ul(style.listGroup)(renderHeader)(p.items map renderItem)
    })
    .build
  def apply(items: Seq[PurchaseOrder[LinePurchaseOrder]],
            edit:PurchaseOrder[LinePurchaseOrder] => Callback,
            delete:PurchaseOrder[LinePurchaseOrder] => Callback) =
    PurchaseOrderList(PurchaseOrderListProps(items,  edit, delete))

  def apply(items: Seq[PurchaseOrder[LinePurchaseOrder]]) = PurchaseOrderList1(PurchaseOrderListProps1(items))
}
