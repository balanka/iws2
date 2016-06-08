package com.kabasoft.iws.dao.test

import utest._
import com.kabasoft.iws.dao.DAOObjects._
import com.kabasoft.iws.shared.Model._

import doobie.imports._
import com.kabasoft.iws.dao.test.analysisspec._
import org.specs2.mutable.Specification
import scalaz.concurrent.Task

import scalaz.concurrent.Task
import doobie.imports._
import doobie.contrib.postgresql.pgtypes._
import org.postgresql.geometric._

object AnalysisTest {


  val xa = DriverManagerTransactor[Task]("org.postgresql.Driver","jdbc:postgresql:world","postgres","")

   val  POrderSelectTest = sql"""SELECT * FROM PurchaseOrder""".query[POrder]

  /*

    case class Country(name: String, indepYear: Int)

    def speakerQuery(lang: String, pct: Double) =
      sql"""
      SELECT C.NAME, C.INDEPYEAR, C.CODE FROM COUNTRYLANGUAGE CL
      JOIN COUNTRY C ON CL.COUNTRYCODE = C.CODE
      WHERE LANGUAGE = $lang AND PERCENTAGE > $pct
    """.query[Country]

    def speakerQuery2 =
      sql"""
      SELECT C.NAME, C.INDEPYEAR, C.CODE FROM COUNTRYLANGUAGE CL
      JOIN COUNTRY C ON CL.COUNTRYCODE = C.CODE
    """.query[(String, Option[Short], String)]

    def arrayTest =
      sql"""
      SELECT ARRAY[1, 2, NULL] test
    """.query[Option[List[String]]]

    def arrayTest2 =
      sql"""
      SELECT ARRAY[1, 2, NULL] test
    """.query[String]

    def pointTest =
      sql"""
      SELECT '(1, 2)'::point test
    """.query[PGpoint]

    def pointTest2 = {
      Meta[PostgresPoint.Point] // why not? ... irritating that it must be instantiated. what to do?
      sql"""
      SELECT '(1, 2)'::point test
    """.query[PGcircle]
    }

    def update(code: String, name: Int) =
      sql"""
      UPDATE COUNTRY SET NAME = $name WHERE CODE = $code
    """.update

    def update2 =
      sql"""
      UPDATE COUNTRY SET NAME = 'foo' WHERE CODE = 'bkah'
    """.update

     */
  }
