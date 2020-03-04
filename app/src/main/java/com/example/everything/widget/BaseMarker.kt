package com.example.everything.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.example.everything.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.green_marker_view.view.*

abstract class BaseMarker(context: Context) : MarkerView(context, R.layout.green_marker_view) {

    private var rectPaint = Paint()

    abstract fun markerText(e: Entry): String

    init {
        with(rectPaint) {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.colorPrimary)
        }
    }

    override fun refreshContent(e: Entry, highlight: Highlight?) {
        tvContent!!.text = markerText(e)
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF? {
        return MPPointF((-(width / 2)).toFloat(), 0f)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val offset = getOffsetForDrawingAtPoint(posX, posY)
        val saveId = canvas.save()

        canvas.translate(posX + offset.x, 0f)

        val rectF = RectF(0f, 0f, 60.dpToPx, 20.dpToPx)
        canvas.drawRoundRect(rectF, 10f, 10f, rectPaint)

        draw(canvas)
        canvas.restoreToCount(saveId)
    }
}

val Int.dpToPx: Float get() = (this * Resources.getSystem().displayMetrics.density)