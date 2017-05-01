package com.kabasoft.iws.dao


import java.util.Date

import scalaz.{Free => F, Category => _, Store => _, _}
import Scalaz._
import scalaz.Foldable
import scalaz.effect.IO
import scalaz.stream.Process
import doobie.free.connection.ConnectionIO
import doobie.imports._
import doobie.syntax._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.shared.common._
import com.kabasoft.iws.shared.Model._

object Queries  {

  def  create: Update0 = createSchema.update
  def bankInsertSQL= "INSERT INTO Bank (id, name, modelId, description)  VALUES (?, ?, ?, ?)"
  def bankSelect =  sql"SELECT * FROM Bank".query[Bank]
  def bankIdSelect(id:String) = sql"SELECT  *FROM Bank where id =$id".query[Bank]
  def bankSelectSome = {id:String =>sql"SELECT  * FROM Bank where id =$id".query[Bank]}
  def bankSelectByGroupId = {id:String =>sql"SELECT  *  FROM Bank where groupid =$id".query[Bank]}
  def bankUpdateName = {(model:Bank) =>sql"Update bank set id = ${model.id}, name =${model.name},  description=${model.description} where id =${model.id}".update}
  def bankDelete = {id:String =>sql"Delete FROM bank where id =$id".update}

  def bankAccountInsertSQL= "INSERT INTO BankAccount (id, name, modelId, description, bic, debit, credit )  VALUES (?, ?, ?, ?, ?, ?, ?)"
  def bankAccountSelect =  sql"SELECT * FROM bankAccount".query[BankAccount]
  def bankAccountIdSelect(id:String) = sql"SELECT  * FROM bankAccount where id =$id".query[BankAccount]
  def bankAccountSelectSome = {id:String =>sql"SELECT  * FROM bankAccount where id =$id".query[BankAccount]}
  def bankAccountSelectByGroupId = {id:String =>sql"SELECT  *  FROM bankAccount where groupid =$id".query[BankAccount]}
  def bankAccountUpdateName = {(model:BankAccount) =>sql"Update bankAccount set id = ${model.id}, name =${model.name},  description=${model.description}, bic =${model.bic}, debit = ${model.debit} , credit = ${model.credit} where id =${model.id}".update}
  def bankAccountDelete = {id:String =>sql"Delete FROM bankAccount where id =$id".update}

  def accountInsertSQL= "INSERT INTO account VALUES (?, ?, ?, ?, ?, ?, ?)"
  def accountSelect =  sql"SELECT * FROM account".query[ACCOUNT_TYPE]
  def accountIdSelect(id:String) = sql"SELECT * FROM account where id =$id".query[ACCOUNT_TYPE]
  def accountSelectSome = {id:String =>sql"SELECT * FROM account where id =$id".query[ACCOUNT_TYPE]}
  def accountSelectByGroupId = {id:String =>sql"SELECT * FROM account where groupid =$id".query[ACCOUNT_TYPE]}
  def accountUpdateName = {(model:Account) =>sql"Update account set name =${model.name}, modelId=${model.modelId}, description=${model.description}, groupId=${model.groupId.getOrElse("")} where id =${model.id}".update}
  def accountDelete = {id:String =>sql"Delete FROM account where id =$id".update}

  def articleInsertSQL= "INSERT INTO article (id, name, modelId, description, price, avgPrice, salesPrice, qttyUnit, packUnit, groupId, vat ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def articleSelect = sql"SELECT id, name, modelId, description, price, avgprice, salesPrice, qttyUnit, packUnit, groupId, vat  FROM article".query[ARTICLE_TYPE]
  def articleIdSelect(id:String) = sql"SELECT * FROM article where id =$id".query[ARTICLE_TYPE]
  def articleSelectByGroupId = {groupId:String =>sql"SELECT id, name, modelId, description, price, avgprice, salesPrice, qttyUnit, packUnit, groupId, vat FROM article where groupid =$groupId".query[ARTICLE_TYPE]}
  def articleSelectSome = {id:String =>sql"SELECT * FROM article  where id =$id".query[ARTICLE_TYPE]}
  def articleUpdateName= {(model:Article) =>sql"Update article set name =${model.name}, description =${model.description}, price =${model.price},  avgprice =${model.avgPrice}, salesPrice =${model.salesPrice}, qttyUnit =${model.qttyUnit} , packUnit =${model.packUnit}, groupId=${model.groupId.getOrElse("")} , vat =${model.vat.getOrElse("0")}  where id =${model.id}".update}
  def articleDelete = {id:String =>sql"Delete FROM article where id =$id".update}

  def quantityUnitInsertSQL = "INSERT INTO quantityUnit VALUES (?, ?, ?, ?)"
  def quantityUnitSelect = sql"SELECT id, name, modelid, description FROM quantityUnit".query[QuantityUnit]
  def QtyUnitWithArticle(id:String) = sql"SELECT  q.id, q.name, q.description FROM QuantityUnit q  LEFT JOIN  article a   ON a.qtty_id = a.id and q.id=$id".query[QuantityUnit]
  def quantityUnitIdSelect(id:String) = sql"SELECT * FROM quantityUnit  where id =$id".query[QuantityUnit]
  def quantityUnitSelectSome = { id:String =>sql"SELECT * FROM quantityUnit where id =$id".query[QuantityUnit]}
  def quantityUnitUpdateName= {(model:QuantityUnit) =>sql"Update quantityUnit set name =${model.name},description=${model.description} where id =${model.id}".update}
  def quantityUnitDelete = {id:String =>sql"Delete FROM quantityUnit where id =$id".update}

  def costCenterInsertSQL = "INSERT INTO costcenter VALUES (?, ?, ?, ?)"
  def costCenterSelect = sql"SELECT id, name, modelId, description FROM costcenter".query[CostCenter]
  def costCenterIdSelect(id:String) = sql"SELECT * FROM costcenter  where id =$id".query[CostCenter]
  def costCenterSelectSome = { id:String =>sql"SELECT * FROM costcenter where id =$id".query[CostCenter]}
  def costCenterUpdateName= {(model:CostCenter) =>sql"Update costcenter set  name =${model.name},description=${model.description} where id =${model.id}".update}
  def costCenterDelete = {id:String =>sql"Delete FROM costcenter where id =$id".update}
  def companyInsertSQL = "INSERT INTO company(id, name, modelId,street, city,state,zip,bankAccountId, purchasingClearingAccountId, " +
    "salesClearingAccountId, paymentClearingAccountId, settlementClearingAccountId, periode, nextPeriode, taxCode, vatCode )" +
    " company VALUES (?, ?,?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)"
  def companySelect = sql"SELECT * FROM company".query[Company]
  def companyIdSelect(id:String)  = sql"SELECT * FROM customer where id =$id".query[Company]
  def companySelect1 = sql"SELECT *  FROM company".query[Company]
  def companySelectSome  = { id:String =>sql"SELECT * FROM company where id =$id".query[Company]}
  def companyUpdateName= {(model:Company) =>sql"Update company set  name =${model.name},  street=${model.street}, city=${model.city}, zip=${model.zip}, state =${model.state},  bankAccountId = ${model.bankAccountId}, purchasingClearingAccountId =${model.purchasingClearingAccountId}, salesClearingAccountId =${model.salesClearingAccountId}, paymentClearingAccountId =${model.paymentClearingAccountId}, settlementClearingAccountId =${model.settlementClearingAccountId}, periode = ${model.periode}, nextPeriode =${model.nextPeriode},taxCode =${model.taxCode}, vatCode =${model.vatId} where id =${model.id}".update}
  def companyDelete = {id:String =>sql"Delete FROM company where id =$id".update}

