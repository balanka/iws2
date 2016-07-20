package com.kabasoft.iws.dao

import java.util.{Calendar, Date}
import scalaz.{Store => _, Scalaz, Foldable, ~>, Id}
import Scalaz._
import  scalaz.Foldable
import Id.Id
import scalaz.effect.IO
import scalaz.stream.Process
import scalaz.concurrent.Task
import doobie.imports._
import doobie.syntax.string._
import doobie.util.transactor.DriverManagerTransactor
import doobie.hi.ConnectionIO
import com.kabasoft.iws.shared._
import com.kabasoft.iws.dao._
import com.kabasoft.iws.shared.DAO
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.dao.Queries._


object DAOObjects  {

  val xa= DriverManagerTransactor[Task]("org.postgresql.Driver","jdbc:postgresql:world","postgres","")

  implicit def accountDAO = new DAO[Account] {

    def insert(model: List[Account]): Int = doobie.imports.Update[Queries.ACCOUNT_TYPE](Queries.accountInsertSQL).
      updateMany(model.map(x =>
      (x.id, x.name, x.modelId, x.description, x.groupId.getOrElse(""), x.dateOfOpen.getOrElse(new Date()), x.dateOfClose.getOrElse(new Date())))).transact(xa).run

    def create = Queries.createAccount.run.transact(xa).run
    def update(model: Account): Int = Queries.accountUpdateName(model).run.transact(xa).run
    def delete(id: String): Int = Queries.accountDelete(id).run.transact(xa).run
    def all: List[Account] = Queries.accountSelect.process.list.transact(xa).run.map(x =>
      Account(x._1, x._2, x._3, x._4, Some(x._5),None, Some(x._6), Some(x._7)).copy(accounts = Some(findSome2(x._1))))

    def find(id: String): List[Account] = Queries.accountIdSelect(id).process.list.transact(xa).run.map(x =>
     Account(x._1, x._2, x._3, x._4, Some(x._5), None, Some(x._6), Some(x._7)).copy(accounts = Some(findSome2(x._1))))

    def findSome(id: String): List[Account] = Queries.accountSelectSome(id).process.list.transact(xa).run.map(x =>
     Account(x._1, x._2, x._3, x._4, Some(x._5),None, Some(x._6), Some(x._7)).copy(accounts = Some(findSome2(x._1))))

    def findSome1(id: Long): List[Account] = Queries.accountSelectSome(id + "").process.list.transact(xa).run.map(x =>
      Account(x._1, x._2, x._3, x._4, Some(x._5), None, Some(x._6), Some(x._7)).copy(accounts = Some(findSome2(x._1))))

    def findSome2(groupid: String): List[Account] = Queries.accountSelectByGroupId(groupid).process.list.transact(xa).run.map(x =>
      Account(x._1, x._2, x._3, x._4, Some(x._5), None, Some(x._6), Some(x._7)).copy(accounts = Some(findSome2(x._1))))

  }

  implicit def articleDAO = new DAO[Article]
  {
     def insert(model: List[Article]): Int = doobie.imports.Update[Queries.ARTICLE_TYPE](Queries.articleInsertSQL).
      updateMany(model.map(x =>
        (x.id, x.name, x.modelId, x.description, x.price,  x.avgPrice, x.salesPrice, x.qttyUnit, x.packUnit,  x.groupId.getOrElse(""), x.vat.getOrElse("0")))).transact(xa).run

     def create = Queries.createArticle.run.transact(xa).run
     def update(model:Article):Int = Queries.articleUpdateName(model).run.transact(xa).run
     def delete(id:String):Int = Queries.articleDelete(id).run.transact(xa).run
     def all: List[Article] = Queries.articleSelect.process.list.transact(xa).run.map(x =>
      Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7, x._8, x._9, Some(x._10), Some(x._11)).copy(articles = Some(findSome2(x._1))))

