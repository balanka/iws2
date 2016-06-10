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

  case class AccountListProps(items: Seq[Account], stateChange: Account => Callback, editItem: Account => Callback, deleteItem: Account => Callback)

  private val AccountList = ReactComponentB[AccountListProps]("AccountList")
    .render_P(p => {

      val style = bss.listGroup
      def renderItem(item: Account) = {
        def editButton =  Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        //List(<.div(bss.formGroup,
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          /* <.table(bss.buttonWarning,^.className := "table-responsive table-condensed",^.tableLayout:="fixed",
             <.tbody(
               //<.tr(bss.formGroup,^.height := 10,//^.fontSize:=10,^.fontWeight:=10,^.maxHeight:=8,
               <.tr(bss.formGroup,^.height := 20,
                 <.td( item.id),
                 <.td( item.description),
                 <.td( item.dateOfOpen.get.toString),
                 <.td( "%06.2f".format(item.balance.amount.toDouble)),
                 //<.td( ^.cls:= "btn btn-default", <.em(^.cls:= "fa fa-edit",Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)))), ^.paddingLeft:=300),
                 <.td(Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS,bss.fa_fa_edit))), ^.paddingLeft:=300),
                 <.td(Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS,bss.fa_fa_delete))),^.marginRight:=1)
               )
             )
           )
          */

          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=10),
          <.span(item.description ,^.paddingLeft:=10),
          //<.span(Utils.format(item.dateOfOpen.get),^.paddingLeft:=10),
          <.span("%06.2f".format(item.balance.amount.toDouble),^.paddingLeft:=10),
          editButton,deleteButton
          )
        //)
       //)

      }
      <.ul(style.listGroup)(p.items.sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[Account], stateChange: Account => Callback, editItem: Account => Callback, deleteItem: Account => Callback) =
    AccountList(AccountListProps(items, stateChange, editItem, deleteItem))
}
