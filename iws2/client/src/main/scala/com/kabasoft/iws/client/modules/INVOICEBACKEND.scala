package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.modules.Utilities._
import com.kabasoft.iws.client.components.{LineVendorInvoiceList, VendorInvoiceList}
import com.kabasoft.iws.gui.BasePanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import diode.data.{Pot, Ready}
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactElement}


import scalacss.ScalaCssReact._


class InvoiceBackend($: BackendScope[Props, State[VendorInvoice[LineVendorInvoice]]]) extends
      Backendx [VendorInvoice[LineVendorInvoice], Props, State[VendorInvoice[LineVendorInvoice]]] ($){

  @inline private def bss = GlobalStyles.bootstrapStyles
  @volatile var gitems = Set.empty[VendorInvoice[LineVendorInvoice]]

  implicit def orderingById[A <: VendorInvoice[LineVendorInvoice]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}

  //val r = List( updateOid1 _, updateStore _, updateAccount _, updateText _, edit _)

  def mounted(props: Props) = {

    Callback {
      IWSCircuit.dispatch(Refresh(Supplier()))
      IWSCircuit.dispatch(Refresh(Store()))
      IWSCircuit.dispatch(Refresh(VendorInvoice[LineVendorInvoice]()))
    }
  }
  def editFx(d:VendorInvoice[LineVendorInvoice]) = {
   // val d =item.getOrElse(VendorInvoice[LineVendorInvoice]())
    $.modState(s => s.copy(item = Some(d)))
  }

  def updateOidFx(idx:String) = $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
  //def updateOid1(idx:String,  bs: BackendScope[Props,State]) = bs.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified

  def updateStoreFx(idx:String) = {
    val storeId = idx.substring(0, idx.indexOf("|"))
    log.debug(s"store is "+storeId)
    $.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId))))) >>setModified
  }

  def updateAccountFx(idx: String) = {
    val supplierId=idx.substring(0, idx.indexOf("|"))
    log.debug(s"ItemId Key is ${supplierId}  ")
    $.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId))))) >>setModified
  }
  def updateTextFx(e: ReactEventI) = {
    val txt = e.target.value
    log.debug(s"txt is ${txt}")
    $.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))  >>setModified
  }

  def runIt = $.state.runNow().item.getOrElse(VendorInvoice[LineVendorInvoice]())

  def saveLineFx(linex:LineVendorInvoice) =  {
    val k = runIt
    val k2 = k.replaceLine( linex.copy(transid = k.tid))

    runCB(k2)

  }
  def setModified  = $.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )

  def runCB(itemx: VendorInvoice[LineVendorInvoice]) = Callback {
    IWSCircuit.dispatch(Update(itemx))

    val ro = itemx.getLines.filter(_.created == true).map(e => itemx.replaceLine(e.copy(created = false).copy(modified = false)))
    if (!ro.isEmpty) {
      val setLineID = ro.head.copy(lines = Some(itemx.getLines map (e => if (e.tid <= 0) e.copy(tid = e.tid - 1) else e)))
      $.modState(s => s.copy(item = Some(setLineID))).runNow()
    } else {
      $.modState(s => s.copy(item = Some(itemx.copy(modified = false)))).runNow()
    }

  }

  def deleteFx(item:VendorInvoice[LineVendorInvoice]) = {
    Callback.log("VendorInvoice deleted>>>>> ${item}")
    Callback {IWSCircuit.dispatch(Delete(item))}
    //$.props >>= (_.proxy.dispatch(Delete(item)))
  }

  def deleteLineFx(line1:LineVendorInvoice) = {
    val  deleted =line1.copy(deleted = true)
    val k =$.state.runNow().item.getOrElse(VendorInvoice[LineVendorInvoice]())
    val k2 = k.replaceLine(deleted)
    $.modState(s => s.copy(item =Some(k2)))
    Callback {IWSCircuit.dispatch(Update(k2))}
    //edited(k2)
  }

  def AddNewLineFx(line:LineVendorInvoice) = {
    val  created =line.copy(created = true)
    log.debug(s"New Line VendorInvoice  created>>>>>  ${line}")
    $.modState(s => s.copy(item = s.item.map(_.add(created.copy(transid =
      s.item.getOrElse(VendorInvoice[LineVendorInvoice]()).tid)))))
  }

  def  AddNewLine(line:LineVendorInvoice) = runLine[LineVendorInvoice](line , AddNewLineFx)
  def  deleteLine(line:LineVendorInvoice) = runLine[LineVendorInvoice](line , deleteLineFx)
  def  saveLine(line:LineVendorInvoice) = runLine[LineVendorInvoice](line , saveLineFx)
  def  delete(item:VendorInvoice[LineVendorInvoice]) = deletex[VendorInvoice[LineVendorInvoice]](item , deleteFx)
  def  edit(item:VendorInvoice[LineVendorInvoice]) = editx[VendorInvoice[LineVendorInvoice]](item , editFx)
  def  updateOid (idx:String) = updateCBField(idx, updateOidFx)
  def  updateAccount (idx:String) = updateCBField(idx, updateAccountFx)
  def  updateStore (idx:String) = updateCBField(idx, updateStoreFx)
  def  updateText (idx:String) = updateTxtField(updateTextFx)

  def  filterWith(line:LineVendorInvoice, search:String) = line.account.getOrElse("").contains(search)

   override def render(p: Props, s: State [VendorInvoice[LineVendorInvoice]] ):ReactElement = {
     val addStylesNew = Seq(bss.pullRight, bss.buttonXS)
     val addStylesSave = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))
     def  fx(d:VendorInvoice[LineVendorInvoice]):Callback = $.modState(s => s.copy(item = Some(d)))

    def saveButton = saveButtonx[VendorInvoice[LineVendorInvoice]](s.item.getOrElse(VendorInvoice[LineVendorInvoice]()),addStylesSave, " Save", fx )
    def newButton = newButtonx[VendorInvoice[LineVendorInvoice]](VendorInvoice[LineVendorInvoice](),addStylesNew, " New", fx )

    gitems = IWSCircuit.zoom(_.store.get.models.get(p.modelId)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[VendorInvoice[LineVendorInvoice]]].toSet
    val items = gitems.toList.sorted
    val header = List(saveButton, newButton)
    val contentTab1 = VendorInvoiceList( items, "VendorInvoice", item => edit(item), item => p.proxy.dispatch(Delete(item)))
    val contentTab2 = buildForm(p, s, items,header)
    buildFormTab(p, s, contentTab1, contentTab2)
  }


  def buildForm (p: Props, s:State[VendorInvoice[LineVendorInvoice]], items:List[VendorInvoice[LineVendorInvoice]], header:Seq[ReactElement]) = {
    val supplier =  IWSCircuit.zoom(_.store.get.models.get(p.accountModelId)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
    val store =  IWSCircuit.zoom(_.store.get.models.get(p.storeModelId)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
    val porder = s.item.getOrElse(VendorInvoice[LineVendorInvoice]().add(LineVendorInvoice(account = Some("4711"))))
    val  storeList=store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
    val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
    log.debug(s" VENDOR INVOICE account  >>>>>  ${s.item.map(_.account.getOrElse("50001")).getOrElse("50001")}")

    def buildContent = {
      <.div(bss.formGroup,
        <.div(^.cls := "container-fluid",
          <.div(^.cls := "row",
            <.div(^.cls := "col-md-24",
              <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed", //^.cellSpacing := 2.px,
                <.tbody(
                  <.tr(bss.formGroup, ^.height := 10.px, ^.cellSpacing := 2.px, ^.cellPadding := 2.px,
                    // <.td(^.cls :="col-xs-24 col-md-2",   ^.width:="10%",  buildItemZ1[String]("Id", s.item.map(_.id), "id")),
                    <.td(^.cls := "col-md-24 col-md-2", ^.width := "10%", ComboBox("id", itemsx = buildTransIdList(items), defValue = "002", evt = updateOid)),
                    <.td(^.cls := "col-md-24 col-md-2", ^.width := "10%", ComboBox("oid", itemsx = buildTransIdList(items), defValue = "001", evt = updateOid)),
                    <.td(^.cls := "col-md-24 col-md-8 col-md-offset-2", ^.width := "50%", ComboBox("Store", itemsx = storeList, defValue = s.item.map(_.store.getOrElse("50001")).getOrElse("50001"), updateStore _)),
                    <.td(^.cls := "col-md-24 col-md-12", ^.width := "50%", ComboBox("Supplier", itemsx = supplierList, defValue = s.item.map(_.account.getOrElse("70002")).getOrElse("70002"), updateAccount _))
                  ),

                  <.tr(bss.formGroup,
                    <.td(LineVendorInvoiceList(porder, AddNewLine, saveLine, deleteLine), ^.colSpan := 4)
                  )
                )
              )
            )
          )
        )
      )
    }

    BasePanel("Vendor Invoice", List(buildContent),header)
  }
}