    def find(id: String): List[Article] = Queries.articleIdSelect(id).process.list.transact(xa).run.map(x =>
      Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7,  x._8, x._9, Some(x._10), Some(x._11)).copy(articles = Some(findSome2(x._1))))

    def findSome(id: String): List[Article] = Queries.articleSelectSome(id).process.list.transact(xa).run.map(x =>
      Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7,  x._8, x._9, Some(x._10), Some(x._11)).copy(articles = Some(findSome2(x._1))))

    def findSome1(id: Long): List[Article] = Queries.articleSelectSome(id + "").process.list.transact(xa).run.map(x =>
      Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7,  x._8, x._9, Some(x._10), Some(x._11)).copy(articles = Some(findSome2(x._1))))

    def findSome2(groupId: String): List[Article] = Queries.articleSelectByGroupId(groupId).process.list.transact(xa).run.map(x =>
      Article(x._1, x._2, x._3, x._4, x._5, x._6, x._7,  x._8, x._9, Some(x._10), Some(x._11)).copy(articles = Some(findSome2(x._1))))
  }

  implicit def bankDAO = new DAO[Bank]
  {
    def insert(model: List[Bank]) :Int =  Update[Bank](Queries.bankInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createBank.run.transact(xa).run
    def update(model:Bank) = Queries.bankUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.bankDelete(id).run.transact(xa).run
    def all= Queries.bankSelect.process.list.transact(xa).run
    def find(id:String) : List[Bank] = Queries.bankIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.bankSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.bankSelectSome(id+"").process.list.transact(xa).run
  }

  implicit def bankAccountDAO = new DAO[BankAccount]
  {
    def insert(model: List[BankAccount]) :Int =  Update[BankAccount](Queries.bankAccountInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createBankAccount.run.transact(xa).run
    def update(model:BankAccount) = Queries.bankAccountUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.bankAccountDelete(id).run.transact(xa).run
    def all= Queries.bankAccountSelect.process.list.transact(xa).run
    def find(id:String) : List[BankAccount] = Queries.bankAccountIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.bankAccountSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.bankAccountSelectSome(id+"").process.list.transact(xa).run
  }


  implicit def categoryDAO = new DAO[ArticleGroup]
  {
    def insert( model: List[ArticleGroup]) :Int = Update[ArticleGroup] (Queries.categoryInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createCategory.run.transact(xa).run
    def update(model:ArticleGroup):Int = Queries.categoryUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.categoryDelete(id).run.transact(xa).run
    def all = Queries.categorySelect.process.list.transact(xa).run
    def find(id:String) : List[ArticleGroup] = Queries.categoryIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.categorySelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.categorySelectSome(id+"").process.list.transact(xa).run
  }
  implicit def companyDAO = new DAO[Company]
  {
    def insert(model: List[Company]) :Int =  Update[Company](Queries.companyInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createCompany.run.transact(xa).run
    def update(model:Company) = Queries.companyUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.companyDelete(id).run.transact(xa).run
    def all= Queries.companySelect.process.list.transact(xa).run
    def find(id:String) : List[Company] = Queries.companyIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.companySelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.companySelectSome(id+"").process.list.transact(xa).run
  }

  implicit def customerDAO = new DAO[Customer]
  {
    def insert(model: List[Customer]) :Int =  Update[Customer](Queries.customerInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createCustomer.run.transact(xa).run
    def update(model:Customer) = Queries.customerUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.customerDelete(id).run.transact(xa).run
    def all= Queries.customerSelect.process.list.transact(xa).run
    def find(id:String) : List[Customer] = Queries.customerIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.customerSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.customerSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def supplierDAO = new DAO[Supplier]
  {
    def insert(model: List[Supplier]) :Int =  Update[Supplier](Queries.supplierInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createSupplier.run.transact(xa).run
    def update(model:Supplier) = Queries.supplierUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.supplierDelete(id).run.transact(xa).run
    def all = Queries.supplierSelect.process.list.transact(xa).run
    def find(id:String) : List[Supplier] = Queries.supplierIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.supplierSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.supplierSelectSome(id+"").process.list.transact(xa).run
  }
    implicit def storeDAO = new DAO[Store]
  {
    def insert(model: List[Store]) :Int =  Update[Store](Queries.storeInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createStore.run.transact(xa).run
    def update(model:Store) = {  println(s"sql ${ Queries.storeUpdateName(model)}") ; Queries.storeUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.storeDelete(id).run.transact(xa).run
    def all = Queries.storeSelect.process.list.transact(xa).run
    def find(id:String) : List[Store] = Queries.storeIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.storeSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.storeSelectSome(id+"").process.list.transact(xa).run
  }

  implicit def costCenterDAO = new DAO[CostCenter]
  {
    def insert(model: List[CostCenter]) :Int =  Update[CostCenter](Queries.costCenterInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createCostCenter.run.transact(xa).run
    def update(model:CostCenter) = Queries.costCenterUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.costCenterDelete(id).run.transact(xa).run
    def all = Queries.costCenterSelect.process.list.transact(xa).run
    def find(id:String)  : List[CostCenter] = Queries.costCenterIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.costCenterSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.costCenterSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def quantityUnitDAO = new DAO[QuantityUnit]
  {
    def insert(model: List[QuantityUnit]) :Int =  Update[QuantityUnit](Queries.quantityUnitInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createQuantityUnit.run.transact(xa).run
    def update(model:QuantityUnit) = Queries.quantityUnitUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.quantityUnitDelete(id).run.transact(xa).run
    def all = Queries.quantityUnitSelect.process.list.transact(xa).run
    def find(id:String)  : List[QuantityUnit] = Queries.quantityUnitIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.quantityUnitSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.quantityUnitSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def vatDAO = new DAO[Vat]
  {
    def insert(model: List[Vat]) :Int =  Update[Vat](Queries.vatInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createVat.run.transact(xa).run
    def update(model:Vat) = Queries.vatUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.vatDelete(id).run.transact(xa).run
    def all = Queries.vatSelect.process.list.transact(xa).run
    def find(id:String)  : List[Vat] = Queries.vatIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.vatSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.vatSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def periodicAccountBalanceDAO = new DAO[PeriodicAccountBalance]
  {
    def insert(model: List[PeriodicAccountBalance]) :Int =  Update[PeriodicAccountBalance](Queries.periodicAccountBalanceInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createPeriodicAccountBalance.run.transact(xa).run
    def update(model:PeriodicAccountBalance) = Queries.periodicAccountBalanceUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.periodicAccountBalanceDelete(id).run.transact(xa).run
    def all = Queries.periodicAccountBalanceSelect.process.list.transact(xa).run
    def find(id:String)  : List[PeriodicAccountBalance] = Queries.periodicAccountBalanceIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.periodicAccountBalanceSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.periodicAccountBalanceSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def stockDAO = new DAO[Stock]
  {
    def insert(model: List[Stock]) :Int =  Update[Stock](Queries.stockInsertSQL).updateMany(model).transact(xa).run
    def create = Queries.createStock.run.transact(xa).run
    def update(model:Stock) = Queries.stockUpdateName(model).run.transact(xa).run
    def delete(id:String):Int = Queries.stockDelete(id).run.transact(xa).run
    def all = Queries.stockSelect.process.list.transact(xa).run
    def find(id:String)  : List[Stock] = Queries.stockIdSelect(id).process.list.transact(xa).run
    def findSome(id:String) = Queries.stockSelectSome(id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.stockSelectSome(id+"").process.list.transact(xa).run
  }
    implicit def linePurchaseOrderDAO = new DAO[LinePurchaseOrder]{
    def insert(model: List[LinePurchaseOrder]) :Int = {
      println(s" inserting ${model}  items")
      val tid:Long = Queries.getSequence ("LinePurchaseOrder", "id").unique.transact(xa).run;
      println(s"  getSequence ${tid} ")
      val ret= doobie.imports.Update[(Long, Long, Int,String,String,BigDecimal,BigDecimal,String,Date,String)](Queries.linePurchaseOrderInsertSQL).
        updateMany(model.filter(_.item != None).map( x=>(tid, x.transid, x.modelId,x.item.get, x.unit.get, x.price, x.quantity,x.vat.get,x.duedate.get,x.text))).transact(xa).run
      ret
    }
    def create = Queries.createLinePurchaseOrder.run.transact(xa).run
    def update(model:LinePurchaseOrder) = { println(s" updating ${model}  items"); Queries.linePurchaseOrderUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.linePurchaseOrderDelete(id.toLong).run.transact(xa).run
    def all:List[LinePurchaseOrder] =  Queries.linePurchaseOrderSelect.process.list.transact(xa).run.map(x =>
                                LinePurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def  find(id:String):List[LinePurchaseOrder] = Queries.linePurchaseOrderIdSelect(id.toLong).process.list.transact(xa).run.map(x =>
                            LinePurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome(id:String) = Queries.linePurchaseOrderSelectSome(id).process.list.transact(xa).run.map(x =>
                          LinePurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome1(id:Long) = Queries.linePurchaseOrderIdSelect(id).process.list.transact(xa).run.map(x => LinePurchaseOrder(x._1,x._2, x._3, Some(x._4),
                                                                                   Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
  }

implicit def purchaseOrderDAO = new DAO[PurchaseOrder[LinePurchaseOrder]]{ 
  def predicate(p:LinePurchaseOrder) = p.id==0
  def insert(model: List[PurchaseOrder[LinePurchaseOrder]]) :Int = {
      val tid:Long = Queries.getSequence ("purchaseorder", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[(Long,Long, Int, String,String)](Queries.purchaseOrderInsertSQL).updateMany(model.map(
                     x=>(tid, x.oid, x.modelId, x.store.get,x.account.get))).transact(xa).run;
      model.map( x=>implicitly[DAO[LinePurchaseOrder]].insert(x.lines.getOrElse(List[LinePurchaseOrder]()).map( z => z.copy(transid=tid))))
      ret
      
     }
    def create = Queries.createPurchaseOrder1.run.transact(xa).run
    def update(model:PurchaseOrder[LinePurchaseOrder]) = {
      val ret = Queries.purchaseOrderUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LinePurchaseOrder) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = linePurchaseOrderDAO.insert(newLines)
      //val k0 = linePurchaseOrderDAO.insert(model.getLines.partition(_.tid ==0L && _.created == true)._1)
      val k1= model.getLines.partition(_.modified)._1.map (linePurchaseOrderDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>linePurchaseOrderDAO.delete(e.id.toString) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(id:String):Int = {

      val r = find(id).map(p =>(p.getLines.map(l =>(linePurchaseOrderDAO.delete(l.id)))))
      Queries.purchaseOrderDelete(id.toLong).run.transact(xa).run
    }
    def all:List[PurchaseOrder[LinePurchaseOrder]] = Queries.purchaseOrderSelect.process.list.transact(xa).run.map(
                       x => PurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5)).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[PurchaseOrder[LinePurchaseOrder]] =
               Queries.purchaseOrderIdSelect(id.toLong).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1,x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.purchaseOrderSelectSome(id).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1, x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LinePurchaseOrder]].findSome1(a)
    def findSome1(id:Long) =Queries.purchaseOrderIdSelect(id).process.list.transact(xa).run.map(
                     x=> PurchaseOrder(x._1,x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LinePurchaseOrder):LinePurchaseOrder = line.copy(transid=id)
  }

  implicit def lineGoodreceivingDAO = new DAO[LineGoodreceiving]{
    def insert(model: List[LineGoodreceiving]) :Int = {
      println(s" inserting ${model}  items")
      val tid:Long = Queries.getSequence ("LineGoodreceiving", "id").unique.transact(xa).run;
      println(s"  getSequence ${tid} ")
      val ret= doobie.imports.Update[(Long, Long, Int,String,String,BigDecimal,BigDecimal,String,Date,String)](Queries.lineGoodreceivingInsertSQL).
        updateMany(model.filter(_.item != None).map( x=>(tid, x.transid, x.modelId,x.item.get, x.unit.get, x.price, x.quantity,x.vat.get,x.duedate.get,x.text))).transact(xa).run
      ret
    }
    def create = Queries.createLineGoodreceiving.run.transact(xa).run
    def update(model:LineGoodreceiving) = { println(s" updating ${model}  items"); Queries.lineGoodreceivingUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.lineGoodreceivingDelete(id.toLong).run.transact(xa).run
    def all:List[LineGoodreceiving] =  Queries.lineGoodreceivingSelect.process.list.transact(xa).run.map(x =>
      LineGoodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def  find(id:String):List[LineGoodreceiving] = Queries.lineGoodreceivingIdSelect(id.toLong).process.list.transact(xa).run.map(x =>
      LineGoodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome(id:String) = Queries.lineGoodreceivingSelectSome(id).process.list.transact(xa).run.map(x =>
      LineGoodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome1(id:Long) = Queries.lineGoodreceivingIdSelect(id).process.list.transact(xa).run.map(x => LineGoodreceiving(x._1,x._2, x._3, Some(x._4),
      Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
  }

  implicit def GoodreceivingDAO = new DAO[Goodreceiving[LineGoodreceiving]]{
    def predicate(p:LineGoodreceiving) = p.id==0
    def insert(model: List[Goodreceiving[LineGoodreceiving]]) :Int = {
      val tid:Long = Queries.getSequence ("goodreceiving", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[(Long,Long, Int, String,String)](Queries.goodreceivingInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse("")))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineGoodreceiving]].insert(x.lines.getOrElse(List[LineGoodreceiving]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create = Queries.createGoodreceiving.run.transact(xa).run
    def update(model:Goodreceiving[LineGoodreceiving]) = {
      val ret = Queries.goodreceivingUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineGoodreceiving) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineGoodreceivingDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineGoodreceivingDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineGoodreceivingDAO.delete(e.id.toString) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(id:String):Int = {
      val r = find(id).map(p =>(p.getLines.map(l =>(lineGoodreceivingDAO.delete(l.id)))))
      Queries.goodreceivingDelete(id.toLong).run.transact(xa).run
    }
    def all:List[Goodreceiving[LineGoodreceiving]] = Queries.goodreceivingSelect.process.list.transact(xa).run.map(
      x => Goodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5)).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[Goodreceiving[LineGoodreceiving]] =
      Queries.goodreceivingIdSelect(id.toLong).process.list.transact(xa).run.map(
        x=> Goodreceiving(x._1,x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.goodreceivingSelectSome(id).process.list.transact(xa).run.map(
      x=> Goodreceiving(x._1, x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LineGoodreceiving]].findSome1(a)
    def findSome1(id:Long) =Queries.goodreceivingIdSelect(id).process.list.transact(xa).run.map(
      x=> Goodreceiving(x._1,x._2, x._3, Some(x._4), Some(x._5)).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LineGoodreceiving):LineGoodreceiving = line.copy(transid=id)
  }

  def create: ConnectionIO[Int] = Queries.create.run

  def runNonParameterizedSingleObjectQuery[A](q: Query0[A]) = q.unique.transact(xa).run
  def processNonParameterizedQuery[A](q: Query0[A]) = q.process.transact(xa)

}

