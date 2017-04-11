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

  case class Prop[A](items: Seq[A], title:String, edit:A => Callback, delete:A=> Callback)
  case class Props [A, B, C, D](proxy: ModelProxy[Pot[Data]], modelId:Int,otransModelId:Int,storeModelId:Int,accountModelId:Int
                          ,instance:A, instance2:B, instance3:C, instance4:D, title:String="")
  case class State [A](item: Option[A] = None)
  case class StateL[A](item: Option[A],  search:String="", edit:Boolean = false)
  case class PropsL[A,B](porder: A, instance:B, newLine:(A,B) =>Callback, saveLine:(A,B) =>Callback, deleteLine:(A,B) =>Callback)

  def runLine1 [A, B<:LineTransaction](instance:A, line:B, fx:(A,B) =>Callback):Callback = fx(instance, line)
  def runLine [B<:LineTransaction](line:B, fx:B =>Callback):Callback = fx(line)

  def editx[A<:IWS](itemx:A, fx:A =>Callback):Callback = fx(itemx)

  def editedx[A<:IWS](itemx:A,  fx:A =>Callback):Callback = {
      editx(itemx, fx)
       Callback {IWSCircuit.dispatch(Update(itemx))}
  }
  def deletex [A<:IWS] (item:A, fx:A =>Callback):Callback = fx(item)

  def newButtonx[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  fx:A =>Callback) =
    Button(Button.Props(editx(item,fx), addStyles = css), Icon.plusSquare, title)

  def saveButtonx[A<:IWS](item:A, css:Seq[scalacss.StyleA], title:String,  fx:A =>Callback) =
    Button(Button.Props(editedx(item, fx), addStyles = css), Icon.circleO, title)

  def updateTxtField(fx:ReactEventI =>Callback) =  fx
  def updateCBField(idx:String,  fx:String =>Callback) =  fx(idx)
  def updateCBFieldL(idx:Long,  fx:Long =>Callback) =  fx(idx)

}
