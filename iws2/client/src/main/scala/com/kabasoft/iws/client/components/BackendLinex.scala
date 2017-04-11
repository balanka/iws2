package com.kabasoft.iws.client.components

import com.kabasoft.iws.client.modules.Utilities.{PropsL, StateL}
import japgolly.scalajs.react.{BackendScope, ReactElement}


abstract class BackendLinex [A,B, P<:PropsL[A,B], S<:StateL[B]]($: BackendScope[P, S]) {

  def render(p:P, s:S): ReactElement

}
