package com.kabasoft.iws.client.modules

import com.kabasoft.iws.shared._
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import com.kabasoft.iws.client.modules.Utilities.Props
import com.kabasoft.iws.client.modules.Utilities.State

object VENDORINVOICE  {

  val component = ReactComponentB[Props]("VendorInvoice")
    .initialState(State[VendorInvoice[LineVendorInvoice]]())
    .renderBackend[InvoiceBackend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]],modelId:Int,otransModelId:Int,storeModelId:Int,accountModelId:Int) =
    component(Props(proxy, modelId, otransModelId, storeModelId,accountModelId))
}
