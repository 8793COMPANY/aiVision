package com.corporation8793.aivision.card

import androidx.cardview.widget.CardView

interface CardAdapter {
    fun getBaseElevation(): Float
    fun getCardViewAt(position: Int): CardView
    fun getCount(): Int

    companion object {
        const val MAX_ELEVATION_FACTOR = 10
    }
}