package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.FDocumentList
import com.kabasoft.iws.client.modules.Utilities._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import japgolly.scalajs.react.{BackendScope, Callback, ReactElement, _}


abstract class FDocumentBackend [C,D] ($: BackendScope[Props[FDocument[LineFDocument], LineFDocument, C, D],
                                          State[FDocument[LineFDocument]]]) extends
               Backendx [FDocument[LineFDocument], LineFDocument, C, D, Props[FDocument[LineFDocument],
                        LineFDocument, C, D], State[FDocument[LineFDocument]]] ($){

  @inline protected def bss = GlobalStyles.bootstrapStyles
  @volatile var gitems = Set.empty[FDocument[LineFDocument]]
  @volatile var oitems = Set.empty[FDocument[LineFDocument]]

  implicit def orderingById[A <: FDocument[LineFDocument]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}


  def mounted(p: Props[FDocument[LineFDocument], LineFDocument, C, D])  = {

    Callback {
      IWSCircuit.dispatch(Refresh(p.instance4))
      IWSCircuit.dispatch(Refresh(p.instance3))
      IWSCircuit.dispatch(Refresh(p.instance))
    }
  }

  def editFx(d:FDocument[LineFDocument]) = $.modState(s => s.copy(item = Some(d)))
  def updateOidFx(idx:Long) = $.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
  def updateIdFx(idx:String)=Callback.empty

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


  def saveLineFx(instance:FDocument[LineFDocument],linex:LineFDocument) =  {
    val k = $.state.runNow().item.getOrElse(instance)
    val k2 = k.replaceLine( linex.copy(transid = k.tid))

    runCB(k2)

  }
  def setModified  = $.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )

  def runCB(itemx: FDocument[LineFDocument]) = Callback {
    IWSCircuit.dispatch(Update(itemx))
    val ro = itemx.getLines.filter(_.created == true).map(e => itemx.replaceLine(e.copy(created = false).copy(modified = false)))
    if (!ro.isEmpty) {
      val setLineID = ro.head.copy(lines = Some(itemx.getLines map (e => if (e.tid <= 0) e.copy(tid = e.tid - 1) else e)))
      $.modState(s => s.copy(item = Some(setLineID))).runNow()
    } else {
      $.modState(s => s.copy(item = Some(itemx.copy(modified = false)))).runNow()
    }

  }

  def deleteFx(item:FDocument[LineFDocument]) = {
    Callback.log("FDocument deleted>>>>> ${item}")
    Callback {IWSCircuit.dispatch(Delete(item))}
  }

  def deleteLineFx(instance:FDocument[LineFDocument],line1:LineFDocument) = {
    val  deleted =line1.copy(deleted = true)
    val k =$.state.runNow().item.getOrElse(instance)
    val k2 = k.replaceLine(deleted)
    $.modState(s => s.copy(item =Some(k2)))
    Callback {IWSCircuit.dispatch(Update(k2))}
  }

  def AddNewLineFx(instance:FDocument[LineFDocument],line:LineFDocument) = {
    val  created =line.copy(created = true)
    log.debug(s"New Line FDocument  created>>>>>  ${created}")
    $.modState(s => s.copy(item = s.item.map(_.add(created.copy(transid =
      s.item.getOrElse(instance).tid)))))
  }

  def  AddNewLine(instance:FDocument[LineFDocument], line:LineFDocument) = runLine1[FDocument[LineFDocument],LineFDocument](instance, line, AddNewLineFx)
  def  deleteLine(instance:FDocument[LineFDocument], line:LineFDocument) = runLine1[FDocument[LineFDocument], LineFDocument](instance, line,  deleteLineFx)
  def  saveLine(instance:FDocument[LineFDocument],line:LineFDocument) = runLine1[FDocument[LineFDocument],LineFDocument](instance, line,  saveLineFx)
  def  delete(item:FDocument[LineFDocument]) = deletex[FDocument[LineFDocument]](item , deleteFx)
  def  edit(item:FDocument[LineFDocument]) = editx[FDocument[LineFDocument]](item , editFx)
  def  updateOid (idx:Long) = updateCBFieldL(idx, updateOidFx)
  def  updateId (idx:String) = updateCBField(idx, updateIdFx)
  def  updateAccount (idx:String) = updateCBField(idx, updateAccountFx)
  def  updateStore (idx:String) = updateCBField(idx, updateStoreFx)
  def  updateText (idx:String) = updateTxtField(updateTextFx)
  def  filterWith(line:LineFDocument, search:String) = line.account.getOrElse("").contains(search)

  override def render(p: Props[FDocument[LineFDocument], LineFDocument, C, D], s: State [FDocument[LineFDocument]] ):ReactElement = {
    val addStylesNew = Seq(bss.pullRight, bss.buttonXS)
    val addStylesSave = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))
    def  fx(d:FDocument[LineFDocument]):Callback = $.modState(s => s.copy(item = Some(d)))
    def saveButton = saveButtonx[FDocument[LineFDocument]](s.item.getOrElse(p.instance),addStylesSave, " Save", fx )
    def newButton = newButtonx[FDocument[LineFDocument]](p.instance,addStylesNew, " New", fx )
    gitems = IWSCircuit.zoom(_.store.get.models.get(p.modelId)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[FDocument[LineFDocument]]].toSet
    oitems  = IWSCircuit.zoom(_.store.get.models.get(p.otransModelId)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[FDocument[LineFDocument]]].toSet
    val items = gitems.toList.sorted
    val otems = oitems.toList.sorted
    log.debug(s"Notemsotemsotemsotemsotems>>>>  ${otems}")
    val header = List(saveButton, newButton)
    val contentTab1 = FDocumentList( items, p.title, item => edit(item), item => p.proxy.dispatch(Delete(item)))
    val contentTab2 = buildForm(p, s, otems, items,header)
     buildFormTab(p, s, contentTab1, contentTab2)
  }


  def buildForm (p: Props[FDocument[LineFDocument], LineFDocument, C,D], s:State[FDocument[LineFDocument]],
                       otems:List[FDocument[LineFDocument]], items:List[FDocument[LineFDocument]], header:Seq[ReactElement]) :ReactElement

}

