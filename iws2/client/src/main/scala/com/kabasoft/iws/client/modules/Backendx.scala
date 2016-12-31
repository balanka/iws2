package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.modules.Utilities.Props
import com.kabasoft.iws.client.modules.Utilities.State
import com.kabasoft.iws.gui.macros.{TabComponent, TabItem}
import japgolly.scalajs.react.{BackendScope, ReactElement}


abstract class Backendx [A, P<:Props, S<:State[A]]($: BackendScope[P, S]) {

    def render(p:P, s:S): ReactElement

  def buildFormTab(p: Props, s: State [A], content1:ReactElement, content2:ReactElement): ReactElement =
      TabComponent(Seq(
        TabItem("vtab1", "List", "#vtab1", true, content1),
        TabItem("vtab2", "Form", "#vtab2", false, content2)
       )
    )

}
