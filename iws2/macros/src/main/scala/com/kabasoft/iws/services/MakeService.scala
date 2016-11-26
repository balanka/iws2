package com.kabasoft.iws.services

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.shared.DAO
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.services.Request._

object MakeService {


  def make[T]: DAO[T] = macro makeImpl[T]
  def makeImpl[T: c.WeakTypeTag](c: Context): c.Tree = {

    import c.universe._
    import com.kabasoft.iws.shared.DAO
    import com.kabasoft.iws.shared.Model._
    import com.kabasoft.iws.dao.DAOObjects._
    import com.kabasoft.iws.services.Request._
    val t = c.weakTypeOf[T]

    q"""
       implicit val api = new DAO[$t] {
           def create():Int = {run.runG[Int](Create[$t])}
           def all(): List[$t] = run.runG[List[$t]](new FindAll[$t])
           def insert(model: List[$t]):Int = run.runG[Int](new Insert[$t](model))
           def findSome(id: String): List[$t] = run.runG[List[$t]](new FindSome[$t](id))
           def findSome1(id: Long): List[$t] = run.runG[List[$t]](new FindSome1[$t](id))
           def find(id: String): List[$t] = run.runG[List[$t]](new Find[$t](id))
           def delete(id: String): Int =  run.runG[Int](new Delete[$t](id))
           def update(item: $t):Int = {
            val i = run.runG[List[$t]](new Find[$t](item.id))
            val k = if (i.size > 0) run.runG[Int](new Update[$t](item)) else run.runG[Int](new Insert[$t](List(item)))
            k
          }

       }
    api
    """
  }

/*
 def makeTransaction[T]:DAO[T] = macro makeTransactionImpl[T]
 def makeTransactionImpl[T:c.WeakTypeTag](c: Context): c.Tree = {

    import c.universe._
    import com.kabasoft.iws.shared.DAO
    import com.kabasoft.iws.shared.Model._
    import com.kabasoft.iws.dao.DAOObjects._
    import com.kabasoft.iws.services.Request._
    val t = c.weakTypeOf[T]

    q"""
       implicit val api = new DAO[$t] {
          override def create():Int = {run.runG[Int](Create[$t])}
          override def all(): List[$t] = run.runG[List[$t]](new FindAll[$t])
          override def insert(model: List[$t]):Int = run.runG[Int](new Insert[$t](model))
          override def findSome(id: String): List[$t] = run.runG[List[$t]](new FindSome[$t](id))
          override def findSome1(id: Long): List[$t] = run.runG[List[$t]](new FindSome1[$t](id))
          override def find(id: String): List[$t] = run.runG[List[$t]](new Find[$t](id))
          override def delete(id: String): Int = run.runG[Int](new Delete[$t](id))
          override def update(item: $t):Int = {
            val i = run.runG[List[$t]](new Find[$t](item.id.toString))
            val k = if (i.size > 0) run.runG[Int](new Update[$t](item)) else run.runG[Int](new Insert[$t](List(item)))
            k
          }
       }
    api
    """
  }
 */
}

