package com.kabasoft.iws.shared

import java.util.Date

import com.kabasoft.iws.shared.common._


//import shapeless.lens
//import monocle.macros._
//import monocle.macros.GenLens



object common {
  type Amount = scala.math.BigDecimal
  def today = new Date()
  type ACCOUNT_TYPE =(String, String, Int, String, String,  Date, Date)
  type ARTICLE_TYPE =(String, String, Int, String, scala.math.BigDecimal, scala.math.BigDecimal, scala.math.BigDecimal,
                        String, String, String, String)
  type STORE_TYPE = (String, String, Int, String,  String, String, String, String)

  type SalesOrder_TYPE =(Long,Long,Int, String,String, String)
  type Goodreceiving_TYPE =(Long,Long,Int, String,String, String)
  type InventoryInvoice_TYPE =(Long,Long,Int, String,String, String)
  type LineVendorInvoice_TYPE = (Long,Long,Int, String,Boolean, String, BigDecimal, Date, String)
  type VendorInvoice_TYPE =(Long,Long,Int, String,String, String)
  type LinePayment_TYPE = (Long,Long,Int, String,Boolean, String, BigDecimal, Date, String)
  type Payment_TYPE =(Long,Long,Int, String,String, String)
  type LineSalesOrder_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type PurchaseOrder_TYPE =(Long,Long,Int, String,String, String)
  type LinePurchaseOrder_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type LineGoodreceiving_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type LineInventoryInvoice_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type LineBillOfDelivery_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type LineSalesInvoice_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  type SalesInvoice_TYPE =(Long,Long,Int, String,String, String)
  type LineCustomerInvoice_TYPE = (Long,Long,Int, String,Boolean, String, BigDecimal, Date, String)
  type CustomerInvoice_TYPE =(Long,Long,Int, String,String, String)
  type LineSettlement_TYPE = (Long,Long,Int, String,Boolean, String, BigDecimal, Date, String)
  type Settlement_TYPE =(Long,Long,Int, String,String, String)
  type BillOfDelivery_TYPE =(Long,Long,Int, String,String, String)
  type FDocument_TYPE =(Long,Long,Int, String,String, String)
  type LineFDocument_TYPE = (Long,Long,Int, String,Boolean, String, BigDecimal, Date, String)
  type IDocument_TYPE = (Long,Long,Int, String,String, String)
  type LineIDocument_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)

}
sealed trait IWS {
  def id:String
  def modelId:Int
}

trait ContainerT [+A<:IWS,-B<:IWS] {
  def update(newItem: B): ContainerT [A,B]
  def updateAll(all: Seq[B]): ContainerT [A,B]
  def remove (item: B): ContainerT  [A,B]
  def size = items.size
  def items : Seq[A]
  def add(newItem: B): ContainerT [A,B]
}

sealed trait  Masterfile extends IWS {
  def name:String
  def description:String

  def canEqual(a: Any) = a.isInstanceOf[Masterfile]
  override def equals(that: Any): Boolean =
    that match {
      case that: Masterfile => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
  override def hashCode:Int = {
    val prime = 31
    var result = 1
    result = prime * result + modelId +id.toInt
    result
  }
}

case class Data  (items: Seq[IWS]) extends ContainerT [IWS,IWS]{
  override def update(newItem: IWS) = {
    items.indexWhere((_.id == newItem.id)) match {
      case -1 =>
        Data(items :+ newItem)
      case index =>
        Data(items.updated(index, newItem))
    }
  }
  override def updateAll(all: Seq[IWS]) =  Data((items.toSet ++all.toSet).toList)
  override def add(newItem: IWS)= Data(items :+ newItem)
  override def remove (item: IWS) = Data(items.filterNot(_.id==item.id))
}

sealed trait Trans extends IWS { def tid:Long}
sealed trait Transaction [L] extends Trans {
    def id = tid.toString
    def tid:Long
    def oid:Long
    def store:Option[String]
    def account:Option[String]
    def lines:Option[List[L]]
    def text:String
  def canEqual(a: Any) = a.isInstanceOf[Transaction[LineTransaction]]
  override def equals(that: Any): Boolean =
    that match {
      case that: Masterfile => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
  override def hashCode:Int = {
    val prime = 31
    var result = 1
    result = prime * result + modelId + tid.toInt
    result
  }

  }
sealed trait LineTransaction extends IWS {
  def id = tid.toString
  def tid: Long
  def transid: Long

  //def modelId: Int


