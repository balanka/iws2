package com.kabasoft.iws.shared

import java.util.Date

import boopickle.Default._
import com.kabasoft.iws.shared.common.Amount

import util.{Failure, Success, Try}
import scalaz._
import Scalaz._

//import shapeless.lens
//import monocle.macros._
//import monocle.macros.GenLens

object common {
  type Amount = scala.math.BigDecimal
  def today = new Date()
}
sealed trait IWS {
  def id:String
  def modelId:Int
}

trait ContainerT [+A<:IWS,-B<:IWS] {
  def update(newItem: B): ContainerT [A,B]
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
  override def add(newItem: IWS)= Data(items :+ newItem)
  override def remove (item: IWS) = Data(items.filterNot(_.id==item.id))
}

sealed trait Trans extends IWS { def tid:Long}
sealed trait Transaction [L] extends Trans {
    def id = tid.toString
    def tid:Long
    def oid:Long
    //def modelId:Int
    def store:Option[String]
    def account:Option[String]
    def lines:Option[List[L]]
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
  def item: Option[String]
  def unit: Option[String]
  def price: Amount
  def duedate: Option[Date]
  def text: String
 }
sealed trait  LineInventoryTransaction extends LineTransaction {
   def quantity: Amount
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
  override def hashCode:Int = {
    val prime = 31
    var result = 1
    result = prime * result + tid.toInt
    result = prime * result + transid.hashCode
     result
  }
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
  override def hashCode:Int = {
    val prime = 31
    var result = 1
    result = prime * result + tid.toInt
    result = prime * result + transid.hashCode
    result
  }
}
case class PurchaseOrder[LinePurchaseOrder] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 101,store:Option[String]=None, account:Option[String]= None,
                                              lines:Option[List[LinePurchaseOrder]]=Some(List.empty[LinePurchaseOrder]),
                                              modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LinePurchaseOrder]{
 def add(line:LinePurchaseOrder) = copy(lines = Some(getLines ++: List(line)))
 def getLines = lines.getOrElse(List.empty[LinePurchaseOrder])
 def getLinesWithId(id:Long) = getLines.filter(equals(_,id))
 def replaceLine( newLine:LinePurchaseOrder) = copy(lines = Some( getLines map ( old => if (newLine.equals(old)) newLine else old )))
// def replaceLine( newLines: List[LinePurchaseOrder]):PurchaseOrder[LinePurchaseOrder] = (newLines map replaceLine).head
  override def  canEqual(a: Any) = a.isInstanceOf[PurchaseOrder[LinePurchaseOrder]]
  override def equals(that: Any): Boolean =
    that match {
      case that: PurchaseOrder[LinePurchaseOrder] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}
case class Goodreceiving[LineGoodreceiving] (tid:Long = 0L,oid:Long = 0L, modelId:Int = 104,store:Option[String]=None, account:Option[String]= None,
                                              lines:Option[List[LineGoodreceiving]]=Some(List.empty[LineGoodreceiving]),
                                              modified:Boolean =false, created:Boolean = false, deleted:Boolean = false) extends Transaction [LineGoodreceiving]{
  def add(line:LineGoodreceiving) = copy(lines = Some(getLines ++: List(line)))
  def getLines:List[LineGoodreceiving] = lines.getOrElse(List.empty[LineGoodreceiving])
  def getLinesWithId(id:Long) = getLines.filter(equals(_,id))

  def replaceLine( newLine:LineGoodreceiving) = copy(lines = Some( getLines map ( old => if (newLine.equals(old))  newLine else old )))
 // def setLineID = copy(lines = Some( getLines map ( e => if(e.tid != 0 ) e else  e.copy(tid = -1))))
  override def  canEqual(a: Any) = a.isInstanceOf[Goodreceiving[LineGoodreceiving]]
  override def equals(that: Any): Boolean =
    that match {
      case that: Goodreceiving[LineGoodreceiving] => that.canEqual(this) && this.hashCode == that.hashCode
      case _ => false
    }
}



// Registre general des affaires  frappees d'appel
//N°d'ordre	Appelant	Intimé	Objet	Origine
//Repertoire des arrets  civils (CAD)
//N°d’arrêt	Date d’audience	N°RG	Nom des parties	Objet	Nature d’arrêt	Origine de juridiction	Chambre
//Registre des appels an correctionels
//N°d'ordre	Date	Prénoms et nom des prévenus	Parti Civil	Prévention	Date et n°de jugement	Origine de Juridiction	Conservation
//  Registre des affaires criminelles
//N°d'ordre	Date d'entrée	1ère autorité saisie	Identité complète des accusés		Date des Faits	Nature et lieux des infractions	Suite Donnée	Partie civil
/*
case class  GeneralRegister(id:String ="", modelId:Int=1000,appelandId:String ="", intimeId:String ="", enterdate:Option[Date]=Some(new Date())
                            ,reason:String ="", origin:String="") extends IWS
case class  CAD(id:String ="", modelId:Int=1002, registerId:String ="", openingDate:Option[Date]=Some(new Date()), enterdate:Option[Date]=Some(new Date()),
                parties:Seq[String]=Seq.empty[String], reason:String ="", typ:String ="", origin:String="", chamber:String="") extends IWS

case class  CorrectionalRegister(id:String ="", modelId:Int=1001, appelandId:String ="", intimeId:String ="", enterdate:Option[Date]=Some(new Date()),
                                 origin:String="",reason:String ="") extends IWS

case class  CrimeRegister(id:String ="", modelId:Int=1003,name:String ="", enterdate:Option[Date]=Some(new Date()),
                          parties:Seq[String]=Seq.empty[String], prevention:String="",jugementdate:Option[Date]=Some(new Date()),
                          jugementId:String ="",origin:String="",conservation:String ="" ) extends IWS
 */

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
object  Account_{ def unapply (in:Account) =Some(in.id,in.name,in.modelId, in.description, in.groupId, in.accounts, in.dateOfOpen,in.dateOfClose,in.balance)}
object  Article_{ def unapply (in:Article) =Some(in.id,in.name, in.modelId, in.description, in.price,
  in.avgPrice, in.salesPrice, in.qttyUnit, in.packUnit, in.vat, in.articles)}
object  PeriodicAccountBalance_{ def unapply (in:PeriodicAccountBalance) =Some(in.id,in.name,in.description, in.accountId, in.periode, in.debit, in.credit)}
object  Stock_{ def unapply (in:Stock) =Some(in.id,in.name,in.description, in.itemId, in.storeId, in.quantity, in.minStock)}
object  ArticleGroup_{ def unapply (in:ArticleGroup) =Some(in.id,in.name,in.modelId, in.description)}
object  LinePurchaseOrder_{ def unapply (in:LinePurchaseOrder) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  PurchaseOrder_{ def unapply (in:PurchaseOrder[LinePurchaseOrder]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.lines)}
object  LineGoodreceiving_{ def unapply (in:LineGoodreceiving) = Some(in.tid,in.transid, in.modelId, in.item, in.unit, in.price, in.quantity, in.vat, in.duedate, in.text)}
object  Goodreceiving_{ def unapply (in:Goodreceiving[LineGoodreceiving]) = Some(in.tid,in.oid, in.modelId, in.store, in.account, in.lines)}
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
}

object Model {

  import common._

 val accounts=List(
   Account("1000", "Kasse", 9, "Kasse",None, None, today.some, today.some),
   Account("2000", "Bank", 9, "Bank", None, None, today.some, today.some),
   Account("3000", "Forderung", 9, "Forderung", None, None, today.some, today.some)
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
