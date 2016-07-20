package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared.Bank
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._


object BankList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items: Seq[Bank],
                              editCB: Option[Bank => Callback] = None,
                              deleteCB:  Option[Bank => Callback] = None)

  private val mList = ReactComponentB[Props]("BankList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Bank) = {
        def  f(acc:Bank):Callback = Callback.empty
        def  editCB(acc:Bank):Callback = p.editCB.getOrElse(f(_))(acc)
        def  deleteCB(acc:Bank):Callback = p.editCB.getOrElse(f(_))(acc)
        def editButton =  Button(Button.Props(editCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(deleteCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        var tag = EmptyTag
        if((p.deleteCB !=None) && (p.editCB !=None)) tag = List(editButton , deleteButton)

        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px,^.height:=30.px, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10.px),
          <.span(item.description ,^.paddingLeft:=10.px),
          tag
          )
      }
      <.ul(style.listGroup)(p.items.sortBy(_.id) map renderItem)
    })
    .build
  def apply(items: Seq[Bank]) = mList(Props(items))

  def apply(items: Seq[Bank], editCB: Option[Bank => Callback], deleteCB: Option[Bank => Callback]) = mList(Props(items,  editCB, deleteCB))
}
