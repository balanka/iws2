package com.kabasoft.iws.gui.macros

import com.kabasoft.iws.shared._
import diode.react.ModelProxy
import diode.data.Pot

trait IWSBackendTrait [T] {

  case class Props(proxy: ModelProxy[Pot[Data]],iws:T)
  //case class State(selectedItem: Option[T] = None, showForm: Boolean = false)

}



