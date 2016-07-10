package com.kabasoft.iws.gui.services

import autowire._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.shared._
import diode._
import diode.data._
import diode.react.ReactConnector
import diode.util._
import boopickle.Default._


import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


case class RootModel [+A<:IWS,-B<:IWS](store:Pot[DStore[A,B]], motd:Pot[String])
case class DStore [+A<:IWS,-B<:IWS](models: Map[Int, Pot[ContainerT[A,B]]]) {
  def updated (newItem: B) = {
     val mapx = models.get(newItem.modelId).get.map(_.update(newItem))
    //log.info("UPDATE UPDATE"+ mapx)
    //val x= models.get(newItem.modelId).get.map(_.update(newItem))
      DStore (models+ (newItem.modelId -> mapx))
  }

   //def updatedAll(newModels: Map[Int, Pot[ContainerT[B,A]]])  = DStore[A,B](newModels.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]])
  def updatedAll(newModels: Map[Int, Pot[ContainerT[B,A]]])  = {
     //log.debug("+++++++++<<<<<<<<<<< newModels: "+newModels)
     DStore[A,B]( models.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]]++
    newModels.asInstanceOf[Map[Int, Pot[ContainerT[A,B]]]])}
  def remove(item:B) = {
    //log.info("+>>>>>>>>Item to Delete++++++ ${item}")
    val x= models.get(item.modelId).get.map(_.remove(item))
    //log.info("+>>>>>>>>Delete++++++${x}")
    DStore( Map(item.modelId ->x))
  }
 }
//case class Store2(models:Map[Int, List[IWS]])
//case class RootModel2(store:Store2)

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
   pickler.addConcreteType[Goodreceiving[LineGoodreceiving]].addConcreteType[LineGoodreceiving]
   pickler.addConcreteType[Vat]
   pickler.addConcreteType[MStore]
   //pickler.addConcreteType[String]

  override def handle = {
    case Refresh (item:IWS) =>
      val x = Map(item.modelId ->Ready(Data(Seq(item))))
      log.info(s"+>>>>>>>>Refresh+++++ RefreshRefresh ===== ${item} ====RefreshRefreshRefreshRefresh+"+x)
      //updated(Ready(value.get.updatedAll(x)))
      updated(Ready(value.get.updated(item)), Effect(AjaxClient[Api].all(item).call().map(UpdateAll[IWS])))
    case UpdateAll(all:Seq[IWS]) =>
      val xx = all.seq.headOption.get
      //log.info("+++++++++>>>>>>>>ZZZZZZZZZ"+all)
     // log.info("+++++++++>>>>>>>>XXX"+xx)
      val  a = all.filter(_.modelId == xx.modelId)
     // val r =value.get.models.get(xx.modelId).get.get
      //log.info("+++++++++aaaa0000000"+ a +"<<<<<<<<<<<"+ all)
      val r =value.get.models.get(xx.modelId).get.get.asInstanceOf[Data].items
     // log.info("+++++++++rrrrr<<<<<<<<<<<"+(all++r))

      val x = Map(xx.modelId ->Ready(Data(all++r)))
      updated(Ready(value.get.updatedAll(x)))
    case Update(item:IWS) =>
      //log.debug("+++++++++<<<<<<<<<<< UpdateTodo: "+item)
      updated(Ready(value.get.updated(item)), Effect(AjaxClient[Api].update(item).call().map(UpdateAll[IWS])))
    case FindAll(item:IWS) =>
      log.info("+++++++++<<<<<<<<<<< FindAll : "+item)
      updated(Ready(value.get.updated(item)), Effect(AjaxClient[Api].all(item).call().map(UpdateAll[IWS])))
    case Delete(item:IWS) =>
      log.info("+++++++++<<<<<<<<<<< Delete Item: "+item)
      //ActionResult.NoChange
      updated(Ready(value.get.remove(item)).asInstanceOf[Pot[DStore[IWS,IWS]]], Effect(AjaxClient[Api].delete(item).call().map(UpdateAll[IWS])))
    }
}

object IWSCircuit extends Circuit[RootModel[IWS,IWS]] with ReactConnector[RootModel[IWS,IWS]] {

