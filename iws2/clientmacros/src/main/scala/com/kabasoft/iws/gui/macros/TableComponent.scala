package com.kabasoft.iws.gui.macros

import com.kabasoft.iws.gui.macros.IWSList.customerList
import com.kabasoft.iws.gui.macros.Bootstrap.{Button, CommonStyle}
import com.kabasoft.iws.shared.{Account, Customer, Data, IWS}
import com.kabasoft.iws.gui.AccordionPanel
import diode.react.ReactPot._
import diode.react._
import diode.data.Pot
import diode.react.ModelProxy
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

import scalacss.ScalaCssReact._

//case class AccordionTabItem[A](id: String, title: String, route: String, active: Boolean, renderx:(A,  ModelProxy[Pot[Data]] => ReactElement, ModelProxy[Pot[Data]]) => ReactElement,
//                               build:ModelProxy[Pot[Data]] =>ReactElement, proxy:ModelProxy[Pot[Data]])

case class AccordionTabItem[A](id: String, title: String, route: String, active: Boolean,
                               build:ModelProxy[Pot[Data]] =>ReactElement, proxy:ModelProxy[Pot[Data]])
case class AccordionMenuItem[A](id: String, title: String, route: String, active: Boolean, tabItems: Seq[AccordionTabItem[A]]) {
  def  add( item:AccordionTabItem[A]) :AccordionMenuItem[A] =copy( tabItems = tabItems ++ Seq(item))
}


  class  MasterDetails [T]  {
   // type T =(Boolean, String, String)
    case class Props(items: AccordionMenuItem[T])
    case class State(tabItem: Option[AccordionTabItem[T]] = None, name:String)
   // case class Backend($: BackendScope[Props, State]) {

      //def mounted(props: Props) =
      // props.items.tabItems.map (  e => e.proxy.dispatch(Refresh(e)))

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

      def render(p: Props, s: State) =

        <.div(^.cls := "container",
          <.div(^.cls := "row", ^.padding := 0, ^.width := 950,
            <.div(^.cls := "col-sm-2 col-md-2", ^.padding := 5,
              <.div(^.cls := "panel-group", ^.id := "accordion", ^.padding := 0,
                <.div(^.role := "tabpanel", ^.padding := 0,
                  buildTabPanel (p.items))
                // p.items map buildTabPanel)
              )
            ),
            <.div(^.cls := "col-sm-9 col-md-9", ^.padding := 2,
              <.div(^.cls := "well",
                <.div(^.cls := "tab-content",
                  p.items.tabItems  map { item2 =>
                    val r = (item2.active, item2.id, item2.title)
                    val tag = if (item2.active) "tab-pane active" else "tab-pane"
                    //buildTabContent (r, item2.build,item2.proxy)
                    <.div(^.cls := tag, ^.role := "tabpanel", ^.id := item2.id, ^.padding := 0, item2.build(item2.proxy))


                  }
                )

              )
            )

          )
        )


   // }
    def buildTabContent(cb: (Boolean, String, String),
                         build:ModelProxy[Pot[Data]] => ReactElement,
                         proxy:ModelProxy[Pot[Data]]): ReactElement = {
      val tag = if (cb._1) "tab-pane active" else "tab-pane"
      <.div(^.cls := tag, ^.role := "tabpanel", ^.id := cb._2, ^.padding := 0, build(proxy))
    }

  /*  val component = ReactComponentB[Props]("AccordionMenu")
      .initialState(State(name="Customer"))
      .renderBackend[Backend]
      .build

    def apply(items: AccordionMenuItem[(Boolean, String, String)]) = component(Props(items))
   */
  }

 //object MasterDetails  extends {



   object customerComponent extends MasterDetails [(Boolean, String, String)] {
     type T=(Boolean, String, String)
     case class Backend($: BackendScope[Props, State]) {

       def mounted(props: Props) =
         Callback.log("Customer mounted>>>>> " +props.items.tabItems)
        // Callback.ifTrue(props.isEmpty, props.proxy.dispatch(Refresh (Customer())))
         //props.items.tabItems.map (  e => e.proxy.dispatch(Refresh(e)))

      /* def BuildRow(row: AccordionTabItem[T]): ReactElement =
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
        */
       def render(p: Props, s: State) =

         <.div(^.cls := "container",
           <.div(^.cls := "row", ^.padding := 0, ^.width := 950,
             <.div(^.cls := "col-sm-2 col-md-2", ^.padding := 5,
               <.div(^.cls := "panel-group", ^.id := "accordion", ^.padding := 0,
                 <.div(^.role := "tabpanel", ^.padding := 0,
                   buildTabPanel (p.items))
                 // p.items map buildTabPanel)
               )
             ),
             <.div(^.cls := "col-sm-9 col-md-9", ^.padding := 2,
               <.div(^.cls := "well",
                 <.div(^.cls := "tab-content",
                   p.items.tabItems  map { item2 =>
                     val r = (item2.active, item2.id, item2.title)
                     buildTabContent (r, item2.build,item2.proxy)
                   }
                 )

               )
             )

           )
         )


     }

     override def  buildTabContent(cb: (Boolean, String, String),
                                   build:ModelProxy[Pot[Data]] => ReactElement,
                                   proxy:ModelProxy[Pot[Data]]): ReactElement =
     {
       val tag = if (cb._1) "tab-pane active" else "tab-pane"
       <.div(^.cls := tag, ^.role := "tabpanel", ^.id := cb._2, ^.padding := 0, build(proxy))
    }
     val component = ReactComponentB[Props]("AccordionMenu")
       .initialState(State(name="Customer"))
       .renderBackend[Backend]
       .build

     def apply(items: AccordionMenuItem[(Boolean, String, String)]) = component(Props(items))

   }

  object supplierComponent extends MasterDetails [(Boolean, String, String)] {
   type T = (Boolean, String, String)
    case class Backend($: BackendScope[Props, State]) {

      //def mounted(props: Props) =
      // dispatch a message to refresh the todos, which will cause TodoStore to fetch todos from the server
        //props.items.tabItems.map (  e => e.proxy.dispatch(Refresh(e)))

    /*  def BuildRow(row: AccordionTabItem[T]): ReactElement =
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
        */
      def render(p: Props, s: State) =

        <.div(^.cls := "container",
          <.div(^.cls := "row", ^.padding := 0, ^.width := 950,
            <.div(^.cls := "col-sm-2 col-md-2", ^.padding := 5,
              <.div(^.cls := "panel-group", ^.id := "accordion", ^.padding := 0,
                <.div(^.role := "tabpanel", ^.padding := 0,
                  buildTabPanel (p.items))
                // p.items map buildTabPanel)
              )
            ),
            <.div(^.cls := "col-sm-9 col-md-9", ^.padding := 2,
              <.div(^.cls := "well",
                <.div(^.cls := "tab-content",
                  p.items.tabItems  map { item2 =>
                    val r = (item2.active, item2.id, item2.title)
                    buildTabContent (r, item2.build,item2.proxy)
                  }
                )

              )
            )

          )
        )


    }

   /* override def  buildTabContent(cb: (Boolean, String, String),
                                  build:ModelProxy[Pot[Data]] => ReactElement,
                                  proxy:ModelProxy[Pot[Data]]): ReactElement =
    {
      val tag = if (cb._1) "tab-pane active" else "tab-pane"
      <.div(^.cls := tag, ^.role := "tabpanel", ^.id := cb._2, ^.padding := 0, build(proxy))
    }
    */

    val component = ReactComponentB[Props]("AccordionMenu")
      .initialState(State(name="Supplier"))
      .renderBackend[Backend]
      //.componentDidMount(scope => scope.backend.mounted(scope.props))
      .build

    def apply(items: AccordionMenuItem[(Boolean, String, String)]) = component(Props(items))
  }

//}
