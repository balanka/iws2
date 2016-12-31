package com.kabasoft.iws.gui

import com.kabasoft.iws.gui.logger._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._


import scala.scalajs.js

object IWSSelect {

  case class Props(label: String, value:String, onChange: String => Callback,  items:List[String])
  case class State(item:String="xxx")
  class Backend($: BackendScope[Props, State]) {

    def edit(itemx: String) = {
      log.debug(" IWSSelect:: edit "+itemx)
      $.modState(s => s.copy(item = itemx))
    }
    def onChange(P: Props)(e: ReactEventI) = {
      val r = e.target.value
      log.debug(" onChange:: "+r)
      edit(r)
      P.onChange(r)
    }
    def render(P: Props, s:State) =
      <.div( //^.padding := "10px",
        <.select(^.padding := "5px", ^.id := "reactselect", ^.value := P.value, ^.fontSize:=12.px,
          ^.onChange ==> {onChange(P) })(
          P.items.map(item => {
            val mx = s.item
            ///log.debug(" IWSSelect:: render mx   %S/%S =  "+mx +"/"+ $.state.runNow().item)
            if(item.equals(mx)) {
            log.debug(" IWSSelect:: render if  "+item)
            <.option(item, ^.value:=item)
          } else {
           // log.debug(" IWSSelect:: render else "+item)
            <.option(item,  ^.value:=item)
          }
  }))
      )
  }

  val component = ReactComponentB[Props]("Select")
    .initialState(State())
    .renderBackend[Backend]
   // .render_P (P =>render(P))
    .build

  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}, label: String,
            value: String, onChange: String => Callback,
            items:List[String]) = component.set(key, ref)(Props(label, value, onChange,items))
}

class  DataList {

  case class Props(label: String, value:String, onChange: String => Callback, items:List[String])
  def onChange(P: Props)(e: ReactEventI) = { val r = e.target.value; log.debug(" DataList:: onChange:: "+r); P.onChange(r)}
  def render(P: Props) =
    <.div( //^.padding := "10px",
      <.input.text( ^.paddingLeft := 5.px, ^.height := 25.px, ^.id := "inp"+P.label,
            ^.name :="inputtxt", ^.value := P.value, ^.list := "datalist"+P.label, ^.onChange ==> {onChange(P) }),
      <.datalist(  ^.paddingLeft := 5.px, ^.height := 25.px, ^.id := "datalist"+P.label, ^.value := P.value, ^.fontSize:=10.px,
        ^.onChange ==> onChange(P))(
        P.items.map(item => (<.option(item,^.value:=item)))
      )
    )

}


object ComboList  extends DataList {

  val component = ReactComponentB[Props]("ComboList")
    .stateless
    .render_P (P =>render(P))
    .build

  def apply(ref: js.UndefOr[String] = "combo", key: js.Any = {"combo"}, label: String,
            value: String, onChange: String => Callback,
            items:List[String]) = component.set(key, ref)(Props(label, value, onChange,items))
}


