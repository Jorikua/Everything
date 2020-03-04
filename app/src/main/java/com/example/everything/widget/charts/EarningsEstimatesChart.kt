package com.example.everything.widget.charts

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import com.example.everything.R
import com.example.everything.widget.ChartYAxisFormatter
import com.example.everything.widget.GreenMarker
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import java.util.*

class EarningsEstimatesChart @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : InteractableChart(context, attrs, defStyleAttr) {

  var valueSelectedListener: ValueSelectedListener? = null

  override fun onFinishInflate() {
    super.onFinishInflate()
    setup()
  }

  private fun setup() {
    description.isEnabled = false
    isAutoScaleMinMaxEnabled = true
    setScaleEnabled(false)
    isDoubleTapToZoomEnabled = false
    val rightAxis = getAxis(YAxis.AxisDependency.RIGHT)
    rightAxis.isEnabled = false

    legend.isEnabled = false
    xAxis.isEnabled = false
    setupAxis()
    setupTopMarker()
    minOffset = 0f

    chartListener = this

    setOnChartValueSelectedListener(this)
  }

  private fun setupAxis() {
    getAxis(YAxis.AxisDependency.LEFT).apply {
      spaceBottom = 0.3f
      spaceTop = 0.3f
      setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
      setDrawLabels(true)
      setDrawZeroLine(false)
      setDrawGridLines(true)
      setDrawAxisLine(false)
      isGranularityEnabled = true
      gridColor = context.getColor(R.color.colorPrimary)
      textColor = context.getColor(R.color.colorPrimary)
      yOffset = -10f
      xOffset = 0f
    }
  }

  private fun setupTopMarker() {
    val selectionMarker = GreenMarker(context)
    selectionMarker.chartView = this
    marker = selectionMarker
  }

  fun render(values: List<ChartValuePerYear>, currentAge: Int, listingCurrency: Currency) {
    getAxis(YAxis.AxisDependency.LEFT).valueFormatter = ChartYAxisFormatter(listingCurrency)
    val splitEntries = splitEntries(values, currentAge)

    val pastEntries = splitEntries.first
    val futureEntries = splitEntries.second

    val previousYears = LineDataSet(pastEntries, null).apply {
      color = context.getColor(R.color.colorPrimaryDark)
    }

    val futureYears = LineDataSet(futureEntries, null).apply {
      color = context.getColor(R.color.colorPrimaryDark)
      enableDashedLine(5f, 5f, 0f)
    }

    val listOfY = futureEntries.map { it.y }
    val max = listOfY.max() ?: 0f
    val average = listOfY.average().toFloat()
    val axisMaximum = max + average
    axisLeft.axisMaximum = axisMaximum

    styleData(previousYears)
    styleData(futureYears)

    val circle = getCircle(pastEntries.last())

    data = LineData(listOf(previousYears, futureYears, circle))
    previousYears.isVisible = true
    futureYears.isVisible = true

    updateData(data)
  }

  private fun styleData(data: LineDataSet) {
    data.apply {
      fillDrawable = context.getDrawable(R.drawable.forecasted_chart_fade)
      setDrawFilled(true)
      lineWidth = 3f
      setDrawCircles(false)
      setDrawValues(false)
      setDrawHorizontalHighlightIndicator(false)
      highLightColor = context.getColor(android.R.color.darker_gray)
      mode = LineDataSet.Mode.CUBIC_BEZIER
    }
  }

  private fun splitEntries(
    values: List<ChartValuePerYear>,
    currentAge: Int
  ): Pair<MutableList<Entry>, MutableList<Entry>> {
    val pastEntries = mutableListOf<Entry>()
    val futureEntries = mutableListOf<Entry>()
    val groupedValues = values.groupBy { it.year <= currentAge }

    val pastValues = groupedValues[true]
    val futureValues = groupedValues[false]

    pastValues?.forEach {
      pastEntries.add(Entry(it.year.toFloat(), it.value.toFloat()))
    }

    futureValues?.forEachIndexed { index, it ->
      if (index == 0) {
        pastEntries.add(Entry(it.year.toFloat(), it.value.toFloat()))
      }
      futureEntries.add(Entry(it.year.toFloat(), it.value.toFloat()))
    }

    return Pair(pastEntries, futureEntries)
  }

  fun visibleShare(checked: Boolean) {
    updateData(data)
  }


  private fun getCircle(chartDataEntry: Entry): LineDataSet {
    val circleDataSet = LineDataSet(listOf(chartDataEntry), null)

    circleDataSet.circleRadius = 10f
    circleDataSet.setCircleColor(context.getColor(R.color.colorAccent))
    circleDataSet.circleHoleColor = context.getColor(R.color.colorAccent)
    circleDataSet.setDrawCircles(true)
    circleDataSet.setDrawValues(false)
    circleDataSet.isHighlightEnabled = false

    return circleDataSet
  }

  override fun onNothingSelected() {
    valueSelectedListener?.onValueSelected(null)
  }

  override fun onValueSelected(e: Entry?, h: Highlight?) {
    valueSelectedListener?.onValueSelected(h)
  }

  interface ValueSelectedListener {
    fun onValueSelected(h: Highlight?)
  }
}

@BindingAdapter("onSelectedValueChanged")
fun setOnSelectedValueChanged(
  earningsEstimatesChart: EarningsEstimatesChart,
  listener: EarningsEstimatesChart.ValueSelectedListener?
) {
  earningsEstimatesChart.valueSelectedListener = listener
}

@BindingAdapter("values", "currentAge", "currency")
fun renderEarningEstimatesChart(
  chart: EarningsEstimatesChart,
  values: List<ChartValuePerYear>?,
  currentAge: Int?,
  currency: Currency?
) {
  if (values == null || currentAge == null || currency == null) return
  chart.render(values, currentAge, currency)
}

data class ChartValuePerYear(val year: Int, val value: Int)