package com.kabasoft.iws.gui.macros


import com.kabasoft.iws.gui._
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import com.kabasoft.iws.gui.macros.Bootstrap._
import com.kabasoft.iws.shared._
import com.kabasoft.iws.gui.macros._
import scalacss.ScalaCssReact._



import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context
import com.kabasoft.iws.gui.macros.TabItem

object BusinessPartnerUIMacro {
  import scala.language.experimental.macros
  def makeUI[T<:IWS](instance:T):IWSBackendTrait[T] = macro makeUIImpl[T]
  def makeUIImpl[T:c.WeakTypeTag] (c: Context)  (instance:c.Expr[T]): c.Tree = {
    val t = c.weakTypeOf[T]
    val n = c.weakTypeOf[T].typeSymbol.name.toTermName.toString
    val a = c.weakTypeOf[T].typeSymbol.name.toTypeName

    import c.universe._
    import com.kabasoft.iws.gui.macros.Bootstrap._

    q"""
    object CustomerList {
      @inline private def bss = GlobalStyles.bootstrapStyles

      case class Props(items: Seq[IWS], stateChange: IWS => Callback, editItem: IWS => Callback, deleteItem: IWS => Callback)

      private val CustomerList = ReactComponentB[Props]("CustomerList")
        .render_P(p => {
          val style = bss.listGroup
          def renderItem(item: $t) = {
            <.li(style.itemOpt(CommonStyle.success),^.fontSize:=12,^.fontWeight:=50,^.maxHeight:=30,^.height:=30, ^.tableLayout:="fixed",
              <.span(item.id),
              <.span(item.name ,^.paddingLeft:=10),
              <.span(item.street ,^.paddingLeft:=10),
              <.span(item.city ,^.paddingLeft:=10),
              <.span(item.state ,^.paddingLeft:=10),
              <.span(item.zip ,^.paddingLeft:=10),
              Button(Button.Props(p.editItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Edit"),
              Button(Button.Props(p.deleteItem(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Delete")
            )
          }
          <.ul(style.listGroup)(p.items.asInstanceOf[Seq[$t]].sortBy(_.id) map renderItem)
        })
        .build

      def apply(items: Seq[IWS], stateChange: IWS => Callback, editItem: IWS => Callback, deleteItem: IWS => Callback) =
        CustomerList(Props(items, stateChange, editItem, deleteItem))
    }


    object BusinessPartnerBackend extends IWSBackendTrait [$t]{
      @inline private def bss = GlobalStyles.bootstrapStyles

      //case class Props(proxy: ModelProxy[Pot[Data]])
      case class State(selectedItem: Option[$t] = None,  name:String)

      class Backend(b: BackendScope[Props, State]) {
        def mounted(props: Props) =
          Callback.ifTrue(props.proxy().isEmpty, props.proxy.dispatch(Refresh(props.iws)))

        def edit(item:Option[$t]) = {
          b.modState(s => s.copy(selectedItem = item))
        }
        def edited(item:$t) = {
          Callback.log("Customer edited>>>>> " +item)  >>
            b.props >>= (_.proxy.dispatch(Update(item)))

        }

        def updateId(e: ReactEventI) = {
          val r = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(id = r))))
        }
        def updateName(e: ReactEventI) = {
          val r = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(name = r))))
        }
        def updateStreet(e: ReactEventI) = {
          val r1 = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(street = r1))))
        }
        def updateCity(e: ReactEventI) = {
          val r2 = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(city = r2))))
        }
        def updateState(e: ReactEventI) = {
          val r3 = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(state = r3))))
        }
        def updateZip(e: ReactEventI) = {
          val r4 = e.target.value
          b.modState(s => s.copy(s.selectedItem.map( z => z.copy(zip = r4))))
        }


     case class Address(id:String, street:String ="", zipCode:String ="", city:String ="", country:String="DE", route:String, title:String)
           val adr1=Address("vtab1","Meisen Str 96","33967","Bielefeld","DE","#vtab1","Home Address")
           val adr2=Address("vtab2","Meisen Str2 96","33967","Bielefeld2","DE","#vtab2","Delivery Address")
           val adr3 =Address("vtab3","Meisen Str3 96","33967","Bielefeld3","DE","#vtab3","Billing Address")
           val item1= TabItem("vtab1","Home","#vtab1",true,buildAddress(adr1,true))
           val item2= TabItem("vtab2","Delivery", "#vtab2",false,buildAddress(adr2))
           val item3= TabItem("vtab3","Billing", "#vtab3",false,buildAddress(adr3))

     def buildTab (items:Seq[TabItem]):ReactElement  =
             <.div(^.cls:="container",
               <.div(^.cls:="row",
                 <.div(^.cls:="col-sm-2",
                   buildTabHeader(items)
                 ),
                 buildTabContent(items)
               )
             )

     def buildTabContent( tabItems: Seq[TabItem])=
           <.div(^.cls:="col-sm-9",
             <.div(^.cls:="tab-content", tabItems map (item => item.content))
           )

     def buildTabHeader( tabItems: Seq[TabItem])=
           <.ul(^.id:="nav-tabs-wrapper", ^.cls:="nav nav-tabs nav-pills nav-stacked well",
             tabItems map (item =>
               if(item.active)
                 <.li(^.cls := "active", <.a(^.href :=item.route, "data-toggle".reactAttr := "tab", item.title))
               else
                 <.li(<.a(^.href:=item.route, "data-toggle".reactAttr:="tab", item.title))
               )
           )

           def buildAddress(adr: Address , active:Boolean=false): ReactElement  = {
             var titleTag = "tab-pane fade"
             if (active) titleTag = "tab-pane fade in active"
             <.div(^.role := "tabpanel", ^.cls := titleTag, ^.id := adr.id,
               <.table(^.cls := "table-responsive table-condensed", ^.tableLayout := "fixed",
                 <.tbody(
                   <.tr(bss.formGroup, ^.height := 20,
                     buildItem("street",adr.street),
                     buildItem("zipCode",adr.zipCode)),
                   <.tr(bss.formGroup, ^.height := 20,
                     buildItem("city", adr.city),
                     buildItem("country",adr.country))
                 ))
             )
           }

     def buildWItem[A](id:String , value:Option[String],evt:ReactEventI=> Callback) =
             List( <.td(<.label(^.`for` := id, id)),
               <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
                 ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10))

     def buildItem(id:String , value:String) =
             List( <.td(<.label(^.`for` := id, id)),
               <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
                 ^.placeholder := id),^.paddingLeft := 1))

       def buildForm(s:State): Seq[ReactElement] =
               List(<.div(bss.formGroup,
                 <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
                   <.tbody(
                     <.tr(^.height := 20,
                       buildWItem("id", s.selectedItem.map(_.id),updateId),
                       buildWItem("name", s.selectedItem.map(_.name),updateName),
                       buildWItem("street", s.selectedItem.map(_.street),updateStreet)
                     ),
                     <.tr(^.height := 20,
                       buildWItem("city", s.selectedItem.map(_.city),updateCity),
                       buildWItem("state", s.selectedItem.map(_.state),updateState),
                       buildWItem("zip", s.selectedItem.map(_.zip),updateZip)
                     )
                 )
                 ))
               )

        def render(p: Props, s: State) =
          Panel(Panel.Props($n), <.div(^.className := "panel-heading",^.padding :=0), <.div(^.padding :=0,
            p.proxy().renderFailed(ex => "Error loading"),
            p.proxy().renderPending(_ > 500, _ => "Loading..."),
            p.proxy().render(_ => AccordionPanel("Edit", buildForm(s),
              List(Button(Button.Props(edited(s.selectedItem.getOrElse($instance)),
                addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.circleO, " Save"),
                Button(Button.Props(edit(Some($instance)), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, " New")))),
            p.proxy().render(
              all => CustomerList(all.items.asInstanceOf[List[$t]],
                item => p.proxy.dispatch(Update(item.asInstanceOf[$t])),
                item => edit(Some(item.asInstanceOf[$t])), item => p.proxy.dispatch(Delete[$t](item.asInstanceOf[$t])))),
                 TabComponent(Seq(item1, item2, item3)))
                )

      }

      val component = ReactComponentB[Props]($n)
        .initialState(State(name=$n))
        .renderBackend[Backend]
        .componentDidMount(scope => scope.backend.mounted(scope.props))
        .build

      def apply(proxy: ModelProxy[Pot[Data]]) = component(Props(proxy,$instance))
    }
      BusinessPartnerBackend
      """
  }
}

