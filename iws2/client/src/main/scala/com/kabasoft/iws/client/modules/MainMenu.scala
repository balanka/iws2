package com.kabasoft.iws.client.modules

import com.kabasoft.iws.gui.AppRouter.{DashboardPage$, Page}
import com.kabasoft.iws.gui.macros.Icon.Icon
import com.kabasoft.iws.shared.IWS
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.router.RouterCtl
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros.{GlobalStyles,Icon, Refresh}
import scalacss.ScalaCssReact._

object MainMenu {
  // shorthand for styles
  @inline private def bss = GlobalStyles.bootstrapStyles

  case class Props(router: RouterCtl[Page], currentLoc: Page, position:Int, name:String, proxy: ModelProxy[Option[Int]], loc:Page, instance:IWS)
  private case class MenuItem(idx: Int, label: (Props) => ReactNode, icon:Icon, location: Page)

  def buildMenu(props: Props): ReactNode = {
    val count = props.proxy().getOrElse(0)
    val commonStyle = if(props.position >1) {
        bss.labelOpt(CommonStyle.success)
    } else {
      bss.labelOpt(CommonStyle.danger)
    }

    Seq(
      <.span(props.name),
      if (count > 0) <.span(commonStyle , bss.labelAsBadge, count) else <.span()
    )
  }

  private def menuItems (position:Int,loc:Page) = {
    if (position ==1) {
    Seq(
     // MenuItem(100, _ => "XX", Icon.check, Home),
      MenuItem(0, _ => "Dashboard", Icon.dashboard, DashboardPage$),
      MenuItem(position, buildMenu, Icon.check, loc)
    )
  } else {
    Seq(MenuItem(position, buildMenu,"" , loc))
     }
  }

  private class Backend($: BackendScope[Props, Unit]) {
    def mounted(props: Props) =
      // dispatch a message to refresh the todos
      Callback.ifTrue(props.proxy.value.isEmpty, props.proxy.dispatch(Refresh(props.instance)))

    def render(props: Props) = {
      <.ul(bss.navbar)(
        // build a list of menu items
        for (item <- menuItems (props.position,props.loc)) yield {
          <.li(^.key := item.idx, (props.currentLoc == item.location) ?= (^.className := "active"),
            props.router.link(item.location)(item.icon, " ", item.label(props))
          )
        }
      )
    }
  }


  private val component = ReactComponentB[Props]("MainMenu")
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(ctl: RouterCtl[Page], currentLoc: Page, position:Int, name:String, proxy: ModelProxy[Option[Int]], loc:Page, instance:IWS): ReactElement =
    component(Props(ctl, currentLoc,position, name,  proxy,loc,instance))
}
