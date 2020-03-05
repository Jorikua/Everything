package com.example.everything

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import com.example.everything.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

  private val viewModel by viewModel<MainViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }
}

@BindingAdapter("percentage")
fun setPercentage(textView: TextView, percentage: Float?) {
  if (percentage == null) return
  val value: Number = when (percentage) {
    0f -> percentage.toInt()
    else -> percentage
  }

  val bgColor = if (percentage < 0) textView.context.getColor(R.color.red_alpha) else textView.context.getColor(R.color.green_alpha)
  val textColor = if (percentage < 0) textView.context.getColor(R.color.red) else textView.context.getColor(R.color.green)

  val text = if (value == 0) value.toString() else String.format("%.2f", value)
  textView.text = "$text%"
  textView.setTextColor(textColor)
  textView.setBackgroundColor(bgColor)
}
