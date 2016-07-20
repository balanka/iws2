package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.BankList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.services.IWSCircuit
import diode.data.Pot
import diode.react._
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._

import scalacss.ScalaCssReact._

object BANK {

  @inline private def bss = GlobalStyles.bootstrapStyles

   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(item: Option[Bank] = None, name:String="Bank")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) =
       Callback {
         IWSCircuit.dispatch(Refresh(Bank()))
       }
      def edit(itemx:Option[Bank]) = {
      $.modState(s => s.copy(item = itemx))
    }


    def edited(item:Bank) = {
      Callback.log("Bank edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:Bank) = {
      val cb = Callback.log("Bank deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "Bank"))
    }

    def updateIt(e: ReactEventI, set: (String, BANK.State ) =>  BANK.State) = {
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
     def renderItem(item: Bank) : ReactElement =
        <.li(bss.listGroup.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.description ,^.paddingLeft:=5)
        )

      def buildForm(s:State): ReactElement =
        //<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(^.height := 20,
                   buildWItem[String]("id", s.item.map(_.id),"id",updateId),
                   buildWItem[String]("name", s.item.map(_.name), "", updateName),
                   buildWItem[String]("description", s.item.map(_.description), "", updateDescription)
                 )
            )
          )
     def buildFormTab(p: Props, s: State, items:List[Bank]): Seq[ReactElement] =
       List(<.div(bss.formGroup,
         TabComponent(Seq(
           TabItem("vtab1", "List", "#vtab1", true,BankList(items, Some(item => edit(Some(item))), Some(item => p.proxy.dispatch(Delete(item))))),
           TabItem("vtab2", "Form", "#vtab2", false,buildForm(s))))
       ))
     def render(p: Props, s: State) = {
       val items =  IWSCircuit.zoom(_.store.get.models.get(11)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Bank]]
       def saveButton = Button(Button.Props(edited(s.item.getOrElse(Bank())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Bank())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
         BasePanel("Bank", buildFormTab(p, s, items), List(newButton,saveButton))
     }
  }

  val component = ReactComponentB[Props]("Bank")
    .initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