  def duedate: Option[Date]
  def text: String
  def modified:Boolean
  def created:Boolean
  def deleted:Boolean
    override def hashCode:Int = {
    val prime = 31
    var result = 1
    result = prime * result + tid.toInt
    result = prime * result + transid.hashCode
    result = prime * result + modelId.hashCode
     result
  }
 }
sealed trait  LineInventoryTransaction extends LineTransaction {
   def item: Option[String]
   def unit: Option[String]
   def price: Amount
   def quantity: Amount
   def vat:Option[String]
   }
sealed trait  LineFinancialsTransaction extends LineTransaction {
  def account: Option[String]
  def oaccount: Option[String]
  def side:Boolean
  def amount: Amount
}

object ModelID extends Enumeration {
  type ID = Value
  val CUSTOMER, ARTICLE, ACCOUNT, SUPPLIER, EMPLOYEE, COST_CENTER, STORE, ARTICLE_GROUP, VAT, QUANTITY_UNIT, RQF,SUPPLIER_CONTRACT,
  ORDER_PROPOSAL, PURCHASE_ORDER,GODD_RECEIVING, INV_S_INVOICE, SUPPLIER_INVOICE, PAYMENT, RQB, BID, CUSTOMER_CONTRACT, SALES_ORDER,
  SALES_INVOICE, INVOICE = Value
  override def toString = Value.toString
  def toEnum(e: ID): String =
    e match {
      case CUSTOMER => "Customer"
      case ARTICLE => "Article"
      case ACCOUNT => "Account"
      case SUPPLIER => "Supplier"
      case EMPLOYEE => "Employee"
      case COST_CENTER => "CostCenter"
      case STORE => "STore"
      case ARTICLE_GROUP => "ArticleGroup"
      case QUANTITY_UNIT => "QuantityUnit"
      case VAT => "ValueAddedTax"
      case RQF => "RequestForQuote"
      case SUPPLIER_CONTRACT => "SupplierContract"
      case ORDER_PROPOSAL => "OrderProposal"
      case PURCHASE_ORDER => "PurchaseOrder"
      case GODD_RECEIVING => "Goodreceiving"
      case INV_S_INVOICE => "SupplierInvoiceInventory"
      case SUPPLIER_INVOICE => "SupplierInvoice"
      case PAYMENT => "Payment"
      case RQB => "RequestForBid"
      case BID => "Bid"
      case CUSTOMER_CONTRACT => "CustomerContract"
      case SALES_ORDER => "SalesOrder"
      case SALES_INVOICE => "SalesInvoice"
      case INVOICE => "Invoice"
    }
  def fromEnum(s: String): Option[ModelID.ID] =
    Option(s) collect {
      case "Customer" =>  CUSTOMER
      case "Article" =>  ARTICLE
      case "Account" =>  ACCOUNT
      case "Supplier" =>  SUPPLIER
      case "Employee" =>  EMPLOYEE
      case "CostCenter" =>  COST_CENTER
      case "Store" =>  STORE
      case "ArticleGroup" =>  ARTICLE_GROUP
      case "QuantityUnit" =>  QUANTITY_UNIT
      case "ValueAddedTax" =>  VAT
      case "RequestForQuote"  =>  RQF
      case  "SupplierContract" => SUPPLIER_CONTRACT
      case  "OrderProposal" => ORDER_PROPOSAL
      case  "PurchaseOrder" => PURCHASE_ORDER
      case  "Goodreceiving" => GODD_RECEIVING
      case  "SupplierInvoiceInventory" => INV_S_INVOICE
      case  "SupplierInvoice"=> SUPPLIER_INVOICE
      case  "Payment" => PAYMENT
      case  "RequestForBid"=> RQB
      case  "Bid" => BID
      case "CustomerContract" =>  CUSTOMER_CONTRACT
      case "SalesOrder" => SALES_ORDER
      case "SalesInvoice" => SALES_INVOICE
      case "Invoice" => INVOICE

    }
}
case class Balance( amount: Amount = 0) extends IWS {
  def id =""
  def modelId=0
}
case class CostCenter(id:String ="-1",  name:String ="", modelId:Int = 6, description:String ="") extends IWS with Masterfile
case class Account (id: String ="-1", name: String  ="", modelId:Int = 9,description:String  ="", groupId:Option[String]= None,
                    accounts:Option[List[Account]] = None, dateOfOpen: Option[Date] = Some(new Date()), dateOfClose: Option[Date] =
                    Some(new Date()), balance: Balance = Balance(0.0)) extends Masterfile

case class Article(id:String ="-1", name:String ="", modelId:Int = 7, description:String  ="", price:Amount = 0, avgPrice:Amount = 0, salesPrice:Amount=0,
                   qttyUnit:String ="Stk", packUnit:String ="Stk", groupId:Option[String] = None, vat:Option[String] = None,
                   articles:Option[List[Article]] = None) extends IWS with Masterfile
case class QuantityUnit(id:String ="-1",name:String ="", modelId:Int = 4 ,description:String ="") extends IWS with Masterfile
case class ArticleGroup(id:String ="-1", name:String ="", modelId:Int = 8, description:String ="") extends IWS with Masterfile
abstract class BusinessPartner(id: String ="-1", name: String ="", modelId:Int, accountId:String, street: String ="", city: String ="", state: String ="", zip: String ="") extends IWS
case class Supplier(id: String ="-1", name: String ="" , modelId:Int = 1, accountId:String ="", street: String ="", city: String ="", state: String ="", zip: String ="") extends
BusinessPartner (id: String, name: String, modelId:Int,  street: String, city: String, state: String , zip: String )
case class Store(id: String ="-1", name: String ="",  modelId:Int = 2, accountId:String ="", street: String ="", city: String ="", state: String ="", zip: String ="", stocks:Option[List[Stock]] =  Some(List.empty[Stock])) extends
BusinessPartner (id: String, name: String ,modelId:Int,   street: String, city: String, state: String , zip: String )
case class Customer(id: String ="-1", name: String ="", modelId:Int = 3, accountId:String ="", street: String ="", city: String ="", state: String ="", zip: String ="") extends
BusinessPartner (id: String, name: String , modelId:Int,  street: String, city: String, state: String , zip: String )

case class Company(id: String ="-1", name: String ="", modelId:Int =10, street: String ="", city: String ="", state: String ="", zip: String ="",
                   bankAccountId:String ="", purchasingClearingAccountId:String ="", salesClearingAccountId:String ="", paymentClearingAccountId:String ="", settlementClearingAccountId:String ="",
                   periode:Int =0, nextPeriode:Int =0, taxCode:String ="", vatId:String ="") extends
  BusinessPartner (id: String, name: String , modelId:Int, street: String, city: String, state: String , zip: String )

case class Bank(id:String ="-1",name:String ="", modelId:Int = 11 ,description:String ="" ) extends Masterfile
case class BankAccount(id:String ="-1",name:String ="", modelId:Int = 12 ,description:String ="" , bic:String ="", debit:Amount =0.0, credit:Amount =0.0) extends Masterfile {
   def iban =id

}
case class Vat(id:String ="-1",name:String ="", modelId:Int = 5 ,description:String ="", percent:Amount =0, inputVatAccountId:String ="", outputVatAccountId:String ="",
               revenueAccountId:String ="", stockAccountId:String ="", expenseAccountId:String ="" ) extends Masterfile
case class PeriodicAccountBalance (id:String, name:String ="", modelId:Int = 106, description:String ="", accountId:String, periode:Int, debit:Amount, credit:Amount) extends Masterfile
case class Stock (id:String, name:String ="", modelId:Int = 107, description:String ="", itemId:String, storeId:String, quantity:Amount, minStock:Amount) extends Masterfile
case class LinePurchaseOrder  (tid:Long = 0L, transid:Long =0, modelId:Int = 102,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                               quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  //def eq (l:LinePurchaseOrder):Boolean = tid == l.tid
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LinePurchaseOrder]
  override def equals(that: Any): Boolean =
    that match {
      case that: LinePurchaseOrder => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

}
object LinePurchaseOrder
{
  def apply(l:LineIDocument) = new LinePurchaseOrder(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LinePurchaseOrder_TYPE) = new LinePurchaseOrder(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class LineSalesOrder  (tid:Long = 0L, transid:Long =0, modelId:Int = 117,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                               quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  //def eq (l:LineSalesOrder):Boolean = tid == l.tid
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineSalesOrder]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineSalesOrder => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineSalesOrder
{
  def apply(l:LineIDocument) = new LineSalesOrder(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineSalesOrder_TYPE) = new LineSalesOrder(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class LineGoodreceiving  (tid:Long = 0L, transid:Long =0, modelId:Int = 105,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                               quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  //def eq (l:LinePurchaseOrder):Boolean = tid == l.tid
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineGoodreceiving]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineGoodreceiving => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineGoodreceiving
{
  def apply(l:LineIDocument) = new LineGoodreceiving(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineGoodreceiving_TYPE) = new LineGoodreceiving(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class LineBillOfDelivery  (tid:Long = 0L, transid:Long =0, modelId:Int = 119,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                               quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  //def eq (l:LineBillOfDelivery):Boolean = tid == l.tid
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineBillOfDelivery]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineBillOfDelivery => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineBillOfDelivery
{
  def apply(l:LineIDocument) = new LineBillOfDelivery(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineBillOfDelivery_TYPE) = new LineBillOfDelivery(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class PurchaseOrder[LinePurchaseOrder] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 101,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LinePurchaseOrder]]=Some(List.empty[LinePurchaseOrder]),
                                              modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LinePurchaseOrder]{
 def add(line:LinePurchaseOrder) = copy(lines = Some(getLines ++: List(line)))
 def getLines = lines.getOrElse(List.empty[LinePurchaseOrder])
 def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
 def replaceLine( newLine:LinePurchaseOrder) = copy(lines = Some( getLines map ( old => if (newLine.equals(old)) newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[PurchaseOrder[LinePurchaseOrder]]
  override def equals(that: Any): Boolean =
    that match {
      case that: PurchaseOrder[LinePurchaseOrder] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object PurchaseOrder 
{

  def apply(f:IDocument[LineIDocument])  =  new PurchaseOrder[LinePurchaseOrder] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x =>  x.map (l =>LinePurchaseOrder(l))))
}


case class SalesOrder[LineSalesOrder] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 116,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LineSalesOrder]]=Some(List.empty[LineSalesOrder]),
                                             modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LineSalesOrder]{
  def add(line:LineSalesOrder) = copy(lines = Some(getLines ++: List(line)))
  def getLines = lines.getOrElse(List.empty[LineSalesOrder])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineSalesOrder) = copy(lines = Some( getLines map ( old => if (newLine.equals(old)) newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[SalesOrder[LineSalesOrder]]
  override def equals(that: Any): Boolean =
    that match {
      case that: SalesOrder[LineSalesOrder] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object SalesOrder 
{

  def apply(f:IDocument[LineIDocument])  =  new SalesOrder[LineSalesOrder] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x =>  x.map (l =>LineSalesOrder(l))))
}


case class Goodreceiving[LineGoodreceiving] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 104,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LineGoodreceiving]]=Some(List.empty[LineGoodreceiving]),
                                              modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LineGoodreceiving]{
  def add(line:LineGoodreceiving) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineGoodreceiving] = lines.getOrElse(List.empty[LineGoodreceiving])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))

  def replaceLine( newLine:LineGoodreceiving) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[Goodreceiving[LineGoodreceiving]]
  override def equals(that: Any): Boolean =
    that match {
      case that: Goodreceiving[LineGoodreceiving] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object Goodreceiving 
{

  def apply(f:IDocument[LineIDocument])  =  new Goodreceiving[LineGoodreceiving] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineGoodreceiving(l))))
}

case class BillOfDelivery[LineBillOfDelivery] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 118,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LineBillOfDelivery]]=Some(List.empty[LineBillOfDelivery]),
                                             modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LineBillOfDelivery]{
  def add(line:LineBillOfDelivery) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineBillOfDelivery] = lines.getOrElse(List.empty[LineBillOfDelivery])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))

  def replaceLine( newLine:LineBillOfDelivery) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[BillOfDelivery[LineBillOfDelivery]]
  override def equals(that: Any): Boolean =
    that match {
      case that: BillOfDelivery[LineBillOfDelivery] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object BillOfDelivery 
{

  def apply(f:IDocument[LineIDocument])  =  new BillOfDelivery[LineBillOfDelivery] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineBillOfDelivery(l))))
}

