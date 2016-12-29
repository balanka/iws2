package com.kabasoft.iws.client.modules

import com.kabasoft.iws.shared._
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._

object VENDORINVOICE  {

  val component = ReactComponentB[Props]("VendorInvoice")
    .initialState(State())
    .renderBackend[InvoiceBackend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply( proxy: ModelProxy[Pot[Data]]) = component(Props(proxy,112,112,2,1))
}