  def customerInsertSQL = "INSERT INTO customer (id, name, modelId,accountId,street, city,state,zip)  VALUES (?, ?,?, ?, ?, ?, ?, ?)"
  def customerSelect = sql"SELECT id, name, modelId,accountId,street, city,state,zip FROM customer".query[Customer]
  def customerIdSelect(id:String)  = sql"SELECT id, name, modelId,accountId,street, city,state,zip FROM customer where id =$id".query[Customer]
  def customerSelect1 = sql"SELECT *  FROM customer".query[Customer]
  def customerSelectSome  = { id:String =>sql"SELECT * FROM customer where id =$id".query[Customer]}
  def customerUpdateName= {(model:Customer) =>sql"Update customer set  name =${model.name}, accountId=${model.accountId}, street=${model.street}, city=${model.city}, zip=${model.zip}, state =${model.state} where id =${model.id}".update}
  def customerDelete = {id:String =>sql"Delete FROM customer where id =$id".update}

  def supplierInsertSQL = "INSERT INTO suppliers (id, name, modelId,accountId,  street, city,state,zip) VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
  def supplierSelect = sql"SELECT id, name, modelId,accountId,street, city,state,zip FROM supplier".query[Supplier]
  def supplierIdSelect(id:String)  = sql"SELECT * FROM supplier where id =$id".query[Supplier]
  def supplierSelect1 = sql"SELECT id, name, modelId,accountId,street, city,state,zip  FROM supplier".query[Supplier]
  def supplierSelectSome  = { id:String =>sql"SELECT * FROM supplier where id =$id".query[Supplier]}
  def supplierUpdateName= {(model:Supplier) =>sql"Update supplier set  name =${model.name}, accountId=${model.accountId},street=${model.street}, city=${model.city}, zip=${model.zip}, state =${model.state} where id =${model.id}".update}
  def supplierDelete = {id:String =>sql"Delete FROM supplier where id =$id".update}

  def storeInsertSQL = "INSERT INTO store (id, name, modelId,accountId,  street, city,state,zip)  VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
  def storeSelect = sql"SELECT id, name, modelId, accountId, street, city, state,zip FROM store".query[STORE_TYPE]
  def storeIdSelect(id:String)  = sql"SELECT id, name, modelId, accountId, street, city, state,zip FROM store where id =$id".query[STORE_TYPE]
  def storeSelect1 = sql"SELECT id, name, modelId, accountId, street, city, state,zip FROM store".query[STORE_TYPE]
  def storeSelectSome  = { id:String =>sql"SELECT * FROM store where id =$id".query[STORE_TYPE]}
  def storeUpdateName= {(model:Store) =>sql"Update store  set name =${model.name}, accountId=${model.accountId}, street=${model.street}, city=${model.city},zip=${model.zip}, state =${model.state} where id =${model.id}".update}
  def storeDelete = {id:String =>sql"Delete FROM store where id =$id".update}

  def categoryInsertSQL = "INSERT INTO category  VALUES (?, ?, ?, ?)"
  def categorySelect = sql"SELECT id, name, modelId, description FROM category".query[ArticleGroup]
  def categoryIdSelect(id:String)  = sql"SELECT * FROM category where id =$id".query[ArticleGroup]
  def categorySelect1 = sql"SELECT id, name, description FROM category ".query[ArticleGroup]
  def categorySelectSome  = { id:String =>sql"SELECT * FROM category where id =$id".query[ArticleGroup]}
  def categoryUpdateName= {(model:ArticleGroup) =>sql"Update category set  name =${model.name}, description=${model.description} where id =${model.id}".update}
  def categoryDelete = {id:String =>sql"Delete FROM category where id =$id".update}

  def periodicAccountBalanceInsertSQL = "INSERT INTO PeriodicAccountBalance  VALUES (?, ?, ?, ?)"
  def periodicAccountBalanceSelect = sql"SELECT * FROM PeriodicAccountBalance".query[PeriodicAccountBalance]
  def periodicAccountBalanceIdSelect(id:String)  = sql"SELECT * FROM PeriodicAccountBalance where id =$id".query[PeriodicAccountBalance]
  def periodicAccountBalanceSelect1 = sql"SELECT * FROM PeriodicAccountBalance ".query[PeriodicAccountBalance]
  def periodicAccountBalanceSelectSome  = { id:String =>sql"SELECT * FROM PeriodicAccountBalance where id =$id".query[PeriodicAccountBalance]}
  def periodicAccountBalanceUpdateName= {(model:PeriodicAccountBalance) =>sql"Update PeriodicAccountBalance set  debit =${model.debit}, credit=${model.credit} where id =${model.id}".update}
  def periodicAccountBalanceDelete = {id:String =>sql"Delete FROM PeriodicAccountBalance where id =$id".update}

  def stockInsertSQL = "INSERT INTO stock (id, modelId, itemId, storeId, quantity, minStock) VALUES (?, ?, ?, ?, ?, ?)"
  def stockSelect = sql"SELECT id, modelId,  itemId, storeId, quantity, minStock FROM stock".query[Stock]
  def stockIdSelect(id:String)  = sql"SELECT * FROM stock where id =$id".query[Stock]
  def stockSelect1 = sql"SELECT * FROM stock ".query[Stock]
  def stockSelectSome  = { id:String =>sql"SELECT * FROM stock where storeId =$id".query[Stock]}
  def stockUpdateName= {(model:Stock) =>sql"Update stock set  quantity =${model.quantity}, minStock=${model.minStock} where id =${model.id}".update}
  def stockDelete = {id:String =>sql"Delete FROM stock where id =$id".update}


  def vatInsertSQL = "INSERT INTO vat (id, name, description, modelId,percent, inputVatAccountId, outputVatAccountId,revenueAccountId, stockAccountId, expenseAccountId ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def vatSelect = sql"SELECT * FROM vat".query[Vat]
  def vatIdSelect(id:String)  = sql"SELECT * FROM vat where id =$id".query[Vat]
  def vatSelect1 = sql"SELECT * FROM vat".query[Vat]
  def vatSelectSome  = { id:String =>sql"SELECT * FROM vat where id =$id".query[Vat]}
  def vatUpdateName= {(model:Vat) =>sql"Update vat set  name =${model.name}, description=${model.description},percent=${model.percent}, inputVatAccountId=${model.inputVatAccountId},  outputVatAccountId=${model.outputVatAccountId},  revenueAccountId=${model.revenueAccountId}, stockAccountId=${model.stockAccountId},  expenseAccountId=${model.expenseAccountId}, where id =${model.id}".update}
  def vatDelete = {id:String =>sql"Delete FROM vat where id =$id".update}

