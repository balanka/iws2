package com.kabasoft.iws.services

import com.kabasoft.iws.dao.DAOObjects._

import scalaz.{Free, ~>, Id, Coyoneda}
import com.kabasoft.iws.shared.Model._
import com.kabasoft.iws.shared.DAO
object Request {

  sealed trait Command[+A]
  sealed trait Service[+A]
  final case class Create[A](implicit val dao:DAO[A]) extends Service[Int]
  final case class Insert[A](model: List[A])( implicit val dao:DAO[A]) extends Service[Int]
  final case class Update[A](model:A) (implicit val dao:DAO[A]) extends Service[Int]
  final case class FindAll[A](implicit val dao:DAO[A]) extends Service[List[A]]
  final case class Find[A](id:String) ( implicit val dao:DAO[A]) extends Service[List[A]]
  final case class FindSome[A](id:String)(implicit val dao:DAO[A]) extends Service[List[A]]
  final case class FindSome1[A](id:Long)(implicit val dao:DAO[A]) extends Service[List[A]]
  final case class Delete[A](id:String)(implicit val dao:DAO[A]) extends Service[Int]
  final case class Execute[A](service:Service[A]) extends Command[A]


  object Create_{
    def unapply[A] (in:Create [A])= Some(in.dao)
  }
  object Insert_ {
   def unapply[A] (in:Insert[A]) = Some(in.model.asInstanceOf[List[A]],in.dao)
  }
  object Update_ {
    def unapply[A] (in:Update[A]) = Some(in.model, in.dao)
  }
  object FindAll_ {
    def unapply[A] (in:FindAll[A] )= Some(in.dao)
  }
  object Find_ {
    def unapply[A] (in:Find [A])= Some(in.id,in.dao)
  }
  object FindSome_ {
    def unapply[A] (in:FindSome [A])= Some(in.id,in.dao)
  }
  object FindSome1_ {
    def unapply[A] (in:FindSome1 [A])= Some(in.id,in.dao)
  }
  object Delete_ {
    def unapply[A] (in:Delete[A] )= Some(in.id,in.dao)
  }
  object Execute_ {
    def unapply[A] (in:Execute[A] )= Some(in.service)
  }
  type Executable[A] = Coyoneda[Command, A]

  def execute[A](service :Service[A]): Free[Executable, A] = Free.liftFC(Execute[A](service): Command[A])
}

import Request._


object CommandInterpreter extends (Command ~> Id.Id) {

  import Id._
  import Request._
  import com.kabasoft.iws.shared.DAO
  import com.kabasoft.iws.dao.DAOObjects._

  def apply[A](in: Command[A]): A =
    in match {
      case Execute_(service) =>
        service match {
          case Create_(dao) => dao.create
          case Insert_( model:List[A], dao:DAO[A]) => dao.insert(model)
          case Update_(model:A,dao:DAO[A]) => dao.update(model)
          case FindAll_(dao) => dao.all
          case Find_(id, dao) => dao.find(id)
          case FindSome_(id, dao) => dao.findSome(id)
          case FindSome1_(id, dao) => dao.findSome1(id)
          case Delete_(id, dao) => dao.delete(id)
        }
    }
}
object run {

  def runG [A](service: Service[A]) =  Free.runFC(execute[A](service))(CommandInterpreter)

}
