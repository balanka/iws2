package com.kabasoft.iws.gui

import java.time.LocalDate
import java.util.Date


import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.{GlobalStyles, Refresh}
import com.kabasoft.iws.shared.{Api, _}
import diode.data.Pot
import diode.react.ModelProxy
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

  def buildSItem[A](id:String,  optValue:Option[ List[A]], defValue:A, evt:String => Callback, proxy1:ModelProxy[Pot[Data]],
                   instance1:Masterfile) =
    List(
     <.td(<.label(^.`for` := id, id), ^.maxHeight:=10.px),
     <.td(
         IWSSelect(label = id, value = defValue.asInstanceOf[String], onChange = evt,proxy = proxy1, instance = instance1, defaultList = optValue.getOrElse(List(defValue.asInstanceOf[String])).asInstanceOf[List[String]])
         //  ^.maxHeight:=10.px,  ^.paddingLeft := 5.px)
       )
    )

}
