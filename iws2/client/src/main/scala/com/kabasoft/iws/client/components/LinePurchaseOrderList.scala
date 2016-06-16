package com.kabasoft.iws.client.components

import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.IWSSelect
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.gui.macros._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared._
import diode.data.{Pot, Ready}
import diode.react.ModelProxy
import java.util.Date

import autowire._
import com.kabasoft.iws.gui.services.{AjaxClient, SPACircuit}

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import java.lang.String

import com.kabasoft.iws.client.SPAMain
import diode.{ActionResult, Effect}

//import org.scalajs.dom

import scalacss.ScalaCssReact._

object LinePurchaseOrderList {
  @inline private def bss = GlobalStyles.bootstrapStyles

  //case class Persona(serviceName:String,serviceId:String,sentMessages:Option[List[String]]){def r( m:String) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[String])++List(m))); def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity) }
  //case class Persona[T](serviceName:String,serviceId:String,sentMessages:Option[List[T]]){def r( m:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T])++List(m))); def rr [T] (old:T, newr:T) = sentMessages.getOrElse(List.empty[T])++ sentMessages map replace((old, newr));  def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity) }
  //defined class Persona

  //case class Persona[T](serviceName:String,serviceId:String,sentMessages:Option[List[T]]){def r( m:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T])++List(m))); def rr [T] (old:T, newr:T) = sentMessages map replace((old, newr));  def replace[T](pairs: (T, T)*) = Map(pairs: _*).withDefault(identity); def m  (old:T, newr:T) =copy(sentMessages=Some(sentMessages.getOrElse(List.empty[T]) map replace((old,newr)))) }
  //defined class Persona
  //scala> Persona("ABC","1",Some(List("1","2")))
 // res73: Persona[String] = Persona(ABC,1,Some(List(1, 2)))

  //scala> res73.m("1","3")
 // res74: Persona[String] = Persona(ABC,1,Some(List(3, 2)))
  //case class State(item: Option[LinePurchaseOrder]= None, porder: Option[PurchaseOrder[LinePurchaseOrder]]= None)
  /*case class Props(proxy: ModelProxy[Pot[Data]], porder: PurchaseOrder[LinePurchaseOrder],
                   newLine:LinePurchaseOrder =>Callback,
                   saveLine:LinePurchaseOrder =>Callback,
                   deleteLine:LinePurchaseOrder =>Callback,predicate:(LinePurchaseOrder,String) =>Boolean) */

  case class State(item: Option[LinePurchaseOrder]= None, articles:Option[List[String]] =None, search:String="")
  case class Props(proxyx: ModelProxy[Pot[Data]], proxy1: ModelProxy[Pot[Data]], porder: PurchaseOrder[LinePurchaseOrder],
                   newLine:LinePurchaseOrder =>Callback,
                   saveLine:LinePurchaseOrder =>Callback,
                   deleteLine:LinePurchaseOrder =>Callback)


  class Backend($: BackendScope[Props, State]) {

