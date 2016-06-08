package services

import java.util.{Date, UUID}

import com.kabasoft.iws.shared._

class TodoService extends Api {


  var todos:List[IWS] = List(
    TodoItem("41424344-4546-4748-494a-4b4c4d4e4f50", 0x61626364, "Wear shirt that says “Life”. Hand out lemons on street corner.", 4711,TodoLow, false),
    TodoItem("2", 0x61626364, "Make vanilla pudding. Put in mayo jar. Eat in public.", 4711, TodoNormal, false),
    TodoItem("3", 0x61626364, "Walk away slowly from an explosion without looking back.",  4711, TodoHigh, false),
    TodoItem("4", 0x61626364, "Sneeze in front of the pope. Get blessed.",  4711, TodoNormal, true)
  )

  override def welcome(name: String): String = s"Welcome to SPA, $name! Time is now ${new Date}"

  def insert(item:IWS) =1
  def create(item:IWS) =1
  override def all(item:IWS): List[IWS] = {
    // provide some fake Todos
    Thread.sleep(300)
    println(s"Sending ${todos.size} Todo items")
    todos
  }

  // update a Todo
  override def update(item:IWS): List[IWS] = {
    // TODO, update database etc :)
    if(todos.exists(_.id == item.id)) {
      todos = todos.collect {
        case i if i.id == item.id => item
        case i => i
      }
      println(s"Todo item was updated: $item")
    } else {
      // add a new item
      val newItem = item.asInstanceOf[TodoItem].copy(id = UUID.randomUUID().toString)
      todos :+= newItem
      println(s"Todo item was added: $newItem")
    }
    Thread.sleep(300)
    todos
  }
  override def find(item: IWS): List[IWS] = {
    println(s"Find item with id = ${item.id}")
    Thread.sleep(300)
    todos = todos.filter(_.id == item.id)
    todos
  }
  override def findSome(item: IWS): List[IWS] = {
    println(s"Find item with id = ${item.id}")
    Thread.sleep(300)
    todos = todos.filter(_.id == item.id)
    todos
  }
  // delete a Todo
  override def delete(item: IWS): List[IWS] = {
    println(s"Deleting item with id = ${item.id}")
    Thread.sleep(300)
    todos = todos.filterNot(_.id == item.id)
    todos
  }
}
