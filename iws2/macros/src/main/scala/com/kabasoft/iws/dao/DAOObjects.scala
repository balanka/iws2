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
import com.kabasoft.iws.shared.common._


object DAOObjects  {

  val xa= DriverManagerTransactor[Task]("org.postgresql.Driver","jdbc:postgresql:world","postgres","")

  implicit def accountDAO = new DAO[Account] {

    def insert(model: List[Account]): Int = doobie.imports.Update[ACCOUNT_TYPE](Queries.accountInsertSQL).
      updateMany(model.map(x =>
      (x.id, x.name, x.modelId, x.description, x.groupId.getOrElse(""), x.dateOfOpen.getOrElse(new Date()), x.dateOfClose.getOrElse(new Date())))).transact(xa).run

    def create (modelId:Int)= Queries.createAccount.run.transact(xa).run
    def update(model: Account): Int = Queries.accountUpdateName(model).run.transact(xa).run
    def delete(model: Account): Int = Queries.accountDelete(model.id).run.transact(xa).run
    def all(model:Account): List[Account] = Queries.accountSelect.process.list.transact(xa).run.map(x =>Account_(x).copy(accounts = Some(findSome2(x._1))))
    def find(model: Account): List[Account] = Queries.accountIdSelect(model.id).process.list.transact(xa).run.map(x =>Account_(x).copy(accounts = Some(findSome2(x._1))))
    def findSome(model: Account): List[Account] = Queries.accountSelectSome(model.id).process.list.transact(xa).run.map(x =>Account_(x).copy(accounts = Some(findSome2(x._1))))
    def findSome1(id: Long): List[Account] = Queries.accountSelectSome(id + "").process.list.transact(xa).run.map(x =>Account_(x).copy(accounts = Some(findSome2(x._1))))
    def findSome2(groupid: String): List[Account] = Queries.accountSelectByGroupId(groupid).process.list.transact(xa).run.map(x => Account_(x).copy(accounts = Some(findSome2(x._1))))

  }

  implicit def articleDAO = new DAO[Article]
  {
     def insert(model: List[Article]): Int = doobie.imports.Update[ARTICLE_TYPE](Queries.articleInsertSQL).
      updateMany(model.map(x =>
        (x.id, x.name, x.modelId, x.description, x.price,  x.avgPrice, x.salesPrice, x.qttyUnit, x.packUnit,  x.groupId.getOrElse(""), x.vat.getOrElse("0")))).transact(xa).run

     def create (modelId:Int)= Queries.createArticle.run.transact(xa).run
     def update(model:Article) = Queries.articleUpdateName(model).run.transact(xa).run
     def delete(model:Article)= Queries.articleDelete(model.id).run.transact(xa).run
     def all(model:Article): List[Article] = Queries.articleSelect.process.list.transact(xa).run.map(x => Article_(x).copy(articles = Some(findSome2(x._1))))
     def find(model:Article)= Queries.articleIdSelect(model.id).process.list.transact(xa).run.map(x => Article_(x).copy(articles = Some(findSome2(x._1))))
     def findSome(model:Article)= Queries.articleSelectSome(model.id).process.list.transact(xa).run.map(x =>Article_(x).copy(articles = Some(findSome2(x._1))))
     def findSome1(id: Long): List[Article] = Queries.articleSelectSome(id + "").process.list.transact(xa).run.map(x => Article_(x).copy(articles = Some(findSome2(x._1))))
     def findSome2(groupId: String): List[Article] = Queries.articleSelectByGroupId(groupId).process.list.transact(xa).run.map(x =>Article_(x).copy(articles = Some(findSome2(x._1))))
  }

  implicit def bankDAO = new DAO[Bank]
  {
    def insert(model: List[Bank]) :Int =  Update[Bank](Queries.bankInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createBank.run.transact(xa).run
    def update(model:Bank) = Queries.bankUpdateName(model).run.transact(xa).run
    def delete(model:Bank):Int = Queries.bankDelete(model.id).run.transact(xa).run
    def all(model:Bank) = Queries.bankSelect.process.list.transact(xa).run
    def find(model:Bank) : List[Bank] = Queries.bankIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Bank) = Queries.bankSelectSome(model.id).process.list.transact(xa).run
    def findSome1(id:Long) = Queries.bankSelectSome(id+"").process.list.transact(xa).run
  }

