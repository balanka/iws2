package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{GoodreceivingList, LineGoodreceivingList}
import com.kabasoft.iws.gui.AccordionPanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import diode.data.{Pot, Ready}
import diode.react.ReactPot._
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

object GOODRECEIVING {

  @inline private def bss = GlobalStyles.bootstrapStyles
  case class Props(proxy: ModelProxy[Pot[Data]])
  case class State(item: Option[Goodreceiving[LineGoodreceiving]] = None)

  class Backend($: BackendScope[Props, State]) {
    def mounted(props: Props) =
      Callback {
        IWSCircuit.dispatch(Refresh(Goodreceiving[LineGoodreceiving]()))
        IWSCircuit.dispatch(Refresh(Supplier()))
        IWSCircuit.dispatch(Refresh(Store()))
      }
   def edit(item:Option[Goodreceiving[LineGoodreceiving]]) = {
      val d =item.getOrElse(Goodreceiving[LineGoodreceiving]())
      $.modState(s => s.copy(item = Some(d)))
    }

    def updateOid(e: ReactEventI) = {
      val l =e.target.value.toLong
      log.debug(s"Oid is "+l)
      $.modState(s => s.copy(item = s.item.map(_.copy(oid = l))))
    }

    def updateStore(idx:String) = {
      val storeId = idx.substring(0, idx.indexOf("|"))
      log.debug(s"store is "+storeId)
      $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
    }

    def updateAccount(e: ReactEventI) = {
      val currentValue = Some(e.target.value)
      log.debug(s"purchaseOrder is zzzzzzzzz"+currentValue)
      $.modState(s => s.copy(item = s.item.map(_.copy(account = currentValue))))
    }

    def updateAccount1(idx: String) = {
      val supplierId=idx.substring(0, idx.indexOf("|"))
      log.debug(s"ItemId Key is ${supplierId}  ")
      $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId)))))
    }

    def edited(order:Goodreceiving[LineGoodreceiving]) = {
      //$.modState(s => s.copy(item =Some(order)))
      $.props >>= (_.proxy.dispatch(Update(order)))
    }
    def saveLine(linex:LineGoodreceiving) = {
      val line = linex.copy(modified=true)
      log.debug(s"purchaseOrder is yyyyyyyyyy" + line)
      val k = $.state.runNow().item.getOrElse(Goodreceiving[LineGoodreceiving]())
      val k2 = k.replaceLine( line.copy(transid = k.tid))
      log.debug(s"purchaseOrder k2 is ${k2} ")
      //$.modState(s => s.copy(item =Some(k2)))>> edited(k2)
      edited(k2)
    }


    def delete(item:Goodreceiving[LineGoodreceiving]) = {
      // val s = $.state.runNow().item
      Callback.log("PurchaseOrder deleted>>>>> ${item}  ${s}")
      $.props >>= (_.proxy.dispatch(Delete(item)))
      //$.modState(s => s.copy(item = None)).runNow()
    }
    def deleteLine(line1:LineGoodreceiving) = {
      val  deleted =line1.copy(deleted = true)
      val k =$.state.runNow().item.getOrElse(Goodreceiving[LineGoodreceiving]())
      val k2 = k.replaceLine(deleted)
      edited(k2)
    }
    def AddNewLine(line:LineGoodreceiving) = {
      val  created =line.copy(created = true)
      log.debug(s"New Line Purchase order before  edit>>>>>  ${line}")
      $.modState(s => s.copy(item = s.item.map(_.add(line.copy(transid=s.item.getOrElse(Goodreceiving[LineGoodreceiving]()).tid)))))
      //editLine()
    }

    def filterWith(line:LineGoodreceiving, search:String) =
      line.item.getOrElse("").contains(search)

    def render(p: Props, s: State) = {

      def saveButton = Button(Button.Props(edited(s.item.getOrElse(Goodreceiving[LineGoodreceiving]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
      def newButton = Button(Button.Props(edit(Some(Goodreceiving[LineGoodreceiving]())),
        addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
      Panel(Panel.Props("Goodreceiving"), <.div(^.className := "panel-heading"),
        <.div(^.padding := 0,
          p.proxy().renderFailed(ex => "Error loading"),
          p.proxy().renderPending(_ > 500, _ => "Loading..."),
          AccordionPanel("Edit", buildForm(p, s), List(saveButton, newButton)),
          p.proxy().render( all  => AccordionPanel("Display", List(GoodreceivingList(all.items.asInstanceOf[Seq[Goodreceiving[LineGoodreceiving]]],
            item => edit(Some(item)), item => p.proxy.dispatch(Delete(item))))))
        )
      )
    }

    def buildForm (p: Props, s:State): Seq[ReactElement] = {
      val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]]
      val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]]
      val porder = s.item.getOrElse(Goodreceiving[LineGoodreceiving]().add(LineGoodreceiving(item = Some("4711"))))
      val  storeList=store map (iws =>(iws.id+"|"+iws.name))
      val  supplierList=supplier map (iws =>(iws.id+"|"+iws.name))
      List(<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(bss.formGroup, ^.height := 10.px,
              buildItem[String]("id", s.item.map(_.id), "id"),
              buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
              buildSItem("store", itemsx=storeList.toList,defValue = "0001", evt = updateStore),
              //buildWItem[String]("account", s.item.map(_.account.getOrElse("account")), "account", updateAccount)
              buildSItem("supplier", itemsx = supplierList.toList, defValue = "KG", evt = updateAccount1)
            )
          )
        ),
        LineGoodreceivingList(porder, AddNewLine, saveLine, deleteLine)
      )
      )
    }
  }

  val component = ReactComponentB[Props]("PURCHASEORDER")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
}
