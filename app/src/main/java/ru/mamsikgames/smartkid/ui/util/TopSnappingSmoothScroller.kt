package ru.mamsikgames.smartkid.ui.util

import android.content.Context
import androidx.recyclerview.widget.LinearSmoothScroller

class TopSnappingSmoothScroller(context: Context) : LinearSmoothScroller(context) {
    override fun getVerticalSnapPreference(): Int {
        return SNAP_TO_START
    }
}