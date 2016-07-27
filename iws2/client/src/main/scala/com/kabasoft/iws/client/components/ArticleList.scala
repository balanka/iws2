package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.{GlobalStyles, Icon}
import com.kabasoft.iws.shared.Article
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}

import scala.scalajs.js
import scalacss.ScalaCssReact._


object ArticleList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  //val formater =NumberFormat.getIntegerInstance(new java.util.Locale("de", "DE"))
  case class Props(items: Seq[Article], fields:Seq[String],
                   editCB: Option[Article => Callback] = None,
                   deleteCB: Option[Article => Callback] = None
  )

  private val ArticleList = ReactComponentB[Props]("ArticleList")
    .render_P(p => {
      val style = bss.listGroup
      val fields = Seq ("Id", "Name", "Description", "Qtty. unit","Pck. unit", "Group","P. Price","Avg Price","Sales price")
      def renderHeader =
        <.li(style.itemOpt(CommonStyle.info),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          p.fields.map( field => (<.span(field ,^.paddingLeft:=10)))
        )

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
          <.span(item.groupId.getOrElse("0").asInstanceOf[String], ^.paddingLeft:=10.px),
          <.span(item.vat.getOrElse("-1").asInstanceOf[String], ^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.price.bigDecimal),^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.avgPrice.bigDecimal),^.paddingLeft:=10.px),
          <.span("%06.2f".format(item.salesPrice.bigDecimal),^.paddingLeft:=10.px),
          <.span(" "),
          tag
        )
      }
      <.ul(style.listGroup)(renderHeader)(p.items.sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[Article], fields:Seq[String]) = ArticleList(Props(items,fields))
  def apply(items: Seq[Article], fields:Seq[String], editCB: Option[Article => Callback], deleteCB:  Option[Article => Callback]) = {
    ArticleList(Props(items,  fields, editCB, deleteCB))

  }
}
