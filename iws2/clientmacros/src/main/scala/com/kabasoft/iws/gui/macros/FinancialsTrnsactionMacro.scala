package com.kabasoft.iws.gui.macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import com.kabasoft.iws.shared._


object FinancialsTrnsactionMacro {
  import scala.language.experimental.macros
  def makeForm[T<:Transaction[L], L<:LineTransaction](instance:L):IWSBackendTrait[T] = macro makeFormImpl[T,L]
  def makeFormImpl[T:c.WeakTypeTag, L:c.WeakTypeTag] (c: Context)  (instance:c.Expr[L]): c.Tree = {
    val st0 = c.weakTypeOf[L]
    val st1 = c.weakTypeOf[T]
    val n = c.weakTypeOf[L].typeSymbol.name.toTermName.toString
   // val a = c.weakTypeOf[T].typeSymbol.name.toTypeName

    import c.universe._

    val r = q""" printn($n) """

    q"""
       import java.util.Date
       import com.kabasoft.iws.gui.BasePanel2
       import com.kabasoft.iws.gui.StringUtils._
       import com.kabasoft.iws.gui.Utils._
       import com.kabasoft.iws.gui.logger._
       import com.kabasoft.iws.gui.macros.Bootstrap.Button.Props
       import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
       import com.kabasoft.iws.gui.macros._
       import com.kabasoft.iws.gui.services.IWSCircuit
       import com.kabasoft.iws.shared._
       import japgolly.scalajs.react._
       import japgolly.scalajs.react.vdom.prefix_<^._
       import org.widok.moment.Moment

 import scalacss.ScalaCssReact._

 object LineFinancialsTransactionList {
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
             def buildIdNameList [A<:Masterfile](list: List[A]): List[String]= list.filter(_.id != "-1") .sortBy(_.id) map (iws =>(iws.id+"|"+iws.name))

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

           MasterfileBackend
          """

  }
}

