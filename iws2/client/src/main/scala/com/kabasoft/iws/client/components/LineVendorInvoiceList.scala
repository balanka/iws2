package com.kabasoft.iws.client.components

import com.kabasoft.iws.shared._
import japgolly.scalajs.react._

  object LineVendorInvoiceList {
  val component = ReactComponentB[Props]("LineVendorInvoiceList")
      .initialState(State( search ="0"))
      .renderBackend[LineVendorInvoiceBackend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:VendorInvoice[LineVendorInvoice], newLine:LineVendorInvoice =>Callback, saveLine:LineVendorInvoice =>Callback,
             deleteLine:LineVendorInvoice => Callback) = component(Props(porder, newLine, saveLine, deleteLine))
}