case class LineInventoryInvoice  (tid:Long = 0L, transid:Long =0, modelId:Int = 111,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                               quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineInventoryInvoice]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineInventoryInvoice => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineInventoryInvoice
{
  def apply(l:LineIDocument) = new LineInventoryInvoice(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineInventoryInvoice_TYPE) = new LineInventoryInvoice(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class LineSalesInvoice  (tid:Long = 0L, transid:Long =0, modelId:Int = 121,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                                  quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                                  modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineSalesInvoice]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineSalesInvoice => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineSalesInvoice
{
  def apply(l:LineIDocument) = new LineSalesInvoice(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineSalesInvoice_TYPE) = new LineSalesInvoice(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9),l._10) 
}
case class InventoryInvoice[LineInventoryInvoice] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 110,store:Option[String]=None, account:Option[String]= None,
                                                   text:String ="", lines:Option[List[LineInventoryInvoice]]=Some(List.empty[LineInventoryInvoice]),
                                             modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineInventoryInvoice]{
  def add(line:LineInventoryInvoice) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineInventoryInvoice] = lines.getOrElse(List.empty[LineInventoryInvoice])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineInventoryInvoice) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[InventoryInvoice[LineInventoryInvoice]]
  override def equals(that: Any): Boolean =
    that match {
      case that: InventoryInvoice[LineInventoryInvoice] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}

