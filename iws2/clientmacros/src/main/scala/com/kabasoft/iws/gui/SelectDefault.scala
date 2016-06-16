package com.kabasoft.iws.gui

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.{FindAll, Refresh}
import diode.data.{Pot, Ready}
import diode.react.ReactPot._
import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared.{Store => MStore, _}

//import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js


class Select[A] {

  case class Props(label: String, value: A, onChange: A => Callback,
                   proxy: ModelProxy[Pot[Data]], instance:Masterfile, defaultList:List[A])
  /* class Backend (t: BackendScope[Props, _]) {

    def mounted(props: Props) =
      Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(FindAll(props.instance)))

    def onChange(P: Props)(e: ReactEventI) =
      P.onChange(e.target.value.asInstanceOf[A])

    //def buildIdNames (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+iws.name))
    def render(P: Props) = {
      log.debug(s" WWWWWWWWWWWWWWWWW ${P.proxy().get.items}")

      <.div(
        P.proxy().render(
          all =>

            <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := P.value.asInstanceOf[String],
              ^.onChange ==> onChange(P))(
              P.defaultList.map(item => (<.option(item.asInstanceOf[String])))
              //buildIdNames(all.items.asInstanceOf[List[Masterfile]]).map(item => (<.option(item)))
            )
        )
      )
    }
  } */

}

object IWSSelect {

 /*case class Backend(t: BackendScope[Props, _]) {

   def mounted(props: Props) =
     Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(FindAll(props.instance)))

    def onChange(P: Props)(e: ReactEventI) =
      P.onChange(e.target.value)

   def buildIdNames (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+iws.name))
    def render(P: Props) = {
      log.debug(s" WWWWWWWWWWWWWWWWW ${P.proxy().get.items}")

      <.div(
          P.proxy().render(
            all =>

                  <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := P.value,
                    ^.onChange ==> P.onChange)(
                    P.defaultList.map(item => (<.option(item)))
                      //buildIdNames(all.items.asInstanceOf[List[Masterfile]]).map(item => (<.option(item)))
                 )
              )
         )
    }
  }

 // <.label(<.strong("Item")),
 // <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := s.item.map(_.item.getOrElse("item")), ^.onChange ==> updateItem1)(

  def onChange(P: Props)(e: ReactEventI) =
    P.onChange(e.target.value)
  */

  case class Props(label: String, value:String, onChange: String => Callback,
                   proxy: ModelProxy[Pot[Data]], instance:Masterfile, defaultList:List[String])
  def onChange(P: Props)(e: ReactEventI) =
    P.onChange(e.target.value)

 def render(P: Props) = {
   log.debug(s" WWWWWWWWWWWWWWWWW ${P.proxy().get.items}")

   <.div(
     P.proxy().render(
       all =>

         <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := P.value,
           ^.onChange ==> onChange(P))(
           P.defaultList.map(item => (<.option(item)))
           //buildIdNames(all.items.asInstanceOf[List[Masterfile]]).map(item => (<.option(item)))
         )
     )
   )
 }


  val component = ReactComponentB[Props]("Select")
    .stateless
   //.renderBackend[Backend]
      //.componentDidMount(scope => scope.backend.mounted(scope.props))
      .render_P (P =>render(P))
      .build



  def apply(ref: js.UndefOr[String] = "", key: js.Any = {}, label: String,
            value: String, onChange: String => Callback,
            proxy: ModelProxy[Pot[Data]], instance:Masterfile, defaultList:List[String]) = component.set(key, ref)(Props(label, value, onChange,proxy, instance,defaultList))
}