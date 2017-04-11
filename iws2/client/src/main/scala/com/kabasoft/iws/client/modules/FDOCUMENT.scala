package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.modules.Utilities.{Props, State}
import com.kabasoft.iws.shared.{Store, _}
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._


object FDOCUMENT  {

  val component = ReactComponentB[Props[FDocument[LineFDocument],LineFDocument, Store, Supplier]]("FDocument")
    .initialState(State[FDocument[LineFDocument]]())
     .renderBackend[SupplyBackend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]],modelId:Int,otransModelId:Int,storeModelId:Int,accountModelId:Int,
             instance:FDocument[LineFDocument],  instance2:LineFDocument, store:Store, supplier:Supplier, title:String) =
    component(Props(proxy, modelId, otransModelId, storeModelId,accountModelId, instance, instance2, store, supplier, title))
}





