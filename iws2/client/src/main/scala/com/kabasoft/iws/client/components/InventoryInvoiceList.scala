package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scalacss.ScalaCssReact._

object InventoryInvoiceList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[InventoryInvoice[LineInventoryInvoice]],
                   edit: InventoryInvoice[LineInventoryInvoice] => Callback,
                   delete: InventoryInvoice[LineInventoryInvoice] => Callback)

  private val inventoryInvoiceList = ReactComponentB[Props]("InventoryInvoiceList")
    .render_P(p => {
      val style = bss.listGroup

      def renderItem(trans:InventoryInvoice[LineInventoryInvoice]) = {
        def editButton =  Button(Button.Props(p.edit(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.delete(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px)(
          <.span("  "),
          <.span(trans.id),
          <.span(" "),
          <.span(trans.oid),
          <.span("    "),
          <.span(trans.store),
          <.span("    "),
          <.span(trans.account),
          editButton,deleteButton
        )
      }

      // <.ul(style.listGroup)(renderHeader)( p.items.filter(_.tid >=52).sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
      <.ul(style.listGroup)(renderHeader(LineInv_Trans_Headers))(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build

  def apply(ref: js.UndefOr[String] = "InventoryInvoiceList", key: js.Any = {"1101"}, items: Seq[InventoryInvoice[LineInventoryInvoice]],
            edit:InventoryInvoice[LineInventoryInvoice] => Callback,
            delete:InventoryInvoice[LineInventoryInvoice] => Callback) = inventoryInvoiceList.set(key, ref)(Props(items, edit, delete))

}