object InventoryInvoice 
{
  def apply(f:IDocument[LineIDocument])  =  new InventoryInvoice[LineInventoryInvoice] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l => LineInventoryInvoice(l))))
}

case class SalesInvoice[LineSalesInvoice] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 120,store:Option[String]=None, account:Option[String]= None,
                                                   text:String ="", lines:Option[List[LineSalesInvoice]]=Some(List.empty[LineSalesInvoice]),
                                                   modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineSalesInvoice]{
  def  add(line:LineSalesInvoice) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineSalesInvoice] = lines.getOrElse(List.empty[LineSalesInvoice])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineSalesInvoice) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[SalesInvoice[LineSalesInvoice]]
  override def equals(that: Any): Boolean =
    that match {
      case that: SalesInvoice[LineSalesInvoice] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object SalesInvoice 
{
  def apply(f:IDocument[LineIDocument])  =  new SalesInvoice[LineSalesInvoice] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => 
                                               x.map (l => LineSalesInvoice(l))))
}
case class LineVendorInvoice  (tid:Long = 0L, transid:Long =0, modelId:Int = 113,account:Option[String] = None,  side:Boolean = true, oaccount:Option[String] = None, amount: Amount = 0,
                                   duedate:Option[Date] = Some(new Date()),text:String ="txt",
                                  modified:Boolean= false, created:Boolean= true, deleted:Boolean= false) extends LineFinancialsTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineVendorInvoice]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineVendorInvoice => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineVendorInvoice
{
  def apply(l:LineFDocument) = new LineVendorInvoice(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineVendorInvoice_TYPE) = new LineVendorInvoice(l._1,l._2, l._3, Some(l._4), l._5, Some(l._6), l._7, Some(l._8), l._9)
}
case class LineCustomerInvoice  (tid:Long = 0L, transid:Long =0, modelId:Int = 123,account:Option[String] = None,  side:Boolean = true, oaccount:Option[String] = None, amount: Amount = 0,
                               duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= true, deleted:Boolean= false) extends LineFinancialsTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineCustomerInvoice]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineCustomerInvoice => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}

object LineCustomerInvoice
{
  def apply(l:LineFDocument) = new LineCustomerInvoice(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)
   def apply(l:LineCustomerInvoice_TYPE) = new LineCustomerInvoice(l._1,l._2, l._3, Some(l._4), l._5, Some(l._6), l._7, Some(l._8), l._9)
}
case class VendorInvoice[LineVendorInvoice] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 112,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LineVendorInvoice]]=Some(List.empty[LineVendorInvoice]),
                                                   modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineVendorInvoice]{
  def add(line:LineVendorInvoice) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineVendorInvoice] = lines.getOrElse(List.empty[LineVendorInvoice])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineVendorInvoice) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[VendorInvoice[LineVendorInvoice]]
  override def equals(that: Any): Boolean =
    that match {
      case that: VendorInvoice[LineVendorInvoice] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object VendorInvoice 
{

  def apply(f:FDocument[LineFDocument])  =  new VendorInvoice[LineVendorInvoice] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>(
             LineVendorInvoice(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)))))
}

