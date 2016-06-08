package com.kabasoft.iws.client.services

import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.shared.{Store => MStore}
import com.kabasoft.iws.shared._
import diode.ActionResult._
import diode.RootModelRW
import diode.data.{Pot, Ready}
//import diode.util._
import com.kabasoft.iws.client.logger._
import utest._
object SPACircuitTests extends TestSuite {
  def tests = TestSuite {
   'IWSHandler - {
    // val ss = await()
       val store = Ready(DStore(Map(4 ->Ready(Data(
         Seq(
           QuantityUnit("L",  "Liter",4,"Liter"),
           QuantityUnit("M",  "Meter",4,"Meter"),
           QuantityUnit("A",  "Ampere",4,"Ampere")
       ))))))

       val newTodos = Seq(
         QuantityUnit("KG","Kilogramm",4,"Kilogramm")
       )
       val motd=Ready("XXXX")
       def build = new IWSHandler(new RootModelRW(store.asInstanceOf[Pot[DStore[IWS,IWS]]]))

       'Refresh - {
         val h = build
         val result = h.handle(Refresh(QuantityUnit()))
         result match {
           case ModelUpdateEffect(newValue, effects) =>
             assert(newValue eq h.value)
             assert(newValue.get.models.get(4).get.get.size == 3)
             assert(effects.size == 1)
           case _ =>
             assert(false)
         }
       }

      'Add - {
       val h = build
       val result = h.handle(Update(QuantityUnit("L", "US Dollar", 4, "US Dollar")))
       result match {
         case ModelUpdateEffect(newValue, effects) =>
           assert(newValue.get.models.get(4).get.get.size == 3)
           val t = newValue.get.models.get(4).get.get.asInstanceOf[Data].items.find(_.id.equals("L")).get
           assert(t.asInstanceOf[QuantityUnit].name.equals("US Dollar") ==true)
           //assert(newValue.get.models.get(4).get.get.asInstanceOf[Data].items(2).id == "L")
           assert(effects.size == 1)
         case _ =>
           assert(false)
       }
     }

     'UpdateAll - {
     val h = build
     val result = h.handle(UpdateAll(newTodos))
     assert(result == ModelUpdate(Ready(DStore(Map(4 ->Ready(Data(newTodos)))))))
   }



    'Update - {
      val h = build
      val result = h.handle(Update(QuantityUnit("Stck","Stueck",4,  "Stueck" )))
      result match {
        case ModelUpdateEffect(newValue, effects) =>
          assert(newValue.get.models.get(4).get.get.size == 4)
          assert(newValue.get.models.get(4).get.get.asInstanceOf[Data].items.exists(_.id == "Stck") ==true)
          assert(effects.size == 1)
        case _ =>
          assert(false)
      }
    }

    'Delete - {
      val h = build
      assert(store.get.models.get(4).get.get.asInstanceOf[Data].items.exists(_.id == "L") ==true)
      val d:IWS = store.get.models.get(4).get.get.asInstanceOf[Data].items.find(_.id.equals("L")).get
      //val d:IWS = store.get.models.get(4).get.get.asInstanceOf[Data].items.head
     // log.info("WWWWWWWWWWWWWWWWWW+++++++++<<<<<<<<<<<"+d)
      val result = h.handle(Delete(d))

      result match {
        case ModelUpdateEffect(newValue, effects) =>
          val a = newValue.get.models.get(4)
          log.info("WWWWWWWWWWWWWWWWWW+++++++++<<<<<<<<<<< a: "+a.get.get.items)
          assert(a.get.get.size == 2)
          //assert(newValue.get.models.get(4).get.get.asInstanceOf[Data].items(0).id== "A")
          assert(newValue.get.models.get(4).get.get.asInstanceOf[Data].items.exists(_.id == "L") ==false)

          assert(effects.size == 1)
        case _ =>
          assert(false)
      }
    }
   }

    'MotdHandler - {
      val model: Pot[String] = Ready("Message of the Day!")
      def build = new MotdHandler(new RootModelRW(model))

      'AddMotd - {
        val h = build
        var result = h.handle(UpdateMotd())
        result match {
          case ModelUpdateEffect(newValue, effects) =>
            assert(newValue.isPending)
            assert(effects.size == 1)
          case _ =>
            assert(false)
        }
        result = h.handle(UpdateMotd(Ready("New message")))
        result match {
          case ModelUpdate(newValue) =>
            assert(newValue.isReady)
            assert(newValue.get == "New message")
          case _ =>
            assert(false)
        }
      }
    }
  }
}
