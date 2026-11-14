package edu.stevens.cs597.seatfinder

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

class SeatMapView : View {
    companion object {
        const val OFFSET_X = "offset_x"
        const val OFFSET_Y = "offset_y"
        const val SCALE = "scale"
        const val SUPER = "super"
    }

    @DrawableRes var mapDrawableId: Int = 0
        set(value) {
            field = value
            mapDrawable = AppCompatResources.getDrawable(context, value) as? BitmapDrawable
            mapDrawable?.callback = this@SeatMapView
        }

    private var mapDrawable: BitmapDrawable? = null
    private var offsetX: Float = 0F
    private var offsetY: Float = 0F
    private var scale: Float = 1F

    private lateinit var gestureDetector: GestureDetector
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
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

    private fun bitmapToScreen(x: Int, y: Int): Pair<Float, Float> {
        val mapWidth = mapDrawable?.bitmap?.width ?: 0
        val mapHeight = mapDrawable?.bitmap?.height ?: 0
        return Pair(
            (x - mapWidth / 2) * scale + width / 2 + offsetX,
            (y - mapHeight / 2) * scale + height / 2 + offsetY
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val gestureHandled = gestureDetector.onTouchEvent(event)
        val scaleGestureHandled = scaleGestureDetector.onTouchEvent(event)
        return gestureHandled || scaleGestureHandled || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mapDrawable?.let {
            val (left, top) = bitmapToScreen(0, 0)
            val (right, bottom) = bitmapToScreen(it.bitmap.width, it.bitmap.height)
            it.setBounds(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
            it.draw(canvas)
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putFloat(OFFSET_X, offsetX)
        bundle.putFloat(OFFSET_Y, offsetY)
        bundle.putFloat(SCALE, scale)
        bundle.putParcelable(SUPER, super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            offsetX = state.getFloat(OFFSET_X)
            offsetY = state.getFloat(OFFSET_Y)
            scale = state.getFloat(SCALE)
            super.onRestoreInstanceState(state.getParcelable(SUPER))
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}