case class CustomerInvoice[LineCustomerInvoice] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 122,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LineCustomerInvoice]]=Some(List.empty[LineCustomerInvoice]),
                                             modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineCustomerInvoice]{
  def add(line:LineCustomerInvoice) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineCustomerInvoice] = lines.getOrElse(List.empty[LineCustomerInvoice])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineCustomerInvoice) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[CustomerInvoice[LineCustomerInvoice]]
  override def equals(that: Any): Boolean =
    that match {
      case that: CustomerInvoice[LineCustomerInvoice] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object CustomerInvoice
{

  def apply(f:FDocument[LineFDocument])  =  new CustomerInvoice[LineCustomerInvoice] (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>(
    LineCustomerInvoice(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)))))
}

case class LinePayment  (tid:Long = 0L, transid:Long =0, modelId:Int = 115,account:Option[String] = None,  side:Boolean = true, oaccount:Option[String] = None, amount: Amount = 0,
                               duedate:Option[Date] = Some(new Date()),text:String ="txt",
                               modified:Boolean= false, created:Boolean= true, deleted:Boolean= false) extends LineFinancialsTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LinePayment]
  override def equals(that: Any): Boolean =
    that match {
      case that: LinePayment => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

}
object LinePayment 
{
 def apply(l:LineFDocument) = new LinePayment(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)
 def apply(l:LinePayment_TYPE) = new LinePayment(l._1,l._2, l._3, Some(l._4), l._5, Some(l._6), l._7, Some(l._8), l._9)
}

case class LineSettlement  (tid:Long = 0L, transid:Long =0, modelId:Int = 125,account:Option[String] = None,  side:Boolean = true, oaccount:Option[String] = None, amount: Amount = 0,
                         duedate:Option[Date] = Some(new Date()),text:String ="txt",
                         modified:Boolean= false, created:Boolean= true, deleted:Boolean= false) extends LineFinancialsTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LinePayment]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineSettlement => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineSettlement
{
  def apply(l:LineFDocument) = new LineSettlement(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)
   def apply(l:LineSettlement_TYPE) = new LineSettlement(l._1,l._2, l._3, Some(l._4), l._5, Some(l._6), l._7, Some(l._8), l._9)
}

case class Payment[LinePayment] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 114,store:Option[String]=None, account:Option[String]= None,
                                             text:String ="", lines:Option[List[LinePayment]]=Some(List.empty[LinePayment]),
                                             modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LinePayment]{
  def add(line:LinePayment) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LinePayment] = lines.getOrElse(List.empty[LinePayment])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LinePayment) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[Payment[LinePayment]]
  override def equals(that: Any): Boolean =
    that match {
      case that: Payment[LinePayment] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object Payment 
{

  def apply(f:FDocument[LineFDocument])  =  new Payment[LinePayment]  (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>(
             LinePayment(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)))))
}
case class Settlement[LineSettlement] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 124,store:Option[String]=None, account:Option[String]= None,
                                 text:String ="", lines:Option[List[LineSettlement]]=Some(List.empty[LineSettlement]),
                                 modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineSettlement]{
  def add(line:LineSettlement) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineSettlement] = lines.getOrElse(List.empty[LineSettlement])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineSettlement) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[Settlement[LineSettlement]]
  override def equals(that: Any): Boolean =
    that match {
      case that: Settlement[LineSettlement] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object Settlement
{

  def apply(f:FDocument[LineFDocument])  =  new Settlement[LineSettlement]  (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>(
    LineSettlement(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)))))
}


case class LineFDocument  (tid:Long = 0L, transid:Long =0, modelId:Int,account:Option[String] = None,  side:Boolean = true, oaccount:Option[String] = None, amount: Amount = 0,
                         duedate:Option[Date] = Some(new Date()),text:String ="txt",
                         modified:Boolean= false, created:Boolean= true, deleted:Boolean= false) extends LineFinancialsTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineFDocument]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineFDocument => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineFDocument 
{
 def apply(l:LineFinancialsTransaction) = new LineFDocument(l.tid,l.transid, l.modelId, l.account, l.side, l.oaccount, l.amount, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineFDocument_TYPE) = new LineFDocument(l._1,l._2, l._3, Some(l._4), l._5, Some(l._6), l._7, Some(l._8), l._9)
}
case class FDocument[LineFDocument] (tid:Long = 0L,oid:Long = 0L, modelId:Int ,store:Option[String]=None, account:Option[String]= None,
                                 text:String ="", lines:Option[List[LineFDocument]]=Some(List.empty[LineFDocument]),
                                 modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineFDocument]{
  def add(line:LineFDocument) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineFDocument] = lines.getOrElse(List.empty[LineFDocument])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineFDocument) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[FDocument[LineFDocument]]
  override def equals(that: Any): Boolean =
    that match {
      case that: FDocument[LineFDocument] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

}
object FDocument 
{
  def apply(f:CustomerInvoice[LineCustomerInvoice])  =  new FDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineFDocument(l))))
  def apply(f:Settlement[LineSettlement])  =  new FDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineFDocument(l))))
  def apply(f:VendorInvoice[LineVendorInvoice])  =  new FDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineFDocument(l))))
  def apply(f:Payment[LinePayment])  =  new FDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l => LineFDocument(l))))
}

