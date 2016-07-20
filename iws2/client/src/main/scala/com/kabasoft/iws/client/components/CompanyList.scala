package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._


object CompanyList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(items:Seq[Company], editCB:Company => Callback, deleteCB:Company => Callback)

  private val companyList = ReactComponentB[Props]("CompanyList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Company) = {
        def editButton =  Button(Button.Props(p.editCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        //def deleteButton = Button(Button.Props(p.deleteCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.street ,^.paddingLeft:=5),
          <.span(item.city ,^.paddingLeft:=5),
          <.span(item.state ,^.paddingLeft:=5),
          <.span(item.zip ,^.paddingLeft:=5),
          <.span(item.bankAccountId ,^.paddingLeft:=5),
          <.span(item.purchasingClearingAccountId ,^.paddingLeft:=5),
          <.span(item.salesClearingAccountId ,^.paddingLeft:=5),
          <.span(item.paymentClearingAccountId,^.paddingLeft:=5),
          <.span(item.settlementClearingAccountId ,^.paddingLeft:=5),
          <.span(item.vatId ,^.paddingLeft:=5),
          <.span(item.taxCode ,^.paddingLeft:=5),
          <.span(item.periode ,^.paddingLeft:=5),
          <.span(item.nextPeriode ,^.paddingLeft:=5),
          editButton
          )
      }
      <.ul(style.listGroup)(p.items.sortBy(_.id) map renderItem)
    })
    .build

  def apply(items:Seq[Company], editCB:Company => Callback, deleteCB:Company => Callback) = companyList(Props(items, editCB, deleteCB))
}
