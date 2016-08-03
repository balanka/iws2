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
    implicit def storeDAO = new DAO[Store]
  {
    def insert(model: List[Store]) :Int =  Update[STORE_TYPE](Queries.storeInsertSQL).updateMany( model.map (x => (x.id, x.name, x.modelId, x.accountId,  x.street, x.city, x.state, x.zip))).transact(xa).run
    def create = Queries.createStore.run.transact(xa).run
    def update(model:Store) = {  println(s"sql ${ Queries.storeUpdateName(model)}") ; Queries.storeUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.storeDelete(id).run.transact(xa).run
    def all = Queries.storeSelect.process.list.transact(xa).run.map(x => f(x)) //Store(x.id,x.name, x.modelId, x.accountId,  x.street, x.city, x.state, x.zip).copy (stocks = Some(implicitly[DAO[Stock]].findSome(x._1))))
    def find(id:String) : List[Store] = Queries.storeIdSelect(id).process.list.transact(xa).run.map(x => f(x))
    def findSome(id:String) = Queries.storeSelectSome(id).process.list.transact(xa).run.map(x => f(x))
    def findSome1(id:Long) = Queries.storeSelectSome(id+"").process.list.transact(xa).run.map(x => f(x))
    def f (x:STORE_TYPE) = Store(x._1,x._2,x._3,x._4,x._5,x._6,x._7,x._8 ).copy (stocks = Some(implicitly[DAO[Stock]].findSome(x._1)))
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

    implicit def linePurchaseOrderDAO = new DAO[LinePurchaseOrder]{
    def insert(model: List[LinePurchaseOrder]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
      val tid:Long = Queries.getSequence ("LinePurchaseOrder", "id").unique.transact(xa).run;
      println(s"  getSequence ${tid} ")
       ret = doobie.imports.Update[LinePurchaseOrder_TYPE](Queries.linePurchaseOrderInsertSQL).
        updateMany(model.filter(_.item != None).map( x=>(tid, x.transid, x.modelId,x.item.get, x.unit.get, x.price, x.quantity,x.vat.get,x.duedate.get,x.text))).transact(xa).run
      }
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
      val ret= doobie.imports.Update[PurchaseOrder_TYPE](Queries.purchaseOrderInsertSQL).updateMany(model.map(
                     x=>(tid, x.oid, x.modelId, x.store.get,x.account.get, x.text))).transact(xa).run;
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
                       x => PurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[PurchaseOrder[LinePurchaseOrder]] =
               Queries.purchaseOrderIdSelect(id.toLong).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.purchaseOrderSelectSome(id).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LinePurchaseOrder]].findSome1(a)
    def findSome1(id:Long) =Queries.purchaseOrderIdSelect(id).process.list.transact(xa).run.map(
                     x=> PurchaseOrder(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LinePurchaseOrder):LinePurchaseOrder = line.copy(transid=id)
  }

  implicit def lineGoodreceivingDAO = new DAO[LineGoodreceiving]{
    def insert(model: List[LineGoodreceiving]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineGoodreceiving", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
         ret = doobie.imports.Update[LineGoodreceiving_TYPE](Queries.lineGoodreceivingInsertSQL).
          updateMany(model.filter(_.item != None).map(x => (tid, x.transid, x.modelId, x.item.get, x.unit.get, x.price, x.quantity, x.vat.get, x.duedate.get, x.text))).transact(xa).run
      }
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
      val ret= doobie.imports.Update[Goodreceiving_TYPE](Queries.goodreceivingInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
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
      x => Goodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[Goodreceiving[LineGoodreceiving]] =
      Queries.goodreceivingIdSelect(id.toLong).process.list.transact(xa).run.map(
        x=> Goodreceiving(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.goodreceivingSelectSome(id).process.list.transact(xa).run.map(
      x=> Goodreceiving(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LineGoodreceiving]].findSome1(a)
    def findSome1(id:Long) =Queries.goodreceivingIdSelect(id).process.list.transact(xa).run.map(
      x=> Goodreceiving(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LineGoodreceiving):LineGoodreceiving = line.copy(transid=id)
  }


  implicit def lineInventoryInvoiceDAO = new DAO[LineInventoryInvoice]{
    def insert(model: List[LineInventoryInvoice]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineInventoryInvoice", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
         ret = doobie.imports.Update[LineInventoryInvoice_TYPE](Queries.lineInventoryInvoiceInsertSQL).
          updateMany(model.filter(_.item != None).map(x => (tid, x.transid, x.modelId, x.item.get, x.unit.get, x.price, x.quantity, x.vat.get, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create = Queries.createLineInventoryInvoice.run.transact(xa).run
    def update(model:LineInventoryInvoice) = { println(s" updating ${model}  items"); Queries.lineInventoryInvoiceUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.lineInventoryInvoiceDelete(id.toLong).run.transact(xa).run
    def all:List[LineInventoryInvoice] =  Queries.lineInventoryInvoiceSelect.process.list.transact(xa).run.map(x =>
      LineInventoryInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def  find(id:String):List[LineInventoryInvoice] = Queries.lineInventoryInvoiceIdSelect(id.toLong).process.list.transact(xa).run.map(x =>
      LineInventoryInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome(id:String) = Queries.lineInventoryInvoiceSelectSome(id).process.list.transact(xa).run.map(x =>
      LineInventoryInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
    def findSome1(id:Long) = Queries.lineInventoryInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineInventoryInvoice(x._1,x._2, x._3, Some(x._4),
      Some(x._5), x._6, x._7,  Some(x._8), Some(x._9), x._10))
  }

  implicit def InventoryInvoiceDAO = new DAO[InventoryInvoice[LineInventoryInvoice]]{
    def predicate(p:LineInventoryInvoice) = p.id==0
    def insert(model: List[InventoryInvoice[LineInventoryInvoice]]) :Int = {
      val tid:Long = Queries.getSequence ("InventoryInvoice", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[InventoryInvoice_TYPE](Queries.inventoryInvoiceInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineInventoryInvoice]].insert(x.lines.getOrElse(List[LineInventoryInvoice]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create = Queries.createInventoryInvoice.run.transact(xa).run
    def update(model:InventoryInvoice[LineInventoryInvoice]) = {
      val ret = Queries.inventoryInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineInventoryInvoice) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineInventoryInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineInventoryInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineInventoryInvoiceDAO.delete(e.id.toString) )

      println(s" InventoryInvoice Line inserted K0 ${k0}   ${newLines}");
      println(s" InventoryInvoice Line updated K2 ${k1}   ");
      println(s" InventoryInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(id:String):Int = {
      val r = find(id).map(p =>(p.getLines.map(l =>(lineInventoryInvoiceDAO.delete(l.id)))))
      Queries.inventoryInvoiceDelete(id.toLong).run.transact(xa).run
    }
    def all:List[InventoryInvoice[LineInventoryInvoice]] = Queries.inventoryInvoiceSelect.process.list.transact(xa).run.map(
      x => InventoryInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[InventoryInvoice[LineInventoryInvoice]] =
      Queries.inventoryInvoiceIdSelect(id.toLong).process.list.transact(xa).run.map(
        x=> InventoryInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.inventoryInvoiceSelectSome(id).process.list.transact(xa).run.map(
      x=> InventoryInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LineInventoryInvoice]].findSome1(a)
    def findSome1(id:Long) =Queries.inventoryInvoiceIdSelect(id).process.list.transact(xa).run.map(
      x=> InventoryInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LineInventoryInvoice):LineInventoryInvoice = line.copy(transid=id)
  }

  implicit def lineVendorInvoiceDAO = new DAO[LineVendorInvoice]{
    def insert(model: List[LineVendorInvoice]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineVendorInvoice", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineVendorInvoice_TYPE](Queries.lineVendorInvoiceInsertSQL).
          updateMany(model.filter(_.account != None).map(x => (tid, x.transid, x.modelId, x.account.get, x.side, x.oaccount.get, x.amount, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create = Queries.createLineVendorInvoice.run.transact(xa).run
    def update(model:LineVendorInvoice) = { println(s" updating ${model}  items"); Queries.lineVendorInvoiceUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.lineVendorInvoiceDelete(id.toLong).run.transact(xa).run
    def all:List[LineVendorInvoice] =  Queries.lineVendorInvoiceSelect.process.list.transact(xa).run.map(x =>
      LineVendorInvoice(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def  find(id:String):List[LineVendorInvoice] = Queries.lineVendorInvoiceIdSelect(id.toLong).process.list.transact(xa).run.map(x =>
      LineVendorInvoice(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def findSome(id:String) = Queries.lineVendorInvoiceSelectSome(id).process.list.transact(xa).run.map(x =>
      LineVendorInvoice(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def findSome1(id:Long) = Queries.lineVendorInvoiceIdSelect(id).process.list.transact(xa).run.map(x => 
      LineVendorInvoice(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
  }

  implicit def vendorInvoiceDAO = new DAO[VendorInvoice[LineVendorInvoice]]{
    def predicate(p:LineVendorInvoice) = p.id==0
    def insert(model: List[VendorInvoice[LineVendorInvoice]]) :Int = {
      val tid:Long = Queries.getSequence ("VendorInvoice", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[VendorInvoice_TYPE](Queries.vendorInvoiceInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineVendorInvoice]].insert(x.lines.getOrElse(List[LineVendorInvoice]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create = Queries.createVendorInvoice.run.transact(xa).run
    def update(model:VendorInvoice[LineVendorInvoice]) = {
      val ret = Queries.vendorInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineVendorInvoice) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineVendorInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineVendorInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineVendorInvoiceDAO.delete(e.id.toString) )

      println(s" VendorInvoice Line inserted K0 ${k0}   ${newLines}");
      println(s" VendorInvoice Line updated K2 ${k1}   ");
      println(s" VendorInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(id:String):Int = {
      val r = find(id).map(p =>(p.getLines.map(l =>(lineVendorInvoiceDAO.delete(l.id)))))
      Queries.vendorInvoiceDelete(id.toLong).run.transact(xa).run
    }
    def all:List[VendorInvoice[LineVendorInvoice]] = Queries.vendorInvoiceSelect.process.list.transact(xa).run.map(
      x => VendorInvoice(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[VendorInvoice[LineVendorInvoice]] =
      Queries.vendorInvoiceIdSelect(id.toLong).process.list.transact(xa).run.map(
        x=> VendorInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.vendorInvoiceSelectSome(id).process.list.transact(xa).run.map(
      x=> VendorInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LineVendorInvoice]].findSome1(a)
    def findSome1(id:Long) = Queries.vendorInvoiceIdSelect(id).process.list.transact(xa).run.map(
      x=> VendorInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LineVendorInvoice):LineVendorInvoice = line.copy(transid=id)
  }
  
  implicit def linePaymentDAO = new DAO[LinePayment]{
    def insert(model: List[LinePayment]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LinePayment", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LinePayment_TYPE](Queries.linePaymentInsertSQL).
          updateMany(model.filter(_.account != None).map(x => (tid, x.transid, x.modelId, x.account.get, x.side, x.oaccount.get, x.amount, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create = Queries.createLinePayment.run.transact(xa).run
    def update(model:LinePayment) = { println(s" updating ${model}  items"); Queries.linePaymentUpdateName(model).run.transact(xa).run}
    def delete(id:String):Int = Queries.linePaymentDelete(id.toLong).run.transact(xa).run
    def all:List[LinePayment] =  Queries.linePaymentSelect.process.list.transact(xa).run.map(x =>
      LinePayment(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def  find(id:String):List[LinePayment] = Queries.linePaymentIdSelect(id.toLong).process.list.transact(xa).run.map(x =>
      LinePayment(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def findSome(id:String) = Queries.linePaymentSelectSome(id).process.list.transact(xa).run.map(x =>
      LinePayment(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
    def findSome1(id:Long) = Queries.linePaymentIdSelect(id).process.list.transact(xa).run.map(x =>
      LinePayment(x._1,x._2, x._3, Some(x._4),x._5, Some(x._6), x._7,  Some(x._8), x._9))
  }

  implicit def paymentDAO = new DAO[Payment[LinePayment]]{
    def predicate(p:LinePayment) = p.id==0
    def insert(model: List[Payment[LinePayment]]) :Int = {
      val tid:Long = Queries.getSequence ("Payment", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[VendorInvoice_TYPE](Queries.paymentInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LinePayment]].insert(x.lines.getOrElse(List[LinePayment]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create = Queries.createPayment.run.transact(xa).run
    def update(model:Payment[LinePayment]) = {
      val ret = Queries.paymentUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LinePayment) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = linePaymentDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (linePaymentDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>linePaymentDAO.delete(e.id.toString) )

      println(s" Payment Line inserted K0 ${k0}   ${newLines}");
      println(s" Payment Line updated K2 ${k1}   ");
      println(s" Payment Line deleted K2 ${k2}   ");
      ret
    }
    def delete(id:String):Int = {
      val r = find(id).map(p =>(p.getLines.map(l =>(linePaymentDAO.delete(l.id)))))
      Queries.paymentDelete(id.toLong).run.transact(xa).run
    }
    def all:List[Payment[LinePayment]] = Queries.paymentSelect.process.list.transact(xa).run.map(
      x => Payment(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(id:String)  : List[Payment[LinePayment]] =
      Queries.paymentIdSelect(id.toLong).process.list.transact(xa).run.map(
        x=> Payment(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(id:String) = Queries.paymentSelectSome(id).process.list.transact(xa).run.map(
      x=> Payment(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (a:Long) = implicitly[DAO[LinePayment]].findSome1(a)
    def findSome1(id:Long) = Queries.paymentIdSelect(id).process.list.transact(xa).run.map(
      x=> Payment(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def  updateLineId(id:Long,line:LinePayment):LinePayment = line.copy(transid=id)
  }
  def create: ConnectionIO[Int] = Queries.create.run

  def runNonParameterizedSingleObjectQuery[A](q: Query0[A]) = q.unique.transact(xa).run
  def processNonParameterizedQuery[A](q: Query0[A]) = q.process.transact(xa)

}