  protected val actionHandler = composeHandlers(
    new IWSHandler(zoomRW(_.store)((m, v) => m.copy(store = v)))
  )
  override protected def initialModel = {

    val store: Pot[DStore[IWS, IWS]] = Ready(DStore(Map(
      1 -> Ready(Data(Seq.empty[Supplier])),
      2 -> Ready(Data(Seq.empty[Store])),
      3 -> Ready(Data(Seq.empty[Customer])),
      4 -> Ready(Data(Seq.empty[QuantityUnit])),
      5 -> Ready(Data(Seq.empty[Vat])),
      6 -> Ready(Data(Seq.empty[CostCenter])),
      7 -> Ready(Data(Seq.empty[Article])),
      9 -> Ready(Data(Seq.empty[Account])),
      8 -> Ready(Data(Seq.empty[ArticleGroup])),
     101 -> Ready(Data(Seq.empty[PurchaseOrder[LinePurchaseOrder]])),
     104 -> Ready(Data(Seq.empty[Goodreceiving[LineGoodreceiving]]))

    )))

    RootModel(store, Empty)

  }
  def getRootModel = initialModel

  def getModel (modelId:Int) = { initialModel.store.get.models.get(modelId)}

  def getEModel (modelId:Int) = { initialModel.store.get.models.get(modelId)}
}
/*
scala -cp ~/Downloads/scalatest_2.11-2.2.6.jar:~/Downloads/scalacheck_2.11-1.12.5.jar:/Users/batemady/.ivy2/cache/me.chrons/diode-data_sjs0.6_2.11/jars/diode-data_sjs0.6_2.11-1.0.0.jar:/Users/batemady/.ivy2/cache/me.chrons/diode_sjs0.6_2.11/jars/diode_sjs0.6_2.11-1.0.0.jar

scala> import diode._
scala> import diode.data._
scala> case class Article ( id:String,name:String,modelId:Int=1, description:String ="") extends Masterfile
defined class Article

scala> case class Account ( id:String,name:String,modelId:Int=2, description:String ="") extends Masterfile
defined class Account

scala> val l1 = List(Article("001","article01"), Article("002","article02"))
l1: List[Article] = List(Article(001,article01,1,), Article(002,article02,1,))

scala> val l2 = List(Account("ac001","account01"), Account("ac002","account02"))
l2: List[Account] = List(Account(ac001,account01,2,), Account(ac002,account02,2,))




scala> import util.{ Try, Success, Failure }
import util.{Try, Success, Failure}

scala> val d1 =DStore(Map(1->Ready(Data(l1)),2 ->Ready(Data(l2))))
d1: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,))))))

scala> val d2 =d1.upatedAll(Map(3 ->Ready(Data(List(Account("acc03","account03"))))))
<console>:37: error: value upatedAll is not a member of DStore[IWS,IWS]
val d2 =d1.upatedAll(Map(3 ->Ready(Data(List(Account("acc03","account03"))))))
^

scala> val d2 =d1.updatedAll(Map(3 ->Ready(Data(List(Account("acc03","account03"))))))
d2: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,)))), 3 -> Ready(Data(List(Account(acc03,account03,2,))))))

scala> val d2 =d1.updatedAll(Map(2 ->Ready(Data(List(Account("acc03","account03"))))))
d2: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,)))), 2 -> Ready(Data(List(Account(acc03,account03,2,))))))



scala> val d3 =d1.updatedAll(Map(1 -> Ready(Data(d1.models.get(1).get.get.items++List(Article("003","Article003"))))))
d3: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,), Article(003,Article003,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,))))))

scala> val d3 =d1.updatedAll(Map(1 -> Ready(Data(d1.models.get(1).get.get.items++List(Article("003","Article003"))))))
d3: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,), Article(003,Article003,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,))))))

scala> val d4 =d3.updatedAll(Map(1 -> Ready(Data(d1.models.get(1).get.get.items++List(Article("004","Article004"))))))
d4: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,), Article(004,Article004,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,))))))
scala> val d5 =d4.updatedAll(Map(1 -> Ready(Data(d1.models.get(1).get.get.items++List(Article("005","Article005"), Article("006","article06"))))))
d5: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,), Article(005,Article005,1,), Article(006,article06,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,))))))

scala> val d6 =d5.updatedAll(Map(2 -> Ready(Data(d5.models.get(2).get.get.items++List(Account("acc003","Account003"), Account("acc004","account004"))))))
d6: DStore[IWS,IWS] = DStore(Map(1 -> Ready(Data(List(Article(001,article01,1,), Article(002,article02,1,), Article(005,Article005,1,), Article(006,article06,1,)))), 2 -> Ready(Data(List(Account(ac001,account01,2,), Account(ac002,account02,2,), Account(acc003,Account003,2,), Account(acc004,account004,2,))))))

*/
