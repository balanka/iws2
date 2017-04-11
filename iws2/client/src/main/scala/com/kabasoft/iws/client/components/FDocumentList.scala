package com.kabasoft.iws.client.components

import com.kabasoft.iws.client.modules.Utilities.Prop
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object FDocumentList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  private val list = ReactComponentB[Prop[FDocument[LineFDocument]]]("FDocumentList")
    .render_P(p => {
      val style = bss.listGroup

      def renderItem(trans:FDocument[LineFDocument]) = {
        def editButton =  Button(Button.Props(p.edit(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.delete(trans), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px)(
          <.span(trans.id, ^.padding:=20.px),
          <.span(trans.oid, ^.padding:=20.px),
          <.span(trans.store, ^.padding:=20.px),
          <.span(trans.account, ^.padding:=20.px),
          editButton,deleteButton
        )
      }
      // <.ul(style.listGroup)(renderHeader)( p.items.filter(_.tid >=52).sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
      <.ul(style.listGroup)(renderHeader(Inv_Trans_Headers++ Array(p.title), 15))(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build

  def apply(items: Seq[FDocument[LineFDocument]],
            title:String,
            edit: FDocument[LineFDocument] => Callback,
            delete: FDocument[LineFDocument] => Callback) = list(Prop[FDocument[LineFDocument]](items, title, edit,delete))

}
