package com.kabasoft.iws.client.modules


import com.kabasoft.iws.client.components.{LinePurchaseOrderList, PurchaseOrderList}
import com.kabasoft.iws.gui.AccordionPanel
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.{SPACircuit, RootModel}
import diode.ModelR

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scalacss.ScalaCssReact._

object PURCHASEORDER {

  @inline private def bss = GlobalStyles.bootstrapStyles
  @volatile private var v= List.empty[Article]
  case class Props(proxy: ModelProxy[Pot[Data]],proxy1: ModelProxy[Pot[Data]])
  case class State(item: Option[PurchaseOrder[LinePurchaseOrder]] = None, line: Option[LinePurchaseOrder] = None, articles: Option[List[Article]] = None)

  class Backend($: BackendScope[Props, State]) {
    /* private var unsubscribe1 = Option.empty[() => Unit]
      //private var unsubscribe2 = Option.empty[() => Unit]

      def willMount = {
        // subscribe to model changes
        Callback {
           unsubscribe1 = Some(IWSCircuit.subscribe(IWSCircuit.zoom(_.data.get.getData(Article())))(articleChangeHandler))
          //unsubscribe2 = Some(IWSCircuit.subscribe(IWSCircuit.zoom(_.data.get.getData(PurchaseOrder[LinePurchaseOrder]())))(orderChangeHandler))
        } //>> $.setState(IWSCircuit.zoom(_.data.get.getData(Article())).eval(IWSCircuit.getModel))
      }

      def willUnmount = Callback {
         unsubscribe1.foreach(f => f())
         unsubscribe1 = None
        //unsubscribe2.foreach(f => f())
        //unsubscribe2 = None
      }

      private def articleChangeHandler(cursor: ModelR[RootModel2, Pot[Data2]]): Unit = {
        // modify state if we are mounted and state has actually changed
        val changes0 = IWSCircuit.zoom(_.data.get.getData(Article())).eval(IWSCircuit.getModel)
        //if ($.isMounted() && changes =!= $.accessDirect.state.articles) {
        if ($.isMounted()) {
          //if ($.isMounted() && modelReader =!= $.accessDirect.state) {
          //$.accessDirect.setState(modelReader())
          val changes = IWSCircuit.zoom(_.data.get.getData(Article())).eval(IWSCircuit.getModel)
          //  v= v:::changes.get.items.asInstanceOf[List[Article]]
          if(changes != None && !changes.get.items.isEmpty) {
            log.debug(s" CHANGES  CHANGES ARTICLEAAA ${changes.get.items.headOption}")
            v= v:::changes.get.items.asInstanceOf[List[Article]]
            $.modState(s => s.copy(articles = Some(changes.get.items.asInstanceOf[List[Article]])))
            // render($.props.runNow(), $.state.runNow())
          }
          log.debug(s" CHANGES  CHANGES ARTICLE0 ${changes0.get.items.headOption }")

          // $.accessDirect.setState(modelReader())
        }
      } */
    def mounted(props: Props) = {


      //log.debug(s"Oid is "+props.proxy.modelReader.zoom(_.get.items))
      //Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]())))>>
      //Callback.ifTrue(props.proxy1().isEmpty, props.proxy1.dispatch(Refresh(Article())))


      // props.proxy.dispatch(Refresh(Article()))


      //props.proxy1.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]())) //>>props.proxy1.dispatch(Refresh (Article()))
      //Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh (PurchaseOrder[LinePurchaseOrder]()))) >>
      // Callback.ifTrue(props.proxy1().isEmpty||props.proxy1().isPending , props.proxy1.dispatch(Refresh (Article())))
      def f:IWS  => Callback = s => props.proxy.dispatch(Refresh(s))
      def f1:PurchaseOrder[LinePurchaseOrder]  => Callback = s => props.proxy.dispatch(Refresh(s))
      def f2: Article => Callback = s => props.proxy1.dispatch(Refresh(s))
      implicit class Chain[A](f: A => Callback) {
        def o [B](g: B => Callback): Tuple2[A, B] => Callback =  {
          case (x, y) => f(x) ; g(y)
        }
      }

      //(f o f) (PurchaseOrder[LinePurchaseOrder]() -> Article())
      // props.proxy1.dispatch(Refresh (Article()))
      props.proxy.dispatch(Refresh(PurchaseOrder[LinePurchaseOrder]())) //>>props.proxy1.dispatch(Refresh (Article()))
    }



    def edit(item:Option[PurchaseOrder[LinePurchaseOrder]]) = {
      val d =item.getOrElse(PurchaseOrder[LinePurchaseOrder]())

      //log.debug(s"purchaseOrder is xxxxxx ${d}")
      $.modState(s => s.copy(item = Some(d)))
      // $.modState(s => s.copy(item = s.item))
    }

    def updateOid(e: ReactEventI) = {
      val l =e.target.value.toLong
      log.debug(s"Oid is "+l)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
    }

