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
import com.kabasoft.iws.shared.Model._

object Queries  {

  def create: Update0 = createSchema.update
  def accountInsertSQL= "INSERT INTO account VALUES (?, ?, ?, ?, ?,?,?)"
  def accountSelect =  sql"SELECT id, name, modelId, description,dateOfOpen, dateOfClose, balance FROM account".query[Account]
  def accountIdSelect(id:String) = sql"SELECT * FROM account where id =$id".query[Account]
  def accountSelectSome = {id:String =>sql"SELECT * FROM account where id =$id".query[Account]}
  def accountUpdateName = {(model:Account) =>sql"Update account set name =${model.name},description=${model.description} where id =${model.id}".update}
  def accountDelete = {id:String =>sql"Delete FROM account where id =$id".update}

  def articleInsertSQL= "INSERT INTO article VALUES (?, ?, ?, ?, ?, ?)"
  def articleSelect = sql"SELECT id, name, modelId, description,price, qtty_id FROM article".query[Article]
  def articleIdSelect(id:String) = sql"SELECT * FROM article where id =$id".query[Article]
  def articleWithQtyUnit(id:String) = sql"SELECT a.id, a.name, a.description,a.price,a.qtty_id FROM article a LEFT JOIN  QuantityUnit q ON q.id = a.qtty_id and a.id=$id".query[Article]
  def articleSelectSome = {id:String =>sql"SELECT * FROM article  where id =$id".query[Article]}
  def articleUpdateName= {(model:Article) =>sql"Update article set name =${model.name}, description =${model.description},price =${model.price},  qtty_id =${model.qtty_id}    where id =${model.id}".update}
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

  def customerInsertSQL = "INSERT INTO customer VALUES (?, ?,?, ?, ?, ?, ?)"
  def customerSelect = sql"SELECT id, name, modelId, street, city, state,zip FROM customer".query[Customer]
  def customerIdSelect(id:String)  = sql"SELECT * FROM customer where id =$id".query[Customer]
  def customerSelect1 = sql"SELECT id, name, street, city, state, zip FROM customer".query[Customer]
  def customerSelectSome  = { id:String =>sql"SELECT * FROM customer where id =$id".query[Customer]}
  def customerUpdateName= {(model:Customer) =>sql"Update customer set  name =${model.name},street=${model.street}, city=${model.city}, zip=${model.zip}, state =${model.state}where id =${model.id}".update}
  def customerDelete = {id:String =>sql"Delete FROM customer where id =$id".update}

  def supplierInsertSQL = "INSERT INTO suppliers VALUES (?, ?, ?, ?, ?, ?, ?)"
  def supplierSelect = sql"SELECT id, name, modelId, street, city, state,zip FROM supplier".query[Supplier]
  def supplierIdSelect(id:String)  = sql"SELECT * FROM supplier where id =$id".query[Supplier]
  def supplierSelect1 = sql"SELECT id, name, street, city, state, zip FROM supplier".query[Supplier]
  def supplierSelectSome  = { id:String =>sql"SELECT * FROM supplier where id =$id".query[Supplier]}
  def supplierUpdateName= {(model:Supplier) =>sql"Update supplier set  name =${model.name},street=${model.street}, city=${model.city}, zip=${model.zip}, state =${model.state} where id =${model.id}".update}
  def supplierDelete = {id:String =>sql"Delete FROM supplier where id =$id".update}

  def storeInsertSQL = "INSERT INTO store VALUES (?, ?, ?, ?, ?, ?, ?)"
  def storeSelect = sql"SELECT id, name, modelId, street, city, state,zip FROM store".query[Store]
  def storeIdSelect(id:String)  = sql"SELECT * FROM store where id =$id".query[Store]
  def storeSelect1 = sql"SELECT id, name, street, city, state, zip FROM store".query[Store]
  def storeSelectSome  = { id:String =>sql"SELECT * FROM store where id =$id".query[Store]}
  def storeUpdateName= {(model:Store) =>sql"Update store  set name =${model.name},street=${model.street}, city=${model.city},zip=${model.zip}, state =${model.state} where id =${model.id}".update}
  def storeDelete = {id:String =>sql"Delete FROM store where id =$id".update}

  def categoryInsertSQL = "INSERT INTO category  VALUES (?, ?, ?, ?)"
  def categorySelect = sql"SELECT id, name, modelId, description FROM category".query[ArticleGroup]
  def categoryIdSelect(id:String)  = sql"SELECT * FROM category where id =$id".query[ArticleGroup]
  def categorySelect1 = sql"SELECT id, name, description FROM category ".query[ArticleGroup]
  def categorySelectSome  = { id:String =>sql"SELECT * FROM category where id =$id".query[ArticleGroup]}
  def categoryUpdateName= {(model:ArticleGroup) =>sql"Update category set  name =${model.name}, description=${model.description} where id =${model.id}".update}
  def categoryDelete = {id:String =>sql"Delete FROM category where id =$id".update}

