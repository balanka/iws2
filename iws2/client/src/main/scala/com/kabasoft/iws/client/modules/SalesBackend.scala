package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.modules.Utilities._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import diode.data.Ready
import japgolly.scalajs.react.BackendScope


class SalesBackend ($: BackendScope[Props[FDocument[LineFDocument], LineFDocument , Store, Customer], State[FDocument[LineFDocument]]]) extends
  FDocumentBackend [Store, Customer] ($){


  override def  storeList (modelId:Int) = {
       val stores =  IWSCircuit.zoom(_.store.get.models.get(modelId)).eval(IWSCircuit.getRootModel).
         getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
         stores.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
    }
 override  def  supplierList ( modelId:Int)= 
 {
   val accounts =  IWSCircuit.zoom(_.store.get.models.get(modelId)).eval(IWSCircuit.getRootModel).
     getOrElse(Ready(Data(List.empty[Customer]))).get.items.asInstanceOf[List[Customer]].toSet
  accounts.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
}
 }

