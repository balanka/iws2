package com.kabasoft.iws.client.components

import com.kabasoft.iws.client.modules.Utilities.{PropsL, StateL}
import com.kabasoft.iws.shared._
import japgolly.scalajs.react._

  object LineVendorInvoiceList {
  val component = ReactComponentB[PropsL[VendorInvoice[LineVendorInvoice],LineVendorInvoice]]("LineVendorInvoiceList")
      .initialState(StateL[LineVendorInvoice]( None, search ="0"))
      .renderBackend[LineVendorInvoiceBackend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

  def apply( porder:VendorInvoice[LineVendorInvoice], instance:LineVendorInvoice, newLine:(VendorInvoice[LineVendorInvoice], LineVendorInvoice) =>Callback,
             saveLine:(VendorInvoice[LineVendorInvoice], LineVendorInvoice) =>Callback,
             deleteLine:(VendorInvoice[LineVendorInvoice], LineVendorInvoice) => Callback) = component(PropsL(porder, instance, newLine, saveLine, deleteLine
  ))
}