  def vatInsertSQL = "INSERT INTO vat VALUES (?, ?, ?, ?, ?)"
  def vatSelect = sql"SELECT id, name, modelId, description, percent FROM vat".query[Vat]
  def vatIdSelect(id:String)  = sql"SELECT * FROM vat where id =$id".query[Vat]
  def vatSelect1 = sql"SELECT id, name, description, percent FROM vat".query[Vat]
  def vatSelectSome  = { id:String =>sql"SELECT * FROM vat where id =$id".query[Vat]}
  def vatUpdateName= {(model:Vat) =>sql"Update vat set  name =${model.name}, description=${model.description} where id =${model.id}".update}
  def vatDelete = {id:String =>sql"Delete FROM vat where id =$id".update}
  type LinePurchaseOrder_TYPE = (Long,Long,Int, String,String,BigDecimal,BigDecimal,String, Date, String)
  def linePurchaseOrderInsertSQL = "INSERT INTO LinePurchaseOrder ( id, transid, modelId, item, unit,price, quantity, vat, duedate, text)  VALUES (?, ?, ?,?,?,?,?,?,?,?)"
  def linePurchaseOrderSelect = sql"SELECT * FROM LinePurchaseOrder".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderIdSelect1(id:String) = sql"SELECT * FROM LinePurchaseOrder  where id =$id".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderIdSelect(id:Long) = sql"SELECT * FROM LinePurchaseOrder  where transid =$id".query[LinePurchaseOrder_TYPE]
  def linePurchaseOrderSelectSome = { id:String =>sql"SELECT * FROM LinePurchaseOrder where id =$id".query[LinePurchaseOrder_TYPE]}
  def linePurchaseOrderUpdateName = {(model:LinePurchaseOrder) =>
    sql"Update LinePurchaseOrder set transid=${model.transid}, item=${model.item.get}, modelId=${model.modelId}, unit=${model.unit.get},quantity = ${model.quantity}, price=${model.price},vat=${model.vat.get},duedate=${model.duedate.get}, text =${model.text} where id =${model.tid}".update}
  def linePurchaseOrderDelete = {(id:Long) =>sql"Delete FROM LinePurchaseOrder where id =$id".update}
  //INSERT INTO PurchaseOrder ( oid, modelId, store, account) VALUES (0,101,'200','1000');
  def purchaseOrderInsertSQL = "INSERT INTO PurchaseOrder (id, oid, modelId, store, account) VALUES (?, ?, ?,?,?)"
  def purchaseOrderSelect = sql"SELECT * FROM PurchaseOrder".query[(Long,Long, Int, String,String)]
  def purchaseOrderIdSelect1(id:String) = sql"SELECT * FROM PurchaseOrder  where id =$id.toLong".query[(Long,Long,Int, String,String)]
  def purchaseOrderIdSelect(id:Long) = sql"SELECT * FROM PurchaseOrder  where id =$id".query[(Long,Long,Int, String,String)]
  def purchaseOrderSelectSome = { id:String =>sql"SELECT * FROM PurchaseOrder where id =$id".query[(Long,Long, Int, String,String)]}
  def purchaseOrderUpdateName= {(model:PurchaseOrder[LinePurchaseOrder]) =>
    sql"Update PurchaseOrder set oid =${model.oid}, store=${model.store.get}, account=${model.account.get} where id =${model.tid}".update}
  def purchaseOrderDelete = {id:Long =>sql"Delete FROM PurchaseOrder where id =$id".update}
  def getSequence (tablename:String,columnname:String) = sql"SELECT nextval(pg_get_serial_sequence( ${tablename}, ${columnname}))".query[Long]

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
                account   VARCHAR NOT NULL

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

  val createAccount=sql"""
           DROP TABLE IF EXISTS account;
           CREATE TABLE account(
                id VARCHAR NOT NULL PRIMARY KEY,
                name   VARCHAR NOT NULL,
                modelId int NOT NULL,
                description     VARCHAR NOT NULL,
                dateOfOpen      DATE NOT NULL,
                dateOfClose     DATE NOT NULL,
                balance   DECIMAL(20,2) NOT NULL
                );""".update

  val createArticle=sql"""
               DROP TABLE IF EXISTS article;
              CREATE TABLE article(
              id VARCHAR NOT NULL PRIMARY KEY,
              name   VARCHAR NOT NULL,
              modelId int NOT NULL,
              description     VARCHAR NOT NULL,
              price  DECIMAL(20,2) NOT NULL,
              qtty_id VARCHAR NOT NULL
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
    percent       DECIMAL(5,2) NOT NULL
  );""".update


  val createSchema =sql"""
           DROP TABLE IF EXISTS account;
           CREATE TABLE account(
                id VARCHAR NOT NULL PRIMARY KEY,
                name   VARCHAR NOT NULL,
                modelId int NOT NULL,
                description     VARCHAR NOT NULL,
                dateOfOpen      DATE NOT NULL,
                dateOfClose     DATE NOT NULL,
                balance   DECIMAL(20,2) NOT NULL
                );
           DROP TABLE IF EXISTS article;
           CREATE TABLE article(
                  id VARCHAR NOT NULL PRIMARY KEY,
                  name   VARCHAR NOT NULL,
                  modelId int NOT NULL,
                  description     VARCHAR NOT NULL,
                  price  DECIMAL(20,2) NOT NULL,
                  qtty_id VARCHAR NOT NULL
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
          percent       DECIMAL(5,2) NOT NULL
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
                account   VARCHAR NOT NULL

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