case class IDocument[LineIDocument] (tid:Long = 0L,oid:Long = 0L, modelId:Int ,store:Option[String]=None, account:Option[String]= None,
                                     text:String ="", lines:Option[List[LineIDocument]]=Some(List.empty[LineIDocument]),
                                     modified:Boolean =false, created:Boolean = true, deleted:Boolean = false) extends Transaction [LineIDocument]{
  def add(line:LineIDocument) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineIDocument] = lines.getOrElse(List.empty[LineIDocument])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
  def replaceLine( newLine:LineIDocument) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
  override def  canEqual(a: Any) = a.isInstanceOf[IDocument[LineIDocument]]
  override def equals(that: Any): Boolean =
    that match {
      case that: IDocument[LineIDocument] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }

}
object IDocument
{

def apply(f:SalesOrder[LineSalesOrder]): IDocument[LineIDocument]  =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l => LineIDocument(l))),f.modified, f.created, f.deleted)
def apply(f:InventoryInvoice[LineInventoryInvoice]):IDocument[LineIDocument]   =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineIDocument(l))), f.modified, f.created, f.deleted)
def apply(f:PurchaseOrder[LinePurchaseOrder]):IDocument[LineIDocument]   =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineIDocument(l))), f.modified, f.created, f.deleted)
def apply(f:Goodreceiving[LineGoodreceiving]):IDocument[LineIDocument]   =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineIDocument(l))),f.modified, f.created, f.deleted)
def apply(f:BillOfDelivery[LineBillOfDelivery]):IDocument[LineIDocument]   =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l =>LineIDocument(l))), f.modified, f.created, f.deleted)
def apply(f:SalesInvoice[LineSalesInvoice]): IDocument[LineIDocument]  =  new IDocument (f.tid, f.oid, f.modelId, f.store, f.account, f.text, f.lines.map( x => x.map (l => LineIDocument(l))),f.modified, f.created, f.deleted)

}

