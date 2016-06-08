package com.kabasoft.iws.gui.macros

import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.shared.{Account, _}
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import diode.react.ModelProxy
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import scalacss.ScalaCssReact._

/*case class AccordionTabItem[A](id: String, title: String, route: String, active: Boolean,
                               build:(AccordionMenuItem[(Boolean, String, String)], ModelProxy[Pot[Data]]) =>ReactElement, proxy:ModelProxy[Pot[Data]])
case class AccordionMenuItem[A](id: String, title: String, route: String, active: Boolean, tabItems: Seq[AccordionTabItem[A]]) {
  def  add( item:AccordionTabItem[A]) :AccordionMenuItem[A] =copy( tabItems = tabItems ++ Seq(item))
 }
*/
abstract class IWSList [A] {
  case class State(item: Option[A]= None)
  case class IWSListProps(proxy:ModelProxy[Pot[Data]], items: Seq[A])
  def buildLines(proxy:ModelProxy[Pot[Data]]): ReactElement
 /*
  abstract  case class Backend($: BackendScope[IWSListProps, State]) {

    //def mounted(props: Props) =
    // props.items.tabItems.map (  e => e.proxy.dispatch(Refresh(e)))
    type T =(Boolean, String, String)

    def BuildRow(row: AccordionTabItem[T]): ReactElement =
      <.tr(
        <.td(
          <.span(^.cls := "glyphicon glyphicon-pencil text-primary"),
          <.a("aria-controls".reactAttr := row.id, ^.role := "tab", "data-toggle".reactAttr := "tab", ^.href := row.route, row.title)
        )
      )

    def BuildTable(rows: Seq[AccordionTabItem[T]]): ReactElement =
      <.table(^.cls := "table", rows map BuildRow)

    def buildTabPanel(tabItem: AccordionMenuItem[T]): ReactElement = {
      val tag = if (tabItem.active) "panel-collapse collapse in" else "panel-collapse collapse"
      <.div(^.cls := "panel panel-default", ^.padding := 0,
        <.div(^.cls := "panel-heading",
          <.h4(^.cls := "panel-title",
            <.a("data-toggle".reactAttr := "collapse", "data-parent".reactAttr := "#accordion", ^.href := tabItem.route,
              <.span(^.cls := "glyphicon glyphicon-folder-close"), tabItem.title
            )
          )
        ),
        <.div(^.cls := tag, ^.id := tabItem.id, ^.padding := 0,
          <.div(^.cls := "panel-body",
            BuildTable(tabItem.tabItems)
          )
        )
      )
    }

    def render(p: IWSListProps, s: State) =

      <.div(^.cls := "container",
        <.div(^.cls := "row", ^.padding := 0, ^.width := 950,
          <.div(^.cls := "col-sm-2 col-md-2", ^.padding := 5,
            <.div(^.cls := "panel-group", ^.id := "accordion", ^.padding := 0,
              <.div(^.role := "tabpanel", ^.padding := 0,
                buildTabPanel (p.menu))
              // p.items map buildTabPanel)
            )
          ),
          <.div(^.cls := "col-sm-9 col-md-9", ^.padding := 2,
            <.div(^.cls := "well",
              <.div(^.cls := "tab-content",
                p.menu.tabItems  map { item2 =>
                  val r = (item2.active, item2.id, item2.title)
                  buildTabContent (r, buildLines, p.menu, item2.proxy)
                }
              )

            )
          )

        )
      )


  }

  def buildTabContent(cb: (Boolean, String, String),
                      build:ModelProxy[Pot[Data]] => ReactElement,
                      tabItem:AccordionMenuItem[(Boolean, String, String)],
                      proxy:ModelProxy[Pot[Data]]): ReactElement = {

    val tag = if (cb._1) "tab-pane active" else "tab-pane"
    <.div(^.cls := tag, ^.role := "tabpanel", ^.id := cb._2, ^.padding := 0, build(proxy))
  }
   */
}

object IWSList {

