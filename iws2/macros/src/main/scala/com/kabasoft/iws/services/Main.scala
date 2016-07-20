package com.kabasoft.iws.services


import com.kabasoft.iws.shared._
import com.kabasoft.iws.shared.common._
import boopickle.Default._
import java.util.Date
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.services.run._
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.services.Request._
import com.kabasoft.iws.shared.DAO
import com.kabasoft.iws.dao._
object Main  {

   def main(args: Array[String]) {
  
  import com.kabasoft.iws.shared.{Store => MStore}
  implicit val amountPickler = transformPickler[BigDecimal,String](b=> String.valueOf(b.doubleValue()),
    t =>  scala.math.BigDecimal(t))
  implicit val datePickler = transformPickler[java.util.Date, Long](_.getTime,t => new java.util.Date(t))
  implicit val pickler = compositePickler[IWS]
  //pickler.addConcreteType[TodoItem]
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
  pickler.addConcreteType[Bank]
  pickler.addConcreteType[BankAccount]
  pickler.addConcreteType[MStore]

     println( "Running Line purchaseorder2  com.kabasoft.iws.services.Main:")
 
  val lineOrder = LinePurchaseOrder(0L,1L,101,Some("001"),Some("Stk"),1000.0,100.0,Some("19"),Some(new Date()),"TEST")
  val purchaseorder =PurchaseOrder(3L, 2L,101,Some("300"),Some("1000"), Some(List(lineOrder)))
   val lineOrder2 = LinePurchaseOrder(0L,3L,101,Some("001"),Some("Stk"),1000.00,900.00,Some("19"),Some(new Date()),"TEST")

//val f:List[PurchaseOrder[LinePurchaseOrder]]= run.runG[List[PurchaseOrder[LinePurchaseOrder]]](Find[PurchaseOrder[LinePurchaseOrder]]("22"))
//println( "purchaseorder :"+f)
//val buf = Pickle.intoBytes(f:+purchaseorder)
 //println( "purchaseorder2 :"+ Unpickle[List[PurchaseOrder[LinePurchaseOrder]]].fromBytes(buf))
//val l= run.runG[Int](Update[PurchaseOrder[LinePurchaseOrder]](purchaseorder))
val l2= run.runG[Int](Insert[LinePurchaseOrder](List(lineOrder2)))
 println( "Line purchaseorder2 :"+ l2)
}
}