case class LineIDocument  (tid:Long = 0L, transid:Long =0, modelId:Int,item:Option[String] = None, unit:Option[String] = None, price: Amount = 0,
                           quantity:Amount = 0,vat:Option[String] = None, duedate:Option[Date] = Some(new Date()),text:String ="txt",
                           modified:Boolean= false, created:Boolean= false, deleted:Boolean= false) extends LineInventoryTransaction {
  def eq (id:Long):Boolean = tid == id
  def eq0:Boolean = tid == 0L
  def eqId(id:Long):Boolean = tid == id
  def canEqual(a: Any) = a.isInstanceOf[LineIDocument]
  override def equals(that: Any): Boolean =
    that match {
      case that: LineIDocument => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
object LineIDocument
{
  def apply(l:LineInventoryTransaction) = new LineIDocument(l.tid,l.transid, l.modelId, l.item, l.unit, l.price, l.quantity, l.vat, l.duedate, l.text, l.modified, l.created, l.deleted)
  def apply(l:LineIDocument_TYPE) = new LineIDocument(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), Some(l._9), l._10)
  //def apply(l:LineIDocument_TYPE) = new LineIDocument(l._1,l._2, l._3, Some(l._4), Some(l._5), l._6, l._7, Some(l._8), l._9, l._10, l._11,l._12)
}

object  Company_{ def unapply (in:Company) =Some(in.id,in.name,in.modelId,  in.street,in.city,in.state,in.zip,
  in.bankAccountId, in.purchasingClearingAccountId, in.salesClearingAccountId, in.paymentClearingAccountId,
  in.settlementClearingAccountId, in.periode, in.nextPeriode, in.taxCode, in.vatId)}
object  Supplier_{ def unapply (in:Supplier) =Some(in.id,in.name,in.modelId, in.accountId, in.street,in.city,in.state,in.zip)}
object  Customer_{ def unapply (in:Customer) =Some(in.id,in.name,in.modelId, in.accountId, in.street,in.city,in.state,in.zip)}
object  Store_{ def unapply (in:Store) =Some(in.id,in.name, in.modelId, in.accountId, in.street,in.city,in.state,in.zip, in.stocks)}
object  Vat_{ def unapply (in:Vat) =Some(in.id,in.name, in.modelId, in.description,in.percent, in.inputVatAccountId,
                                  in.outputVatAccountId, in.revenueAccountId, in.stockAccountId, in.expenseAccountId)}
object  CostCenter_{ def unapply (in:CostCenter) =Some(in.id,in.name, in.modelId, in.description)}
object  QuantityUnit_{ def unapply (in:QuantityUnit) =Some(in.id,in.name,in.modelId, in.description)}
object  Bank_{ def unapply (in:Bank) =Some(in.id,in.name,in.modelId, in.description)}
object  BankAccount_{ def unapply (in:BankAccount) =Some(in.id,in.name,in.modelId, in.description, in.bic, in.debit, in.credit)}
object  Account_{ def unapply (in:Account) =Some(in.id,in.name,in.modelId, in.description, in.groupId, in.accounts, in.dateOfOpen,in.dateOfClose,in.balance)
         def apply(x:ACCOUNT_TYPE) =Account(x._1, x._2, x._3, x._4, Some(x._5),None, Some(x._6), Some(x._7))}
object  Article_{ def unapply (in:Article) =Some(in.id,in.name, in.modelId, in.description, in.price,
  in.avgPrice, in.salesPrice, in.qttyUnit, in.packUnit, in.vat, in.articles)
 def apply( x :ARTICLE_TYPE) = Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7,  x._8, x._9, Some(x._10), Some(x._11))
}
object  PeriodicAccountBalance_{ def unapply (in:PeriodicAccountBalance) =Some(in.id,in.name,in.description, in.accountId, in.periode, in.debit, in.credit)}
object  Stock_{ def unapply (in:Stock) =Some(in.id,in.name,in.description, in.itemId, in.storeId, in.quantity, in.minStock)}
object  ArticleGroup_{ def unapply (in:ArticleGroup) =Some(in.id,in.name,in.modelId, in.description)}
object  LinePurchaseOrder_{ def unapply (in:LinePurchaseOrder) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  LineSalesOrder_{ def unapply (in:LineSalesOrder) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  PurchaseOrder_{ def unapply (in:PurchaseOrder[LinePurchaseOrder]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  SalesOrder_{ def unapply (in:SalesOrder[LineSalesOrder]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}

object  LineGoodreceiving_{ def unapply (in:LineGoodreceiving) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  LineBillOfDelivery_{ def unapply (in:LineBillOfDelivery) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  Goodreceiving_{ def unapply (in:Goodreceiving[LineGoodreceiving]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  BillOfDelivery_{ def unapply (in:BillOfDelivery[LineBillOfDelivery]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  LineInventoryInvoice_{ def unapply (in:LineInventoryInvoice) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  LineSalesInvoice_{ def unapply (in:LineSalesInvoice) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  InventoryInvoice_{ def unapply (in:InventoryInvoice[LineInventoryInvoice]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  SalesInvoice_{ def unapply (in:SalesInvoice[LineSalesInvoice]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  LineVendorInvoice_{ def unapply (in:LineVendorInvoice) = Some(in.tid,in.transid, in.modelId, in.account, in.side, in.oaccount, in.amount,  in.duedate, in.text)}
object  LineCustomerInvoice_{ def unapply (in:LineCustomerInvoice) = Some(in.tid,in.transid, in.modelId, in.account, in.side, in.oaccount, in.amount,  in.duedate, in.text)}
object  VendorInvoice_{ def unapply (in:VendorInvoice[LineVendorInvoice]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  CustomerInvoice_{ def unapply (in:CustomerInvoice[LineCustomerInvoice]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  LinePayment_{ def unapply (in:LinePayment) = Some(in.tid,in.transid, in.modelId, in.account, in.side, in.oaccount, in.amount,  in.duedate, in.text)}
object  LineSettlement_{ def unapply (in:LineSettlement) = Some(in.tid,in.transid, in.modelId, in.account, in.side, in.oaccount, in.amount,  in.duedate, in.text)}
object  Payment_{ def unapply (in:Payment[LinePayment]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  Settlement_{ def unapply (in:Settlement[LineSettlement]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}
object  LineFDocument_{ def unapply (in:LineFDocument) = Some(in.tid,in.transid, in.modelId, in.account, in.side, in.oaccount, in.amount,  in.duedate, in.text)}
object  FDocument_{ def unapply (in:FDocument[LineFDocument]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.text, in.lines)}

//object  GeneralRegister_{ def unapply (in:GeneralRegister) = Some(in.id, in.modelId, in.appelandId, in.intimeId, in.enterdate,in.reason,in.origin)}
//implicit def PurchaseOrderOrdering: Ordering[PurchaseOrder[LinePurchaseOrder]] = Ordering.fromLessThan(_.tid > _.tid)
//implicit def orderingById[A <: PurchaseOrder[LinePurchaseOrder]]: Ordering[A] = Ordering.by(e => (e.tid, e.tid))

/*
object Lenses {
  val accountL= GenLens[Account]
  val customerL= GenLens[Customer]
  val supplierL= GenLens[Supplier]
  val articleL= GenLens[Article]
  val articleGroupL= GenLens[ArticleGroup]
  val storeL= GenLens[Store]
  val quantityUnitL= GenLens[QuantityUnit]
  val purchaseOrderL= GenLens[PurchaseOrder[LinePurchaseOrder]]
  val costcenterL= GenLens[CostCenter]

  val AccountName     = accountL(_.name)

  //val AccountDescription     = lens[Account].description

}
*/

/*
object Account {


  private def validateAccountNo(id: String,modelId:Int) =
    if (id.isEmpty || id.size < 5) s"Account No has to be at least 5 characters long: found $id".failureNel[String]
    else id.successNel[String]

  private def validateOpenCloseDate(od: Date, cd: Option[Date]) = cd.map { c =>
    if (c before od) s"Close date [$c] cannot be earlier than open date [$od]".failureNel[(Option[Date], Option[Date])]
    else (od.some, cd).successNel[String]
  }.getOrElse {
    (od.some, cd).successNel[String]
  }

  private def validateRate(rate: BigDecimal) =
    if (rate <= BigDecimal(0)) s"Interest rate $rate must be > 0".failureNel[BigDecimal]
    else rate.successNel[String]

  def checkingAccount(id: String, name: String, modelId:Int, description: String, openDate: Option[Date], closeDate: Option[Date],
                      balance: Balance): \/[NonEmptyList[String], Account] = {

    val od = openDate.getOrElse(new Date())

    (
      validateAccountNo(id,modelId) |@|
        validateOpenCloseDate(openDate.getOrElse(new Date()), closeDate)
      ) { (n, d) =>
      Account(n, name, modelId,description, None, None, d._1, d._2, balance)
    }.disjunction
  }


  private def validateAccountAlreadyClosed(a: Account) = {
    if (a.dateOfClose isDefined) s"Account ${a.id} is already closed".failureNel[Account]
    else a.successNel[String]
  }

  private def validateCloseDate(a: Account, cd: Date) = {
    if (cd before a.dateOfOpen.get) s"Close date [$cd] cannot be earlier than open date [${a.dateOfOpen.get}]".failureNel[Date]
    else cd.successNel[String]
  }

  def close(a: Account, closeDate: Date): \/[NonEmptyList[String], Account] = {
    (validateAccountAlreadyClosed(a) |@| validateCloseDate(a, closeDate)) { (acc, d) =>
      acc match {
        case c: Account => c.copy(dateOfClose = Some(closeDate))
        //case s: SavingsAccount  => s.copy(dateOfClose = Some(closeDate))
      }
    }.disjunction
  }

  private def checkBalance(a: Account, amount: Amount) = {
    if (amount < 0 && a.balance.amount < -amount) s"Insufficient amount in ${a.id} to debit".failureNel[Account]
    else a.successNel[String]
  }

  def updateBalance(a: Account, amount: Amount): \/[NonEmptyList[String], Account] = {
    (validateAccountAlreadyClosed(a) |@| checkBalance(a, amount)) { (_, _) =>
      a match {
        case c: Account => c.copy(balance = Balance(c.balance.amount + amount))
        // case s: SavingsAccount  => s.copy(balance = Balance(s.balance.amount + amount))
      }
    }.disjunction
  }
}*/

object Model {

  import common._

 val accounts=List(
   Account("1000", "Kasse", 9, "Kasse",None, None, Some(today), Some(today)),
   Account("2000", "Bank", 9, "Bank", None, None, Some(today),Some(today)),
   Account("3000", "Forderung", 9, "Forderung", None, None, Some(today),Some(today))
 )

  val quantityUnits=List(
    QuantityUnit("KG","Kilogramm", 4, "Kilogram"),
    QuantityUnit("Ltr","Liter", 4, "Liter"),
    QuantityUnit("Krt","Karton", 4, "Karton"),
    QuantityUnit("Stk","Stueck", 4, "Stueck")
  )
  val articles=List(
    Article("001","Masterfile", 7, "Financials Application for Enterprise",BigDecimal(5000.0), BigDecimal(5000.0), BigDecimal(5000.0),"Stk", "Stk", None, None),
    Article("002","Inventory", 7, "Financials Application for Enterprise",BigDecimal(5000.0), BigDecimal(5000.0), BigDecimal(5000.0), "Stk", "Stk", None, None),
    Article("003","Purchasing",7,  "Financials Application for Enterprise",BigDecimal(5000.0),BigDecimal(5000.0), BigDecimal(5000.0),"Stk", "Stk", None, None),
    Article("004","CRM", 7, "Financials Application for Enterprise",BigDecimal(5000.0),BigDecimal(5000.0), BigDecimal(5000.0), "Stk", "Stk", None, None),
    Article("005","Financials",7, "Financials Application for Enterprise",BigDecimal(5000.0), BigDecimal(5000.0), BigDecimal(5000.0), "Stk", "Stk", None, None),
    Article("006","Analytics",7, "Enterprise Analytics & Decision support for Management",BigDecimal(50000.0), BigDecimal(5000.0), BigDecimal(5000.0), "Stk", "Stk", None, None)


  )

  val suppliers = List(
    Supplier("101", "Acme, Inc.",  1,    "99 Market Street", "Groundsville", "CA", "95199"),
    Supplier( "499", "Superior Coffee", 1, "1 Party Place",    "Mendocino",    "CA", "95460"),
    Supplier("150", "The High Ground", 1, "100 Coffee Lane",  "Meadows",      "CA", "93966")
  )
  val customers = List(
    Customer("501", "Chemec GmbH.",3,      "99 Market Street", "Groundsville", "CA", "95199"),
    Customer( "555", "Studentenwerk Bielefeld  AoR",3, "1 Party Place",    "Mendocino",    "CA", "95460"),
    Customer("560", "Gulp GmbH", 3, "100 Coffee Lane",   "Meadows",      "CA", "93966")
  )

  val constCenters=List(
    CostCenter("100","Vertrieb", 6,  "Sales"),
    CostCenter("200","Einkauf", 6, "Purchasing"),
    CostCenter("300","Produktion", 6, "Production"),
    CostCenter("400","Lager", 6, "Store"),
    CostCenter("500","Verwaltung", 6, "Accounting"),
    CostCenter("600","Geschaeftsleitung", 6, "Management")
  )

  val articleGroups=List(
    ArticleGroup("100","Getraenke", 8,  "Getraenke"),
    ArticleGroup("200","Auto", 8, "Auto"),
    ArticleGroup("300","Software", 8, "Software"),
    ArticleGroup("400","Hardware", 8, "Hardware"),
    ArticleGroup("500","Electronik", 8, "Electronik"),
    ArticleGroup("600","Service", 8, "Service")
  )


}
