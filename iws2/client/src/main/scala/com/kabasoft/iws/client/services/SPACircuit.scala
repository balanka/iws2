package com.kabasoft.iws.client.services


import autowire._
import diode._
import diode.util._
import diode.data._
import diode.react.ReactConnector
import com.kabasoft.iws.client.logger._
import com.kabasoft.iws.shared.{Store => MStore, _}
import com.kabasoft.iws.shared.Model._
import boopickle.Default._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import com.kabasoft.iws.gui.macros._

case class UpdateMotd(potResult: Pot[String] = Empty) extends PotAction[String, UpdateMotd] {
  override def next(value: Pot[String]) = UpdateMotd(value)
}

// The base model of our application
case class RootModel [+A<:IWS,-B<:IWS](store:Pot[DStore[A,B]], motd:Pot[String])
case class DStore [+A<:IWS,-B<:IWS](models: Map[Int, Pot[ContainerT[A,B]]]) {
  def updated (newItem: B) = {
      val x= models.get(newItem.modelId).get.map(_.updated(newItem))
      DStore (models+ (newItem.modelId -> x))
  }

   def updatedAll(newModels: Map[Int, Pot[ContainerT[B,A]]])  = DStore[A,B](newModels.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]])
  //def updatedAll(newModels: Map[Int, Pot[ContainerT[B,A]]])  = DStore[A,B](models.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]]++
  //  newModels.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]])
  def remove(item:B) = {
    val x= models.get(item.modelId).get.map(_.remove(item))
    DStore( Map(item.modelId ->x))
  }
 }


 /**
  * Handles actions related to todos
  * @param modelRW Reader/Writer to access the model
  * @tparam M
  */

class IWSHandler[M](modelRW: ModelRW[M, Pot[DStore[IWS,IWS]]]) extends ActionHandler(modelRW) {
  import boopickle.Default._
   import com.kabasoft.iws.shared.{Store => MStore}

   implicit val amountPickler = transformPickler[BigDecimal,String](b=> String.valueOf(b.doubleValue()),
     t =>  scala.math.BigDecimal(t))
   implicit val datePickler = transformPickler[java.util.Date, Long](_.getTime,t => new java.util.Date(t))
   implicit val pickler = compositePickler[IWS]
   pickler.addConcreteType[TodoItem]
   pickler.addConcreteType[CostCenter]
   pickler.addConcreteType[Balance]
   pickler.addConcreteType[Account]
   pickler.addConcreteType[Article]
   pickler.addConcreteType[Supplier]
   pickler.addConcreteType[Customer]
   pickler.addConcreteType[QuantityUnit]
   pickler.addConcreteType[ArticleGroup]
   pickler.addConcreteType[LinePurchaseOrder]
   pickler.addConcreteType[PurchaseOrder[LinePurchaseOrder]].addConcreteType[LinePurchaseOrder]
   pickler.addConcreteType[Vat]
   pickler.addConcreteType[MStore]

  override def handle = {
    case Refresh (item:IWS) =>
      val x=Map(item.modelId ->Ready(Data(Seq(item))))
      log.info("+>>>>>>>>Refresh++++++"+x)
      updated(Ready(value.get.updatedAll(x)))
    case UpdateAll(all:Seq[IWS]) =>
      val xx=all.seq.headOption.get
      //log.info("+++++++++>>>>>>>>XXX"+xx)
      val  a=all.filter(_.modelId==xx.modelId)
      //log.info("+++++++++<<<<<<<<<<<"+a)
      val x=Map(xx.modelId ->Ready(Data(a)))
      updated(Ready(value.get.updatedAll(x)))
    case Update(item:IWS) =>
      log.info("+++++++++<<<<<<<<<<< UpdateTodo: "+item)
      updated(Ready(value.get.updated(item)), Effect(AjaxClient[Api].update(item).call().map(UpdateAll[IWS])))
    case Delete(item:IWS) =>
      log.info("+++++++++<<<<<<<<<<< Delete Item: "+item)
      updated(Ready(value.get.remove(item)).asInstanceOf[Pot[DStore[IWS,IWS]]], Effect(AjaxClient[Api].delete(item).call().map(UpdateAll[IWS])))
                     //if(!item.id.isEmpty && !item.id.equals("-1")) {Effect(AjaxClient[Api].delete(item).call().map(UpdateAll[IWS]))}
                     //else {  log.info("+++++++++<<<<<<<<<<< Delete Item with id: "+item); Effect(AjaxClient[Api].all(item).call().map(UpdateAll[IWS]))})
  }
}



/**
  * Handles actions related to the Motd
  * @param modelRW Reader/Writer to access the model
  * @tparam M
  */
class MotdHandler[M](modelRW: ModelRW[M, Pot[String]]) extends ActionHandler(modelRW) {
  implicit val runner = new RunAfterJS
  override def handle = {
    case action: UpdateMotd =>
      val updateF = action.effect(AjaxClient[Api].welcome("User X").call())(identity)
      action.handleWith(this, updateF)(PotAction.handler())
  }
}

// Application circuit
object SPACircuit extends Circuit[RootModel[IWS,IWS]] with ReactConnector[RootModel[IWS,IWS]] {

  protected val actionHandler = combineHandlers(
    new IWSHandler(zoomRW(_.store)((m, v) => m.copy(store = v))),
    new MotdHandler(zoomRW(_.motd)((m, v) => m.copy(motd = v)))
  )
  override protected def initialModel = {

    val store: Pot[DStore[IWS, IWS]] = Ready(DStore(Map(
      1 -> Ready(Data(Seq(Supplier()))),
      3 -> Ready(Data(Seq(Customer()))),
      //3 -> Ready(Data(Seq.empty[Customer])),
      4 -> Ready(Data(Seq(QuantityUnit("1", "QuantityUnit", 4, "QuantityUnit")))),
      6 -> Ready(Data(Seq(CostCenter("1", "CostCenter", 6, "CostCenter")))),
      7 -> Ready(Data(Seq(Article()))),
      9 -> Ready(Data(Seq.empty[Account])),
      8 -> Ready(Data(Seq(ArticleGroup()))),
     101 -> Ready(Data(Seq(PurchaseOrder[LinePurchaseOrder]()))),
      4711 -> Ready(Data(Seq(TodoItem())))
    )))

    RootModel(store, Empty)

  }
}