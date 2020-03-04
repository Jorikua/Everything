package com.example.everything.widget

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

class ChartYAxisFormatter(private val currency: Currency) : ValueFormatter() {

	override fun getAxisLabel(value: Float, axis: AxisBase?): String {
		return value.toDollar.roundToInt().toBigNumber.prefixCurrency(currency)
	}
}

class ChartXAxisFormatter : ValueFormatter() {

	override fun getAxisLabel(value: Float, axis: AxisBase?): String {
		return value.toInt().toString()
	}
}

val Float.toDollar: Float get() = this / 100f
val Number.toBigNumber: String get() = printBigNumber(this)

fun printBigNumber(count: Number?): String {
  if (count == null) return ""
  if (count.toLong() < 1000) return "" + count
  val exp = (ln(count.toDouble()) / ln(1000.0)).toInt()
  return String.format(
    "%.1f %c",
    count.toLong() / 1000.0.pow(exp.toDouble()),
    "kMGTPE"[exp - 1]
  )
}

fun String.prefixCurrency(currency: Currency?): String =
  currency?.symbol.plus(" ").plus(this)