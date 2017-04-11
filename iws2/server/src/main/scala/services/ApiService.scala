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
     case _: Bank => MakeService.make[Bank].create(item.modelId)
     case _: BankAccount =>MakeService.make[BankAccount].create(item.modelId)
     case _: Company =>MakeService.make[Company].create(item.modelId)
     case _: Account =>MakeService.make[Account].create(item.modelId)
     case _: Article => MakeService.make[Article].create(item.modelId)
     case _: Supplier => MakeService.make[Supplier].create(item.modelId)
     case _: Customer => MakeService.make[Customer].create(item.modelId)
     case _: Store => MakeService.make[Store].create(item.modelId)
     case _: QuantityUnit => MakeService.make[QuantityUnit].create(item.modelId)
     case _: Vat => MakeService.make[Vat].create(item.modelId)
     case _: CostCenter => MakeService.make[CostCenter].create(item.modelId)
     case _: ArticleGroup => MakeService.make[ArticleGroup].create(item.modelId)
     case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].create(item.modelId)
     case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].create(item.modelId)
     case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].create(item.modelId)
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].create(item.modelId)
     case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].create(item.modelId)
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].create(item.modelId)
     case _: FDocument[LineFDocument] => MakeService.make[FDocument[LineFDocument]].create(item.modelId)
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
     case _: LinePurchaseOrder => MakeService.make[LinePurchaseOrder].insert(List(item.asInstanceOf[LinePurchaseOrder]))
     case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].insert(List(item.asInstanceOf[Goodreceiving[LineGoodreceiving]]))
     case _: LineGoodreceiving => MakeService.make[LineGoodreceiving].insert(List(item.asInstanceOf[LineGoodreceiving]))
     case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].insert(List(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]]))
     case _: LineInventoryInvoice => MakeService.make[LineInventoryInvoice].insert(List(item.asInstanceOf[LineInventoryInvoice]))
     case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].insert(List(item.asInstanceOf[VendorInvoice[LineVendorInvoice]]))
     case _: LineVendorInvoice => MakeService.make[LineVendorInvoice].insert(List(item.asInstanceOf[LineVendorInvoice]))
     case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].insert(List(item.asInstanceOf[Payment[LinePayment]]))
     case _: LinePayment => MakeService.make[LinePayment].insert(List(item.asInstanceOf[LinePayment]))
     case _: FDocument[LineFDocument] => MakeService.make[FDocument[LineFDocument]].insert(List(item.asInstanceOf[FDocument[LineFDocument]]))
     case _: LineFDocument => MakeService.make[LineFDocument].insert(List(item.asInstanceOf[LineFDocument]))
   }
 }
  def find(item:IWS) = {
   item match {
    case _: Bank  => MakeService.make[Bank].find(item.asInstanceOf[Bank])
    case _: BankAccount  => MakeService.make[BankAccount].find(item.asInstanceOf[BankAccount])
    case _: Company  => MakeService.make[Company].find(item.asInstanceOf[Company])
    case _: Account  => MakeService.make[Account].find(item.asInstanceOf[Account])
    case _: Supplier => MakeService.make[Supplier].find(item.asInstanceOf[Supplier])
    case _: Customer => MakeService.make[Customer].find(item.asInstanceOf[Customer])
    case _: Store => MakeService.make[Store].find(item.asInstanceOf[Store])
    case _: QuantityUnit => MakeService.make[QuantityUnit].find(item.asInstanceOf[QuantityUnit])
    case _: Vat => MakeService.make[Vat].find(item.asInstanceOf[Vat])
    case _: CostCenter  => MakeService.make[CostCenter].find(item.asInstanceOf[CostCenter])
    case _: Article => MakeService.make[Article].find(item.asInstanceOf[Article])
    case _: ArticleGroup => MakeService.make[ArticleGroup].find(item.asInstanceOf[ArticleGroup])
    case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].find(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]])
    case _: LinePurchaseOrder => MakeService.make[LinePurchaseOrder].find(item.asInstanceOf[LinePurchaseOrder])
    case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].find(item.asInstanceOf[Goodreceiving[LineGoodreceiving]])
    case _: LineGoodreceiving => MakeService.make[LineGoodreceiving].find(item.asInstanceOf[LineGoodreceiving])
    case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].find(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]])
    case _: LineInventoryInvoice => MakeService.make[LineInventoryInvoice].find(item.asInstanceOf[LineInventoryInvoice])
    case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].find(item.asInstanceOf[VendorInvoice[LineVendorInvoice]])
    case _: LineVendorInvoice => MakeService.make[LineVendorInvoice].find(item.asInstanceOf[LineVendorInvoice])
    case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].find(item.asInstanceOf[Payment[LinePayment]])
    case _: LinePayment => MakeService.make[LinePayment].find(item.asInstanceOf[LinePayment])
    case _: FDocument[LineFDocument] => MakeService.make[FDocument[LineFDocument]].find(item.asInstanceOf[FDocument[LineFDocument]])
    case _: LineFDocument => MakeService.make[LineFDocument].find(item.asInstanceOf[LineFDocument])
   }
 }
   def findSome(item:IWS) = {
    item match {
    case _: Bank  => MakeService.make[Bank].findSome(item.asInstanceOf[Bank])
    case _: BankAccount  => MakeService.make[BankAccount].findSome(item.asInstanceOf[BankAccount])
    case _: Company  => MakeService.make[Company].findSome(item.asInstanceOf[Company])
    case _: Account  => MakeService.make[Account].findSome(item.asInstanceOf[Account])
    case _: Supplier => MakeService.make[Supplier].findSome(item.asInstanceOf[Supplier])
    case _: Customer => MakeService.make[Customer].findSome(item.asInstanceOf[Customer])
    case _: Store => MakeService.make[Store].findSome(item.asInstanceOf[Store])
    case _: QuantityUnit => MakeService.make[QuantityUnit].findSome(item.asInstanceOf[QuantityUnit])
    case _: Vat => MakeService.make[Vat].findSome(item.asInstanceOf[Vat])
    case _: CostCenter  => MakeService.make[CostCenter].findSome(item.asInstanceOf[CostCenter])
    case _: Article => MakeService.make[Article].findSome(item.asInstanceOf[Article])
    case _: ArticleGroup => MakeService.make[ArticleGroup].findSome(item.asInstanceOf[ArticleGroup])
    case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].findSome(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]])
    case _: LinePurchaseOrder => MakeService.make[LinePurchaseOrder].findSome(item.asInstanceOf[LinePurchaseOrder])
    case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].findSome(item.asInstanceOf[Goodreceiving[LineGoodreceiving]])
    case _: LineGoodreceiving => MakeService.make[LineGoodreceiving].findSome(item.asInstanceOf[LineGoodreceiving])
    case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].findSome(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]])
    case _: LineInventoryInvoice => MakeService.make[LineInventoryInvoice].findSome(item.asInstanceOf[LineInventoryInvoice])
    case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].findSome(item.asInstanceOf[VendorInvoice[LineVendorInvoice]])
    case _: LineVendorInvoice => MakeService.make[LineVendorInvoice].findSome(item.asInstanceOf[LineVendorInvoice])
    case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].findSome(item.asInstanceOf[Payment[LinePayment]])
    case _: LinePayment => MakeService.make[LinePayment].findSome(item.asInstanceOf[LinePayment])
    case _: FDocument[LineFDocument] => MakeService.make[FDocument[LineFDocument]].findSome(item.asInstanceOf[FDocument[LineFDocument]])
    case _: LineFDocument => MakeService.make[LineFDocument].findSome(item.asInstanceOf[LineFDocument])
    }
  }
   def all(item:IWS) ={
   println(s"  got all  call for  ${item}  as request")
    item match {
      case _: Bank => MakeService.make[Bank].all(item.asInstanceOf[Bank])
      case _: BankAccount => MakeService.make[BankAccount].all(item.asInstanceOf[BankAccount])
      case _: Company => MakeService.make[Company].all(item.asInstanceOf[Company])
      case _: Account => MakeService.make[Account].all(item.asInstanceOf[Account])
      case _: Supplier => MakeService.make[Supplier].all(item.asInstanceOf[Supplier])
      case _: Customer => MakeService.make[Customer].all(item.asInstanceOf[Customer])
      case _: Store => MakeService.make[Store].all(item.asInstanceOf[Store])
      case _: QuantityUnit => MakeService.make[QuantityUnit].all(item.asInstanceOf[QuantityUnit])
      case _: Vat => MakeService.make[Vat].all(item.asInstanceOf[Vat])
      case _: CostCenter => MakeService.make[CostCenter].all(item.asInstanceOf[CostCenter])
      case _: Article => MakeService.make[Article].all(item.asInstanceOf[Article])
      case _: ArticleGroup => MakeService.make[ArticleGroup].all(item.asInstanceOf[ArticleGroup])
      case _: PurchaseOrder[LinePurchaseOrder] => MakeService.make[PurchaseOrder[LinePurchaseOrder]].all(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]])
      case _: LinePurchaseOrder => MakeService.make[LinePurchaseOrder].all(item.asInstanceOf[LinePurchaseOrder])
      case _: Goodreceiving[LineGoodreceiving] => MakeService.make[Goodreceiving[LineGoodreceiving]].all(item.asInstanceOf[Goodreceiving[LineGoodreceiving]])
      case _: LineGoodreceiving => MakeService.make[LineGoodreceiving].all(item.asInstanceOf[LineGoodreceiving].asInstanceOf[LineGoodreceiving])
      case _: InventoryInvoice[LineInventoryInvoice] => MakeService.make[InventoryInvoice[LineInventoryInvoice]].all(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]])
      case _: LineInventoryInvoice => MakeService.make[LineInventoryInvoice].all(item.asInstanceOf[LineInventoryInvoice])
      case _: VendorInvoice[LineVendorInvoice] => MakeService.make[VendorInvoice[LineVendorInvoice]].all(item.asInstanceOf[VendorInvoice[LineVendorInvoice]])
      case _: LineVendorInvoice => MakeService.make[LineVendorInvoice].all(item.asInstanceOf[LineVendorInvoice])
      case _: Payment[LinePayment] => MakeService.make[Payment[LinePayment]].all(item.asInstanceOf[Payment[LinePayment]])
      case _: LinePayment => MakeService.make[LinePayment].all(item.asInstanceOf[LinePayment])
      case _: FDocument[LineFDocument] => MakeService.make[FDocument[LineFDocument]].all(item.asInstanceOf[FDocument[LineFDocument]])
      case _: LineFDocument => MakeService.make[LineFDocument].all(item.asInstanceOf[LineFDocument])
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
      case _: PurchaseOrder[LinePurchaseOrder] => {  MakeService.make[PurchaseOrder[LinePurchaseOrder]].update(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]]); find(item)}
      case _: LinePurchaseOrder => {  MakeService.make[LinePurchaseOrder].update(item.asInstanceOf[LinePurchaseOrder]); find(item)}
      case _: Goodreceiving[LineGoodreceiving] => {MakeService.make[Goodreceiving[LineGoodreceiving]].update(item.asInstanceOf[Goodreceiving[LineGoodreceiving]]); find(item)}
      case _: LineGoodreceiving => { MakeService.make[LineGoodreceiving].update(item.asInstanceOf[LineGoodreceiving]); find(item)}
      case _: InventoryInvoice[LineInventoryInvoice] => {MakeService.make[InventoryInvoice[LineInventoryInvoice]].update(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]]);find(item)}
      case _: LineInventoryInvoice => {MakeService.make[LineInventoryInvoice].update(item.asInstanceOf[LineInventoryInvoice]); find(item)}
      case _: VendorInvoice[LineVendorInvoice] => {MakeService.make[VendorInvoice[LineVendorInvoice]].update(item.asInstanceOf[VendorInvoice[LineVendorInvoice]]);find(item)}
      case _: LineVendorInvoice => {MakeService.make[LineVendorInvoice].update(item.asInstanceOf[LineVendorInvoice]); find(item)}
      case _: Payment[LinePayment] => {MakeService.make[Payment[LinePayment]].update(item.asInstanceOf[Payment[LinePayment]]);find(item)}
      case _: LinePayment => {MakeService.make[LinePayment].update(item.asInstanceOf[LinePayment]); find(item)}
      case _: FDocument[LineFDocument] => {MakeService.make[FDocument[LineFDocument]].update(item.asInstanceOf[FDocument[LineFDocument]]);find(item)}
      case _: LineFDocument => {MakeService.make[LineFDocument].update(item.asInstanceOf[LineFDocument]); find(item)}
    }
  }

   def delete(item: IWS) = {
     println(s"delete ${item} items")
    item match {
      case _: Bank  => {MakeService.make[Bank].delete(item.asInstanceOf[Bank]); all(item)}
      case _: BankAccount  => {MakeService.make[BankAccount].delete(item.asInstanceOf[BankAccount]); all(item)}
      case _: Company  => {MakeService.make[Company].delete(item.asInstanceOf[Company]); all(item)}
      case _: Account  => {MakeService.make[Account].delete(item.asInstanceOf[Account]); all(item)}
      case _: Supplier => {MakeService.make[Supplier].delete(item.asInstanceOf[Supplier]); all(item)}
      case _: Customer => {MakeService.make[Customer].delete(item.asInstanceOf[Customer]); all(item)}
      case _: Store => {MakeService.make[Store].delete(item.asInstanceOf[Store]); all(item)}
      case _: QuantityUnit => {MakeService.make[QuantityUnit].delete(item.asInstanceOf[QuantityUnit]); all(item)}
      case _: Vat => {MakeService.make[Vat].delete(item.asInstanceOf[Vat]); all(item)}
      case _: CostCenter  => {MakeService.make[CostCenter].delete(item.asInstanceOf[CostCenter]); all(item)}
      case _: Article => {MakeService.make[Article].delete(item.asInstanceOf[Article]); all(item)}
      case _: ArticleGroup => {MakeService.make[ArticleGroup].delete(item.asInstanceOf[ArticleGroup]); all(item)}
      case _: PurchaseOrder[LinePurchaseOrder] => {  MakeService.make[PurchaseOrder[LinePurchaseOrder]].delete(item.asInstanceOf[PurchaseOrder[LinePurchaseOrder]]); all(item)}
      case _: LinePurchaseOrder => {  MakeService.make[LinePurchaseOrder].delete(item.asInstanceOf[LinePurchaseOrder]); all(item)}
      case _: Goodreceiving[LineGoodreceiving] => {MakeService.make[Goodreceiving[LineGoodreceiving]].delete(item.asInstanceOf[Goodreceiving[LineGoodreceiving]]); all(item)}
      case _: LineGoodreceiving => { MakeService.make[LineGoodreceiving].delete(item.asInstanceOf[LineGoodreceiving]); all(item)}
      case _: InventoryInvoice[LineInventoryInvoice] => {MakeService.make[InventoryInvoice[LineInventoryInvoice]].delete(item.asInstanceOf[InventoryInvoice[LineInventoryInvoice]]);all(item)}
      case _: LineInventoryInvoice => {MakeService.make[LineInventoryInvoice].delete(item.asInstanceOf[LineInventoryInvoice]); all(item)}
      case _: VendorInvoice[LineVendorInvoice] => {MakeService.make[VendorInvoice[LineVendorInvoice]].delete(item.asInstanceOf[VendorInvoice[LineVendorInvoice]]);all(item)}
      case _: LineVendorInvoice => {MakeService.make[LineVendorInvoice].delete(item.asInstanceOf[LineVendorInvoice]); all(item)}
      case _: Payment[LinePayment] => {MakeService.make[Payment[LinePayment]].delete(item.asInstanceOf[Payment[LinePayment]]);all(item)}
      case _: LinePayment => {MakeService.make[LinePayment].delete(item.asInstanceOf[LinePayment]); all(item)}
      case _: FDocument[LineFDocument] => {MakeService.make[FDocument[LineFDocument]].delete(item.asInstanceOf[FDocument[LineFDocument]]);all(item)}
      case _: LineFDocument => {MakeService.make[LineFDocument].delete(item.asInstanceOf[LineFDocument]); all(item)}
    }
  }
}