    def mounted(props: Props) = {

      import boopickle.Default._
      import com.kabasoft.iws.shared.{Store => MStore}

      implicit val amountPickler = transformPickler[BigDecimal,String](b=> String.valueOf(b.doubleValue()),
        t =>  scala.math.BigDecimal(t))
      implicit val datePickler = transformPickler[java.util.Date, Long](_.getTime,t => new java.util.Date(t))
      implicit val pickler = compositePickler[IWS]
      pickler.addConcreteType[Article]

      pickler.addConcreteType[TodoItem]
      pickler.addConcreteType[CostCenter]
      pickler.addConcreteType[Balance]
      pickler.addConcreteType[Account]

      pickler.addConcreteType[Supplier]
      pickler.addConcreteType[Customer]
      pickler.addConcreteType[QuantityUnit]
      pickler.addConcreteType[ArticleGroup]
      pickler.addConcreteType[LinePurchaseOrder]
      pickler.addConcreteType[PurchaseOrder[LinePurchaseOrder]].addConcreteType[LinePurchaseOrder]
      pickler.addConcreteType[Vat]
      pickler.addConcreteType[MStore]
      def buildIdNames (list: List[IWS]): List[String]= list.asInstanceOf[List[Masterfile]] map (iws =>(iws.id+iws.name))
      //Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(LinePurchaseOrder())))

      // val z = props.proxy1.zoom(_.get.items.size)
      // val z1 = props.proxy1.zoom(_.get.items.get)
      //log.debug(s"1111111111 ${z.value} ${SPAMain.CIRCUIT}")

      Callback.ifTrue(props.proxy1().isEmpty, props.proxy1.dispatch(FindAll(Article())))
      log.debug(s"1111111111 ${SPAMain.CIRCUIT.value}")
      Callback.ifTrue(props.proxy1().isEmpty, props.proxy1.dispatch(FindAll(Article())))
    // val v = SPACircuit.getModel(7)

      //log.debug(s"VVVVVVVVVVVVVVVVVVVVVVVVV ${v}  ")
      ///SPACircuit.connect(_.store.get.models.getOrElse(7,Ready(Data(List(Article())))).map(_.asInstanceOf[Data].items.count(!_.id.isEmpty)).toOption)(proxy =>
      //$.modState(s => s.copy(articles =s.articles)).runNow()
      /*
      def buildIdNameList (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+iws.name))
     // $.modState(s => s.copy(articles = Some(buildIdNameList(v.asInstanceOf[List[Masterfile]]))))
      $.modState(s => s.copy(articles =s.articles))
      */

      //def buildMap( l:List [IWS]) = l.fold("") (String) =
      /*Effect(AjaxClient[Api].all(Article(id="001")).call().map(buildIdNames).onSuccess({
      //val x = AjaxClient[Api].all(Article(id="001")).call().map (buildIdNames).onSuccess({
        case values => $.modState(s => s.copy(articles = Some(values))).runNow()
          ActionResult.NoChange
      }))*/

     // log.info(s"VVVVVVVVVVVVVVVVVVVVVVVVV articles ${$.state.runNow().articles}  ")
     //$.modState(s => s.copy(articles =s.articles))

    }

    def edit(line:LinePurchaseOrder) = {
      log.debug(s" order to edit Line is ${line}")
       $.modState(s => s.copy(item = Some(line)))
    }

    def updateItem1(l: String) = {
     // def updateItem1(e: ReactEventI) = {
     // val l =Some(e.target.value)
      log.debug(s"ItemId Key is ${l}  ")
      //Callback.log(s"KEY pressed >>>>>>>>>>>>>>>>>>>>>>>> ${l}")>>
      $.modState(s => s.copy(item = s.item.map(_.copy(item = Some(l)))))>>$.modState(s => s.copy(search =s.search+l))
     // $.modState(s => s.copy(item =  s.item))
    }

    def updateItem(e: ReactEventI, s1:State) = {
      val l =Some(e.target.value)
     log.debug(s"ItemId is ${l}  State: ${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(item = l))))
      //$.modState(s => s.copy(porder = Some(order)))>>
    }
    def updateUnit(e: ReactEventI) = {
      val l =e.target.value
       log.debug(s" Unit is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(unit = Some(l)))))
    }
    def updateVat(e: ReactEventI) = {
      val l =e.target.value
       log.debug(s"Vat is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(vat = Some(l)))))
    }
    def updateDuedate(e: ReactEventI) = {
      val l =e.target.value.toLong
      $.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(new Date(l))))))
    }
    def updateText(e: ReactEventI) = {
      val l =e.target.value
      $.modState(s => s.copy(item = s.item.map(_.copy(text = l))))
    }
    def updatePrice(e: ReactEventI, s1:State) = {
      val l =e.target.value.toDouble
      log.debug(s"Item price is ${l} State:${s1}")
      $.modState(s => s.copy(item = s.item.map(_.copy(price = l))))
    }
    def updateQuantity(e: ReactEventI) = {
      val l =e.target.value
      log.debug(s"Item quantity is ${l}")
      $.modState(s => s.copy(item = s.item.map(_.copy(quantity = BigDecimal(l)))))
    }

    def delete(line:LinePurchaseOrder, deleteLineCallback:LinePurchaseOrder =>Callback) =
      Callback.log(s"LinePurchaseOrder deleted>>>>>  ${line}") >> deleteLineCallback(line)

