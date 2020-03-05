package com.example.everything

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.everything.widget.charts.ChartValuePerYear
import com.example.everything.widget.prefixCurrency
import com.example.everything.widget.toBigNumber
import com.github.mikephil.charting.highlight.Highlight
import java.util.*
import kotlin.random.Random

class MainViewModel(private val repository: Repository) : ViewModel() {

  val values = ObservableField<List<ChartValuePerYear>>()
  val currency = ObservableField<Currency>(Currency.getInstance(Locale.getDefault()))
  val currentAge = ObservableInt(22)

  val highlight = ObservableField<Highlight>()

  val currentAgeEarnings = ObservableField<String>()
  val earningsPercentageDelta = ObservableField<Float>(0f)

  val contractsChecked = ObservableBoolean(true)
  val allIncomeChecked = ObservableBoolean(true)

  private val currentAgeEarningsInCurrency: String
    get() {
      val year = currentAge.get()
      val earnings: Int = values.get()?.firstOrNull { it.year == year }?.value ?: 0
      return convertEarningsToString(earnings)
    }

  private val currentAgeEarningsInValue: Int
    get() {
      val year = currentAge.get()
      return values.get()?.firstOrNull { it.year == year }?.value ?: 0
    }


  private fun convertEarningsToString(earnings: Int): String {
    return (earnings / 100).toBigNumber.prefixCurrency(currency.get())
  }

  init {
    val list = mutableListOf<ChartValuePerYear>()

    var revenue = 345465.0
    for (i in 18 until 37) {
      if (i < 26) {
        revenue += revenue * (if (Random(1).nextBoolean()) 0.9 else 1.3)
      } else {
        revenue -= revenue * (if (Random(1).nextBoolean()) 0.7 else 0.5)
      }
      list.add(ChartValuePerYear(i, revenue.toInt()))
    }

    values.set(list)

    currentAgeEarnings.set(currentAgeEarningsInCurrency)
  }

  val data = liveData {
    val list = repository.getList()
    emit(list)
  }

  fun highlightData(h: Highlight?) {
    highlight.set(h)
    if (h == null) {
      currentAgeEarnings.set(currentAgeEarningsInCurrency)
      return
    }

    currentAgeEarnings.set(convertEarningsToString(h.y.toInt()))

    val currentEarningsDelta = when {
      currentAgeEarningsInValue < h.y -> h.y / currentAgeEarningsInValue
      currentAgeEarningsInValue > h.y -> -currentAgeEarningsInValue / h.y
      else -> 0f
    } * 100
    earningsPercentageDelta.set(currentEarningsDelta)
  }

  fun onContractsCheck(checked: Boolean) {
    contractsChecked.set(checked)
  }

  fun onAllIncomeCheck(checked: Boolean) {
    allIncomeChecked.set(checked)
  }
}