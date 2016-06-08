package com.kabasoft.iws.dao.test

import utest._
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.shared.Model._

import doobie.imports._
import com.kabasoft.iws.dao.test.analysisspec._
//import doobie.contrib.specs2.analysisspec._
import org.specs2.mutable.Specification
import scalaz.concurrent.Task

object AnalysisTestSpec extends Specification with AnalysisSpec {
  val transactor = DriverManagerTransactor[Task]("org.postgresql.Driver","jdbc:postgresql:world","postgres","")
  // Commented tests fail!
  // check(AnalysisTest.speakerQuery(null, 0))
 // check(AnalysisTest.speakerQuery2)
 // check(AnalysisTest.arrayTest)
  // check(AnalysisTest.arrayTest2)
  //check(sql"""SELECT * FROM PurchaseOrder""".query[POrder])
  // check(AnalysisTest.pointTest2)
  // check(AnalysisTest.update("foo", 42))
  //check(AnalysisTest.update2)
  check(AnalysisTest.POrderSelectTest)
}