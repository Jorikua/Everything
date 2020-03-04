package com.example.everything

import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.everything.widget.charts.ChartValuePerYear
import com.github.mikephil.charting.highlight.Highlight
import java.util.*
import kotlin.random.Random

class MainViewModel(private val repository: Repository) : ViewModel() {

  val values = ObservableField<List<ChartValuePerYear>>()
  val currency = ObservableField<Currency>(Currency.getInstance(Locale.getDefault()))
  val currentAge = ObservableInt(22)

  val highlight = ObservableField<Highlight>()

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
  }

  val data = liveData {
    val list = repository.getList()
    emit(list)
  }

  fun highlightData(h: Highlight?) {
    highlight.set(h)
  }
}