  /*
  object  accountList extends IWSList [Account] {
    @inline private def bss = GlobalStyles.bootstrapStyles
    //case class State(item: Option[Account]= None)
    //case class IWSListProps(proxy:ModelProxy[Pot[Data]], items: Seq[Account])

    override  def buildLines(menu:AccordionMenuItem[(Boolean, String, String)],proxy:ModelProxy[Pot[Data]]): ReactElement = {
      //val proxy = customerList.IWSListProps.proxy
      <.div(proxy().render( all => accountList(menu, proxy, all.items.asInstanceOf[List[Account]])))
    }



    case class CBackend( override val $: BackendScope[IWSListProps, State]) extends Backend ($) {
       def mounted(props: IWSListProps) =
         Callback.log("Account mounted>>>>> " +props)>>
         Callback.ifTrue(props.items.isEmpty, props.proxy.dispatch(Refresh (Customer())))

      def edit(item:Option[Account]) = {
        $.modState(s => s.copy(item = item))
      }
      def edited(item:Account) = {

        Callback.log("Account edited>>>>> " +item) >>
         $.props >>= (_.proxy.dispatch(Update(item)))

      }

      def delete(item:Account) = {
        Callback.log("Account deleted>>>>> " +item)  >>
        $.props >>= (_.proxy.dispatch(Delete(item)))
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
        val r = e.target.value
        $.modState(s => s.copy(s.item.map( z => z.copy(description = r))))
      }

      def buildForm(s: State): Seq[ReactElement] =
        List(<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(bss.formGroup, ^.height := 20,
                buildWItem("id", s.item.map(_.id), updateId),
                buildWItem("name", s.item.map(_.name), updateName),
                buildWItem("description", s.item.map(_.description), updateDescription))
            )
          )
        )
        )

      def buildWItem[A](id:String , value:Option[String],evt:ReactEventI=> Callback) =
        List( <.td(<.label(^.`for` := id, id)),
          <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
            ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10))

      def buildItem(id:String , value:String) =
        List( <.td(<.label(^.`for` := id, id)),
          <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
            ^.placeholder := id),^.paddingLeft := 1))

       override def   render(p: IWSListProps, s: State) = {
         def saveButton = Button(Button.Props(edited(s.item.getOrElse(Account())), addStyles = Seq(bss.pullRight, bss.buttonXS,
           bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
         def newButton =  Button(Button.Props(edit(Some(Account())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")

         def renderItem(item:Account) = {

           def editButton =  Button(Button.Props(edit(Some(item)), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
           def deleteButton = Button(Button.Props(delete(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
        val style = bss.listGroup
        def renderItem(item:Account) = {

          <.li(style.itemOpt(CommonStyle.success), ^.fontSize := 12, ^.fontWeight := 50, ^.maxHeight := 30,
                                                    ^.height := 30, ^.tableLayout := "fixed",

            <.span(item.id),
            <.span(item.name, ^.paddingLeft := 10),
            <.span(item.description, ^.paddingLeft := 10),
            //<.span(Utils.format(item.dateOfOpen.get),^.paddingLeft:=10),
            <.span("%06.2f".format(item.balance.amount.toDouble), ^.paddingLeft := 10)

            //Button(Button.Props(edit(Some(item)), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Edit"),
            //Button(Button.Props(delete(item), addStyles = Seq(bss.pullRight, bss.buttonXS)), "Delete")
          )
        }

         <.div(^.cls := "panel panel-default panel-table", ^.padding := 0,
           <.div(^.cls := "panel-heading",
             <.div(^.cls := "row",
               <.div(^.cls := "col col-xs-6",
                 <.h3(^.cls := "panel-title", s.item.getOrElse(Customer()).modelId)
               ),
               <.div(^.cls := "col col-xs-6 text-right",
                 <.h4(^.cls := "button", List(saveButton, newButton))
               )
             )
           ),
           <.div(^.cls := "panel-body",
             <.ul(style.listGroup) ( buildForm(s))(p.items.sortBy(_.id) map renderItem)
           ),
           <.div(^.cls := "panel-footer",
             <.div(^.cls := "row",
               <.div(^.cls := "col col-xs-8", "Page 1 of 5",
                 <.div(^.cls := "col col-xs-10",
                   <.ul(^.cls := "pagination hidden-xs pull-right",
                     <.li(<.a(^.href := "#", "«")),
                     <.li(<.a(^.href := "#", "1")),
                     <.li(<.a(^.href := "#", "2")),
                     <.li(<.a(^.href := "#", "3")),
                     <.li(<.a(^.href := "#", "4")),
                     <.li(<.a(^.href := "#", "5")),
                     <.li(<.a(^.href := "#", "»"))
                   )
                 )
               )
             )
           )
         )
        //<.ul(style.listGroup)( buildForm(s))(p.items.sortBy(_.id) map renderItem)
      }
    }
    val component = ReactComponentB[IWSListProps]("Account")
      .initialState(State(Some(Account())))
      .renderBackend[CBackend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

    def apply(menu:AccordionMenuItem[(Boolean, String, String)], proxy:ModelProxy[Pot[Data]], accounts:Seq[Account]) = component(IWSListProps( menu,proxy, accounts))

    //def apply(accounts:Seq[Account] , change: Option[Account] => Callback,
    //          edit: Option[Account] => Callback, delete: Account => Callback) = component(IWSListProps(accounts,change,edit,delete))
  }
    */
  object  customerList  extends IWSList [Customer] {
    @inline private def bss = GlobalStyles.bootstrapStyles

