package com.kabasoft.iws.client.components

import com.kabasoft.iws.shared.Account
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles,Icon}
import scalacss.ScalaCssReact._



object AccountList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class AccountListProps(items: Seq[Account],
                              editCB: Option[ Account => Callback]=None,
                              deleteCB:  Option[Account => Callback]=None)

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
        //List(<.div(bss.formGroup,
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",

          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10),
          <.span(item.description ,^.paddingLeft:=10),
          //<.span(Utils.format(item.dateOfOpen.get),^.paddingLeft:=10),
          <.span("%06.2f".format(item.balance.amount.toDouble),^.paddingLeft:=10),
          tag
          )
      }
      <.ul(style.listGroup)(p.items.sortBy(_.id) map renderItem)
    })
    .build
  def apply(items: Seq[Account]) =
    AccountList(AccountListProps(items))

  def apply(items: Seq[Account], editCB: Option[Account => Callback], deleteCB: Option[Account => Callback]) =
    AccountList(AccountListProps(items,  editCB, deleteCB))
}
