package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js
import scalacss.ScalaCssReact._

object GoodreceivingList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[Goodreceiving[LineGoodreceiving]],
                   edit: Goodreceiving[LineGoodreceiving] => Callback,
                   delete: Goodreceiving[LineGoodreceiving] => Callback)

  private val goodreceivingList = ReactComponentB[Props]("GoodreceivingList")
    .render_P(p => {
      val style = bss.listGroup

      def renderItem(trans:Goodreceiving[LineGoodreceiving]) = {
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
      <.ul(style.listGroup)(renderHeader(Inv_Trans_Headers,25))(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build

  def apply(ref: js.UndefOr[String] = "GOODRECEIVINGLIST", key: js.Any = {"1041"}, items: Seq[Goodreceiving[LineGoodreceiving]],
            edit:Goodreceiving[LineGoodreceiving] => Callback,
            delete:Goodreceiving[LineGoodreceiving] => Callback) = goodreceivingList.set(key, ref)(Props(items, edit, delete))

}
