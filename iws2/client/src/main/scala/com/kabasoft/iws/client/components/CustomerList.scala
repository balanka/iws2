package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared.Customer
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._


object CustomerList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[Customer], editItem: Customer => Callback, deleteItem: Customer => Callback)

  private val CustomerList = ReactComponentB[Props]("CustomerList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Customer) = {
        def editButton =  Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10),
          <.span(item.street ,^.paddingLeft:=10),
          <.span(item.zip ,^.paddingLeft:=10),
          <.span(item.city ,^.paddingLeft:=10),
          <.span(item.state ,^.paddingLeft:=10),
          <.span(item.accountId ,^.paddingLeft:=10),
          editButton,deleteButton
          )
      }
      <.ul(style.listGroup) (renderHeader(BusinessPartner_headers))(p.items.sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[Customer], editItem: Customer => Callback, deleteItem: Customer => Callback) =
    CustomerList(Props(items, editItem, deleteItem))
}