  def linePurchaseOrderInsertSQL = "INSERT INTO LinePurchaseOrder ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def linePurchaseOrderSelect = sql"SELECT * FROM LinePurchaseOrder".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderIdSelect1(id:String) = sql"SELECT * FROM LinePurchaseOrder  where id =$id".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderIdSelect(id:Long) = sql"SELECT * FROM LinePurchaseOrder  where transid =$id".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderSelectSome = { id:String =>sql"SELECT * FROM LinePurchaseOrder where id =$id".query[LinePurchaseOrder_TYPE]}
  def linePurchaseOrderUpdateName = {(model:LinePurchaseOrder) =>
    sql"Update LinePurchaseOrder set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def linePurchaseOrderDelete = {(id:Long) =>sql"Delete FROM LinePurchaseOrder where id =$id".update}
  //INSERT INTO PurchaseOrder ( oid, modelId, store, account) VALUES (0,101,'200','1000');


  def purchaseOrderInsertSQL = "INSERT INTO PurchaseOrder (id, oid, modelId, store, account, text) VALUES (?, ?, ?,?,?, ?)"
  def purchaseOrderSelect = sql"SELECT * FROM PurchaseOrder".query[PurchaseOrder_TYPE]
  def purchaseOrderIdSelect1(id:String) = sql"SELECT * FROM PurchaseOrder  where id =$id.toLong".query[PurchaseOrder_TYPE]
  def purchaseOrderIdSelect(id:Long) = sql"SELECT * FROM PurchaseOrder  where id =$id".query[PurchaseOrder_TYPE]
  def purchaseOrderSelectSome = { id:String =>sql"SELECT * FROM PurchaseOrder where id =$id".query[PurchaseOrder_TYPE]}
  def purchaseOrderUpdateName= {(model:PurchaseOrder[LinePurchaseOrder]) =>
    sql"Update PurchaseOrder set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def purchaseOrderDelete = {id:Long =>sql"Delete FROM PurchaseOrder where id =$id".update}


  def lineGoodreceivingInsertSQL = "INSERT INTO LineGoodreceiving ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def lineGoodreceivingSelect = sql"SELECT * FROM LineGoodreceiving".query[LineGoodreceiving_TYPE]
  def lineGoodreceivingIdSelect1(id:String) = sql"SELECT * FROM LineGoodreceiving  where id =$id".query[LineGoodreceiving_TYPE]
  def lineGoodreceivingIdSelect(id:Long) = sql"SELECT * FROM LineGoodreceiving  where transid =$id".query[LineGoodreceiving_TYPE]
  def lineGoodreceivingSelectSome = { id:String =>sql"SELECT * FROM LineGoodreceiving where id =$id".query[LineGoodreceiving_TYPE]}
  def lineGoodreceivingUpdateName = {(model:LineGoodreceiving) =>
    sql"Update LineGoodreceiving set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineGoodreceivingDelete = {(id:Long) =>sql"Delete FROM LineGoodreceiving where id =$id".update}

  def goodreceivingInsertSQL = "INSERT INTO Goodreceiving (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def goodreceivingSelect = sql"SELECT * FROM Goodreceiving".query[Goodreceiving_TYPE]
  def goodreceivingIdSelect1(id:String) = sql"SELECT * FROM Goodreceiving  where id =$id.toLong".query[Goodreceiving_TYPE]
  def goodreceivingIdSelect(id:Long) = sql"SELECT * FROM Goodreceiving  where id =$id".query[Goodreceiving_TYPE]
  def goodreceivingSelectSome = { id:String =>sql"SELECT * FROM Goodreceiving where id =$id".query[Goodreceiving_TYPE]}
  def goodreceivingUpdateName= {(model:Goodreceiving[LineGoodreceiving]) =>
    sql"Update Goodreceiving set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def goodreceivingDelete = {id:Long =>sql"Delete FROM Goodreceiving where id =$id".update}


  def lineInventoryInvoiceInsertSQL = "INSERT INTO LineInventoryInvoice ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def lineInventoryInvoiceSelect = sql"SELECT * FROM LineInventoryInvoice".query[LineInventoryInvoice_TYPE]
  def lineInventoryInvoiceIdSelect1(id:String) = sql"SELECT * FROM LineInventoryInvoice  where id =$id".query[LineInventoryInvoice_TYPE]
  def lineInventoryInvoiceIdSelect(id:Long) = sql"SELECT * FROM LineInventoryInvoice  where transid =$id".query[LineInventoryInvoice_TYPE]
  def lineInventoryInvoiceSelectSome = { id:String =>sql"SELECT * FROM LineInventoryInvoice where id =$id".query[LineInventoryInvoice_TYPE]}
  def lineInventoryInvoiceUpdateName = {(model:LineInventoryInvoice) =>
    sql"Update LineInventoryInvoice set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineInventoryInvoiceDelete = {(id:Long) =>sql"Delete FROM LineInventoryInvoice where id =$id".update}

  def inventoryInvoiceInsertSQL = "INSERT INTO InventoryInvoice (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def inventoryInvoiceSelect = sql"SELECT * FROM InventoryInvoice".query[InventoryInvoice_TYPE]
  def inventoryInvoiceIdSelect1(id:String) = sql"SELECT * FROM InventoryInvoice  where id =$id.toLong".query[InventoryInvoice_TYPE]
  def inventoryInvoiceIdSelect(id:Long) = sql"SELECT * FROM InventoryInvoice  where id =$id".query[InventoryInvoice_TYPE]
  def inventoryInvoiceSelectSome = { id:String =>sql"SELECT * FROM InventoryInvoice where id =$id".query[InventoryInvoice_TYPE]}
  def inventoryInvoiceUpdateName= {(model:InventoryInvoice[LineInventoryInvoice]) =>
    sql"Update InventoryInvoice set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def inventoryInvoiceDelete = {id:Long =>sql"Delete FROM InventoryInvoice where id =$id".update}


  def lineVendorInvoiceInsertSQL = "INSERT INTO LineVendorInvoice ( id, transid, modelId, account, side ,oaccount, amount,  duedate, text)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def lineVendorInvoiceSelect = sql"SELECT * FROM LineVendorInvoice".query[LineVendorInvoice_TYPE]
  def lineVendorInvoiceIdSelect1(id:String) = sql"SELECT * FROM LineVendorInvoice  where id =$id".query[LineVendorInvoice_TYPE]
  def lineVendorInvoiceIdSelect(id:Long) = sql"SELECT * FROM LineVendorInvoice  where transid =$id".query[LineVendorInvoice_TYPE]
  def lineVendorInvoiceSelectSome = { id:String =>sql"SELECT * FROM LineVendorInvoice where id =$id".query[LineVendorInvoice_TYPE]}
  def lineVendorInvoiceUpdateName = {(model:LineVendorInvoice) =>
    sql"Update LineVendorInvoice set transid=${model.transid},  modelId=${model.modelId}, account=${model.account.get}, side = ${model.side}, oaccount=${model.oaccount.get}, amount=${model.amount}, duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineVendorInvoiceDelete = {(id:Long) =>sql"Delete FROM LineVendorInvoice where id =$id".update}

  def vendorInvoiceInsertSQL = "INSERT INTO VendorInvoice (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def vendorInvoiceSelect = sql"SELECT * FROM VendorInvoice".query[VendorInvoice_TYPE]
  def vendorInvoiceIdSelect1(id:String) = sql"SELECT * FROM VendorInvoice  where id =$id.toLong".query[VendorInvoice_TYPE]
  def vendorInvoiceIdSelect(id:Long) = sql"SELECT * FROM VendorInvoice  where id =$id".query[VendorInvoice_TYPE]
  def vendorInvoiceSelectSome = { id:String =>sql"SELECT * FROM VendorInvoice where id =$id".query[VendorInvoice_TYPE]}
  def vendorInvoiceUpdateName= {(model:VendorInvoice[LineVendorInvoice]) =>
    sql"Update VendorInvoice set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def vendorInvoiceDelete = {id:Long =>sql"Delete FROM VendorInvoice where id =$id".update}



  def linePaymentInsertSQL = "INSERT INTO LinePayment ( id, transid, modelId, account, side ,oaccount, amount,  duedate, text)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def linePaymentSelect = sql"SELECT * FROM LinePayment".query[LinePayment_TYPE]
  def linePaymentIdSelect1(id:String) = sql"SELECT * FROM LinePayment  where id =$id".query[LinePayment_TYPE]
  def linePaymentIdSelect(id:Long) = sql"SELECT * FROM LinePayment  where transid =$id".query[LinePayment_TYPE]
  def linePaymentSelectSome = { id:String =>sql"SELECT * FROM LinePayment where id =$id".query[LinePayment_TYPE]}
  def linePaymentUpdateName = {(model:LinePayment) =>
    sql"Update LinePayment set transid=${model.transid},  modelId=${model.modelId}, account=${model.account.get}, side = ${model.side}, oaccount=${model.oaccount.get}, amount=${model.amount}, duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def linePaymentDelete = {(id:Long) =>sql"Delete FROM LinePayment where id =$id".update}
  

  def paymentInsertSQL = "INSERT INTO Payment (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def paymentSelect = sql"SELECT * FROM Payment".query[Payment_TYPE]
  def paymentIdSelect1(id:String) = sql"SELECT * FROM Payment  where id =$id.toLong".query[Payment_TYPE]
  def paymentIdSelect(id:Long) = sql"SELECT * FROM Payment  where id =$id".query[Payment_TYPE]
  def paymentSelectSome = { id:String =>sql"SELECT * FROM Payment where id =$id".query[Payment_TYPE]}
  def paymentUpdateName= {(model:Payment[LinePayment]) =>
    sql"Update payment set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def paymentDelete = {id:Long =>sql"Delete FROM Payment where id =$id".update}


  def lineSalesOrderInsertSQL = "INSERT INTO LineSalesOrder ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def lineSalesOrderSelect = sql"SELECT * FROM LineSalesOrder".query[LineSalesOrder_TYPE]
  def lineSalesOrderIdSelect1(id:String) = sql"SELECT * FROM LineSalesOrder  where id =$id".query[LineSalesOrder_TYPE]
  def lineSalesOrderIdSelect(id:Long) = sql"SELECT * FROM LineSalesOrder  where transid =$id".query[LineSalesOrder_TYPE]
  def lineSalesOrderSelectSome = { id:String =>sql"SELECT * FROM LineSalesOrder where id =$id".query[LineSalesOrder_TYPE]}
  def lineSalesOrderUpdateName = {(model:LineSalesOrder) =>
    sql"Update LineSalesOrder set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineSalesOrderDelete = {(id:Long) =>sql"Delete FROM LineSalesOrder where id =$id".update}
  //INSERT INTO SalesOrder ( oid, modelId, store, account) VALUES (0,101,'200','1000');

  def salesOrderInsertSQL = "INSERT INTO SalesOrder (id, oid, modelId, store, account, text) VALUES (?, ?, ?,?,?, ?)"
  def salesOrderSelect = sql"SELECT * FROM SalesOrder".query[SalesOrder_TYPE]
  def salesOrderIdSelect1(id:String) = sql"SELECT * FROM SalesOrder  where id =$id.toLong".query[SalesOrder_TYPE]
  def salesOrderIdSelect(id:Long) = sql"SELECT * FROM SalesOrder  where id =$id".query[SalesOrder_TYPE]
  def salesOrderSelectSome = { id:String =>sql"SELECT * FROM SalesOrder where id =$id".query[SalesOrder_TYPE]}
  def salesOrderUpdateName= {(model:SalesOrder[LineSalesOrder]) =>
    sql"Update SalesOrder set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def salesOrderDelete = {id:Long =>sql"Delete FROM SalesOrder where id =$id".update}


  def lineBillOfDeliveryInsertSQL = "INSERT INTO LineBillOfDelivery ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def lineBillOfDeliverySelect = sql"SELECT * FROM LineBillOfDelivery".query[LineBillOfDelivery_TYPE]
  def lineBillOfDeliveryIdSelect1(id:String) = sql"SELECT * FROM LineBillOfDelivery  where id =$id".query[LineBillOfDelivery_TYPE]
  def lineBillOfDeliveryIdSelect(id:Long) = sql"SELECT * FROM LineBillOfDelivery  where transid =$id".query[LineBillOfDelivery_TYPE]
  def lineBillOfDeliverySelectSome = { id:String =>sql"SELECT * FROM LineBillOfDelivery where id =$id".query[LineBillOfDelivery_TYPE]}
  def lineBillOfDeliveryUpdateName = {(model:LineBillOfDelivery) =>
    sql"Update LineBillOfDelivery set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineBillOfDeliveryDelete = {(id:Long) =>sql"Delete FROM LineBillOfDelivery where id =$id".update}

  def billOfDeliveryInsertSQL = "INSERT INTO BillOfDelivery (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def billOfDeliverySelect = sql"SELECT * FROM BillOfDelivery".query[BillOfDelivery_TYPE]
  def billOfDeliveryIdSelect1(id:String) = sql"SELECT * FROM BillOfDelivery  where id =$id.toLong".query[BillOfDelivery_TYPE]
  def billOfDeliveryIdSelect(id:Long) = sql"SELECT * FROM BillOfDelivery  where id =$id".query[BillOfDelivery_TYPE]
  def billOfDeliverySelectSome = { id:String =>sql"SELECT * FROM BillOfDelivery where id =$id".query[BillOfDelivery_TYPE]}
  def billOfDeliveryUpdateName= {(model:BillOfDelivery[LineBillOfDelivery]) =>
    sql"Update BillOfDelivery set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def billOfDeliveryDelete = {id:Long =>sql"Delete FROM BillOfDelivery where id =$id".update}


  def lineSalesInvoiceInsertSQL = "INSERT INTO LineSalesInvoice ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def lineSalesInvoiceSelect = sql"SELECT * FROM LineSalesInvoice".query[LineSalesInvoice_TYPE]
  def lineSalesInvoiceIdSelect1(id:String) = sql"SELECT * FROM LineSalesInvoice  where id =$id".query[LineSalesInvoice_TYPE]
  def lineSalesInvoiceIdSelect(id:Long) = sql"SELECT * FROM LineSalesInvoice  where transid =$id".query[LineSalesInvoice_TYPE]
  def lineSalesInvoiceSelectSome = { id:String =>sql"SELECT * FROM LineSalesInvoice where id =$id".query[LineSalesInvoice_TYPE]}
  def lineSalesInvoiceUpdateName = {(model:LineSalesInvoice) =>
    sql"Update LineSalesInvoice set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineSalesInvoiceDelete = {(id:Long) =>sql"Delete FROM LineSalesInvoice where id =$id".update}

  def salesInvoiceInsertSQL = "INSERT INTO SalesInvoice (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def salesInvoiceSelect = sql"SELECT * FROM SalesInvoice".query[SalesInvoice_TYPE]
  def salesInvoiceIdSelect1(id:String) = sql"SELECT * FROM SalesInvoice  where id =$id.toLong".query[SalesInvoice_TYPE]
  def salesInvoiceIdSelect(id:Long) = sql"SELECT * FROM SalesInvoice  where id =$id".query[SalesInvoice_TYPE]
  def salesInvoiceSelectSome = { id:String =>sql"SELECT * FROM SalesInvoice where id =$id".query[SalesInvoice_TYPE]}
  def salesInvoiceUpdateName= {(model:SalesInvoice[LineSalesInvoice]) =>
    sql"Update SalesInvoice set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def salesInvoiceDelete = {id:Long =>sql"Delete FROM SalesInvoice where id =$id".update}



  def lineCustomerInvoiceInsertSQL = "INSERT INTO LineCustomerInvoice ( id, transid, modelId, account, side ,oaccount, amount,  duedate, text)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def lineCustomerInvoiceSelect = sql"SELECT * FROM LineCustomerInvoice".query[LineCustomerInvoice_TYPE]
  def lineCustomerInvoiceIdSelect1(id:String) = sql"SELECT * FROM LineCustomerInvoice  where id =$id".query[LineCustomerInvoice_TYPE]
  def lineCustomerInvoiceIdSelect(id:Long) = sql"SELECT * FROM LineCustomerInvoice  where transid =$id".query[LineCustomerInvoice_TYPE]
  def lineCustomerInvoiceSelectSome = { id:String =>sql"SELECT * FROM LineCustomerInvoice where id =$id".query[LineCustomerInvoice_TYPE]}
  def lineCustomerInvoiceUpdateName = {(model:LineCustomerInvoice) =>
    sql"Update LineCustomerInvoice set transid=${model.transid},  modelId=${model.modelId}, account=${model.account.get}, side = ${model.side}, oaccount=${model.oaccount.get}, amount=${model.amount}, duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineCustomerInvoiceDelete = {(id:Long) =>sql"Delete FROM LineCustomerInvoice where id =$id".update}

  def customerInvoiceInsertSQL = "INSERT INTO CustomerInvoice (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def customerInvoiceSelect = sql"SELECT * FROM CustomerInvoice".query[CustomerInvoice_TYPE]
  def customerInvoiceIdSelect1(id:String) = sql"SELECT * FROM CustomerInvoice  where id =$id.toLong".query[CustomerInvoice_TYPE]
  def customerInvoiceIdSelect(id:Long) = sql"SELECT * FROM CustomerInvoice  where id =$id".query[CustomerInvoice_TYPE]
  def customerInvoiceSelectSome = { id:String =>sql"SELECT * FROM CustomerInvoice where id =$id".query[CustomerInvoice_TYPE]}
  def customerInvoiceUpdateName= {(model:CustomerInvoice[LineCustomerInvoice]) =>
    sql"Update CustomerInvoice set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def customerInvoiceDelete = {id:Long =>sql"Delete FROM CustomerInvoice where id =$id".update}


  def lineSettlementInsertSQL = "INSERT INTO LineSettlement ( id, transid, modelId, account, side ,oaccount, amount,  duedate, text)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
  def lineSettlementSelect = sql"SELECT * FROM LineSettlement".query[LineSettlement_TYPE]
  def lineSettlementIdSelect1(id:String) = sql"SELECT * FROM LineSettlement  where id =$id".query[LineSettlement_TYPE]
  def lineSettlementIdSelect(id:Long) = sql"SELECT * FROM LineSettlement  where transid =$id".query[LineSettlement_TYPE]
  def lineSettlementSelectSome = { id:String =>sql"SELECT * FROM LineSettlement where id =$id".query[LineSettlement_TYPE]}
  def lineSettlementUpdateName = {(model:LineSettlement) =>
    sql"Update LineSettlement set transid=${model.transid},  modelId=${model.modelId}, account=${model.account.get}, side = ${model.side}, oaccount=${model.oaccount.get}, amount=${model.amount}, duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def lineSettlementDelete = {(id:Long) =>sql"Delete FROM LineSettlement where id =$id".update}


  def settlementInsertSQL = "INSERT INTO Settlement (id, oid, modelId, store, account, text) VALUES (?, ?, ?, ?, ?, ?)"
  def settlementSelect = sql"SELECT * FROM Settlement".query[Settlement_TYPE]
  def settlementIdSelect1(id:String) = sql"SELECT * FROM Settlement  where id =$id.toLong".query[Settlement_TYPE]
  def settlementIdSelect(id:Long) = sql"SELECT * FROM Settlement  where id =$id".query[Settlement_TYPE]
  def settlementSelectSome = { id:String =>sql"SELECT * FROM Settlement where id =$id".query[Settlement_TYPE]}
  def settlementUpdateName= {(model:Settlement[LineSettlement]) =>
    sql"Update Settlement set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def settlementDelete = {id:Long =>sql"Delete FROM Settlement where id =$id".update}



  def getSequence (tablename:String,columnname:String) = sql"SELECT nextval(pg_get_serial_sequence( ${tablename}, ${columnname}))".query[Long]

val MAP:Map[Int, Map[String, Any]] = Map.empty[Int, Map[String, Any]]
  Map(114 -> Map("TABLE" ->"Payment"),
      115 -> Map("TABLE" ->"LinePayment"))
def getTable(modelId:Int) =MAP.get(modelId).get("TABLE")
  val createBank=sql"""
  DROP TABLE IF EXISTS bank;
     CREATE TABLE bank(
    id VARCHAR NOT NULL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    modelId int NOT NULL,
    description     VARCHAR NOT NULL
  );""".update
  val createBankAccount=sql"""
  DROP TABLE IF EXISTS bankAccount;
     CREATE TABLE bankAccount(
    id VARCHAR NOT NULL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    modelId int NOT NULL,
    description     VARCHAR NOT NULL,
    bic VARCHAR NOT NULL,
    debit DECIMAL(20,2),
    credit DECIMAL(20,2)
  );""".update
  val createCompany=sql"""
  DROP TABLE IF EXISTS Company CASCADE;
  CREATE TABLE Company (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    modelId int NOT NULL,
    street   VARCHAR NOT NULL,
    city     VARCHAR NOT NULL,
    state    VARCHAR NOT NULL,
    zip      VARCHAR NOT NULL,
   bankAccountId  VARCHAR NOT NULL,
   purchasingClearingAccountId  VARCHAR NOT NULL,
   salesClearingAccountId  VARCHAR NOT NULL,
   paymentClearingAccountId  VARCHAR NOT NULL,
   settlementClearingAccountId  VARCHAR NOT NULL,
   periode int NOT NULL,
   nextPeriode int NOT NULL,
   taxCode VARCHAR NOT NULL,
   vatCode VARCHAR NOT NULL
  );""".update
  val createSettlement = sql"""
           DROP SEQUENCE IF EXISTS SettlementId_seq;
           CREATE SEQUENCE SettlementId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS Settlement;
           CREATE TABLE Settlement(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR

                );""".update
  val createPayment = sql"""
           DROP SEQUENCE IF EXISTS PaymentId_seq;
           CREATE SEQUENCE PaymentId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS Payment;
           CREATE TABLE Payment(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR

                );""".update
  val createLinePayment=sql"""
           DROP SEQUENCE IF EXISTS LinePaymentId_seq;
           CREATE SEQUENCE LinePaymentId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LinePayment;
           CREATE TABLE LinePayment(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update

  val createLineSettlement=sql"""
           DROP SEQUENCE IF EXISTS LineSettlementId_seq;
           CREATE SEQUENCE LineSettlementId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineSettlement;
           CREATE TABLE LineSettlement(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update

  val createVendorInvoice=sql"""
           DROP SEQUENCE IF EXISTS VendorInvoiceId_seq;
           CREATE SEQUENCE VendorInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS VendorInvoice;
           CREATE TABLE VendorInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createCustomerInvoice=sql"""
           DROP SEQUENCE IF EXISTS CustomerInvoiceId_seq;
           CREATE SEQUENCE CustomerInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS CustomerInvoice;
           CREATE TABLE CustomerInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update

  val createLineVendorInvoice=sql"""
           DROP SEQUENCE IF EXISTS LineVendorInvoiceId_seq;
           CREATE SEQUENCE LineVendorInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineVendorInvoice;
           CREATE TABLE LineVendorInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
  val createLineCustomerInvoice=sql"""
           DROP SEQUENCE IF EXISTS LineCustomerInvoiceId_seq;
           CREATE SEQUENCE LineCustomerInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineCustomerInvoice;
           CREATE TABLE LineCustomerInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update

  val createInventoryInvoice=sql"""
           DROP SEQUENCE IF EXISTS InventoryInvoiceId_seq;
           CREATE SEQUENCE InventoryInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS InventoryInvoice;
           CREATE TABLE InventoryInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createSalesInvoice=sql"""
           DROP SEQUENCE IF EXISTS SalesInvoiceId_seq;
           CREATE SEQUENCE SalesInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS SalesInvoice;
           CREATE TABLE SalesInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createLineInventoryInvoice=sql"""
           DROP SEQUENCE IF EXISTS LineInventoryInvoiceId_seq;
           CREATE SEQUENCE LineInventoryInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineInventoryInvoice;
           CREATE TABLE LineInventoryInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
  val createLineSalesInvoice=sql"""
           DROP SEQUENCE IF EXISTS LineSalesInvoiceId_seq;
           CREATE SEQUENCE LineSalesInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineSalesInvoice;
           CREATE TABLE LineSalesInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
  val createGoodreceiving=sql"""
           DROP SEQUENCE IF EXISTS GoodreceivingId_seq;
           CREATE SEQUENCE GoodreceivingId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS Goodreceiving;
           CREATE TABLE Goodreceiving(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createBillOfDelivery=sql"""
           DROP SEQUENCE IF EXISTS BillOfDeliveryId_seq;
           CREATE SEQUENCE BillOfDeliveryId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS BillOfDelivery;
           CREATE TABLE BillOfDelivery(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createLineGoodreceiving=sql"""
           DROP SEQUENCE IF EXISTS LineGoodreceivingId_seq;
           CREATE SEQUENCE LinePurchaseOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineGoodreceiving;
           CREATE TABLE LineGoodreceiving(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
  val createLineBillOfDelivery=sql"""
           DROP SEQUENCE IF EXISTS LineBillOfDeliveryId_seq;
           CREATE SEQUENCE LineBillOfDeliveryId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineBillOfDelivery;
           CREATE TABLE LineBillOfDelivery(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
 val createPurchaseOrder1=sql"""
           DROP SEQUENCE IF EXISTS PurchaseOrderId_seq;
           CREATE SEQUENCE PurchaseOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS PurchaseOrder;
           CREATE TABLE PurchaseOrder(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createSalesOrder1=sql"""
           DROP SEQUENCE IF EXISTS SalesOrderId_seq;
           CREATE SEQUENCE SalesOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS SalesOrder;
           CREATE TABLE SalesOrder(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );""".update
  val createLinePurchaseOrder=sql"""
           DROP SEQUENCE IF EXISTS LinePurchaseOrderId_seq;
           CREATE SEQUENCE LinePurchaseOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LinePurchaseOrder;
           CREATE TABLE LinePurchaseOrder(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL, 
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update
  val createLineSalesOrder=sql"""
           DROP SEQUENCE IF EXISTS LineSalesOrderId_seq;
           CREATE SEQUENCE LineSalesOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineSalesOrder;
           CREATE TABLE LineSalesOrder(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );""".update

  val createAccount=sql"""
           DROP TABLE IF EXISTS account;
           CREATE TABLE account(
                id VARCHAR NOT NULL PRIMARY KEY,
                name   VARCHAR NOT NULL,
                modelId int NOT NULL,
                description     VARCHAR NOT NULL,
                groupid  VARCHAR NOT NULL,
                dateOfOpen      DATE NOT NULL,
                dateOfClose     DATE NOT NULL,
                balance   DECIMAL(20,2) NOT NULL DEFAULT 0.0
                );""".update

  val createArticle=sql"""
               DROP TABLE IF EXISTS article;
              CREATE TABLE article(
              id VARCHAR NOT NULL PRIMARY KEY,
              name   VARCHAR NOT NULL,
              modelId int NOT NULL,
              description     VARCHAR NOT NULL,
              price  DECIMAL(20,2) NOT NULL,
              qttyUnit VARCHAR NOT NULL,
              packUnit VARCHAR NOT NULL,
              groupid  VARCHAR NOT NULL
               );""".update
  val createCostCenter=sql"""
  DROP TABLE IF EXISTS costcenter;
     CREATE TABLE costcenter(
    id VARCHAR NOT NULL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    modelId int NOT NULL,
    description     VARCHAR NOT NULL
  );""".update
  val createCategory=sql"""
  DROP TABLE IF EXISTS category;
     CREATE TABLE category(
    id VARCHAR NOT NULL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    modelId int NOT NULL,
    description     VARCHAR NOT NULL
  );""".update
  val createQuantityUnit=sql"""
  DROP TABLE IF EXISTS quantityUnit;
     CREATE TABLE quantityUnit(
    id VARCHAR NOT NULL PRIMARY KEY,
    name   VARCHAR NOT NULL,
    modelId int NOT NULL,
    description     VARCHAR NOT NULL
  );""".update

  val createSupplier=sql"""
  DROP TABLE IF EXISTS supplier CASCADE;
  CREATE TABLE supplier (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    modelId int NOT NULL,
    street   VARCHAR NOT NULL,
    city     VARCHAR NOT NULL,
    state    VARCHAR NOT NULL,
    zip      VARCHAR NOT NULL
  );""".update

  val createCustomer=sql"""
  DROP TABLE IF EXISTS Customer CASCADE;
  CREATE TABLE Customer (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    modelId int NOT NULL,
    street   VARCHAR NOT NULL,
    city     VARCHAR NOT NULL,
    state    VARCHAR NOT NULL,
    zip      VARCHAR NOT NULL
  );""".update

  val createStore=sql"""
  DROP TABLE IF EXISTS store CASCADE;
  CREATE TABLE store (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    modelId int NOT NULL,
    street   VARCHAR NOT NULL,
    city     VARCHAR NOT NULL,
    state    VARCHAR NOT NULL,
    zip      VARCHAR NOT NULL
  );""".update

  val createVat=sql"""
  DROP TABLE IF EXISTS vat CASCADE;
  CREATE TABLE vat (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR NOT NULL,
    modelId int NOT NULL,
    description   VARCHAR NOT NULL,
    percent       DECIMAL(5,2) NOT NULL,
   inputVatAccountId VARCHAR NOT NULL,
   outputVatAccountId VARCHAR NOT NULL,
   revenueAccountId VARCHAR NOT NULL,
   stockAccountId VARCHAR NOT NULL,
   expenseAccountId VARCHAR NOT NULL
  );""".update


  val createStock=sql"""
  DROP TABLE IF EXISTS stock CASCADE;
  CREATE TABLE stock (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR DEFAULT '',
    modelId int NOT NULL,
    description   VARCHAR DEFAULT '',
   itemId       VARCHAR  NOT NULL,
   storeId      VARCHAR NOT NULL,
    quantity DECIMAL(8,2) NOT NULL,
    minStock  DECIMAL (8, 2) DEFAULT 0.0
  );""".update

  val createPeriodicAccountBalance = sql"""
  DROP TABLE IF EXISTS PeriodicAccountBalance CASCADE;
  CREATE TABLE PeriodicAccountBalance (
    id   VARCHAR     NOT NULL PRIMARY KEY,
    name VARCHAR DEFAULT '',
    modelId int NOT NULL,
    description   VARCHAR DEFAULT '',
   accountId       VARCHAR  NOT NULL,
   periode      int NOT NULL,
   debit DECIMAL(8,2) NOT NULL,
   credit  DECIMAL (8, 2)  NOT NULL
  );""".update

  val createSchema =sql"""
           DROP SEQUENCE IF EXISTS PaymentId_seq;
           CREATE SEQUENCE PaymentId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS Payment;
           CREATE TABLE Payment(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR

                );
           DROP SEQUENCE IF EXISTS LinePaymentId_seq;
           CREATE SEQUENCE LinePaymentId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LinePayment;
           CREATE TABLE LinePayment(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );

           DROP SEQUENCE IF EXISTS VendorInvoiceId_seq;
           CREATE SEQUENCE VendorInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS VendorInvoice;
           CREATE TABLE VendorInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );

           DROP SEQUENCE IF EXISTS LineVendorInvoiceId_seq;
           CREATE SEQUENCE LineVendorInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineVendorInvoice;
           CREATE TABLE LineVendorInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                account   VARCHAR NOT NULL,
                side VARCHAR NOT NULL,
                oaccount   VARCHAR NOT NULL,
                amount  DECIMAL(20,2) NOT NULL,
                duedate      DATE NOT NULL,
                text  VARCHAR
                );

      DROP TABLE IF EXISTS Company CASCADE;
      CREATE TABLE Company (
        id   VARCHAR     NOT NULL PRIMARY KEY,
        name VARCHAR NOT NULL,
        modelId int NOT NULL,
        street   VARCHAR NOT NULL,
        city     VARCHAR NOT NULL,
        state    VARCHAR NOT NULL,
        zip      VARCHAR NOT NULL,
        bankAccountId  VARCHAR NOT NULL,
        purchasingClearingAccountId  VARCHAR NOT NULL,
        salesClearingAccountId  VARCHAR NOT NULL,
        paymentClearingAccountId  VARCHAR NOT NULL,
        settlementClearingAccountId  VARCHAR NOT NULL,
        periode int NOT NULL,
        nextPeriode int NOT NULL,
        taxCode VARCHAR NOT NULL,
        vatCode VARCHAR NOT NULL
      );
        DROP TABLE IF EXISTS bank;
        CREATE TABLE bank(
         id VARCHAR NOT NULL PRIMARY KEY,
          name   VARCHAR NOT NULL,
          modelId int NOT NULL,
          description VARCHAR NOT NULL
          );
        DROP TABLE IF EXISTS bankAccount;
        CREATE TABLE bankAccount(
          id VARCHAR NOT NULL PRIMARY KEY,
          name   VARCHAR NOT NULL,
          modelId int NOT NULL,
          description VARCHAR NOT NULL,
          bic VARCHAR NOT NULL,
          debit DECIMAL(20,2),
          credit DECIMAL(20,2)
         );
  DROP TABLE IF EXISTS stock CASCADE;
      CREATE TABLE stock (
        id   VARCHAR     NOT NULL PRIMARY KEY,
        name VARCHAR,
        modelId int NOT NULL,
        description   VARCHAR,
        itemId       VARCHAR  NOT NULL,
        storeId      VARCHAR NOT NULL,
        quantity DECIMAL(8,2) NOT NULL,
        minStock  DECIMAL (8, 2) DEFAULT 0.0
      );

      DROP TABLE IF EXISTS PeriodicAccountBalance CASCADE;
      CREATE TABLE PeriodicAccountBalance (
        id   VARCHAR     NOT NULL PRIMARY KEY,
        name VARCHAR,
        modelId int NOT NULL,
        description   VARCHAR DEFAULT "",
        accountId       VARCHAR  NOT NULL,
        periode      int NOT NULL,
        debit DECIMAL(8,2) NOT NULL,
        credit  DECIMAL (8, 2)  NOT NULL
        );

           DROP TABLE IF EXISTS account;
           CREATE TABLE account(
                id VARCHAR NOT NULL PRIMARY KEY,
                name   VARCHAR NOT NULL,
                modelId int NOT NULL,
                description     VARCHAR NOT NULL,
                groupid  VARCHAR NOT NULL,
                dateOfOpen      DATE NOT NULL,
                dateOfClose     DATE NOT NULL,
                balance   DECIMAL(20,2) NOT NULL DEFAULT 0.0
                );
           DROP TABLE IF EXISTS article;
           CREATE TABLE article(
                  id VARCHAR NOT NULL PRIMARY KEY,
                  name   VARCHAR NOT NULL,
                  modelId int NOT NULL,
                  description     VARCHAR NOT NULL,
                  price  DECIMAL(20,2) NOT NULL,
                  qttyUnit VARCHAR NOT NULL,
                  packUnit VARCHAR NOT NULL,
                  groupid  VARCHAR NOT NULL
                );
         DROP TABLE IF EXISTS quantityUnit;
         CREATE TABLE quantityUnit(
                     id VARCHAR NOT NULL PRIMARY KEY,
                     name   VARCHAR NOT NULL,
                     modelId int NOT NULL,
                     description     VARCHAR NOT NULL
                   );
         DROP TABLE IF EXISTS category;
         CREATE TABLE category(
                  id VARCHAR NOT NULL PRIMARY KEY,
                  name   VARCHAR NOT NULL,
                  modelId int NOT NULL,
                  description     VARCHAR NOT NULL
                 );
        DROP TABLE IF EXISTS supplier CASCADE;
        CREATE TABLE supplier (
          id   VARCHAR     NOT NULL PRIMARY KEY,
          name VARCHAR NOT NULL,
          modelId int NOT NULL,
          accountId VARCHAR NOT NULL,
          street   VARCHAR NOT NULL,
          city     VARCHAR NOT NULL,
          state    VARCHAR NOT NULL,
          zip      VARCHAR NOT NULL        
        );
       DROP TABLE IF EXISTS customer CASCADE;
       CREATE TABLE customer (
         id   VARCHAR     NOT NULL PRIMARY KEY,
         name VARCHAR NOT NULL,
         modelId int NOT NULL,
         accountId VARCHAR NOT NULL,
         street   VARCHAR NOT NULL,
         city     VARCHAR NOT NULL,
         state    VARCHAR NOT NULL,
         zip      VARCHAR NOT NULL
         );
       DROP TABLE IF EXISTS store CASCADE;
       CREATE TABLE store (
         id   VARCHAR     NOT NULL PRIMARY KEY,
         name VARCHAR NOT NULL,
         modelId int NOT NULL,
         accountId VARCHAR NOT NULL,
         street   VARCHAR NOT NULL,
         city     VARCHAR NOT NULL,
         state    VARCHAR NOT NULL,
         zip      VARCHAR NOT NULL
         );
        DROP TABLE IF EXISTS vat CASCADE;
        CREATE TABLE vat (
          id   VARCHAR     NOT NULL PRIMARY KEY,
          name VARCHAR NOT NULL,
          modelId int NOT NULL,
          description   VARCHAR NOT NULL,
          percent       DECIMAL(5,2) NOT NULL,
          inputVatAccountId VARCHAR NOT NULL,
           outputVatAccountId VARCHAR NOT NULL,
           revenueAccountId VARCHAR NOT NULL,
           stockAccountId VARCHAR NOT NULL,
           expenseAccountId VARCHAR NOT NULL
         );

           DROP SEQUENCE IF EXISTS PurchaseOrderId_seq;
           CREATE SEQUENCE PurchaseOrderId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS PurchaseOrder;
           CREATE TABLE PurchaseOrder(
                id bigserial NOT NULL PRIMARY KEY,
                oid bigint NOT NULL ,
                modelId int NOT NULL,
                store   VARCHAR NOT NULL,
                account   VARCHAR NOT NULL,
                text  VARCHAR
                );

     DROP SEQUENCE IF EXISTS LinePurchaseOrderId_seq;
     CREATE SEQUENCE LinePurchaseOrderId_seq
        INCREMENT 1
        MINVALUE 1
        MAXVALUE 9223372036854775807
        START 1
        CACHE 1;
        DROP TABLE IF EXISTS LinePurchaseOrder;
     CREATE TABLE LinePurchaseOrder(
         id bigserial NOT NULL PRIMARY KEY,
         transid     bigint  NOT NULL ,
         modelId int NOT NULL,
         item   VARCHAR NOT NULL,
         unit   VARCHAR NOT NULL,
         price  DECIMAL(20,2) NOT NULL,
         quantity    DECIMAL(20,2) NOT NULL,
         vat    VARCHAR NOT NULL,
         duedate      DATE NOT NULL,
         text  VARCHAR
       );

           DROP SEQUENCE IF EXISTS InventoryInvoice_seq;
           CREATE SEQUENCE InventoryInvoice_seq
                  INCREMENT 1
                  MINVALUE 1
                  MAXVALUE 9223372036854775807
                  START 1
                  CACHE 1;
                  DROP TABLE IF EXISTS InventoryInvoice;
                  CREATE TABLE InventoryInvoice(
                       id bigserial NOT NULL PRIMARY KEY,
                       oid bigint NOT NULL ,
                       modelId int NOT NULL,
                       store   VARCHAR NOT NULL,
                       account   VARCHAR NOT NULL,
                       text  VARCHAR
                 );

           DROP SEQUENCE IF EXISTS LineInventoryInvoiceId_seq;
           CREATE SEQUENCE LineInventoryInvoiceId_seq
             INCREMENT 1
             MINVALUE 1
             MAXVALUE 9223372036854775807
             START 1
             CACHE 1;
           DROP TABLE IF EXISTS LineInventoryInvoice;
           CREATE TABLE LineInventoryInvoice(
                id bigserial NOT NULL PRIMARY KEY,
                transid     bigint  NOT NULL ,
                modelId int NOT NULL,
                item   VARCHAR NOT NULL,
                unit   VARCHAR NOT NULL,
                price  DECIMAL(20,2) NOT NULL,
                quantity    DECIMAL(20,2) NOT NULL,
                vat    VARCHAR NOT NULL, 
                duedate      DATE NOT NULL,
                text  VARCHAR
                );

                       |DROP TABLE IF EXISTS Settlement
                       |CREATE TABLE Settlement(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  oid bigint NOT NULL ,
                       |  modelId int NOT NULL,
                       |  store   VARCHAR NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  text  VARCHAR
                       |
 |)
                       |DROP SEQUENCE IF EXISTS SettlementId_seq
                       |CREATE SEQUENCE SettlementId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS LineSettlement
                       |CREATE TABLE LineSettlement(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  transid     bigint  NOT NULL ,
                       |  modelId int NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  side VARCHAR NOT NULL,
                       |  oaccount   VARCHAR NOT NULL,
                       |  amount  DECIMAL(20,2) NOT NULL,
                       |  duedate      DATE NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS LineSettlementId_seq
                       |CREATE SEQUENCE LineSettlementId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS CustomerInvoice
                       |CREATE TABLE CustomerInvoice(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  oid bigint NOT NULL ,
                       |  modelId int NOT NULL,
                       |  store   VARCHAR NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS CustomerInvoiceId_seq
                       |CREATE SEQUENCE CustomerInvoiceId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS LineCustomerInvoice
                       |CREATE TABLE LineCustomerInvoice(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  transid     bigint  NOT NULL ,
                       |  modelId int NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  side VARCHAR NOT NULL,
                       |  oaccount   VARCHAR NOT NULL,
                       |  amount  DECIMAL(20,2) NOT NULL,
                       |  duedate      DATE NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS LineCustomerInvoiceId_seq
                       |CREATE SEQUENCE LineCustomerInvoiceId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS SalesInvoice
                       |CREATE TABLE SalesInvoice(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  oid bigint NOT NULL ,
                       |  modelId int NOT NULL,
                       |  store   VARCHAR NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS SalesInvoiceId_seq
                       |CREATE SEQUENCE SalesInvoiceId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS LineSalesInvoice
                       |CREATE TABLE LineSalesInvoice(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  transid     bigint  NOT NULL ,
                       |  modelId int NOT NULL,
                       |  item   VARCHAR NOT NULL,
                       |  unit   VARCHAR NOT NULL,
                       |  price  DECIMAL(20,2) NOT NULL,
                       |  quantity    DECIMAL(20,2) NOT NULL,
                       |  vat    VARCHAR NOT NULL,
                       |  duedate      DATE NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS LineSalesInvoiceId_seq
                       |CREATE SEQUENCE LineSalesInvoiceId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |
 |DROP TABLE IF EXISTS BillOfDelivery
                       |CREATE TABLE BillOfDelivery(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  oid bigint NOT NULL ,
                       |  modelId int NOT NULL,
                       |  store   VARCHAR NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  text  VARCHAR
                       |)
                       |
 |DROP SEQUENCE IF EXISTS BillOfDeliveryId_seq
                       |CREATE SEQUENCE BillOfDeliveryId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |
 |DROP TABLE IF EXISTS LineBillOfDelivery
                       |CREATE TABLE LineBillOfDelivery(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  transid     bigint  NOT NULL ,
                       |  modelId int NOT NULL,
                       |  item   VARCHAR NOT NULL,
                       |  unit   VARCHAR NOT NULL,
                       |  price  DECIMAL(20,2) NOT NULL,
                       |  quantity    DECIMAL(20,2) NOT NULL,
                       |  vat    VARCHAR NOT NULL,
                       |  duedate      DATE NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS LineBillOfDeliveryId_seq
                       |CREATE SEQUENCE LineBillOfDeliveryId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |
 |DROP TABLE IF EXISTS SalesOrder
                       |CREATE TABLE SalesOrder(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  oid bigint NOT NULL ,
                       |  modelId int NOT NULL,
                       |  store   VARCHAR NOT NULL,
                       |  account   VARCHAR NOT NULL,
                       |  text  VARCHAR
                       |)
                       |
 |DROP SEQUENCE IF EXISTS SalesOrderId_seq
                       |CREATE SEQUENCE SalesOrderId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
                       |DROP TABLE IF EXISTS LineSalesOrder
                       |CREATE TABLE LineSalesOrder(
                       |  id bigserial NOT NULL PRIMARY KEY,
                       |  transid     bigint  NOT NULL ,
                       |  modelId int NOT NULL,
                       |  item   VARCHAR NOT NULL,
                       |  unit   VARCHAR NOT NULL,
                       |  price  DECIMAL(20,2) NOT NULL,
                       |  quantity    DECIMAL(20,2) NOT NULL,
                       |  vat    VARCHAR NOT NULL,
                       |  duedate      DATE NOT NULL,
                       |  text  VARCHAR
                       |)
                       |DROP SEQUENCE IF EXISTS LineSalesOrderId_seq
                       |CREATE SEQUENCE LineSalesOrderId_seq
                       |INCREMENT 1
                       |MINVALUE 1
                       |MAXVALUE 9223372036854775807
                       |START 1
                       |CACHE 1
    """

  /*
insert into account values ('100','IWS Masterfiles','IWS Masterfiles','2015/01/01','2015/12/31',0.0);
insert into account values ('200','IWS Sales','IWS Sales','2015/01/01','2015/12/31',0.0);
insert into account values ('300','IWS Purchasing','IWS Purchasing','2015/01/01','2015/12/31',0.0);
insert into account values ('400','IWS Inventory','IWS Inventory','2015/01/01','2015/12/31',0.0);
insert into account values ('500','IWS Financials','IWS Financials','2015/01/01','2015/12/31',0.0);
insert into account values ('600','IWS Statistics','IWS Statistics','2015/01/01','2015/12/31',0.0);

INSERT INTO Article VALUES('001','Masterfile', 7, 'Financials Application for Enterprise',5000.0,'Stk');
INSERT INTO Article VALUES('002','Inventory', 7, 'Financials Application for Enterprise',5000.0,'Stk');
INSERT INTO Article VALUES('003','Purchasing',7,  'Financials Application for Enterprise',5000.0,'Stk');
INSERT INTO Article VALUES('004','CRM', 7, 'Financials Application for Enterprise',5000.0,'Stk');
INSERT INTO Article VALUES('005','Financials',7, 'Financials Application for Enterprise',5000.0,'Stk');
INSERT INTO Article VALUES('006','Analytics',7, 'Enterprise Analytics & Decision support for Management',50000.0,'Stk');

insert into store values ('100','Lager Bielefeld','Am Schwarzbach1','Bielefeld','NRW','33739');
insert into store values ('200','Lager Frankfurt','Mainzer Landstrasse 1 ','Frankfurt','Hessen','69800');

insert into customer values ('5000','Studentenwerk Bielefeld AoR',3,'Universitaetstr. 25','Bielefeld','NRW','33619');
insert into  customer values ('50001','Studentenwerk Siegen AoR',3,'Universitaetstr 1 ','Siegen','NRW','58001');
insert into  customer values ('50002','Studentenwerk Duesseldorf AoR',3,'Universitaetstr .1 ','Duesseldorf','NRW','69800');
insert into  customer values ('50003','Chemec GmbH',3,'Meisen Str 96','Bielefeld','NRW','33607');

insert into  supplier values ('7000','Hardkernel INC',3,'Universitaetstr. 25','Bielefeld','NRW','33619');
insert into  supplier values ('70001','Gulp Informationsservices GmbH',3,'Universitaetstr 1 ','Frankfurt am Main','Hessen','66901');
insert into  supplier values ('70002','Metro AG',3,'Universitaetstr .1 ','Duesseldorf','NRW','69800');
insert into  supplier values ('70003','Chemec GmbH',3,'Meisen Str 96','Bielefeld','NRW','33607');


INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1001000', '', 107, '', '1000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1002000', '', 107, '', '2000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1004000', '', 107, '', '4000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1007000', '', 107, '', '7000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1005000', '', 107, '', '5000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('1006000', '', 107, '', '6000', '100', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2001000', '', 107, '', '1000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2002000', '', 107, '', '2000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2003000', '', 107, '', '3000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2004000', '', 107, '', '4000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2005000', '', 107, '', '5000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)   VALUES ('2006000', '', 107, '', '6000', '200', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3001000', '', 107, '', '1000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3002000', '', 107, '', '2000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3003000', '', 107, '', '3000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3004000', '', 107, '', '4000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3005000', '', 107, '', '5000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3006000', '', 107, '', '6000', '300', 0.0, 0.0);
INSERT INTO stock(id, name, modelid, description, itemid, storeid, quantity, minstock)    VALUES ('3007000', '', 107, '', '7000', '300', 0.0, 0.0);

insert into category values ('001','Mineralwasser',8,'Mineralwasser');
insert into category values ('002','Software',8,'Software');
insert into category values ('003','Hardware',8,'Hardware');

insert into vat values ('0','0% Mwst','0% Mwst',0);
insert into vat values ('7','7% Mwst','7% Mwst',7);
insert into vat values ('19','19% Mwst','19% Mwst',19);

INSERT linePOrder VALUES (nextval('LinePOrderId_seq'), ?,?,?,?,?,?,?,?)"
INSERT INTO PurchaseOrder (id, oid, modelId, store, account) VALUES (0,101,'200','1000');
insert into LinePurchaseOrder (transid,modelId,item,unit,price,quantity,vat,duedate,text)VALUES (1,101,'001','Stk',1000,100,'19','2015/12/31','TEST');


INSERT INTO linePOrder VALUES (nextval('POrderId',"800"), 'nothing');
*/

}
