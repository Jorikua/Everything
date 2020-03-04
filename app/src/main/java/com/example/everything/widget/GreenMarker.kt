package com.example.everything.widget

import android.content.Context
import com.github.mikephil.charting.data.Entry

class GreenMarker(context: Context) : BaseMarker(context) {
    override fun markerText(e: Entry): String {
        return e.x.toInt().toString()
    }
}