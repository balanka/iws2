package com.kabasoft.iws.shared

/*import boopickle.Default._

sealed trait IWS { def id:String
                   def modelId:Int
                }
sealed trait TodoPriority

case object TodoLow extends TodoPriority
case object TodoNormal extends TodoPriority
case object TodoHigh extends TodoPriority

case class TodoItem(id: String ="", timeStamp: Int =0, content: String ="", modelId:Int =4711, priority: TodoPriority =TodoLow, completed: Boolean=false) extends IWS

object TodoPriority {
  implicit val todoPriorityPickler: Pickler[TodoPriority] = generatePickler[TodoPriority]
}
object Model {

  import boopickle.Default._

 sealed trait Masterfile  extends IWS {
   def  name: String
   def  description: String
  }

  case class CostCenter(id: String = "", name: String = "", description: String = "", modelId:Int=4712) extends Masterfile

  object CostCenter_ {
    def unapply(in: CostCenter) = Some(in.id, in.name, in.description,in.modelId)

  }


  //implicit val masterfilePickler: Pickler[Masterfile] = generatePickler[Masterfile]
  implicit val pickler = compositePickler[IWS]
  //pickler.addConcreteType[Masterfile]
  pickler.addConcreteType[CostCenter]

}
 */