    def save(line:LinePurchaseOrder, saveLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" saved  Line order is ${line}")
      $.modState(s => s.copy(item = Some(line)))>> saveLineCallback(line)

    }
    def newLine(line:LinePurchaseOrder, newLineCallback:LinePurchaseOrder =>Callback) = {
      log.debug(s" newLine called with   ${line}")
     newLineCallback(line)>> edit(line)
    }

    def render(p:Props, s: State) = {
      val style = bss.listGroup
      log.debug(s" KKKKKKKKKKKKKKKKKK ${ArticleList.ITEMS}")
      //log.debug(s"2222222222222 ${SPAMain.CIRCUIT.value}")
      log.debug(s" WWWWWWWWWWWWWWWWW ${p.proxy1().get.items}")
      val its  = p.porder.lines.getOrElse(Seq.empty[LinePurchaseOrder])
      def saveButton = Button(Button.Props(save(s.item.getOrElse(LinePurchaseOrder()),p.saveLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " SaveLine")
      def newButton = Button(Button.Props( newLine(LinePurchaseOrder( created = true),p.newLine),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " NewLine")
      def buildIdNameList (list: List[Masterfile]): List[String]= list map (iws =>(iws.id+iws.name))
      val editFormLine:Seq [TagMod]=List(
              //buildWItem[String]("item", s.item.map(_.item.getOrElse("item")), "item", updateItem(_, s)),
             //IWSSelect("1", "2", "item", s.item.map(_.item.getOrElse("item")).get, updateItem1,p.proxy1, Article()),
         //  p.proxy1().get.items
            //     all => (
            /*<.div(
            <.label(<.strong("Item")),
            <.select(^.paddingLeft := "5px", ^.id := "reactselect", ^.value := s.item.map(_.item.getOrElse("item")), ^.onChange ==> updateItem1)(
              List("0001","002","003","0004").map(item => <.option(item)))),*/
            // buildIdNameList( p.proxy1().get.items.asInstanceOf[List[Masterfile]]).map(item => <.option(item)))),
               buildSItem[String]("item", optValue = Some(List("0001","002","003","0004")), defValue = "0001", evt = updateItem1, p.proxy1 , com.kabasoft.iws.shared.Article("002")),
             // buildSItem[String]("item",  s.item.map(_.item.getOrElse("item")), "item", updateItem1, p.proxy1 , com.kabasoft.iws.shared.Article()),
              buildWItem[BigDecimal]("price", s.item.map(_.price), 0.0, updatePrice(_, s)),
              buildWItem[BigDecimal]("quantity", s.item.map(_.quantity), 0.0, updateQuantity),
              buildWItem[String]("unit", s.item.map(_.unit.getOrElse("unit")), "unit", updateUnit),
              buildWItem[String]("vat", s.item.map(_.vat.getOrElse("vat")), "19", updateVat),

              buildWItem[Date]("Duedate", s.item.map(_.duedate.getOrElse(new Date())), new Date(), updateDuedate),
              saveButton, newButton)

      <.div(bss.formGroup,
        //<.ul(style.listGroup)(its.filter(p.predicate (_,s.search)).sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.ul(style.listGroup)(its.sortBy(_.tid)(Ordering[Long].reverse) map (e =>renderItem(e,p))),
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height :=5.px, ^.maxHeight:=8.px,
               if(its.size>0) editFormLine else List(saveButton, newButton)
              //IWSSelect("1", "2", "item", s.item.map(_.item.getOrElse("item")).get, updateItem1,p.proxy1, Article())

            )
          )
        )
      )
    }

    def renderItem(item:LinePurchaseOrder, p: Props) = {
      def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
      def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
                            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
      val style = bss.listGroup
      <.li(style.itemOpt(CommonStyle.warning),^.fontSize:=12.px,^.fontWeight:=50.px,^.maxHeight:=30.px,
                                             ^.height:=30.px, ^.alignContent:="center", ^.tableLayout:="fixed",
        <.span(item.id),
        <.span(item.item ,^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.price.bigDecimal),^.paddingLeft:=10.px),
        <.span("%06.2f".format(item.quantity.bigDecimal),^.paddingLeft:=10.px),
        <.span(item.unit ,^.paddingLeft:=10.px),
        <.span(item.vat ,^.paddingLeft:=10.px),
        <.span(editButton, deleteButton,^.alignContent:="center")
      )
    }

  }


  val component = ReactComponentB[Props]("LinePurchaseOrderList")
      .initialState(State( search ="0"))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .shouldComponentUpdate(scope => (scope.currentState ne scope.nextState) || (scope.currentProps ne scope.nextProps))
    .build

  def apply( proxy: ModelProxy[Pot[Data]], proxy1: ModelProxy[Pot[Data]],
             porder:PurchaseOrder[LinePurchaseOrder],
             newLine:LinePurchaseOrder =>Callback,
             saveLine:LinePurchaseOrder =>Callback,
             deleteLine:LinePurchaseOrder => Callback) =
             component(Props(proxy,proxy1,porder, newLine, saveLine,deleteLine))

}
