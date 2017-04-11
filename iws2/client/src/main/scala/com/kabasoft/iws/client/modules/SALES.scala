package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.modules.Utilities.{Props, State}
import com.kabasoft.iws.shared.{Store, _}
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._


object SALES  {

  val component = ReactComponentB[Props[FDocument[LineFDocument],LineFDocument, Store, Customer]]("Settlement")
    .initialState(State[FDocument[LineFDocument]]())
     .renderBackend[SalesBackend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]],modelId:Int,otransModelId:Int,storeModelId:Int,accountModelId:Int,
             instance:FDocument[LineFDocument],  instance2:LineFDocument, store:Store, customer:Customer, title:String) =
    component(Props(proxy, modelId, otransModelId, storeModelId,accountModelId, instance, instance2,store, customer,title))
}







