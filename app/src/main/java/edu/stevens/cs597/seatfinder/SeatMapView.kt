package edu.stevens.cs597.seatfinder

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.withStyledAttributes

class SeatMapView : View {
    private var mapDrawable: Drawable? = null
    private var offsetX: Float = 0F
    private var offsetY: Float = 0F
    private var scale: Float = 1F

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

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

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                offsetX -= distanceX
                offsetY -= distanceY
                invalidate()
                return true
            }
        })

        scaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                scale *= detector.scaleFactor
                val relFocusX = detector.focusX - width / 2
                val relFocusY = detector.focusY - height / 2
                offsetX = relFocusX - (relFocusX - offsetX) * detector.scaleFactor
                offsetY = relFocusY - (relFocusY - offsetY) * detector.scaleFactor
                invalidate()
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val gestureHandled = gestureDetector.onTouchEvent(event)
        val scaleGestureHandled = scaleGestureDetector.onTouchEvent(event)
        return gestureHandled || scaleGestureHandled || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mapDrawable?.let {
            val left = (width / 2 - scale * it.intrinsicWidth / 2 + offsetX).toInt()
            val top = (height / 2 - scale * it.intrinsicHeight / 2 + offsetY).toInt()
            val right = (width / 2 + scale * it.intrinsicWidth / 2 + offsetX).toInt()
            val bottom = (height / 2 + scale * it.intrinsicHeight / 2 + offsetY).toInt()

            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }
}