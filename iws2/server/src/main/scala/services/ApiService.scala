package services

import java.util.Date

import com.kabasoft.iws.shared._
import com.kabasoft.iws.dao._
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.services._
import com.kabasoft.iws.services.Request._


class ApiService extends Api {

   def welcome(name: String): String = s"Welcome to IWS, $name! Time is now ${new Date}"

  def create(item:IWS) ={
   item match {
     case _: Bank =>MakeService.make[Bank].create
     case _: BankAccount =>MakeService.make[BankAccount].create
     case _: Company =>MakeService.make[Company].create
     case _: Account =>MakeService.make[Account].create
     case _: Article => MakeService.make[Article].create
     case _: Supplier => MakeService.make[Supplier].create
     case _: Customer => MakeService.make[Customer].create
     case _: Store => MakeService.make[Store].create
     case _: QuantityUnit => MakeService.make[QuantityUnit].create
     case _: Vat => MakeService.make[Vat].create
     case _: CostCenter => MakeService.make[CostCenter].create
     case _: ArticleGroup => MakeService.make[ArticleGroup].create
     case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].create
     case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].create
     case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].create
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].create
     case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].create
   }
 }
  def insert(item:IWS) ={
    println(s"  got insert  call for  ${item}  as request")
   item match {
     case _: Bank =>MakeService.make[Bank].insert(List(item.asInstanceOf[Bank]))
     case _: BankAccount =>MakeService.make[BankAccount].insert(List(item.asInstanceOf[BankAccount]))
     case _: Company =>MakeService.make[Company].insert(List(item.asInstanceOf[Company]))
     case _: Account =>MakeService.make[Account].insert(List(item.asInstanceOf[Account]))
     case _: Article => MakeService.make[Article].insert(List(item.asInstanceOf[Article]))
     case _: Supplier => MakeService.make[Supplier].insert(List(item.asInstanceOf[Supplier]))
     case _: Customer => MakeService.make[Customer].insert(List(item.asInstanceOf[Customer]))
     case _: Store => MakeService.make[Store].insert(List(item.asInstanceOf[Store]))
     case _: QuantityUnit => MakeService.make[QuantityUnit].insert(List(item.asInstanceOf[QuantityUnit]))
     case _: Vat => MakeService.make[Vat].insert(List(item.asInstanceOf[Vat]))
     case _: CostCenter => MakeService.make[CostCenter].insert(List(item.asInstanceOf[CostCenter]))
     case _: Vat => MakeService.make[Vat].insert(List(item.asInstanceOf[Vat]))
     case _: ArticleGroup => MakeService.make[ArticleGroup].insert(List(item.asInstanceOf[ArticleGroup]))
     case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].insert(List(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]]))
     case _: LinePurchaseOrder => MakeService.makeTransaction[LinePurchaseOrder].insert(List(item.asInstanceOf[LinePurchaseOrder]))
     case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].insert(List(item.asInstanceOf[Goodreceiving[LineGoodreceiving]]))
     case _: LineGoodreceiving => MakeService.makeTransaction[LineGoodreceiving].insert(List(item.asInstanceOf[LineGoodreceiving]))
     case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].insert(List(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]]))
     case _: LineInventoryInvoice => MakeService.makeTransaction[LineInventoryInvoice].insert(List(item.asInstanceOf[LineInventoryInvoice]))
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].insert(List(item.asInstanceOf[VendorInvoice[LineVendorInvoice]]))
     case _: LineVendorInvoice => MakeService.makeTransaction[LineVendorInvoice].insert(List(item.asInstanceOf[LineVendorInvoice]))
     case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].insert(List(item.asInstanceOf[Payment[LinePayment]]))
     case _: LinePayment => MakeService.makeTransaction[LinePayment].insert(List(item.asInstanceOf[LinePayment]))     
   }
 }
  def find(item:IWS) ={
   item match {
     case _: Bank =>MakeService.make[Bank].find(item.id)
     case _: BankAccount =>MakeService.make[BankAccount].find(item.id)
     case _: Company =>MakeService.make[Company].find(item.id)
     case _: Account =>MakeService.make[Account].find(item.id)
     case _: Article => MakeService.make[Article].find(item.id)
     case _: Supplier => MakeService.make[Supplier].find(item.id)
     case _: Customer => MakeService.make[Customer].find(item.id)
     case _: Store => MakeService.make[Store].find(item.id)
     case _: QuantityUnit => MakeService.make[QuantityUnit].find(item.id)
     case _: Vat => MakeService.make[Vat].find(item.id)
     case _: CostCenter => MakeService.make[CostCenter].find(item.id)
     case _: ArticleGroup => MakeService.make[ArticleGroup].find(item.id)
     case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].find(item.id)
     case _: LinePurchaseOrder => MakeService.makeTransaction[LinePurchaseOrder].find(item.id)
     case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].find(item.id)
     case _: LineGoodreceiving => MakeService.makeTransaction[LineGoodreceiving].find(item.id)
     case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].find(item.id)
     case _: LineInventoryInvoice => MakeService.makeTransaction[LineInventoryInvoice].find(item.id)
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].find(item.id)
     case _: LineVendorInvoice => MakeService.makeTransaction[LineVendorInvoice].find(item.id)
     case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].find(item.id)
     case _: LinePayment => MakeService.makeTransaction[LinePayment].find(item.id)     
   }
 }
   def findSome(item:IWS) = {
    item match {
    case _: Bank  => MakeService.make[Bank].findSome(item.id)
    case _: BankAccount  => MakeService.make[BankAccount].findSome(item.id)
    case _: Company  => MakeService.make[Company].findSome(item.id)
    case _: Account  => MakeService.make[Account].findSome(item.id)
    case _: Supplier => MakeService.make[Supplier].findSome(item.id)
    case _: Customer => MakeService.make[Customer].findSome(item.id)
    case _: Store => MakeService.make[Store].findSome(item.id)
    case _: QuantityUnit => MakeService.make[QuantityUnit].findSome(item.id)
    case _: Vat => MakeService.make[Vat].findSome(item.id)
    case _: CostCenter  => MakeService.make[CostCenter].findSome(item.id)
    case _: Article => MakeService.make[Article].findSome(item.id)
    case _: ArticleGroup => MakeService.make[ArticleGroup].findSome(item.id)
    case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].findSome(item.id)
    case _: LinePurchaseOrder => MakeService.makeTransaction[LinePurchaseOrder].findSome(item.id)
    case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].findSome(item.id)
    case _: LineGoodreceiving => MakeService.makeTransaction[LineGoodreceiving].findSome(item.id)
    case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].findSome(item.id)
    case _: LineInventoryInvoice => MakeService.makeTransaction[LineInventoryInvoice].findSome(item.id)
    case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].findSome(item.id)
    case _: LineVendorInvoice => MakeService.makeTransaction[LineVendorInvoice].findSome(item.id)
    case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].findSome(item.id)
    case _: LinePayment => MakeService.makeTransaction[LinePayment].findSome(item.id)      
    }
  }
   def all(item:IWS) ={
   println(s"  got all  call for  ${item}  as request")
    item match {
      case _: Bank => MakeService.make[Bank].all
      case _: BankAccount => MakeService.make[BankAccount].all
      case _: Company => MakeService.make[Company].all
      case _: Account => MakeService.make[Account].all
      case _: Supplier => MakeService.make[Supplier].all
      case _: Customer => MakeService.make[Customer].all
      case _: Store => MakeService.make[Store].all
      case _: QuantityUnit => MakeService.make[QuantityUnit].all
      case _: Vat => MakeService.make[Vat].all
      case _: CostCenter => MakeService.make[CostCenter].all
      case _: Article => MakeService.make[Article].all
      case _: ArticleGroup => MakeService.make[ArticleGroup].all
      case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].all
      case _: LinePurchaseOrder => MakeService.makeTransaction[LinePurchaseOrder].all
      case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].all
      case _: LineGoodreceiving => MakeService.makeTransaction[LineGoodreceiving].all
      case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].all
      case _: LineInventoryInvoice => MakeService.makeTransaction[LineInventoryInvoice].all
      case _: VendorInvoice[LineVendorInvoice] => MakeService.makeTransaction[VendorInvoice[LineVendorInvoice]].all
      case _: LineVendorInvoice => MakeService.makeTransaction[LineVendorInvoice].all
      case _: Payment[LinePayment] => MakeService.makeTransaction[Payment[LinePayment]].all
      case _: LinePayment => MakeService.makeTransaction[LinePayment].all      
    }
  }
   def update(item:IWS) = {
    println(s" update ${item} items")
    item match {
      case _: Bank  => {MakeService.make[Bank].update(item.asInstanceOf[Bank]); find(item)}
      case _: BankAccount  => {MakeService.make[BankAccount].update(item.asInstanceOf[BankAccount]); find(item)}
      case _: Company  => {MakeService.make[Company].update(item.asInstanceOf[Company]); find(item)}
      case _: Account  => {MakeService.make[Account].update(item.asInstanceOf[Account]); find(item)}
      case _: Supplier => {MakeService.make[Supplier].update(item.asInstanceOf[Supplier]); find(item)}
      case _: Customer => {MakeService.make[Customer].update(item.asInstanceOf[Customer]); find(item)}
      case _: Store => {MakeService.make[Store].update(item.asInstanceOf[Store]); find(item)}
      case _: QuantityUnit => {MakeService.make[QuantityUnit].update(item.asInstanceOf[QuantityUnit]); find(item)}
      case _: Vat => {MakeService.make[Vat].update(item.asInstanceOf[Vat]); all(item)}
      case _: CostCenter  => {MakeService.make[CostCenter].update(item.asInstanceOf[CostCenter]); find(item)}
      case _: Article => {MakeService.make[Article].update(item.asInstanceOf[Article]); all(item)}
      case _: ArticleGroup => {MakeService.make[ArticleGroup].update(item.asInstanceOf[ArticleGroup]); find(item)}
      case _: PurchaseOrder[LinePurchaseOrder] => {  MakeService.makeTransaction[PurchaseOrder[LinePurchaseOrder]].update(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]]); find(item)}
      case _: LinePurchaseOrder => {  MakeService.makeTransaction[LinePurchaseOrder].update(item.asInstanceOf[LinePurchaseOrder]); find(item)}
      case _: Goodreceiving[LineGoodreceiving] => {MakeService.make[Goodreceiving[LineGoodreceiving]].update(item.asInstanceOf[Goodreceiving[LineGoodreceiving]]); find(item)}
      case _: LineGoodreceiving => { MakeService.makeTransaction[LineGoodreceiving].update(item.asInstanceOf[LineGoodreceiving]); find(item)}
      case _: InventoryInvoice[LineInventoryInvoice] => {MakeService.makeTransaction[InventoryInvoice[LineInventoryInvoice]].update(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]]);find(item)}
      case _: LineInventoryInvoice => {MakeService.makeTransaction[LineInventoryInvoice].update(item.asInstanceOf[LineInventoryInvoice]); find(item)}
      case _: VendorInvoice[LineVendorInvoice] => {MakeService.make[VendorInvoice[LineVendorInvoice]].update(item.asInstanceOf[VendorInvoice[LineVendorInvoice]]);find(item)}
      case _: LineVendorInvoice => {MakeService.makeTransaction[LineVendorInvoice].update(item.asInstanceOf[LineVendorInvoice]); find(item)}
      case _: Payment[LinePayment] => {MakeService.make[Payment[LinePayment]].update(item.asInstanceOf[Payment[LinePayment]]);find(item)}
      case _: LinePayment => {MakeService.makeTransaction[LinePayment].update(item.asInstanceOf[LinePayment]); find(item)}
    }
  }

   def delete(item: IWS) = {
     println(s"delete ${item} items")
    item match {
      case _: Bank  => {MakeService.make[Bank].delete(item.id); all(item)}
      case _: BankAccount  => {MakeService.make[BankAccount].delete(item.id); all(item)}
      case _: Company  => {MakeService.make[Company].delete(item.id); all(item)}
      case _: Account  => {MakeService.make[Account].delete(item.id); all(item)}
      case _: Article  => {MakeService.make[Article].delete(item.id); all(item)}
      case _: Supplier => {MakeService.make[Supplier].delete(item.id); all(item)}
      case _: Customer => {MakeService.make[Customer].delete(item.id); all(item)}
      case _: Store => {MakeService.make[Store].delete(item.id); all(item)}
      case _: QuantityUnit => {MakeService.make[QuantityUnit].delete(item.id); all(item)}
      case _: Vat => {MakeService.make[Vat].delete(item.id); all(item)}
      case _: CostCenter  => {MakeService.make[CostCenter].delete(item.id); all(item)}
      case _: ArticleGroup  => {MakeService.make[ArticleGroup].delete(item.id); all(item)}
      case _: PurchaseOrder[LinePurchaseOrder] => {MakeService.make[PurchaseOrder[LinePurchaseOrder]].delete(item.id); all(item)}
      case _: LinePurchaseOrder => {MakeService.makeTransaction[LinePurchaseOrder].delete(item.id); all(item)}
      case _: Goodreceiving[LineGoodreceiving] => { MakeService.make[Goodreceiving[LineGoodreceiving]].delete(item.id); all(item)}
      case _: LineGoodreceiving => { MakeService.makeTransaction[LineGoodreceiving].delete(item.id); all(item)}
      case _: InventoryInvoice[LineInventoryInvoice] => { MakeService.makeTransaction[InventoryInvoice[LineInventoryInvoice]].delete(item.id); all(item)}
      case _: LineInventoryInvoice => { MakeService.makeTransaction[LineInventoryInvoice].delete(item.id); all(item)}
      case _: VendorInvoice[LineVendorInvoice] => {MakeService.make[VendorInvoice[LineVendorInvoice]].delete(item.id); all(item)}
      case _: LineVendorInvoice => {MakeService.makeTransaction[LineVendorInvoice].delete(item.id); all(item)}
      case _: Payment[LinePayment] => {MakeService.make[Payment[LinePayment]].delete(item.id); all(item)}
      case _: LinePayment => {MakeService.makeTransaction[LinePayment].delete(item.id); all(item)}
    }
  }
}