    //case class State(item: Option[Customer]= None)
    //case class IWSListProps(proxy:ModelProxy[Pot[Data]], items: Seq[Customer])

    override  def buildLines(proxy:ModelProxy[Pot[Data]]): ReactElement = {
      //val proxy = customerList.IWSListProps.proxy
      <.div(proxy().render( all => customerList(proxy, all.items.asInstanceOf[List[Customer]])))
    }

   case class Backend ($: BackendScope[IWSListProps, State])  {
      def mounted(props: IWSListProps) =
        Callback.log("Customer mounted>>>>> " +props.items)
        //Callback.ifTrue(props.items.isEmpty, props.proxy.dispatch(Refresh (Customer())))

      def edit(item:Option[Customer]) = {
        $.modState(s => s.copy(item = item))
      }

      def edited(item:Customer) = {
        Callback.log("Customer edited>>>>> " +item) >>
        $.props >>= (_.proxy.dispatch(Update(item)))
      }

      def delete(item:Customer) = {
        Callback.log("Customer deleted>>>>> " +item)  >>
          $.props >>= (_.proxy.dispatch(Delete(item)))
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
      def buildWItem(id:String , value:Option[String],evt:ReactEventI=> Callback) =
        List( <.td(<.label(^.`for` := id, id)),
          <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
            ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10))

      def buildItem(id:String , value:Option[String]) =
        List( <.td(<.label(^.`for` := id, id)),
          <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
            ^.placeholder := id),^.paddingLeft := 1))

