package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.GlobalStyles
import com.kabasoft.iws.shared.{Article, IWS, Masterfile}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}

import scala.scalajs.js
import scalacss.ScalaCssReact._


object ArticleList {
  @inline private def bss = GlobalStyles.bootstrapStyles
  //val formater =NumberFormat.getIntegerInstance(new java.util.Locale("de", "DE"))
  case class ArticleListProps(items: Seq[IWS],
    stateChange: IWS => Callback,
    editItem: IWS => Callback,
    deleteItem: IWS => Callback
  )

  def updateItem1(l: String) = {
    // def updateItem1(e: ReactEventI) = {
    // val l =Some(e.target.value)
    //log.debug(s"ItemId Key is ${l}  ")
    Callback.log(s"KEY pressed >>>>>>>>>>>>>>>>>>>>>>>> ${l}")
   // $.modState(s => s.copy(item = s.item.map(_.copy(item = Some(l)))))>>$.modState(s => s.copy(search =s.search+l))
    // $.modState(s => s.copy(item =  s.item))
  }
  private val ArticleList = ReactComponentB[ArticleListProps]("ArticleList")
    .render_P(p => {
      val style = bss.listGroup
      def buildIdNameList (list: Seq[Masterfile]): Seq[String]= list map (iws =>(iws.id+iws.name))
      def renderItem(item: Article) = {
        <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,
          <.span(" "),
          <.span(item.id),
          <.span(" "),
          <.span(item.name),
          <.span(" "),
          <.span(item.description),
          <.span(" "),
         // <.span(buildSItem[String]("item",
           // itemsx = buildIdNameList(p.items.asInstanceOf[Seq[Masterfile]]).asInstanceOf[List[String]],
         //   defValue = "0001", evt = updateItem1)),


          // <.s(item.dateOfOpen.get.toString),
         // <.span(" "),
         // <.s("%06.2f".format(item.balance.amount.toDouble)),
          Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), "Edit"),
          Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS,bss.buttonOpt(CommonStyle.danger))), "Delete")
        )
      }
      <.ul(style.listGroup)(p.items.asInstanceOf[Seq[Article]].sortBy(_.id) map renderItem)
    })
    .build

  def apply(items: Seq[IWS], stateChange: IWS => Callback, editItem: IWS => Callback, deleteItem: IWS => Callback) = {
    ArticleList(ArticleListProps(items, stateChange, editItem, deleteItem))

  }
}
