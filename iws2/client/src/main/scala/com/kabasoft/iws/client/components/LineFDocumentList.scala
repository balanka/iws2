package com.kabasoft.iws.client.components

import com.kabasoft.iws.client.modules.Utilities.{PropsL, StateL}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._

object LineFDocumentList {
  val component = ReactComponentB[PropsL[FDocument[LineFDocument],LineFDocument]]("LineFDocumentList")
    .initialState(StateL[LineFDocument]( None, search ="0"))
    .renderBackend[LineFDocumentBackend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( porder:FDocument[LineFDocument], instance:LineFDocument, newLine:(FDocument[LineFDocument], LineFDocument) =>Callback,
             saveLine:(FDocument[LineFDocument], LineFDocument) =>Callback,
             deleteLine:(FDocument[LineFDocument], LineFDocument) => Callback) = component(PropsL(porder, instance, newLine, saveLine, deleteLine
  ))
}
