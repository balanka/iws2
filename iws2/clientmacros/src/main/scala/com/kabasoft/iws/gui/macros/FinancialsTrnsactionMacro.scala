package com.kabasoft.iws.gui.macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import com.kabasoft.iws.shared._

/*
object FinancialsTrnsactionMacro {

  import scala.language.experimental.macros
  def makeForm[T<:Transaction[L], L<:LineTransaction](trans:T, instance:L):IWSBackendTrait[T] = macro makeFormImpl[T,L]
  def makeFormImpl[T:c.WeakTypeTag, L:c.WeakTypeTag] (c: Context)  (trans:c.Expr[T], instance:c.Expr[L]): c.Tree = {
    val st0 = c.weakTypeOf[L]
    val st1 = c.weakTypeOf[T]
    val n = c.weakTypeOf[L].typeSymbol.name.toTermName.toString
    import c.universe._

   val qs = q"""

    import java.util.Date
    import com.kabasoft.iws.gui.BasePanel
    import com.kabasoft.iws.gui.BasePanel2
    import com.kabasoft.iws.gui.StringUtils._
    import com.kabasoft.iws.gui.Utils._
    import com.kabasoft.iws.gui.logger._
    import com.kabasoft.iws.gui.macros.Bootstrap.Button.Props
    import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
    import com.kabasoft.iws.gui.macros._
    import com.kabasoft.iws.gui.services.IWSCircuit
    import com.kabasoft.iws.shared._
    import diode.data.{Pot, Ready}
    import japgolly.scalajs.react._
    import japgolly.scalajs.react.vdom.prefix_<^._
    import org.widok.moment.Moment
    import scalacss.ScalaCssReact._
    import scala.scalajs.js

    object LineVendorInvoiceListxx {
      @inline private def bss = GlobalStyles.bootstrapStyles
      case class State(item: Option[$st0]= None,  search:String="", edit:Boolean = false)
      case class Props(porder: $st1[$st0],
                       newLine:$st0 =>Callback,
                       saveLine:$st0 =>Callback,
                       deleteLine:$st0 =>Callback)
      class Backend(bs: BackendScope[Props, State]) {

        def mounted(props: Props) = Callback {
          IWSCircuit.dispatch(Refresh(Account()))
        }

        def edit(line:$st0) =  bs.modState(s => s.copy(item = Some(line)))

        def updateAccount(id:String) = {
          val accountId = id.substring(0, id.indexOf(""))
          bs.modState(s => s.copy(item = s.item.map(_.copy(account = Some(accountId)))))>>
            setModified
        }

        def updateOAccount(id:String) = {
          val oaccountId = id.substring(0, id.indexOf(""))
          bs.modState(s => s.copy(item = s.item.map(_.copy(oaccount = Some(oaccountId)))))>> setModified
        }

        def updateSide(e: ReactEventI) = {
          e.preventDefault()
          val sideId = e.target.checked
          bs.modState(s => s.copy(item = s.item.map(_.copy(side = sideId)))) >> setModified
        }

        def updateDuedate(e: ReactEventI) = {
          e.preventDefault()
          val l = e.target.value
          Moment.locale("de_DE")
          val m = Moment(l)
          val _date=m.toDate()
          val _helsenkiOffset = 2*60*60000
          val _userOffset = _date.getTimezoneOffset()*60000
          val _helsenkiTime = new Date((_date.getTime()+_helsenkiOffset+_userOffset).toLong)

          bs.modState(s => s.copy(item = s.item.map(_.copy(duedate = Some(_helsenkiTime)))))>> setModified
        }
        def updateText(e: ReactEventI) = {
          e.preventDefault()
          val l =e.target.value
          bs.modState(s => s.copy(item = s.item.map(_.copy(text = l))))>> setModified
        }

        def updateAmount(e: ReactEventI, s1:State) = {
          e.preventDefault()
          val l =e.target.value.toDouble
          bs.modState(s => s.copy(item = s.item.map(_.copy(amount = l))))>> setModified
        }

        def delete(line:$st0, deleteLineCallback:$st0 =>Callback) = deleteLineCallback(line)
        def save(line:$st0, saveLineCB:$st0 =>Callback) = saveLineCB(line)   >> Callback {resetState }
        def resetState =    bs.modState(s => s.copy(item = None).copy(edit = false)).runNow()
        def setModified  =  bs.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )
        def newLine(line:$st0, newLineCallback:$st0 =>Callback) = newLineCallback(line)>> edit(line)


        def render(p:Props, s: State) = {
          val lines  = p.porder.lines.getOrElse(Seq.empty[$st0])
          def items =  IWSCircuit.zoom(_.store.get.models.get(9)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Account]]
          def saveButton = Button(Button.Props(save(s.item.getOrElse($instance),p.saveLine),
            addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, "")
          def newButton = Button(Button.Props( newLine($instance, p.newLine),
            addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "")
          def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list.filter(_.id != "-1") .sortBy(_.id) map (iws =>(iws.id+""+iws.name))

          def editLine : Seq [TagMod] = List(
            <.div(^.cls :="row",
              buildSItemN("Account", itemsx = buildIdNameList(items), defValue = "0001", evt = updateAccount, "col-xs-3"),
              buildWItemN[Boolean]("D/C", s.item.map(_.side), true, evt = updateSide, "col-xs-1" ),
              buildSItemN("OAccount", itemsx = buildIdNameList(items), defValue = "7", evt = updateOAccount, "col-xs-3"),
              buildWItemN[BigDecimal]("Amount", s.item.map(_.amount), 0.0, updateAmount(_, s),"col-xs-2"),
              buildField("Duedate", updateDuedate,"col-xs-2")),
            <.div(^.cls :="row",buildAreaItem("Text", s.item.map(_.text), "", updateText,"col-xs-12"))
          )

          renderBase(p, s, lines, saveButton _, newButton _, editLine _)
        }

        private def renderBase(p: Props, s: State, lines: Seq[$st0],
                               savex: ()   => ReactComponentU[Button.Props, Unit, Unit, TopNode],
                               newx: ()    => ReactComponentU[Button.Props, Unit, Unit, TopNode],
                               editLine: () => Seq[TagMod]) = {
          <.div(bss.formGroup,
            BasePanel2(renderHeader2(LineFin_Trans_Headers,List(savex(), newx()), 15), lines.sortBy(_.tid)(Ordering[Long].reverse) map (e => renderItem(e, p, s))),
            <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
              <.tbody(
                <.tr(bss.formGroup, ^.height := 30.px, ^.maxHeight := 30.px,
                  if (lines.size > 0) editLine() else  Seq.empty[ReactElement]
                )
              )
            )
          )
        }

        def renderItem(item:$st0, p: Props, s:State) = {
          def editButton =  Button(Button.Props(edit(item), addStyles = Seq(bss.pullRight, bss.buttonXS,
            bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
          def deleteButton = Button(Button.Props(delete (item,p.deleteLine), addStyles = Seq(bss.pullRight, bss.buttonXS,
            bss.buttonOpt(CommonStyle.danger))), Icon.trashO, "")
          val defaultL = LinePurchaseOrder()
          val cond = item.tid==s.item.getOrElse(defaultL).tid
          val style = if(cond) bss.listGroup.itemOpt(CommonStyle.warning) else bss.listGroup.itemOpt(CommonStyle.success)

          <.li( style,
            ^.fontSize:=12.px, ^.fontWeight:=50.px, ^.maxHeight:=30.px, ^.height:=30.px, ^.alignContent:="center", ^.tableLayout:="fixed",
            <.span(deleteButton , ^.alignContent:="left"),
            <.span(item.id, ^.padding:=10.px),
            <.span(item.account, ^.padding:=10.px),
            <.input.checkbox(^.checked := item.side, ^.padding:=10.px),
            <.span(item.oaccount ,^.padding:=10.px),
            <.span("%06.2f".format(item.amount.bigDecimal),^.padding:=10.px),
            <.span( Moment(item.duedate.get.getTime).format("DD.MM.YYYY"),^.paddingLeft:=10, ^.paddingRight:=30),
            <.span(item.text, ^.paddingRight:=5),
            <.span(editButton,^.alignContent:="center")
          )
        }
      }

      val component = ReactComponentB[Props]($n)
        .initialState(State( search ="0"))
        .renderBackend[Backend]
        .componentDidMount(scope => scope.backend.mounted(scope.props))
        .build

      def apply( porder:VendorInvoice[$st0],
                 newLine:$st0 =>Callback,
                 saveLine:$st0 =>Callback,
                 deleteLine:$st0 => Callback) =
        component(Props(porder, newLine, saveLine, deleteLine))
    }
    """
"""
 object TRANSACTION {

   @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
         case class State(item: Option[$st1[$st0]] = None)
         @volatile var gitems = Set.empty[$st1[$st0]]
         def updateOid1(idx:String,   $$: BackendScope[Props,State]) = {
            $$.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong))))
         }
         def updateStore(idx:String, bs: BackendScope[Props,State]) = {
           val storeId = idx.substring(0, idx.indexOf(""))
           log.debug(s"store is "+storeId)
           bs.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId)))))
         }
         def updateAccount(idx: String, bs: BackendScope[Props,State]) = {
           val supplierId=idx.substring(0, idx.indexOf("|"))
           bs.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId)))))
         }
         def updateText(e: ReactEventI, bs: BackendScope[Props,State]) = {
           val txt = e.target.value
           bs.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))
         }
         class Backend( $$: BackendScope[Props, State]) {

     implicit def orderingById[A <: $st1[$st0]]: Ordering[A] = {Ordering.by(e => (e.tid, e.tid))}

     val r = List( updateOid1 _, updateStore _, updateAccount _, updateText _, edit _,  edited _)

     def mounted(props: Props) = {

       Callback {
               IWSCircuit.dispatch(Refresh(Supplier()))
               IWSCircuit.dispatch(Refresh(Store()))
               IWSCircuit.dispatch(Refresh($trans))
             }
           }
          def edit(item:Option[$st1[$st0]]) = {
             val d =item.getOrElse($trans)
            $$.modState(s => s.copy(item = Some(d)))
           }

     def updateOid(idx:String) = {
              $$.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
           }
           def updateOid1(idx:String,   $$: BackendScope[Props,State]) = {
             $$.modState(s => s.copy(item = s.item.map(_.copy(oid = idx.toLong)))) >>setModified
           }
           def updateStore(idx:String) = {
             val storeId = idx.substring(0, idx.indexOf("|"))
             log.debug(s"store is "+storeId)
              $$.modState(s => s.copy(item = s.item.map(_.copy(store = Some(storeId))))) >>setModified
           }

     def updateAccount(idx: String) = {
             val supplierId=idx.substring(0, idx.indexOf(""))
              $$.modState(s => s.copy(item = s.item.map(_.copy(account = Some(supplierId))))) >>setModified
           }
           def updateText(e: ReactEventI) = {
             val txt = e.target.value
             $$.modState(s => s.copy(item = s.item.map(_.copy(text = txt))))  >>setModified
           }
           def edited(order:$st1[$st0]) = {
             $$.modState(s => s.copy(item =Some(order)))
             Callback {IWSCircuit.dispatch(Update(order))}
           }
           def runIt =  $$.state.runNow().item.getOrElse($trans)
           def saveLine(linex:$st0) =  {
             val k = runIt
             val k2 = k.replaceLine( linex.copy(transid = k.tid))

       runCB(k2)

     }
           def setModified  =   $$.modState(s => s.copy(item = if(!s.item.map(_.created).getOrElse(false)) s.item.map(_.copy(modified = true)) else s.item) )
           def runCB (itemx:$st1[$st0]) = Callback {
             IWSCircuit.dispatch(Update(itemx))

       val ro = itemx.getLines.filter(_.created == true).map(e => itemx.replaceLine(e.copy(created = false).copy(modified = false)))
             if (!ro.isEmpty) {
               val setLineID = ro.head.copy(lines = Some(itemx.getLines map (e => if (e.tid <= 0) e.copy(tid = e.tid - 1) else e)))
                 $$.modState(s => s.copy(item = Some(setLineID))).runNow()
             }else {
                 $$.modState(s => s.copy(item = Some(itemx.copy(modified = false)))).runNow()
             }

     }

     def delete(item:$st1[$st0]) = {
              $$.props >>= (_.proxy.dispatch(Delete(item)))
           }
           def runDelete(item1:$st1[$st0]) =   {
             IWSCircuit.dispatch(Update(item1))
             Callback {
                 $$.modState(s => s.copy(item = Some(item1))).runNow()
             }
           }
           def deleteLine(line1:$st0) = {
             val  deleted =line1.copy(deleted = true)
             val k =  $$.state.runNow().item.getOrElse($trans)
             val k2 = k.replaceLine(deleted)
             edited(k2)
           }

     def AddNewLine(line:$st0) = {
             val  created =line.copy(created = true)
                $$.modState(s => s.copy(item = s.item.map(_.add(created.copy(transid=
                       s.item.getOrElse($trans).tid)))))
           }

     def filterWith(line:$st0, search:String) =
             line.account.getOrElse("").contains(search)

     def buildFormTab(p: Props, s: State, items:List[$st1[$st0]],header:Seq[ReactElement]): ReactElement =
               TabComponent(Seq(
                 TabItem("vtab1", "List", "#vtab1", true,
                   VendorInvoiceList( items, "VendorInvoice", item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
                 TabItem("vtab2", "Form", "#vtab2", false, buildForm(p, s, items,header))
               ))

     def render(p: Props, s: State) = {

       def saveButton = Button(Button.Props(edited(s.item.getOrElse($trans)),
               addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save")
             def newButton = Button(Button.Props(edit(Some($trans)),
               addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")
               gitems = IWSCircuit.zoom(_.store.get.models.get(112)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[$st1[$st0]]].toSet
             val items = gitems.toList.sorted
             val header =  List(saveButton, newButton)
             buildFormTab(p, s, items, header)
           }

     def buildForm (p: Props, s:State, items:List[$st1[$st0]], header:Seq[ReactElement]) = {
             val supplier =  IWSCircuit.zoom(_.store.get.models.get(1)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Supplier]))).get.items.asInstanceOf[List[Supplier]].toSet
             val store =  IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).getOrElse(Ready(Data(List.empty[Store]))).get.items.asInstanceOf[List[Store]].toSet
             val porder = s.item.getOrElse($trans.add($instance))
             val  storeList=store.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
             val  supplierList=supplier.toList.filter(_.id !="-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))
       BasePanel("Vendor Invoice",
              List(<.div(bss.formGroup,
               <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
                 <.tbody(
                   <.tr(bss.formGroup, ^.height := 10.px,
                     buildItem[String]("Id", s.item.map(_.id), "id"),
                     //buildWItem[Long]("oid", s.item.map(_.oid), 1L, updateOid),
                     buildSItem("oid", itemsx = buildTransIdList(items) , defValue = "001", evt = updateStore),
                     buildSItem("Store", itemsx = storeList,defValue = "0001", evt = updateStore),
                     buildSItem("Supplier", itemsx = supplierList, defValue = "KG", evt = updateAccount),
                     buildWItem("Text", s.item.map(_.text), defValue = "txt", evt = updateText)
                   )
                 )
               ),
                 LineVendorInvoiceListxx(porder, AddNewLine, saveLine, deleteLine)
             )),header)
           }
         }
   val component = ReactComponentB[Props]("VendorInvoice")
           .initialState(State())
           .renderBackend[Backend]
           .componentDidMount(scope => scope.backend.mounted(scope.props))
           .build

   def apply( proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))
       }
       TRANSACTION


  }
}
*/
