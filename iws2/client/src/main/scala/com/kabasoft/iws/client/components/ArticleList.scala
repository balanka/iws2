package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared.{Article, IWS}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}

import scala.scalajs.js
import scalacss.ScalaCssReact._


object ArticleList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  //val formater =NumberFormat.getIntegerInstance(new java.util.Locale("de", "DE"))
  case class Props(items: Seq[IWS],
                   editCB: Option[Article => Callback] = None,
                   deleteCB: Option[Article => Callback] = None
  )

  private val ArticleList = ReactComponentB[Props]("ArticleList")
    .render_P(p => {
      val style = bss.listGroup
      def renderItem(item: Article) = {
        def  f(acc:Article):Callback = Callback.empty
        def  editCB(acc:Article):Callback = p.editCB.getOrElse(f(_))(acc)
        def  deleteCB(acc:Article):Callback = p.editCB.getOrElse(f(_))(acc)
        def editButton =  Button(Button.Props(editCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
        def deleteButton = Button(Button.Props(deleteCB(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        var tag = EmptyTag
        if((p.deleteCB != None) && (p.editCB != None)) tag = List(editButton , deleteButton)
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12.px, ^.fontWeight:=50.px, ^.maxHeight:=30.px,
          <.span(item.id , ^.paddingLeft:=10.px),
          <.span(item.name, ^.paddingLeft:=10.px),
          <.span(item.description, ^.paddingLeft:=10.px),
          <.span(item.qttyUnit, ^.paddingLeft:=10.px),
          <.span(item.packUnit, ^.paddingLeft:=10.px),
          //<.span(item.groupId.get, ^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.price.bigDecimal),^.paddingLeft:=10.px),
          <.span(" "),
          tag
        )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[Article]].sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[Article]) = ArticleList(Props(items))
  def apply(items: Seq[Article], editCB: Option[Article => Callback], deleteCB:  Option[Article => Callback]) = {
    ArticleList(Props(items,  editCB, deleteCB))

  }
}
