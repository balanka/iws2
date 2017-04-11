package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.{FDocumentList, LineFDocumentList}
import com.kabasoft.iws.client.modules.Utilities._
import com.kabasoft.iws.gui.BasePanel
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.macros._
import com.kabasoft.iws.gui.services.IWSCircuit
import com.kabasoft.iws.shared._
import diode.data.Ready
import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{BackendScope, Callback, ReactElement, _}

import scalacss.ScalaCssReact._


class SalesBackend ($: BackendScope[Props[FDocument[LineFDocument], LineFDocument , Store, Customer], State[FDocument[LineFDocument]]]) extends
  FDocumentBackend  ($){



  override def buildForm (p: Props[FDocument[LineFDocument], LineFDocument, Store, Customer], s:State[FDocument[LineFDocument]],
                          otems:List[FDocument[LineFDocument]],items:List[FDocument[LineFDocument]], header:Seq[ReactElement]) = {

    val supplier =  IWSCircuit.zoom(_.store.get.models.get(p.accountModelId)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Customer]))).get.items.asInstanceOf[List[Customer]].toSet
    val store =  IWSCircuit.zoom(_.store.get.models.get(p.storeModelId)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
    val porder = s.item.getOrElse(p.instance.add(p.instance2))
    val  storeList=store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
    val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
    //log.debug(s" FDocument  account  >>>>>  ${items}" )
    log.debug(s" FDocument  account  >>>>>  ${s.item.map(_.account.getOrElse("50001")).getOrElse("50001")}" )

    def buildContent = {
      <.div(bss.formGroup,
        <.div(^.cls := "container-fluid",
          <.div(^.cls := "row",
            <.div(^.cls := "col-md-24",
              <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed", //^.cellSpacing := 2.px,
                <.tbody(
                  <.tr(bss.formGroup, ^.height := 10.px, ^.cellSpacing := 2.px, ^.cellPadding := 2.px,
                    // <.td(^.cls :="col-xs-24 col-md-2",   ^.width:="10%",  buildItemZ1[String]("Id", s.item.map(_.id), "id")),
                    <.td(^.cls := "col-md-24 col-md-2", ^.width := "10%", ComboBox("id", itemsx = buildTransIdList(items), defValue =  s.item.map(_.id).getOrElse(""), updateId _)),
                    <.td(^.cls := "col-md-24 col-md-2", ^.width := "10%", ComboBoxL("oid", itemsx = buildTransIdList(otems), s.item.map(_.oid).getOrElse(0), updateOid  _)),
                    <.td(^.cls := "col-md-24 col-md-8", ^.width := "50%", ComboBox("Store", itemsx = storeList, defValue = s.item.map(_.store.getOrElse("")).getOrElse(""), updateStore _)),
                    <.td(^.cls := "col-md-24 col-md-12", ^.width := "50%", ComboBox("Supplier", itemsx = supplierList, defValue = s.item.map(_.account.getOrElse("")).getOrElse(""), updateAccount _))
                  ),

                  <.tr(bss.formGroup,
                    <.td(LineFDocumentList(porder, p.instance2, AddNewLine, saveLine, deleteLine), ^.colSpan := 4)
                  )
                )
              )
            )
          )
        )
      )
    }

    BasePanel(p.title, List(buildContent),header)
  }
}

