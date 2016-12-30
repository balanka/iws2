package com.kabasoft.iws.client.modules

import japgolly.scalajs.react._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.Button
import com.kabasoft.iws.gui.macros.{Delete, Icon, Update}
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared.IWS
import scalacss.ScalaCssReact._


object Utilities {

  case class Statex [A](item: Option[A] = None)
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
  def edit[A<:IWS](itemx:Option[A], bs: BackendScope[Props, Statex[A]]) = bs.modState(s => s.copy(item = itemx))


  def editedx[A<:IWS](itemx:A, bs: BackendScope[Props, Statex[A]]) = {
    //bs.modState(s => s.copy(item = itemx))
    edit(Some(itemx), bs)
    Callback {IWSCircuit.dispatch(Update(itemx))}
  }
  def delete [A<:IWS] (item:A, bs: BackendScope[Props, Statex[A]]) = {
    Callback.log("VendorInvoice deleted>>>>> ${item}")
    Callback {IWSCircuit.dispatch(Delete(item))}
    //bs.props >>= (_.proxy.dispatch(Delete(item)))
  }
  def newButton[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  bs: BackendScope[Props, Statex[A]])=
    Button(Button.Props(edit(Some(item),bs), addStyles = css), Icon.plusSquare, title)

  def saveButton[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  bs: BackendScope[Props, Statex[A]]) =
    Button(Button.Props(editedx(item, bs), addStyles = css), Icon.circleO, title)
}
