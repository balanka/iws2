package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object StoreList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[IWS], editItem: IWS => Callback, deleteItem: IWS => Callback)

  private val storeList = ReactComponentB[Props]("StoreList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Store) = {
        def editButton =  Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10),
          <.span(item.street ,^.paddingLeft:=10),
          <.span(item.city ,^.paddingLeft:=10),
          <.span(item.state ,^.paddingLeft:=10),
          <.span(item.zip ,^.paddingLeft:=10),
          <.span(item.accountId ,^.paddingLeft:=10),
          editButton,deleteButton
          )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[Store]].sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[IWS], editItem: IWS => Callback, deleteItem: IWS => Callback) = storeList(Props(items, editItem, deleteItem))
}
