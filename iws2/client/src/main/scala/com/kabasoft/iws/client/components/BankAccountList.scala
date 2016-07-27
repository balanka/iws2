package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.StringUtils._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared.BankAccount
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._


object BankAccountList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[BankAccount],
                              editCB: Option[BankAccount => Callback] = None,
                              deleteCB:  Option[BankAccount => Callback] = None)

  private val mList = ReactComponentB[Props]("BankAccountList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: BankAccount) = {
        def  f(acc:BankAccount):Callback = Callback.empty
        def  editCB(acc:BankAccount):Callback = p.editCB.getOrElse(f(_))(acc)
        def  deleteCB(acc:BankAccount):Callback = p.editCB.getOrElse(f(_))(acc)
        def editButton =  Button(Button.Props(editCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(deleteCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        var tag = EmptyTag
        if((p.deleteCB !=None) && (p.editCB !=None)) tag = List(editButton , deleteButton)

        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px,^.height:=30.px, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10.px),
          <.span(item.description ,^.paddingLeft:=10.px),
          <.span(item.bic ,^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.debit.toDouble),^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.credit.toDouble),^.paddingLeft:=10.px),
          tag
          )
      }
      <.ul(style.listGroup)(renderHeader(BankAccount_headers))(p.items.sortBy(_.id) map renderItem)
    })
    .build
  def apply(items: Seq[BankAccount]) = mList(Props(items))

  def apply(items: Seq[BankAccount], editCB: Option[BankAccount => Callback], deleteCB: Option[BankAccount => Callback]) = mList(Props(items,  editCB, deleteCB))
}
