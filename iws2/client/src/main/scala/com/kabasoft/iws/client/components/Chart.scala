package com.kabasoft.iws.client.components

import japgolly.scalajs.react.vdom.prefix_<^._
import japgolly.scalajs.react.{Callback, ReactComponentB}
import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSName



@js.native
trait ChartDataset extends js.Object {
  def label: String = js.native
  def fillColor: String = js.native
  def strokeColor: String = js.native
  def data: js.Array[Double] = js.native
  def value: Double = js.native
  def color: String = js.native
  def highlight: String = js.native
}


object ChartDataset {
  def apply(data: Seq[Double], label: String, value:Double =0.0d, color: String = "#8080FF", highlight: String = "#B0E2FF",
                               fillColor: String = "#9FB6CD", strokeColor: String = "#404080"): ChartDataset = {
    js.Dynamic.literal(
      data = data.toJSArray,
      label = label,
      value =value,
      color = color,
      highlight=highlight,
      fillColor = fillColor,
      strokeColor = strokeColor
    ).asInstanceOf[ChartDataset]
  }
}

@js.native
trait ChartData extends js.Object {
  def labels: js.Array[String] = js.native
  def datasets: js.Array[ChartDataset] = js.native

}

object ChartData {
  def apply(labels: Seq[String], datasets: Seq[ChartDataset]): ChartData = {
    js.Dynamic.literal(
      labels = labels.toJSArray,
      datasets = datasets.toJSArray
    ).asInstanceOf[ChartData]
  }
}

// define a class to access the Chart.js component
@js.native
@JSName("Chart")
class JSChart(ctx: js.Dynamic) extends js.Object {
  // create different kinds of charts
  def Line(data: ChartData): js.Dynamic = js.native
  def Bar(data: ChartData): js.Dynamic = js.native
  def Radar(data: ChartData): js.Dynamic = js.native
  def PolarArea(data: ChartData): js.Dynamic = js.native
  def Doughnut(data: ChartData): js.Dynamic = js.native
}

object Chart {

  // available chart styles
  sealed trait ChartStyle

  case object LineChart extends ChartStyle

  case object BarChart extends ChartStyle
  case object RadarChart extends ChartStyle
  case object PolarAreaChart extends ChartStyle
  case object DoughnutChart extends ChartStyle


  case class ChartProps(name: String, style: ChartStyle, data: ChartData, width: Int = 400, height: Int = 200)

  val Chart = ReactComponentB[ChartProps]("Chart")
    .render_P(p =>
      <.canvas(^.width := p.width, ^.height := p.height)
      )
    .domType[HTMLCanvasElement]
    .componentDidMount(scope => Callback {
      // access context of the canvas
      val ctx = scope.getDOMNode().getContext("2d")
      // create the actual chart using the 3rd party component
      scope.props.style match {
        case LineChart => new JSChart(ctx).Line(scope.props.data)
        case BarChart => new JSChart(ctx).Bar(scope.props.data)
        case RadarChart => new JSChart(ctx).Radar(scope.props.data)
        case PolarAreaChart => new JSChart(ctx).PolarArea(scope.props.data)
        case DoughnutChart => new JSChart(ctx).Doughnut(scope.props.data)
        case _ => throw new IllegalArgumentException
      }
    }).build

  def apply(props: ChartProps) = Chart(props)
}
