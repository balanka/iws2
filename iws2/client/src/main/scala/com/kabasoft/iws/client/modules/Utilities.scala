package com.kabasoft.iws.client.modules

import japgolly.scalajs.react._
import com.kabasoft.iws.gui.logger._


object Utilities {
  def updateOid1(idx: String, bs: BackendScope[Props, State]) = {
    // val oId = idx.substring(0, idx.indexOf("|"))
    //  log.debug(s"oid is "+oId)
    bs.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))
  }

  def updateStore(idx: String, bs: BackendScope[Props, State]) = {
    val storeId = idx.substring(0, idx.indexOf("|"))
    log.debug(s"store is " + storeId)
    bs.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
  }

  def updateAccount(idx: String, bs: BackendScope[Props, State]) = {
    val supplierId = idx.substring(0, idx.indexOf("|"))
    log.debug(s"ItemId Key is ${supplierId}  ")
    bs.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId)))))
  }

  def updateText(e: ReactEventI, bs: BackendScope[Props, State]) = {
    val txt = e.target.value
    log.debug(s"txt is ${txt}")
    bs.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))
  }

  def setModified(bs: BackendScope[Props, State]) = bs.modState(s =>
    s.copy(item =
      if (!s.item.map(_.created).getOrElse(false)) {
        s.item.map(_.copy(modified = true))
      } else {
        s.item
      }
    ))

}