  implicit def bankAccountDAO = new DAO[BankAccount]
  {
    def insert(model: List[BankAccount]) :Int =  Update[BankAccount](Queries.bankAccountInsertSQL).updateMany(model).transact(xa).run
    def create (modelId:Int)= Queries.createBankAccount.run.transact(xa).run
    def update(model:BankAccount) = Queries.bankAccountUpdateName(model).run.transact(xa).run
    def delete(model:BankAccount):Int = Queries.bankAccountDelete(model.id).run.transact(xa).run
    def all(model:BankAccount)= Queries.bankAccountSelect.process.list.transact(xa).run
    def find(model:BankAccount) : List[BankAccount] = Queries.bankAccountIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:BankAccount) = Queries.bankAccountSelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.bankAccountSelectSome(id+"").process.list.transact(xa).run
  }


  implicit def categoryDAO = new DAO[ArticleGroup]
  {
    def insert( model: List[ArticleGroup]) :Int = Update[ArticleGroup] (Queries.categoryInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createCategory.run.transact(xa).run
    def update(model:ArticleGroup):Int = Queries.categoryUpdateName(model).run.transact(xa).run
    def delete(model:ArticleGroup):Int = Queries.categoryDelete(model.id).run.transact(xa).run
    def all(model:ArticleGroup) = Queries.categorySelect.process.list.transact(xa).run
    def find(model:ArticleGroup) : List[ArticleGroup] = Queries.categoryIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:ArticleGroup) = Queries.categorySelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.categorySelectSome(id+"").process.list.transact(xa).run
  }
  implicit def companyDAO = new DAO[Company]
  {
    def insert(model: List[Company]) :Int =  Update[Company](Queries.companyInsertSQL).updateMany(model).transact(xa).run
    def create (modelId:Int)= Queries.createCompany.run.transact(xa).run
    def update(model:Company) = Queries.companyUpdateName(model).run.transact(xa).run
    def delete(model:Company):Int = Queries.companyDelete(model.id).run.transact(xa).run
    def all(model:Company)= Queries.companySelect.process.list.transact(xa).run
    def find(model:Company) : List[Company] = Queries.companyIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Company) = Queries.companySelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.companySelectSome(id+"").process.list.transact(xa).run
  }

  implicit def customerDAO = new DAO[Customer]
  {
    def insert(model: List[Customer]) :Int =  Update[Customer](Queries.customerInsertSQL).updateMany(model).transact(xa).run
    def create (modelId:Int)= Queries.createCustomer.run.transact(xa).run
    def update(model:Customer) = Queries.customerUpdateName(model).run.transact(xa).run
    def delete(model:Customer):Int = Queries.customerDelete(model.id).run.transact(xa).run
    def all(model:Customer)= Queries.customerSelect.process.list.transact(xa).run
    def find(model:Customer) : List[Customer] = Queries.customerIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Customer) = Queries.customerSelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.customerSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def supplierDAO = new DAO[Supplier]
  {
    def insert(model: List[Supplier]) :Int =  Update[Supplier](Queries.supplierInsertSQL).updateMany(model).transact(xa).run
    def create (modelId:Int)= Queries.createSupplier.run.transact(xa).run
    def update(model:Supplier) = Queries.supplierUpdateName(model).run.transact(xa).run
    def delete(model:Supplier):Int = Queries.supplierDelete(model.id).run.transact(xa).run
    def all (model:Supplier)= Queries.supplierSelect.process.list.transact(xa).run
    def find(model:Supplier) : List[Supplier] = Queries.supplierIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Supplier) = Queries.supplierSelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.supplierSelectSome(id+"").process.list.transact(xa).run
  }

  implicit def stockDAO = new DAO[Stock]
  {
    def insert(model: List[Stock]) :Int =  Update[Stock](Queries.stockInsertSQL).updateMany(model).transact(xa).run
    def create (modelId:Int)= Queries.createStock.run.transact(xa).run
    def update(model:Stock) = Queries.stockUpdateName(model).run.transact(xa).run
    def delete(model:Stock):Int = Queries.stockDelete(model.id).run.transact(xa).run
    def all (model:Stock)= Queries.stockSelect.process.list.transact(xa).run
    def find(model:Stock)  : List[Stock] = Queries.stockIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Stock) = Queries.stockSelectSome(model.id).process.list.transact(xa).run
    def findStockByStore(id:String) = Queries.stockSelectSome(id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.stockSelectSome(id+"").process.list.transact(xa).run
  }
    implicit def storeDAO = new DAO[Store]
  {
    def insert(model: List[Store]) :Int =  Update[STORE_TYPE](Queries.storeInsertSQL).updateMany( model.map (x => (x.id, x.name, x.modelId, x.accountId,  x.street, x.city, x.state, x.zip))).transact(xa).run
    def create(modelId:Int) = Queries.createStore.run.transact(xa).run
    def update(model:Store) = {  println(s"sql ${ Queries.storeUpdateName(model)}") ; Queries.storeUpdateName(model).run.transact(xa).run}
    def delete(model:Store):Int = Queries.storeDelete(model.id).run.transact(xa).run
    def all(model:Store) = Queries.storeSelect.process.list.transact(xa).run.map(x => f(x)) 
    def find(model:Store) : List[Store] = Queries.storeIdSelect(model.id).process.list.transact(xa).run.map(x => f(x))
    def findSome(model:Store) = Queries.storeSelectSome(model.id).process.list.transact(xa).run.map(x => f(x))
    def f (x:STORE_TYPE) = Store(x._1,x._2,x._3,x._4,x._5,x._6,x._7,x._8 ).copy (stocks = Some(Queries.stockSelectSome(x._1).process.list.transact(xa).run))
    }

  implicit def costCenterDAO = new DAO[CostCenter]
  {
    def insert(model: List[CostCenter]) :Int =  Update[CostCenter](Queries.costCenterInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createCostCenter.run.transact(xa).run
    def update(model:CostCenter) = Queries.costCenterUpdateName(model).run.transact(xa).run
    def delete(model:CostCenter):Int = Queries.costCenterDelete(model.id).run.transact(xa).run
    def all (model:CostCenter)= Queries.costCenterSelect.process.list.transact(xa).run
    def find(model:CostCenter)  : List[CostCenter] = Queries.costCenterIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:CostCenter) = Queries.costCenterSelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.costCenterSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def quantityUnitDAO = new DAO[QuantityUnit]
  {
    def insert(model: List[QuantityUnit]) :Int =  Update[QuantityUnit](Queries.quantityUnitInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createQuantityUnit.run.transact(xa).run
    def update(model:QuantityUnit) = Queries.quantityUnitUpdateName(model).run.transact(xa).run
    def delete(model:QuantityUnit):Int = Queries.quantityUnitDelete(model.id).run.transact(xa).run
    def all (model:QuantityUnit)= Queries.quantityUnitSelect.process.list.transact(xa).run
    def find(model:QuantityUnit)  : List[QuantityUnit] = Queries.quantityUnitIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:QuantityUnit) = Queries.quantityUnitSelectSome(model.id).process.list.transact(xa).run
  }
  implicit def vatDAO = new DAO[Vat]
  {
    def insert(model: List[Vat]) :Int =  Update[Vat](Queries.vatInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createVat.run.transact(xa).run
    def update(model:Vat) = Queries.vatUpdateName(model).run.transact(xa).run
    def delete(model:Vat):Int = Queries.vatDelete(model.id).run.transact(xa).run
    def all (model:Vat) = Queries.vatSelect.process.list.transact(xa).run
    def find(model:Vat)  : List[Vat] = Queries.vatIdSelect(model.id).process.list.transact(xa).run
    def findSome(model:Vat) = Queries.vatSelectSome(model.id).process.list.transact(xa).run
    //def findSome1(id:Long) = Queries.vatSelectSome(id+"").process.list.transact(xa).run
  }
  implicit def periodicAccountBalanceDAO = new DAO[PeriodicAccountBalance]
  {
    def insert(model: List[PeriodicAccountBalance]) :Int =  Update[PeriodicAccountBalance](Queries.periodicAccountBalanceInsertSQL).updateMany(model).transact(xa).run
    def create(modelId:Int) = Queries.createPeriodicAccountBalance.run.transact(xa).run
    def update(model:PeriodicAccountBalance) = Queries.periodicAccountBalanceUpdateName(model).run.transact(xa).run
    def delete(model:PeriodicAccountBalance):Int = Queries.periodicAccountBalanceDelete(model.id).run.transact(xa).run
    def all(model:PeriodicAccountBalance) = Queries.periodicAccountBalanceSelect.process.list.transact(xa).run
    def find(model:PeriodicAccountBalance)  : List[PeriodicAccountBalance] = Queries.periodicAccountBalanceIdSelect(model.id).process.list.transact(xa).run
   def findSome(model:PeriodicAccountBalance) = Queries.periodicAccountBalanceSelectSome(model.id).process.list.transact(xa).run
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
    def create (modelId:Int) = Queries.createLinePurchaseOrder.run.transact(xa).run
    def update(model:LinePurchaseOrder) = { println(s" updating ${model}  items"); Queries.linePurchaseOrderUpdateName(model).run.transact(xa).run}
    def delete(model:LinePurchaseOrder):Int = Queries.linePurchaseOrderDelete(model.id.toLong).run.transact(xa).run
    def all(model:LinePurchaseOrder):List[LinePurchaseOrder] =  Queries.linePurchaseOrderSelect.process.list.transact(xa).run.map(x => LinePurchaseOrder(x))
    def  find(model:LinePurchaseOrder):List[LinePurchaseOrder] = Queries.linePurchaseOrderIdSelect(model.id.toLong).process.list.transact(xa).run.map(x => LinePurchaseOrder(x))
    def findSome(model:LinePurchaseOrder) = Queries.linePurchaseOrderSelectSome(model.id).process.list.transact(xa).run.map(x => LinePurchaseOrder(x))
    def findSome1(id:Long) = Queries.linePurchaseOrderIdSelect(id).process.list.transact(xa).run.map(x => LinePurchaseOrder(x))
  }

implicit def purchaseOrderDAO = new DAO[PurchaseOrder[LinePurchaseOrder]]{
  def f2(tid:Long, p:PurchaseOrder[LinePurchaseOrder]) = p.copy(lines = Some(p.getLines.map(z => z.copy(transid=tid))))
  def predicate(p:LinePurchaseOrder) = p.id==0
  def insert(model: List[PurchaseOrder[LinePurchaseOrder]]) :Int = {
      val tid:Long = Queries.getSequence ("purchaseorder", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[PurchaseOrder_TYPE](Queries.purchaseOrderInsertSQL).updateMany(model.map (t => f2(tid, t)).map(
                     x=>(tid, x.oid, x.modelId, x.store.get,x.account.get, x.text))).transact(xa).run;
      //model.map( x=>implicitly[DAO[LinePurchaseOrder]].insert(x.lines.getOrElse(List[LinePurchaseOrder]()).map( z => z.copy(transid=tid))))
      model.map( x=>implicitly[DAO[LinePurchaseOrder]].insert(x.lines.getOrElse(List[LinePurchaseOrder]())))
      ret
      
     }
    def create (modelId:Int) = Queries.createPurchaseOrder1.run.transact(xa).run
      def update(model:PurchaseOrder[LinePurchaseOrder]) = {
      val ret = Queries.purchaseOrderUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LinePurchaseOrder) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = linePurchaseOrderDAO.insert(newLines)
      //val k0 = linePurchaseOrderDAO.insert(model.getLines.partition(_.tid ==0L && _.created == true)._1)
      val k1= model.getLines.partition(_.modified)._1.map (linePurchaseOrderDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>linePurchaseOrderDAO.delete(e) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:PurchaseOrder[LinePurchaseOrder]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(linePurchaseOrderDAO.delete(l)))))
      Queries.purchaseOrderDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:PurchaseOrder[LinePurchaseOrder]):List[PurchaseOrder[LinePurchaseOrder]] = Queries.purchaseOrderSelect.process.list.transact(xa).run.map(
                       x => PurchaseOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:PurchaseOrder[LinePurchaseOrder])  : List[PurchaseOrder[LinePurchaseOrder]] =
               Queries.purchaseOrderIdSelect(model.id.toLong).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:PurchaseOrder[LinePurchaseOrder]) = Queries.purchaseOrderSelectSome(model.id).process.list.transact(xa).run.map(
                      x=> PurchaseOrder(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.linePurchaseOrderIdSelect(id).process.list.transact(xa).run.map(x => LinePurchaseOrder(x)) 
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

    def create (modelId:Int) = Queries.createLineGoodreceiving.run.transact(xa).run
    def update(model:LineGoodreceiving) = { println(s" updating ${model}  items"); Queries.lineGoodreceivingUpdateName(model).run.transact(xa).run}
    def delete(model:LineGoodreceiving):Int = Queries.lineGoodreceivingDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineGoodreceiving):List[LineGoodreceiving] =  Queries.lineGoodreceivingSelect.process.list.transact(xa).run.map(x =>LineGoodreceiving(x))
    def  find(model:LineGoodreceiving):List[LineGoodreceiving] = Queries.lineGoodreceivingIdSelect(model.id.toLong).process.list.transact(xa).run.map(x =>LineGoodreceiving(x))
    def findSome(model:LineGoodreceiving) = Queries.lineGoodreceivingSelectSome(model.id).process.list.transact(xa).run.map(x =>LineGoodreceiving(x))
    def findSome1(id:Long) = Queries.lineGoodreceivingIdSelect(id).process.list.transact(xa).run.map(x => LineGoodreceiving(x))
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
    def create(modelId:Int) = Queries.createGoodreceiving.run.transact(xa).run
    def update(model:Goodreceiving[LineGoodreceiving]) = {
      val ret = Queries.goodreceivingUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineGoodreceiving) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineGoodreceivingDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineGoodreceivingDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineGoodreceivingDAO.delete(e) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:Goodreceiving[LineGoodreceiving]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineGoodreceivingDAO.delete(l)))))
      Queries.goodreceivingDelete(model.tid).run.transact(xa).run
    }
    def all(model:Goodreceiving[LineGoodreceiving]):List[Goodreceiving[LineGoodreceiving]] = Queries.goodreceivingSelect.process.list.transact(xa).run.map(
      x => Goodreceiving(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:Goodreceiving[LineGoodreceiving])  : List[Goodreceiving[LineGoodreceiving]] =
      Queries.goodreceivingIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> Goodreceiving(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:Goodreceiving[LineGoodreceiving]) = Queries.goodreceivingSelectSome(model.id).process.list.transact(xa).run.map(
      x=> Goodreceiving(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) =  Queries.lineGoodreceivingIdSelect(id).process.list.transact(xa).run.map(x => LineGoodreceiving(x))
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
    def create(modelId:Int) = Queries.createLineInventoryInvoice.run.transact(xa).run
    def update(model:LineInventoryInvoice) = { println(s" updating ${model}  items"); Queries.lineInventoryInvoiceUpdateName(model).run.transact(xa).run}
    def delete(model:LineInventoryInvoice):Int = Queries.lineInventoryInvoiceDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineInventoryInvoice):List[LineInventoryInvoice] =  Queries.lineInventoryInvoiceSelect.process.list.transact(xa).run.map(x =>LineInventoryInvoice(x))
    def  find(model:LineInventoryInvoice):List[LineInventoryInvoice] = Queries.lineInventoryInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(x =>LineInventoryInvoice(x))
    def findSome(model:LineInventoryInvoice) = Queries.lineInventoryInvoiceSelectSome(model.id).process.list.transact(xa).run.map(x =>LineInventoryInvoice(x))
    def findSome1(id:Long) = Queries.lineInventoryInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineInventoryInvoice(x))

  }

  implicit def inventoryInvoiceDAO = new DAO[InventoryInvoice[LineInventoryInvoice]]{
    def predicate(p:LineInventoryInvoice) = p.id==0
    def insert(model: List[InventoryInvoice[LineInventoryInvoice]]) :Int = {
      val tid:Long = Queries.getSequence ("InventoryInvoice", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[InventoryInvoice_TYPE](Queries.inventoryInvoiceInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineInventoryInvoice]].insert(x.lines.getOrElse(List[LineInventoryInvoice]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create(modelId:Int)= Queries.createInventoryInvoice.run.transact(xa).run
    def update(model:InventoryInvoice[LineInventoryInvoice]) = {
      val ret = Queries.inventoryInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineInventoryInvoice) = pred.tid == 0L && pred.created == true

      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineInventoryInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineInventoryInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineInventoryInvoiceDAO.delete(e) )
     
      println(s" InventoryInvoice Line inserted K0 ${k0}  line to insert: ${newLines}");
      println(s" InventoryInvoice Line updated K2 ${k1}   ");
      println(s" InventoryInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:InventoryInvoice[LineInventoryInvoice]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineInventoryInvoiceDAO.delete(l)))))
      Queries.inventoryInvoiceDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:InventoryInvoice[LineInventoryInvoice]):List[InventoryInvoice[LineInventoryInvoice]] = Queries.inventoryInvoiceSelect.process.list.transact(xa).run.map(
      x => InventoryInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:InventoryInvoice[LineInventoryInvoice])  : List[InventoryInvoice[LineInventoryInvoice]] =
      Queries.inventoryInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> InventoryInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:InventoryInvoice[LineInventoryInvoice]) = Queries.inventoryInvoiceSelectSome(model.id).process.list.transact(xa).run.map(
      x=> InventoryInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.lineInventoryInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineInventoryInvoice(x))
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
    def create (modelId:Int)= Queries.createLineVendorInvoice.run.transact(xa).run
    def update(model:LineVendorInvoice) = { println(s" updating ${model}  items"); Queries.lineVendorInvoiceUpdateName(model).run.transact(xa).run}
    def delete(model:LineVendorInvoice):Int = Queries.lineVendorInvoiceDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineVendorInvoice):List[LineVendorInvoice] =  Queries.lineVendorInvoiceSelect.process.list.transact(xa).run.map(x => LineVendorInvoice(x))
    def  find(model:LineVendorInvoice):List[LineVendorInvoice] = Queries.lineVendorInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(x => LineVendorInvoice(x))
    def findSome(model:LineVendorInvoice) = Queries.lineVendorInvoiceSelectSome(model.id).process.list.transact(xa).run.map(x =>LineVendorInvoice(x))
    def findSome1(id:Long) = Queries.lineVendorInvoiceIdSelect(id).process.list.transact(xa).run.map(x =>LineVendorInvoice(x))

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
    def create (modelId:Int) = Queries.createVendorInvoice.run.transact(xa).run
    def update(model:VendorInvoice[LineVendorInvoice]) = {
      println(s" VendorInvoice to  update ${model}");
      val ret = Queries.vendorInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineVendorInvoice) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineVendorInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineVendorInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineVendorInvoiceDAO.delete(e) )

      println(s" VendorInvoice Line inserted K0 ${k0}   ${newLines}");
      println(s" VendorInvoice Line updated K2 ${k1}   ");
      println(s" VendorInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:VendorInvoice[LineVendorInvoice]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineVendorInvoiceDAO.delete(l)))))
      Queries.vendorInvoiceDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:VendorInvoice[LineVendorInvoice]):List[VendorInvoice[LineVendorInvoice]] = Queries.vendorInvoiceSelect.process.list.transact(xa).run.map(
      x => VendorInvoice(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:VendorInvoice[LineVendorInvoice])  : List[VendorInvoice[LineVendorInvoice]] =
      Queries.vendorInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> VendorInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:VendorInvoice[LineVendorInvoice]) = Queries.vendorInvoiceSelectSome(model.id).process.list.transact(xa).run.map(
      x=> VendorInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) =  Queries.lineVendorInvoiceIdSelect(id).process.list.transact(xa).run.map(x =>LineVendorInvoice(x))
    def  updateLineId(id:Long,line:LineVendorInvoice):LineVendorInvoice = line.copy(transid=id)
      def mapl(l:LineFDocument):LineVendorInvoice = LineVendorInvoice(l)

    def mapx(t:Transaction[LineFDocument]):VendorInvoice[LineVendorInvoice] = VendorInvoice[LineVendorInvoice](t.tid,t.oid,
        t.modelId,  t.store, t.account,  t.text, t.lines.map( x => x.map(y =>mapl(y) )))
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
    def create (lodelId:Int) = Queries.createLinePayment.run.transact(xa).run
    def update(model:LinePayment) = { println(s" updating ${model}  items"); Queries.linePaymentUpdateName(model).run.transact(xa).run}
    def delete(model:LinePayment):Int = Queries.linePaymentDelete(model.id.toLong).run.transact(xa).run
    def all(model:LinePayment):List[LinePayment] =  Queries.linePaymentSelect.process.list.transact(xa).run.map(x => LinePayment(x))
    def  find(model:LinePayment):List[LinePayment] = Queries.linePaymentIdSelect(model.id.toLong).process.list.transact(xa).run.map(x => LinePayment(x))
    def findSome(model:LinePayment) = Queries.linePaymentSelectSome(model.id).process.list.transact(xa).run.map(x => LinePayment(x))
    def findSome1(id:Long) = Queries.linePaymentIdSelect(id).process.list.transact(xa).run.map(x => LinePayment(x))
    def  updateLineId(id:Long,line:LinePayment):LinePayment = line.copy(transid=id)
  }

  implicit def paymentDAO = new DAO[Payment[LinePayment]]{
    def predicate(p:LinePayment) = p.id==0
    def insert(model: List[Payment[LinePayment]]) :Int = {
      println(s" inserting ${model}  items")
      val tid:Long = Queries.getSequence ("Payment", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[Payment_TYPE](Queries.paymentInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LinePayment]].insert(x.lines.getOrElse(List[LinePayment]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create(modelId:Int) = Queries.createPayment.run.transact(xa).run
    def update(model:Payment[LinePayment]) = {
      val ret = Queries.paymentUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LinePayment) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = linePaymentDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (linePaymentDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>linePaymentDAO.delete(e) )

      println(s" Payment Line inserted K0 ${k0}   ${newLines}");
      println(s" Payment Line updated K2 ${k1}   ");
      println(s" Payment Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:Payment[LinePayment]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(linePaymentDAO.delete(l)))))
      Queries.paymentDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:Payment[LinePayment]):List[Payment[LinePayment]] = Queries.paymentSelect.process.list.transact(xa).run.map(
      x => Payment(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:Payment[LinePayment])  : List[Payment[LinePayment]] =
      Queries.paymentIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> Payment(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:Payment[LinePayment]) = Queries.paymentSelectSome(model.id).process.list.transact(xa).run.map(
      x=> Payment(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.linePaymentIdSelect(id).process.list.transact(xa).run.map(x => LinePayment(x))
    def  updateLineId(id:Long,line:LinePayment):LinePayment = line.copy(transid=id)


  }

  implicit def lineSalesOrderDAO = new DAO[LineSalesOrder]{
    def insert(model: List[LineSalesOrder]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid:Long = Queries.getSequence ("LineSalesOrder", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineSalesOrder_TYPE](Queries.lineSalesOrderInsertSQL).
          updateMany(model.filter(_.item != None).map( x=>(tid, x.transid, x.modelId,x.item.get, x.unit.get, x.price, x.quantity,x.vat.get,x.duedate.get,x.text))).transact(xa).run
      }
      ret
    }
    def create (modelId:Int) = Queries.createLineSalesOrder.run.transact(xa).run
    def update(model:LineSalesOrder) = { println(s" updating ${model}  items"); Queries.lineSalesOrderUpdateName(model).run.transact(xa).run}
    def delete(model:LineSalesOrder):Int = Queries.lineSalesOrderDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineSalesOrder):List[LineSalesOrder] =  Queries.lineSalesOrderSelect.process.list.transact(xa).run.map(x =>  LineSalesOrder(x))
    def  find(model:LineSalesOrder):List[LineSalesOrder] = Queries.lineSalesOrderIdSelect(model.id.toLong).process.list.transact(xa).run.map(x => LineSalesOrder(x))
    def findSome(model:LineSalesOrder) = Queries.lineSalesOrderSelectSome(model.id).process.list.transact(xa).run.map(x => LineSalesOrder(x))
    def findSome1(id:Long) = Queries.lineSalesOrderIdSelect(id).process.list.transact(xa).run.map(x => LineSalesOrder(x))
  }

  implicit def salesOrderDAO = new DAO[SalesOrder[LineSalesOrder]]{
    def f2(tid:Long, p:SalesOrder[LineSalesOrder]) = p.copy(lines = Some(p.getLines.map(z => z.copy(transid=tid))))
    def predicate(p:LineSalesOrder) = p.id==0
    def insert(model: List[SalesOrder[LineSalesOrder]]) :Int = {
      val tid:Long = Queries.getSequence ("Salesorder", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[SalesOrder_TYPE](Queries.salesOrderInsertSQL).updateMany(model.map (t => f2(tid, t)).map(
        x=>(tid, x.oid, x.modelId, x.store.get,x.account.get, x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineSalesOrder]].insert(x.lines.getOrElse(List[LineSalesOrder]())))
      ret

      }
    def create (modelId:Int) = Queries.createSalesOrder1.run.transact(xa).run
    def update(model:SalesOrder[LineSalesOrder]) = {
      val ret = Queries.salesOrderUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineSalesOrder) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineSalesOrderDAO.insert(newLines)
      //val k0 = lineSalesOrderDAO.insert(model.getLines.partition(_.tid ==0L && _.created == true)._1)
      val k1= model.getLines.partition(_.modified)._1.map (lineSalesOrderDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineSalesOrderDAO.delete(e) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:SalesOrder[LineSalesOrder]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineSalesOrderDAO.delete(l)))))
      Queries.salesOrderDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:SalesOrder[LineSalesOrder]):List[SalesOrder[LineSalesOrder]] = Queries.salesOrderSelect.process.list.transact(xa).run.map(
      x => SalesOrder(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:SalesOrder[LineSalesOrder])  : List[SalesOrder[LineSalesOrder]] =
      Queries.salesOrderIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> SalesOrder(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:SalesOrder[LineSalesOrder]) = Queries.salesOrderSelectSome(model.id).process.list.transact(xa).run.map(
      x=> SalesOrder(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.lineSalesOrderIdSelect(id).process.list.transact(xa).run.map(x => LineSalesOrder(x))
    def  updateLineId(id:Long,line:LineSalesOrder):LineSalesOrder = line.copy(transid=id)
  }


  implicit def lineBillOfDeliveryDAO = new DAO[LineBillOfDelivery]{
    def insert(model: List[LineBillOfDelivery]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineBillOfDelivery", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineBillOfDelivery_TYPE](Queries.lineBillOfDeliveryInsertSQL).
          updateMany(model.filter(_.item != None).map(x => (tid, x.transid, x.modelId, x.item.get, x.unit.get, x.price, x.quantity, x.vat.get, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }

    def create (modelId:Int) = Queries.createLineBillOfDelivery.run.transact(xa).run
    def update(model:LineBillOfDelivery) = { println(s" updating ${model}  items"); Queries.lineBillOfDeliveryUpdateName(model).run.transact(xa).run}
    def delete(model:LineBillOfDelivery):Int = Queries.lineBillOfDeliveryDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineBillOfDelivery):List[LineBillOfDelivery] =  Queries.lineBillOfDeliverySelect.process.list.transact(xa).run.map(x =>LineBillOfDelivery(x))
    def  find(model:LineBillOfDelivery):List[LineBillOfDelivery] = Queries.lineBillOfDeliveryIdSelect(model.id.toLong).process.list.transact(xa).run.map(x =>LineBillOfDelivery(x))
    def findSome(model:LineBillOfDelivery) = Queries.lineBillOfDeliverySelectSome(model.id).process.list.transact(xa).run.map(x =>LineBillOfDelivery(x))
    def findSome1(id:Long) = Queries.lineBillOfDeliveryIdSelect(id).process.list.transact(xa).run.map(x => LineBillOfDelivery(x))
  }

  implicit def billOfDeliveryDAO = new DAO[BillOfDelivery[LineBillOfDelivery]]{
    def predicate(p:LineBillOfDelivery) = p.id==0
    def insert(model: List[BillOfDelivery[LineBillOfDelivery]]) :Int = {
      val tid:Long = Queries.getSequence ("BillOfDelivery", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[BillOfDelivery_TYPE](Queries.billOfDeliveryInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineBillOfDelivery]].insert(x.lines.getOrElse(List[LineBillOfDelivery]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create(modelId:Int) = Queries.createBillOfDelivery.run.transact(xa).run
    def update(model:BillOfDelivery[LineBillOfDelivery]) = {
      val ret = Queries.billOfDeliveryUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineBillOfDelivery) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineBillOfDeliveryDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineBillOfDeliveryDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineBillOfDeliveryDAO.delete(e) )

      println(s" PO Line inserted K0 ${k0}   ${newLines}");
      println(s" PO Line updated K2 ${k1}   ");
      println(s" PO Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:BillOfDelivery[LineBillOfDelivery]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineBillOfDeliveryDAO.delete(l)))))
      Queries.billOfDeliveryDelete(model.tid).run.transact(xa).run
    }
    def all(model:BillOfDelivery[LineBillOfDelivery]):List[BillOfDelivery[LineBillOfDelivery]] = Queries.billOfDeliverySelect.process.list.transact(xa).run.map(
      x => BillOfDelivery(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:BillOfDelivery[LineBillOfDelivery])  : List[BillOfDelivery[LineBillOfDelivery]] =
      Queries.billOfDeliveryIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> BillOfDelivery(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:BillOfDelivery[LineBillOfDelivery]) = Queries.billOfDeliverySelectSome(model.id).process.list.transact(xa).run.map(
      x=> BillOfDelivery(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) =  Queries.lineBillOfDeliveryIdSelect(id).process.list.transact(xa).run.map(x => LineBillOfDelivery(x))
    def  updateLineId(id:Long,line:LineBillOfDelivery):LineBillOfDelivery = line.copy(transid=id)
  }



  implicit def lineSalesInvoiceDAO = new DAO[LineSalesInvoice]{
    def insert(model: List[LineSalesInvoice]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineSalesInvoice", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineSalesInvoice_TYPE](Queries.lineSalesInvoiceInsertSQL).
          updateMany(model.filter(_.item != None).map(x => (tid, x.transid, x.modelId, x.item.get, x.unit.get, x.price, x.quantity, x.vat.get, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create(modelId:Int) = Queries.createLineSalesInvoice.run.transact(xa).run
    def update(model:LineSalesInvoice) = { println(s" updating ${model}  items"); Queries.lineSalesInvoiceUpdateName(model).run.transact(xa).run}
    def delete(model:LineSalesInvoice):Int = Queries.lineSalesInvoiceDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineSalesInvoice):List[LineSalesInvoice] =  Queries.lineSalesInvoiceSelect.process.list.transact(xa).run.map(x =>LineSalesInvoice(x)) 
    def find(model:LineSalesInvoice):List[LineSalesInvoice] = Queries.lineSalesInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(x =>LineSalesInvoice(x)) 
    def findSome(model:LineSalesInvoice) = Queries.lineSalesInvoiceSelectSome(model.id).process.list.transact(xa).run.map(x =>LineSalesInvoice(x)) 
    def findSome1(id:Long) = Queries.lineSalesInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineSalesInvoice(x)) 
  }

  implicit def salesInvoiceDAO = new DAO[SalesInvoice[LineSalesInvoice]]{
    def predicate(p:LineSalesInvoice) = p.id==0
    def insert(model: List[SalesInvoice[LineSalesInvoice]]) :Int = {
      val tid:Long = Queries.getSequence ("SalesInvoice", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[SalesInvoice_TYPE](Queries.salesInvoiceInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineSalesInvoice]].insert(x.lines.getOrElse(List[LineSalesInvoice]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create(modelId:Int)= Queries.createSalesInvoice.run.transact(xa).run
    def update(model:SalesInvoice[LineSalesInvoice]) = {
      val ret = Queries.salesInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineSalesInvoice) = pred.tid == 0L && pred.created == true

      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineSalesInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineSalesInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineSalesInvoiceDAO.delete(e) )

      println(s" SalesInvoice Line inserted K0 ${k0}  line to insert: ${newLines}");
      println(s" SalesInvoice Line updated K2 ${k1}   ");
      println(s" SalesInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:SalesInvoice[LineSalesInvoice]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineSalesInvoiceDAO.delete(l)))))
      Queries.salesInvoiceDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:SalesInvoice[LineSalesInvoice]):List[SalesInvoice[LineSalesInvoice]] = Queries.salesInvoiceSelect.process.list.transact(xa).run.map(
      x => SalesInvoice(x._1,x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:SalesInvoice[LineSalesInvoice])  : List[SalesInvoice[LineSalesInvoice]] =
      Queries.salesInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> SalesInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:SalesInvoice[LineSalesInvoice]) = Queries.salesInvoiceSelectSome(model.id).process.list.transact(xa).run.map(
      x=> SalesInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.lineSalesInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineSalesInvoice(x))

    def  updateLineId(id:Long,line:LineSalesInvoice):LineSalesInvoice = line.copy(transid=id)
  }

  implicit def lineCustomerInvoiceDAO = new DAO[LineCustomerInvoice]{
    def insert(model: List[LineCustomerInvoice]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineCustomerInvoice", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineCustomerInvoice_TYPE](Queries.lineCustomerInvoiceInsertSQL).
          updateMany(model.filter(_.account != None).map(x => (tid, x.transid, x.modelId, x.account.get, x.side, x.oaccount.get, x.amount, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create (modelId:Int)= Queries.createLineCustomerInvoice.run.transact(xa).run
    def update(model:LineCustomerInvoice) = { println(s" updating ${model}  items"); Queries.lineCustomerInvoiceUpdateName(model).run.transact(xa).run}
    def delete(model:LineCustomerInvoice):Int = Queries.lineCustomerInvoiceDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineCustomerInvoice):List[LineCustomerInvoice] =  Queries.lineCustomerInvoiceSelect.process.list.transact(xa).run.map(x => LineCustomerInvoice(x))
    def  find(model:LineCustomerInvoice):List[LineCustomerInvoice] = Queries.lineCustomerInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(x =>LineCustomerInvoice(x))
    def findSome(model:LineCustomerInvoice) = Queries.lineCustomerInvoiceSelectSome(model.id).process.list.transact(xa).run.map(x => LineCustomerInvoice(x))
    def findSome1(id:Long) = Queries.lineCustomerInvoiceIdSelect(id).process.list.transact(xa).run.map(x => LineCustomerInvoice(x))

  }

  implicit def customerInvoiceDAO = new DAO[CustomerInvoice[LineCustomerInvoice]]{
    def predicate(p:LineCustomerInvoice) = p.id==0
    def insert(model: List[CustomerInvoice[LineCustomerInvoice]]) :Int = {
      val tid:Long = Queries.getSequence ("CustomerInvoice", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[CustomerInvoice_TYPE](Queries.customerInvoiceInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineCustomerInvoice]].insert(x.lines.getOrElse(List[LineCustomerInvoice]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create (modelId:Int) = Queries.createCustomerInvoice.run.transact(xa).run
    def update(model:CustomerInvoice[LineCustomerInvoice]) = {
      println(s" CustomerInvoice to  update ${model}");
      val ret = Queries.customerInvoiceUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineCustomerInvoice) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineCustomerInvoiceDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineCustomerInvoiceDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineCustomerInvoiceDAO.delete(e) )

      println(s" CustomerInvoice Line inserted K0 ${k0}   ${newLines}");
      println(s" CustomerInvoice Line updated K2 ${k1}   ");
      println(s" CustomerInvoice Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:CustomerInvoice[LineCustomerInvoice]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineCustomerInvoiceDAO.delete(l)))))
      Queries.customerInvoiceDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:CustomerInvoice[LineCustomerInvoice]):List[CustomerInvoice[LineCustomerInvoice]] = Queries.customerInvoiceSelect.process.list.transact(xa).run.map(
      x => CustomerInvoice(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:CustomerInvoice[LineCustomerInvoice])  : List[CustomerInvoice[LineCustomerInvoice]] =
      Queries.customerInvoiceIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> CustomerInvoice(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:CustomerInvoice[LineCustomerInvoice]) = Queries.customerInvoiceSelectSome(model.id).process.list.transact(xa).run.map(
      x=> CustomerInvoice(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) =  Queries.lineCustomerInvoiceIdSelect(id).process.list.transact(xa).run.map(x =>LineCustomerInvoice(x))
    def  updateLineId(id:Long,line:LineCustomerInvoice):LineCustomerInvoice = line.copy(transid=id)

  }

  implicit def lineSettlementDAO = new DAO[LineSettlement]{
    def insert(model: List[LineSettlement]) :Int = {
      println(s" inserting ${model}  items")
      var ret = 0
      if(!model.empty) {
        val tid: Long = Queries.getSequence("LineSettlement", "id").unique.transact(xa).run;
        println(s"  getSequence ${tid} ")
        ret = doobie.imports.Update[LineSettlement_TYPE](Queries.lineSettlementInsertSQL).
          updateMany(model.filter(_.account != None).map(x => (tid, x.transid, x.modelId, x.account.get, x.side, x.oaccount.get, x.amount, x.duedate.get, x.text))).transact(xa).run
      }
      ret
    }
    def create (lodelId:Int) = Queries.createLineSettlement.run.transact(xa).run
    def update(model:LineSettlement) = { println(s" updating ${model}  items"); Queries.lineSettlementUpdateName(model).run.transact(xa).run}
    def delete(model:LineSettlement):Int = Queries.lineSettlementDelete(model.id.toLong).run.transact(xa).run
    def all(model:LineSettlement):List[LineSettlement] =  Queries.lineSettlementSelect.process.list.transact(xa).run.map(x => LineSettlement(x))
    def find(model:LineSettlement):List[LineSettlement] = Queries.lineSettlementIdSelect(model.id.toLong).process.list.transact(xa).run.map(x => LineSettlement(x))
    def findSome(model:LineSettlement) = Queries.lineSettlementSelectSome(model.id).process.list.transact(xa).run.map(x => LineSettlement(x))
    def findSome1(id:Long) = Queries.lineSettlementIdSelect(id).process.list.transact(xa).run.map(x =>LineSettlement(x))
    def updateLineId(id:Long,line:LineSettlement):LineSettlement = line.copy(transid=id)
  }

  implicit def settlementDAO = new DAO[Settlement[LineSettlement]]{
    def predicate(p:LineSettlement) = p.id==0
    def insert(model: List[Settlement[LineSettlement]]) :Int = {
      println(s" inserting ${model}  items")
      val tid:Long = Queries.getSequence ("Settlement", "id").unique.transact(xa).run;
      val ret= doobie.imports.Update[Settlement_TYPE](Queries.settlementInsertSQL).updateMany(model.map(
        x=>(tid, x.oid, x.modelId, x.store.getOrElse(""),x.account.getOrElse(""), x.text))).transact(xa).run;
      model.map( x=>implicitly[DAO[LineSettlement]].insert(x.lines.getOrElse(List[LineSettlement]()).map( z => z.copy(transid=tid))))
      ret

    }
    def create(modelId:Int) = Queries.createSettlement.run.transact(xa).run
    def update(model:Settlement[LineSettlement]) = {
      val ret = Queries.settlementUpdateName(model).run.transact(xa).run;
      def prdicate (pred:LineSettlement) = pred.tid == 0L && pred.created == true
      val newLines = model.getLines.partition(prdicate)._1
      val k0 = lineSettlementDAO.insert(newLines)
      val k1= model.getLines.partition(_.modified)._1.map (lineSettlementDAO.update )
      val k2 = model.getLines.partition(_.deleted)._1.map ( e =>lineSettlementDAO.delete(e) )

      println(s" Settlement Line inserted K0 ${k0}   ${newLines}");
      println(s" Settlement Line updated K2 ${k1}   ");
      println(s" Settlement Line deleted K2 ${k2}   ");
      ret
    }
    def delete(model:Settlement[LineSettlement]):Int = {
      val r = find(model).map(p =>(p.getLines.map(l =>(lineSettlementDAO.delete(l)))))
      Queries.settlementDelete(model.id.toLong).run.transact(xa).run
    }
    def all(model:Settlement[LineSettlement]):List[Settlement[LineSettlement]] = Queries.settlementSelect.process.list.transact(xa).run.map(
      x => Settlement(x._1, x._2, x._3, Some(x._4),Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def find(model:Settlement[LineSettlement])  : List[Settlement[LineSettlement]] =
      Queries.settlementIdSelect(model.id.toLong).process.list.transact(xa).run.map(
        x=> Settlement(x._1,x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def findSome(model:Settlement[LineSettlement]) = Queries.settlementSelectSome(model.id).process.list.transact(xa).run.map(
      x=> Settlement(x._1, x._2, x._3, Some(x._4), Some(x._5), x._6).copy(lines = Some(f(x._1))))
    def f (id:Long) = Queries.lineSettlementIdSelect(id).process.list.transact(xa).run.map(x =>LineSettlement(x))
    def  updateLineId(id:Long,line:LineSettlement):LineSettlement = line.copy(transid=id)

  }

    implicit def fDocumentDAO = new DAO[FDocument[LineFDocument]]{
          
    def insert(model: List[FDocument[LineFDocument]]) :Int = insertDocument(model) //{val m = getModelList(model); getDAO(m(0).modelId).insert(m) }
    def create (modelId:Int) = getDAO(modelId).create(modelId)
    def update(model:FDocument[LineFDocument]) = updateDocument(model)   
    def delete(model:FDocument[LineFDocument]):Int =  deleteDocument(model) 
    def all(model:FDocument[LineFDocument]) = allFDocument(model)
    def find(model:FDocument[LineFDocument]) =findFDocument(model)
    def findSome(model:FDocument[LineFDocument]) = findSomeFDocument(model)
   // def findSome1(model:FDocument[LineFDocument]) =getDAO(model(0).modelId).all(model)
  }


   implicit def lineFDocumentDAO = new DAO[LineFDocument]{
     def insert(model: List[LineFDocument])  = insertLineDocument(model)
    def create (modelId:Int) = getDAO(modelId).create(modelId)
    def update(model:LineFDocument) = updateLineDocument(model)
    def delete(model:LineFDocument):Int = deleteLineDocument(model) 
    def all (model:LineFDocument) =   allLineFDocument(model)
    def find(model:LineFDocument) = findLineFDocument(model)
    def findSome(model:LineFDocument) = findSomeLineFDocument(model)

  }


  def findSomeLineFDocument( model:LineFDocument):List[LineFDocument]= {
    model.modelId match {
      case 113  => lineVendorInvoiceDAO.findSome(LineVendorInvoice(model)).map(m =>LineFDocument(m))
      case 115  => linePaymentDAO.findSome(LinePayment(model)).map(m =>LineFDocument(m))
      case 123  => lineCustomerInvoiceDAO.findSome(LineCustomerInvoice(model)).map(m =>LineFDocument(m))
      case 125  => lineSettlementDAO.findSome(LineSettlement(model)).map(m =>LineFDocument(m))
    }
  }
   def findLineFDocument( model:LineFDocument):List[LineFDocument]= {
    model.modelId match {
      case 113  => lineVendorInvoiceDAO.find(LineVendorInvoice(model)).map(m =>LineFDocument(m))
      case 115  => linePaymentDAO.find(LinePayment(model)).map(m =>LineFDocument(m))
      case 123  => lineCustomerInvoiceDAO.find(LineCustomerInvoice(model)).map(m =>LineFDocument(m))
      case 125  => lineSettlementDAO.find(LineSettlement(model)).map(m =>LineFDocument(m))
    }
  }
  def findSomeFDocument( model:FDocument[LineFDocument]):List[FDocument[LineFDocument]]= {
    model.modelId match {
      case 112  => vendorInvoiceDAO.findSome(VendorInvoice(model)).map(m =>FDocument(m))
      case 114  => paymentDAO.findSome(Payment(model)).map(m =>FDocument(m))
      case 122  => customerInvoiceDAO.findSome(CustomerInvoice(model)).map(m =>FDocument(m))
      case 124  => settlementDAO.findSome(Settlement(model)).map(m =>FDocument(m))
    }
  }

  def findFDocument( model:FDocument[LineFDocument]):List[FDocument[LineFDocument]]= {
    model.modelId match {
      case 112  => vendorInvoiceDAO.find(VendorInvoice(model)).map(m =>FDocument(m))
      case 114  => paymentDAO.find(Payment(model)).map(m =>FDocument(m))
      case 122  => customerInvoiceDAO.find(CustomerInvoice(model)).map(m =>FDocument(m))
      case 124  => settlementDAO.find(Settlement(model)).map(m =>FDocument(m))
    }
  }

  def allLineFDocument( model:LineFDocument):List[LineFDocument]= {
    model.modelId match {
      case 113  => lineVendorInvoiceDAO.all(LineVendorInvoice(model)).map(m =>LineFDocument(m))
      case 115  => linePaymentDAO.all(LinePayment(model)).map(m =>LineFDocument(m))
      case 123  => lineCustomerInvoiceDAO.all(LineCustomerInvoice(model)).map(m =>LineFDocument(m))
      case 125  => lineSettlementDAO.all(LineSettlement(model)).map(m =>LineFDocument(m))
    }
  }
  def allFDocument( model:FDocument[LineFDocument]):List[FDocument[LineFDocument]]= {
    model.modelId match {
      case 112  => vendorInvoiceDAO.all(VendorInvoice(model)).map(m =>FDocument(m))
      case 114  => paymentDAO.all(Payment(model)).map(m =>FDocument(m))
      case 122  => customerInvoiceDAO.all(CustomerInvoice(model)).map(m =>FDocument(m))
      case 124  => settlementDAO.all(Settlement(model)).map(m =>FDocument(m))
    }
  }
 def insertDocument(models: List [FDocument[LineFDocument]]):Int = {
   models(0).modelId match {
      case 112 => vendorInvoiceDAO.insert(models.map( model => VendorInvoice(model)))
      case 114 => paymentDAO.insert(models.map( model =>Payment(model)))
      case 122 => customerInvoiceDAO.insert(models.map( model => CustomerInvoice(model)))
      case 124 => settlementDAO.insert(models.map( model =>Settlement(model)))
   }
 } 
  def insertLineDocument(models: List[LineFDocument]):Int= {
   models(0).modelId match {
      case 113 => lineVendorInvoiceDAO.insert(models.map( model =>LineVendorInvoice(model)))
      case 115 => linePaymentDAO.insert(models.map( model =>LinePayment(model)))
      case 123 => lineCustomerInvoiceDAO.insert(models.map( model =>LineCustomerInvoice(model)))
      case 125 => lineSettlementDAO.insert(models.map( model =>LineSettlement(model)))
   }
 } 
 def getDAO(modelId:Int) = {
   modelId match {
      case 112 => vendorInvoiceDAO
      case 113 => lineVendorInvoiceDAO
      case 114 => paymentDAO
      case 115 => linePaymentDAO
      case 122 => customerInvoiceDAO
      case 124 => settlementDAO

   }
 }

  def updateLineDocument( model:LineFDocument):Int= {
    model.modelId match {
      case 113  => lineVendorInvoiceDAO.update(LineVendorInvoice(model))
      case 115  => linePaymentDAO.update(LinePayment(model))
      case 123  => lineCustomerInvoiceDAO.update(LineCustomerInvoice(model))
      case 125  => lineSettlementDAO.update(LineSettlement(model))
    }
  }
  def updateDocument( model:FDocument[LineFDocument]):Int= {
    model.modelId match {
      case 112  => vendorInvoiceDAO.update(VendorInvoice(model))
      case 114  => paymentDAO.update(Payment(model))
      case 122  => customerInvoiceDAO.update(CustomerInvoice(model))
      case 124  => settlementDAO.update(Settlement(model))
    }
  }
  def deleteLineDocument( model:LineFDocument) = {
    model.modelId match {
      case 113  => lineVendorInvoiceDAO.delete(LineVendorInvoice(model))
      case 115  => linePaymentDAO.delete(LinePayment(model))
      case 123  => lineCustomerInvoiceDAO.delete(LineCustomerInvoice(model))
      case 125  => lineSettlementDAO.delete(LineSettlement(model))
    }
  }
  def deleteDocument( model:FDocument[LineFDocument]) = {
    model.modelId match {
      case 112  => vendorInvoiceDAO.delete(VendorInvoice(model))
      case 114  => paymentDAO.delete(Payment(model))
      case 122  => customerInvoiceDAO.delete(CustomerInvoice(model))
      case 124  => settlementDAO.delete(Settlement(model))
    }
  }
  def create: ConnectionIO[Int] = Queries.create.run

  def runNonParameterizedSingleObjectQuery[A](q: Query0[A]) = q.unique.transact(xa).run
  def processNonParameterizedQuery[A](q: Query0[A]) = q.process.transact(xa)

}

