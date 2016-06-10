package com.kabasoft.iws.gui

import java.time.LocalDate
import java.util.Date

import com.kabasoft.iws.gui.macros.GlobalStyles
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import scalacss.ScalaCssReact._


object Utils{
  @inline private def bss = GlobalStyles.bootstrapStyles

  def format(d:Date) = {
    val START=1900
    val  x=LocalDate.of(d.getYear,d.getMonth, d.getDate)
    x.getDayOfMonth.toString.concat( ".".concat(x.getMonthValue.toString.concat(".").concat((x.getYear()+START).toString)))
  }

  def buildWItem[A](id:String , value:Option[A], defValue:A, evt:ReactEventI=> Callback) = {
    val m = value getOrElse defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.onChange ==> evt, ^.maxHeight:=2.px, ^.paddingLeft := 10.px))
  }

  def buildItem[A](id:String , value:Option[A], defValue:A) = {
    val m = value getOrElse  defValue
    List(<.td(<.label(^.`for` := id, id), ^.maxHeight:=2.px),
      <.td(<.input.text(bss.formControl, ^.id := id, ^.value := m.toString,
        ^.placeholder := id), ^.maxHeight:=2.px,  ^.paddingLeft := 10.px))
  }

}
