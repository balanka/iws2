package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.BankAccountList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.services.IWSCircuit
import diode.data.Pot
import diode.react.ReactPot._
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import com.kabasoft.iws.shared._
//import com.kabasoft.iws.shared.Lenses._
import com.kabasoft.iws.gui.macros._

import scalacss.ScalaCssReact._
//import monocle.macros.GenLens

object BANKACCOUNT {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(item: Option[BankAccount] = None, name:String="BankAccount")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
       Callback {
         IWSCircuit.dispatch(Refresh(BankAccount()))
       }
      def edit(itemx:Option[BankAccount]) = {
      $.modState(s => s.copy(item = itemx))
    }


    def edited(item:BankAccount) = {
      Callback.log("BankAccount edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:BankAccount) = {
      val cb = Callback.log("BankAccount deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "BankAccount"))
    }


    def updateIt(e: ReactEventI, set: (String, BANKACCOUNT.State ) =>  BANKACCOUNT.State) = {
      val r = e.target.value
      $.modState(s=>(set (r,s) ))
    }

    def updateId(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(id = r))))
    }
    def updateName(e: ReactEventI) = {
      val r = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(name = r))))
    }
    def updateDescription(e: ReactEventI) = {
      val r1 = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(description = r1))))
    }
     def updateBIC(e: ReactEventI) = {
       val r1 = e.target.value
       $.modState(s => s.copy(s.item.map( z => z.copy(bic = r1))))
     }


     def renderItem(item: Bank) : ReactElement =
        <.li(bss.listGroup.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.description ,^.paddingLeft:=5)
        )

     def  m :(String , BANKACCOUNT.State) =>  BANKACCOUNT.State = (p, s) => s.copy(s.item.map(z => z.copy(id = p)))

      def buildForm(s:State): ReactElement =
        //<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(^.height := 20,
                   buildWItem[String]("id", s.item.map(_.id),"id",updateId),
                   buildWItem[String]("name", s.item.map(_.name), "", updateName),
                   buildWItem[String]("description", s.item.map(_.description), "", updateDescription)),
              <.tr(^.height := 20,
                   buildWItem[String]("BIC", s.item.map(_.bic), "", updateBIC),
                   buildWItem[BigDecimal]("debit", s.item.map(_.debit), 0.0, noAction),
                   buildWItem[BigDecimal]("credit", s.item.map(_.credit), 0.0, noAction)
                 )
            )
          )

     def render(p: Props, s: State) ={
       val items =  IWSCircuit.zoom(_.store.get.models.get(12)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[BankAccount]]
       def saveButton = Button(Button.Props(edited(s.item.getOrElse(BankAccount())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(BankAccount())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
       Panel(Panel.Props("BankAccount"), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
         p.proxy().renderFailed(ex => "Error loading"),
         p.proxy().renderPending(_ > 500, _ => "Loading..."),
         AccordionPanel("Edit", Seq(buildForm(s)), List(newButton,saveButton)),
        // p.proxy().render(
          BankAccountList(items,
            Some(item => edit(Some(item))),
            Some(item => p.proxy.dispatch(Delete(item))))
         //TabComponent(Seq(item1, item2, item3)))
        )
       )
     }

  }

  val component = ReactComponentB[Props]("BankAccount")
    //.initialState(State(name="Customer"))
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
