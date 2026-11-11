package edu.stevens.cs597.seatfinder

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes

class SeatMapView : View {
    private var mapDrawable: Drawable? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.SeatMapView, defStyleAttr, 0) {
            if (hasValue(R.styleable.SeatMapView_mapDrawable)) {
                mapDrawable = getDrawable(R.styleable.SeatMapView_mapDrawable)
                mapDrawable?.callback = this@SeatMapView
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        mapDrawable?.let {
            it.setBounds(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight)
            it.draw(canvas)
        }
    }
}