      def buildForm(s:State): ReactElement =
      //<.div(bss.formGroup,
        <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
          <.tbody(
            <.tr(^.height := 20,
              //buildWItem("id", s.item.map(_.id),updateIt(m("",s))),
              buildWItem("id", s.item.map(_.id),updateId),
              buildWItem("name", s.item.map(_.name),updateName),
              buildWItem("street", s.item.map(_.street),updateStreet)
            ),
            <.tr(^.height := 20,
              buildWItem("city", s.item.map(_.city),updateCity),
              buildWItem("state", s.item.map(_.state),updateState),
              buildWItem("zip", s.item.map(_.zip),updateZip)

            )
          )
        )


      def   render(p: IWSListProps, s: State) = {
        //if(p.items.isEmpty) p.proxy.dispatch(Refresh (Customer()))
        val style = bss.listGroup
        def saveButton = Button(Button.Props(edited(s.item.getOrElse(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS,
          bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
        def newButton =  Button(Button.Props(edit(Some(Customer())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")

        def renderItem(item:Customer) = {

          def editButton =  Button(Button.Props(edit(Some(item)), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
          def deleteButton = Button(Button.Props(delete(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
          <.li(style.itemOpt(CommonStyle.success), ^.fontSize := 12, ^.fontWeight := 50, ^.maxHeight := 30, ^.height := 30, ^.tableLayout := "fixed",

            <.span(item.id),
            <.span(item.name ,^.paddingLeft:=5),
            <.span(item.street ,^.paddingLeft:=5),
            <.span(item.city ,^.paddingLeft:=5),
            <.span(item.zip ,^.paddingLeft:=5),
            deleteButton,
            editButton
          )
        }

        <.div(^.cls := "panel panel-default panel-table", ^.padding := 0,
          <.div(^.cls := "panel-heading",
            <.div(^.cls := "row",
              <.div(^.cls := "col col-xs-6",
                <.h3(^.cls := "panel-title", s.item.getOrElse(Customer()).modelId)
              ),
              <.div(^.cls := "col col-xs-6 text-right",
                <.h4(^.cls := "button", List(saveButton, newButton))
              )
            )
          ),
          <.div(^.cls := "panel-body",
            <.ul(style.listGroup) ( buildForm(s))(p.items.sortBy(_.id) map renderItem)
          ),
          <.div(^.cls := "panel-footer",
            <.div(^.cls := "row",
              <.div(^.cls := "col col-xs-8", "Page 1 of 5",
                <.div(^.cls := "col col-xs-10",
                  <.ul(^.cls := "pagination hidden-xs pull-right",
                    <.li(<.a(^.href := "#", "«")),
                    <.li(<.a(^.href := "#", "1")),
                    <.li(<.a(^.href := "#", "2")),
                    <.li(<.a(^.href := "#", "3")),
                    <.li(<.a(^.href := "#", "4")),
                    <.li(<.a(^.href := "#", "5")),
                    <.li(<.a(^.href := "#", "»"))
                  )
                )
              )
            )
          )

        )


      }
    }
    val component = ReactComponentB[IWSListProps]("Customer")
      .initialState(State(Some(Customer())))
      .renderBackend[Backend]
      .componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

    def apply(proxy:ModelProxy[Pot[Data]], customers:Seq[Customer]) = component(IWSListProps(proxy, customers))
  }

 object  supplierList   extends IWSList [Supplier]  {
   @inline private def bss = GlobalStyles.bootstrapStyles

   //case class State(item: Option[Supplier]= None)
  // case class IWSListProps(proxy:ModelProxy[Pot[Data]], items: Seq[Supplier])
   override def buildLines(proxy:ModelProxy[Pot[Data]]): ReactElement =
         <.div(proxy().render( all => supplierList( proxy, all.items.asInstanceOf[List[Supplier]])))

    case class Backend ($: BackendScope[IWSListProps, State])     {
     def mounted(props: IWSListProps) =
        Callback.log("Supplier mounted>>>>> " +props.items)
        //Callback.ifTrue(props.items.isEmpty, props.proxy.dispatch(Refresh (Supplier())))

     def edit(item:Option[Supplier]) = {
        $.modState(s => s.copy(item = item))
      }

      def edited(item:Supplier) =
        Callback.log("Supplier edited>>>>> " +item) >>
         $.props >>= (_.proxy.dispatch(Update(item)))

      
      def delete(item:Supplier) = {
        Callback.log("Supplier deleted>>>>> " + item) >>
          $.props >>= (_.proxy.dispatch(Delete(Supplier())))
         $.props >>= (_.proxy.dispatch(UpdateAll( Seq(Supplier()))))
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
    def buildWItem(id:String , value:Option[String],evt:ReactEventI=> Callback) =
      List( <.td(<.label(^.`for` := id, id)),
        <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
          ^.placeholder := id),  ^.onChange ==> evt, ^.paddingLeft := 10))

    def buildItem(id:String , value:Option[String]) =
        List( <.td(<.label(^.`for` := id, id)),
              <.td(<.input.text(bss.formControl, ^.id := id, ^.value := value,
        ^.placeholder := id),^.paddingLeft := 1))

          def buildForm(s:State): ReactElement =
        //<.div(bss.formGroup,
          <.table(^.className := "table-responsive table-condensed", ^.tableLayout := "fixed",
            <.tbody(
              <.tr(^.height := 20,
                //buildWItem("id", s.item.map(_.id),updateIt(m("",s))),           
                buildWItem("ids", s.item.map(_.id),updateId),
                buildWItem("names", s.item.map(_.name),updateName),
                buildWItem("streets", s.item.map(_.street),updateStreet)
                 ),
              <.tr(^.height := 20,
                buildWItem("city", s.item.map(_.city),updateCity),
                buildWItem("state", s.item.map(_.state),updateState),
                buildWItem("zip", s.item.map(_.zip),updateZip)
                
              )
            )
          )


       def   render(p: IWSListProps, s: State) = {
         //if(p.items.isEmpty) p.proxy.dispatch(Refresh (Supplier()))
        val style = bss.listGroup
        def saveButton = Button(Button.Props(edited(s.item.getOrElse(Supplier())), addStyles = Seq(bss.pullRight, bss.buttonXS,
          bss.buttonOpt(CommonStyle.success))), Icon.circleO, "Save")
        def newButton =  Button(Button.Props(edit(Some(Supplier())), addStyles = Seq(bss.pullRight, bss.buttonXS)), Icon.plusSquare, "New")

        def renderItem(item:Supplier) = {
          def editButton =  Button(Button.Props(edit(Some(item)), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.success))), Icon.edit, "")
          def deleteButton = Button(Button.Props(delete(item), addStyles = Seq(bss.pullRight, bss.buttonXS, bss.buttonOpt(CommonStyle.danger))), Icon.trash, "")
          <.li(style.itemOpt(CommonStyle.success), ^.fontSize := 12, ^.fontWeight := 50, ^.maxHeight := 30, ^.height := 30, ^.tableLayout := "fixed",
            <.span(item.id),
            <.span(item.name ,^.paddingLeft:=5),
            <.span(item.street ,^.paddingLeft:=5),
            <.span(item.city ,^.paddingLeft:=5),
            <.span(item.zip ,^.paddingLeft:=5),
            deleteButton,
            editButton
          )
        }
            <.div(^.cls := "panel panel-default panel-table", ^.padding := 0,
               <.div(^.cls := "panel-heading",
                      <.div(^.cls := "row",
                <.div(^.cls := "col col-xs-6",
                  <.h3(^.cls := "panel-title", s.item.getOrElse(Customer()).modelId)
                ),
                <.div(^.cls := "col col-xs-6 text-right",
                  <.h4(^.cls := "button", List(saveButton, newButton))
                )
              )
                   ),
                <.div(^.cls := "panel-body",
                     <.ul(style.listGroup) ( buildForm(s))(p.items.sortBy(_.id) map renderItem)
                    ),
                <.div(^.cls := "panel-footer",
              <.div(^.cls := "row",
                <.div(^.cls := "col col-xs-8", "Page 1 of 5",
                  <.div(^.cls := "col col-xs-10",
                    <.ul(^.cls := "pagination hidden-xs pull-right",
                      <.li(<.a(^.href := "#", "«")),
                      <.li(<.a(^.href := "#", "1")),
                      <.li(<.a(^.href := "#", "2")),
                      <.li(<.a(^.href := "#", "3")),
                      <.li(<.a(^.href := "#", "4")),
                      <.li(<.a(^.href := "#", "5")),
                      <.li(<.a(^.href := "#", "»"))
                    )
                  )
                )
              )
            )
           )


      }
  }
   val component = ReactComponentB[IWSListProps]("Supplier")
     .initialState(State(Some(Supplier())))
     .renderBackend[Backend]
     .componentDidMount(scope => scope.backend.mounted(scope.props))
     .build

   def apply(proxy:ModelProxy[Pot[Data]], suppliers:Seq[Supplier]) = component(IWSListProps(proxy, suppliers))
}

}