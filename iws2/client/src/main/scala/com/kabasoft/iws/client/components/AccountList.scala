package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.shared.Account
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import scalacss.ScalaCssReact._
import org.widok.moment._

object AccountList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class AccountListProps(items: Seq[Account],
                              editCB: Option[ Account => Callback] = None,
                              deleteCB:  Option[Account => Callback] = None)

  private val AccountList = ReactComponentB[AccountListProps]("AccountList")
    .render_P(p => {

      val style = bss.listGroup
      def renderItem(item: Account) = {
        def  f(acc:Account):Callback = Callback.empty
        def  editCB(acc:Account):Callback = p.editCB.getOrElse(f(_))(acc)
        def  deleteCB(acc:Account):Callback = p.editCB.getOrElse(f(_))(acc)
        def editButton =  Button(Button.Props(editCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(deleteCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        var tag = EmptyTag
        if((p.deleteCB !=None) && (p.editCB !=None)) tag = List(editButton , deleteButton)

        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px,^.height:=30.px, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10.px),
          <.span(item.description ,^.paddingLeft:=10.px),
          <.span( Moment(item.dateOfOpen.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10),
          <.span("%06.2f".format(item.balance.amount.toDouble),^.paddingLeft:=10.px),
          <.span(item.groupId.getOrElse("0").asInstanceOf[String], ^.paddingLeft:=10.px),
          tag
          )
      }
      <.ul(style.listGroup)(renderHeader(Account_headers))(p.items.sortBy(_.id) map renderItem)
    })
    .build
  def apply(items: Seq[Account]) =
    AccountList(AccountListProps(items))

  def apply(items: Seq[Account], editCB: Option[Account => Callback], deleteCB: Option[Account => Callback]) =
    AccountList(AccountListProps(items,  editCB, deleteCB))
}
