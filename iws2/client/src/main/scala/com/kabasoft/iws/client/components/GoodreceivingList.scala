package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object GoodreceivingList {
  @inline private def bss = GlobalStyles.bootstrapStyles


  case class Props(items: Seq[Goodreceiving[LineGoodreceiving]],
                   edit: Goodreceiving[LineGoodreceiving] => Callback,
                   delete: Goodreceiving[LineGoodreceiving] => Callback)

  private val goodreceivingList = ReactComponentB[Props]("GoodreceivingList")
    .render_P(p => {
      val style = bss.listGroup
      log.debug(s" Goodreceiving list items >>>>>>>>>>>>>>>>>>>>>>>> ${p.items}")
      def renderHeader = {
        <.li(style.itemOpt(CommonStyle.info),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px)(
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
      def renderItem(trans:Goodreceiving[LineGoodreceiving]) = {
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
          // <.span("    "),
          // <.span(item.quantity.toDouble)
        )
      }
      log.debug(s" FFFFFFFFFFFFFFFFFFF ${p.items}")
      //<.ul(style.listGroup)(renderHeader)
      // <.ul(style.listGroup)(renderHeader)( p.items.filter(_.tid >=52).sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
      <.ul(style.listGroup)(renderHeader)(p.items.sortBy(_.tid)(Ordering[Long].reverse) map renderItem)
    })
    .build

  def apply(items: Seq[Goodreceiving[LineGoodreceiving]],
            edit:Goodreceiving[LineGoodreceiving] => Callback,
            delete:Goodreceiving[LineGoodreceiving] => Callback) = GoodreceivingList(Props(items, edit, delete))

}