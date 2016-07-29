package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object VendorInvoiceList {
  @inline private def bss = GlobalStyles.bootstrapStyles


  case class Props(items: Seq[VendorInvoice[LineVendorInvoice]],
                   edit: VendorInvoice[LineVendorInvoice] => Callback,
                   delete: VendorInvoice[LineVendorInvoice] => Callback)
  private val vendorInvoiceList = ReactComponentB[Props]("VendorInvoiceList")
    .render_P(p => {
      val style = bss.listGroup

      def renderItem(trans:VendorInvoice[LineVendorInvoice]) = {
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
      <.ul(style.listGroup)(renderHeader(Inv_Trans_Headers))(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build

  def apply(items: Seq[VendorInvoice[LineVendorInvoice]],
            edit: VendorInvoice[LineVendorInvoice] => Callback,
            delete: VendorInvoice[LineVendorInvoice] => Callback) = vendorInvoiceList(Props(items,edit,delete))

}
