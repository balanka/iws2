package com.kabasoft.iws.client.modules

import japgolly.scalajs.react._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap.Button
import com.kabasoft.iws.gui.macros.{Delete, Icon, Update}
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared.{Data, IWS, LineTransaction, Transaction}
import diode.data.Pot
import diode.react.ModelProxy

import scalacss.ScalaCssReact._


object Utilities {

  case class Props(proxy: ModelProxy[Pot[Data]], modelId:Int,otransModelId:Int,storeModelId:Int,accountModelId:Int )
  //case class State(item: Option[VendorInvoice[LineVendorInvoice]] = None)
  case class State [A](item: Option[A] = None)
  /*def updateOid1(idx: String, bs: BackendScope[Props, State]) = {

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
  */
  def runLine [B<:LineTransaction](line:B, fx:B =>Callback):Callback = fx(line)

  def editx[A<:IWS](itemx:A, fx:A =>Callback):Callback = fx(itemx)

  def editedx[A<:IWS](itemx:A,  fx:A =>Callback):Callback = {
    editx(itemx, fx)
    Callback {IWSCircuit.dispatch(Update(itemx))}
  }
  def deletex [A<:IWS] (item:A, fx:A =>Callback):Callback = fx(item)//Callback {IWSCircuit.dispatch(Delete(item))}

  def newButtonx[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  fx:A =>Callback) =
    Button(Button.Props(editx(item,fx), addStyles = css), Icon.plusSquare, title)

  def saveButtonx[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  fx:A =>Callback) =
    Button(Button.Props(editedx(item, fx), addStyles = css), Icon.circleO, title)

  def updateTxtField(fx:ReactEventI =>Callback) =  fx
  def updateCBField(idx:String,  fx:String =>Callback) =  fx(idx)


}
