package com.example.everything.widget.charts

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import com.example.everything.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.highlight.Highlight

class SecondaryBarChart @JvmOverloads constructor(
  context: Context,
  attributeSet: AttributeSet? = null,
  defStyle: Int = 0
) :
  BarChart(context, attributeSet, defStyle) {

  override fun onFinishInflate() {
    super.onFinishInflate()
    setupView()
  }

  private fun setupView() {
    isHighlightPerTapEnabled = false
    isHighlightPerDragEnabled = false
    isDoubleTapToZoomEnabled = false

    setDrawValueAboveBar(false)
    isScaleYEnabled = false
    isScaleXEnabled = false
    legend.isEnabled = false
    description.isEnabled = false
    setFitBars(true)
    minOffset = 0f

    val rightAxis = getAxis(YAxis.AxisDependency.RIGHT)
    rightAxis.isEnabled = false

    val yLeftAxis = getAxis(YAxis.AxisDependency.LEFT)
    yLeftAxis.isEnabled = false

    xAxis.isEnabled = false
  }

  fun render(dataEntries: List<ChartValuePerYear>) {
    val dataSet =
      BarDataSet(dataEntries.map { BarEntry(it.year.toFloat(), it.value.toFloat()) }, null)
    dataSet.color = context.getColor(R.color.colorPrimaryDark)
    dataSet.setDrawValues(false)
    dataSet.isHighlightEnabled = true
    dataSet.barBorderColor = context.getColor(R.color.colorPrimaryDark)

    val barData = BarData(dataSet)
    barData.barWidth = 0.9f
    data = barData

    xAxis.setLabelCount(dataEntries.size, true)
  }
}

@BindingAdapter("values")
fun setValues(chart: SecondaryBarChart, values: List<ChartValuePerYear>?) {
  if (values.isNullOrEmpty()) return
  chart.render(values)
}

@BindingAdapter("highlight")
fun setHighlight(chart: SecondaryBarChart, h: Highlight?) {
  if (h == null) {
    chart.highlightValue(null)
    return
  }
  Log.d("TAGG", h.toString())
  chart.highlightValue(h.x, 0, false)
}