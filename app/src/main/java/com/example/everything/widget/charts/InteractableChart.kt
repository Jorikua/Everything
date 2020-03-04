package com.example.everything.widget.charts

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewParent
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

open class InteractableChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChart(context, attrs, defStyleAttr), OnChartGestureListener, OnChartValueSelectedListener {
    var chartListener: OnChartValueSelectedListener? = null
        set(value) {
            field = value
            setOnChartValueSelectedListener(value)
        }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
        chartListener?.onNothingSelected()
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {

    }

    override fun onChartSingleTapped(me: MotionEvent?) {

    }

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {

    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {

    }

    override fun onChartLongPressed(me: MotionEvent?) {

    }

    override fun onChartDoubleTapped(me: MotionEvent?) {

    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {

    }

    fun updateData(data: LineData) {
        data.notifyDataChanged()
        notifyDataSetChanged()
        invalidate()
        initAnimate()
    }

    private fun initAnimate() {
        animateX(300, Easing.EaseInOutBack)
        animateY(200, Easing.Linear)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                (context as? Activity).enableParentTouchEvent(parent)
                isHighlightPerDragEnabled = false
                highlightValues(emptyArray())
                chartListener?.onNothingSelected()
            }

            else -> {
                (context as? Activity).disableParentTouchEvent(parent)
                isHighlightPerDragEnabled = true
            }
        }

        return super.onTouchEvent(event)
    }
}



fun Activity?.disableParentTouchEvent(parent: ViewParent) {
    if (this == null) return
//    val pagerListing = findViewById<ViewPager2>(R.id.pagerListing)
//    pagerListing?.requestDisallowInterceptTouchEvent(true)
    parent.parent.requestDisallowInterceptTouchEvent(true)
}

fun Activity?.enableParentTouchEvent(parent: ViewParent) {
    if (this == null) return
//    val pagerListing = findViewById<ViewPager2>(R.id.pagerListing)
//    pagerListing?.requestDisallowInterceptTouchEvent(false)
    parent.parent.requestDisallowInterceptTouchEvent(false)
}
