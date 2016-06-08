package com.kabasoft.iws.services

import com.kabasoft.iws.shared._
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.services.run._
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.services.Request._
import com.kabasoft.iws.shared.DAO
 class DAOService  extends DAO[Account] {

   /*
  implicit val findAccount = new Find[com.kabasoft.iws.shared.Model.Account]("100")
  implicit val findAll = new FindAll[com.kabasoft.iws.shared.Model.Account]
  implicit val findSomeAccount = new FindSome[com.kabasoft.iws.shared.Model.Account]("200")
  implicit val createAccount = new Create[com.kabasoft.iws.shared.Model.Account]
  //implicit val insertAccount = new Insert[com.kabasoft.iws.shared.Model.Account](accounts)
  implicit val updateAccount = new Update[com.kabasoft.iws.shared.Model.Account]("500", "Account 500")
  implicit val deleteAccount = new Delete[com.kabasoft.iws.shared.Model.Account]("500")
   */



  override def create ():Int = run.runG[Int](Create[Account])
  override def insert(model:List[Account]) = run.runG[Int](Insert[Account](model))
  //override def insert(model:List[Account]) = 1
  override def all:List[Account] = run.runG[List[Account]](FindAll[Account])
  override def find(id:String) :List[Account]= run.runG[List[Account]](Find[Account] (id))
  override def update(item:Account) :Int= run.runG[Int](Update[Account](item))
  override def findSome(id:String) :List[Account]= run.runG[List[Account]](FindSome[Account](id))
  override def findSome1(id:Long) :List[Account]= run.runG[List[Account]](FindSome1[Account](id))
  override def delete(id:String) :Int= run.runG[Int](Delete[Account](id))
  // def create = run.runF.run(Create[com.kabasoft.iws.shared.Model.Account](DAO.accountDAO))
  // def insert(model:List[Account]) = run.runF.run(Insert[com.kabasoft.iws.shared.Model.Account](model, DAO.accountDAO))
   // def all = run.runF.run(new FindAll[com.kabasoft.iws.shared.Model.Account](DAO.accountDAO))
   //def find(id:String) = run.runF.run(new Find[com.kabasoft.iws.shared.Model.Account, String]("a2345", DAO.accountDAO))
   // def findSome(id:String) = run.runF.run(new FindSome[com.kabasoft.iws.shared.Model.Account, String]("a2345", DAO.accountDAO))
  // def update(id:String,name:String)= run.runF.run(new FindAll[com.kabasoft.iws.shared.Model.Account](DAO.accountDAO))
  // def delete(id: String)= run.runF.run(new Delete[com.kabasoft.iws.shared.Model.Account](id, DAO.accountDAO))



/*
  implicit val findAccount = new Find[Account, String]("a2345", DAO.accountDAO)
  implicit val findSomeAccount = new FindSome[Account, String]("a4567", DAO.accountDAO)
  implicit val findArticle = new Find[Article, String]("001", DAO.articleDAO)
  println(run.runF.run(createAccount)+ " account droped and created !")
  println(run.runF.run(insertAccount)+ " object(s) inserted !")
  println(run.runF.run(findAllAccount)+ " object(s) found !")
  println(run.runF.run(findAccount)+ " object(s) found !")
  println(run.runF.run(findSomeAccount)+ " object(s) found !")
  println(run.runF.run(findArticle)+ " object(s) found !")
 */

}
