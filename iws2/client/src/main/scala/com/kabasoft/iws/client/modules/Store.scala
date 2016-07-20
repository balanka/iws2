package com.kabasoft.iws.client.modules

import com.kabasoft.iws.client.components.StoreList
import com.kabasoft.iws.gui.Utils._
import com.kabasoft.iws.gui._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.gui.logger._
import com.kabasoft.iws.gui.services.{IWSCircuit, RootModel}
import diode.ModelR
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros.{TabComponent, _}
import com.kabasoft.iws.gui.macros.TabItem
import scalacss.ScalaCssReact._

object STORE {

  @inline private def bss = GlobalStyles.bootstrapStyles
  @volatile var itemsx = Set.empty[Store]
  implicit def orderingById[A <: Store]: Ordering[A] = {Ordering.by(e => (e.id, e.id))}
   case class Props(proxy: ModelProxy[Pot[Data]])
   case class State(item: Option[Store] = None, name:String="Store")
   class Backend($: BackendScope[Props, State]) {
     def mounted(props: Props) = {
       IWSCircuit.subscribe(IWSCircuit.zoom(_.store.get.models.get(2).get)) (listener)

       def listener(cursor: ModelR[RootModel[IWS,IWS], Pot[ContainerT[IWS,IWS]]]): Unit = {
         itemsx  = collection.immutable.SortedSet[Store]() ++
           IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Store]].toSet
         log.debug(s" Store Listener ${itemsx}")
         render(props,$.state.runNow())
       }
       Callback {
         IWSCircuit.dispatch(Refresh(Store()))
       }
     }

      def edit(itemx:Option[Store]) = {
      $.modState(s => s.copy(item = itemx))
    }

    def edited(item:Store) = {
      Callback.log("Store edited>>>>> " +item)  >>
      $.props >>= (_.proxy.dispatch(Update(item)))

     }

    def delete(item:Store) = {
      val cb = Callback.log("Store deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
       cb >> $.modState(s => s.copy(name = "Store"))
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
     def renderItem(item: Customer) : ReactElement =
        <.li(bss.listGroup.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
          <.span(item.id),
          <.span(item.name ,^.paddingLeft:=5),
          <.span(item.street ,^.paddingLeft:=5),
          <.span(item.city ,^.paddingLeft:=5),
          <.span(item.zip ,^.paddingLeft:=5)
        )

      def buildForm(s:State): ReactElement =
        //<.div(bss.formGroup,
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
                 )
            )
          )
     def buildFormTab(p: Props, s: State, items:List[Store]): Seq[ReactElement] =
       List(<.div(bss.formGroup,
         TabComponent(Seq(
           TabItem("vtab1", "List", "#vtab1", true, StoreList(items, item => edit(Some(item.asInstanceOf[Store])), item => p.proxy.dispatch(Delete(item)))),
           TabItem("vtab2", "Form", "#vtab2", false,buildForm(s))))
       ))
     def render(p: Props, s: State) = {
       def saveButton = Button(Button.Props(edited(s.item.getOrElse(Store())), addStyles = Seq(bss.pullRight, bss.buttonXS,
         bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
       def newButton =  Button(Button.Props(edit(Some(Store())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")
       if( itemsx.filter(!_.id.isEmpty).size <=1) {
         itemsx = IWSCircuit.zoom(_.store.get.models.get(2)).eval(IWSCircuit.getRootModel).get.get.items.asInstanceOf[List[Store]].toSet
       }
       val items = itemsx.toList.sorted
       BasePanel("Store", buildFormTab(p, s,items), List(newButton,saveButton))
     }

  }

  val component = ReactComponentB[Props]("Store")
    .initialState(State( Some(Store())))
    //.initialState(State())
    .renderBackend[Backend]
    .componentDidMount(scope => scope.backend.mounted(scope.props))
    .build

  def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy))

}
