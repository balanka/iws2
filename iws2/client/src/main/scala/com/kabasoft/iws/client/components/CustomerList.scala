package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon, Utils}
import com.kabasoft.iws.shared.{Account, _}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._


object CustomerList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[IWS], editItem: IWS => Callback, deleteItem: IWS => Callback)

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
          <.span(item.city ,^.paddingLeft:=10),
          <.span(item.state ,^.paddingLeft:=10),
          <.span(item.zip ,^.paddingLeft:=10),
          editButton,deleteButton
          //Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Edit"),
          //Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Delete")
          )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[Customer]].sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[IWS], editItem: IWS => Callback, deleteItem: IWS => Callback) =
    CustomerList(Props(items, editItem, deleteItem))
}
