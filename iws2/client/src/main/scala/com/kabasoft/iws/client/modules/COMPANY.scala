package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.CompanyList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.IWSCircuit

import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros.{TabComponent, _}
import com.kabasoft.iws.gui.macros.TabItem
import scalacss.ScalaCssReact._
//import monocle.macros.GenLens

object COMPANY {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(item: Option[Company] = None, name:String="Company")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
       Callback {
         IWSCircuit.dispatch(Refresh(Company()))
       }
      def edit(itemx:Option[Company]) = {
      $.modState(s => s.copy(item = itemx))
    }


    def edited(item:Company) = {
      Callback.log("Company edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:Company) = {
      val cb = Callback.log("Company deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "Company"))
    }


    def updateIt(e: ReactEventI, set: (String, COMPANY.State ) =>  COMPANY.State) = {
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
    def updateStreet(e: ReactEventI) = {
      val r1 = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(street = r1))))
    }
    def updateCity(e: ReactEventI) = {
      val r2 = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(city = r2))))
    }
    def updateState(e: ReactEventI) = {
      val r3 = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(state = r3))))
    }
    def updateZip(e: ReactEventI) = {
      val r4 = e.target.value
      $.modState(s => s.copy(s.item.map( z => z.copy(zip = r4))))
    }
    
    
    case class Address(id:String, street:String ="", zipCode:String ="", city:String ="", country:String="DE", route:String, title:String)
     def renderItem(item: Company) : ReactElement =
        <.li(bss.listGroup.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.street ,^.paddingLeft:=5),
          <.span(item.city ,^.paddingLeft:=5),
          <.span(item.zip ,^.paddingLeft:=5)
        )

     def  m :(String , COMPANY.State) =>  COMPANY.State = (p, s) => s.copy(s.item.map( z => z.copy(id = p)))

     def buildFormTab(p: Props, s: State, items:List[Company]): Seq[ReactElement] =
       List(<.div(bss.formGroup,
         TabComponent(Seq(
           TabItem("vtab1", "List", "#vtab1", true,
             CompanyList(items, item => edit(Some(item)), item => p.proxy.dispatch(Delete(item)))),
           TabItem("vtab2", "Form", "#vtab2", false, buildForm(s))
         ))
       ))
      def buildForm(s:State) =
        <.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(^.height := 20,
                   buildWItem[String]("id", s.item.map(_.id),"id",updateId),
                   buildWItem[String]("name", s.item.map(_.name), "", updateName),
                   buildWItem[String]("street", s.item.map(_.street), "", updateStreet)
                 ),
              <.tr(^.height := 20,
                    buildWItem[String]("city", s.item.map(_.city), "", updateCity),
                    buildWItem[String]("state", s.item.map(_.state), "", updateState),
                    buildWItem[String]("zip", s.item.map(_.zip), "", updateZip)
                 ),
              <.tr(^.height := 20,
                buildWItem[String]("Bank acc", s.item.map(_.bankAccountId), "", updateCity),
                buildWItem[String]("Purchasing Cl.Acc", s.item.map(_.purchasingClearingAccountId), "", updateState),
                buildWItem[String]("Sales Cl.Acc", s.item.map(_.salesClearingAccountId), "", updateZip)
              ),
              <.tr(^.height := 20,
                buildWItem[String]("Payment Cl.Acc", s.item.map(_.paymentClearingAccountId), "", updateCity),
                buildWItem[String]("Settlement Cl.Acc", s.item.map(_.settlementClearingAccountId), "", updateState),
                buildWItem[String]("Sales Cl.Acc", s.item.map(_.salesClearingAccountId), "", updateZip)
              ),
              <.tr(^.height := 20,
                buildWItem[String]("taxCode", s.item.map(_.taxCode), "", updateCity),
                buildWItem[String]("VAT ID", s.item.map(_.vatId), "", updateState),
                buildWItem[Integer]("Periode", s.item.map(_.periode), 2016, updateZip),
                buildWItem[Integer]("Next periode", s.item.map(_.nextPeriode), 2017, updateZip)
              )
            )
          )
        )

     def render(p: Props, s: State) = {
       val items =  IWSCircuit.zoom(_.store.get.models.get(10)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Company]]
       def saveButton = Button(Button.Props(edited(s.item.getOrElse(Company())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Company())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
         BasePanel("Company", buildFormTab(p,s, items), List(newButton,saveButton))
     }

  }

  val component = ReactComponentB[Props]("Company")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