    def updateStore(e: ReactEventI) = {
      val r = Some(e.target.value)
      log.debug(s"store is "+r)
      $.modState(s => s.copy(item = s.item.map(_.copy(store =r ))))
    }

    def updateAccount(e: ReactEventI) = {
      val currentValue = Some(e.target.value)
      log.debug(s"purchaseOrder is zzzzzzzzz"+currentValue)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = currentValue))))
    }


    def edited(order:PurchaseOrder[LinePurchaseOrder]) = {
      //$.modState(s => s.copy(item =Some(order)))
      $.props >>= (_.proxy.dispatch(Update(order)))
      //LinePurchaseOrderList.apply($.props.runNow().proxy, item, AddNewLine, editLine, saveLine, deleteLine)
      // $.modState(s => s.copy(item =Some(order)))
    }

    def saveLine(line:LinePurchaseOrder) = {
      log.debug(s"purchaseOrder is yyyyyyyyyy"+line)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      def cond(line1:LinePurchaseOrder, line2:LinePurchaseOrder ) =(line.tid == line2.tid)  &&(line1.created ==true)
      val k2 = k.replaceLine(k.getLines.filter(cond(_,line)).headOption.getOrElse(LinePurchaseOrder()), line.copy(transid = k.tid))
      log.debug(s"purchaseOrder k2 is ${k2} ")
      //$.modState(s => s.copy(item =Some(k2)))>> edited(k2)
      edited(k2)
      //val k3 = k.replaceLine(k.getLines.filter(cond(_,line)).headOption.getOrElse(LinePurchaseOrder()), line.copy(created = false))
      //log.debug(s"purchaseOrder K3 ${k3} ")
      //$.modState(s => s.copy(item =Some(k3)))

    }

    def delete(item:PurchaseOrder[LinePurchaseOrder]) = {
      // val s = $.state.runNow().item
      Callback.log("PurchaseOrder deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def deleteLine(line1:LinePurchaseOrder) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(PurchaseOrder[LinePurchaseOrder]())
      val k2 = k.replaceLine(k.getLines.filter(_.tid == deleted.tid).headOption.getOrElse(LinePurchaseOrder()), deleted)
      edited(k2)
    }
    def AddNewLine(line:LinePurchaseOrder) = {
      val  created =line.copy(created = true)
      log.debug(s"New Line Purchase order before  edit>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]()).tid)))))
      //editLine()
      // log.debug(s"purchaseOrder state is "+$.state.runNow().item)
      // Callback.log(s"RUNNING State>>>>>  ${  $.state.runNow().item}")
    }

    def filterWith(line:LinePurchaseOrder, search:String) =
      line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {
      def saveButton = Button(Button.Props(edited(s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(PurchaseOrder[LinePurchaseOrder]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      //val orders  =IWSCircuit.zoom(_.data.get.getData(PurchaseOrder[LinePurchaseOrder]())).eval(IWSCircuit.getModel).get.items.asInstanceOf[List[PurchaseOrder[LinePurchaseOrder]]]
     // log.debug(s" ORDERS ORDERS ${orders.headOption} }")
      Panel(Panel.Props("Purchase Order"), <.div(^.className := "panel-heading"),
        <.div(^.padding := 0,
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          //p.proxy1().render( all1  => AccordionPanel("Edit", buildForm(p, s /*, all1.items.asInstanceOf[List[Article]]*/), List(saveButton, newButton))),
          AccordionPanel("Edit", buildForm(p, s /*, all1.items.asInstanceOf[List[Article]]*/), List(saveButton, newButton)),
          p.proxy().render( all  => AccordionPanel("Display", List(PurchaseOrderList(all.items.asInstanceOf[Seq[PurchaseOrder[LinePurchaseOrder]]],
            item => edit(Some(item)), item => p.proxy.dispatch(Delete(item))))))
        )
      )


    }
    // def buildForm (p: Props, s:State, items:List[Article]): Seq[ReactElement] = {
    def buildForm (p: Props, s:State): Seq[ReactElement] = {
      val items =  if (v ==None || v.isEmpty) SPACircuit.zoom(_.store.get.models.get(7)).eval(SPACircuit.getRootModel).get.get.items.asInstanceOf[List[Article]]
      else v

      log.debug(s" ARTICLES ${items.headOption} }")

      val porder = s.item.getOrElse(PurchaseOrder[LinePurchaseOrder]().add(LinePurchaseOrder(item = Some("4711"))))
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              // <.div(bss.formGroup,
              buildItem[String]("id", s.item.map(_.id), "id"),
              buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildWItem[String]("store", s.item.map(_.store.getOrElse("store")), "store", updateStore),
              buildWItem[String]("account", s.item.map(_.account.getOrElse("account")), "account", updateAccount)
            )
          )
        )
        ,LinePurchaseOrderList(p.proxy1, items, porder, AddNewLine, saveLine, deleteLine)
      )
      )
    }
  }

  val component = ReactComponentB[Props]("PURCHASEORDER")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]],proxy1: ModelProxy[Pot[Data]]) = component(Props(proxy,proxy1))
}
