package com.example.myapplication.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.R

class CustomProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var scaleCapacity: Int
    private var scaleDivisionValue: Float
    private val paint: Paint = Paint()

    private var widthMinusPadding: Float = 0f
    private var widthMinusPadding10Percent: Float = 0f
    private var widthMinusPadding90Percent: Float = 0f
    private var heightMinusPadding: Float = 0f

    var progress: Float = 0f
        set(value) {
            field = value
            postInvalidate()
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, 0, 0).apply {
            try {
                scaleCapacity = getInteger(R.styleable.CustomProgressBar_scaleCapacity, 5)
                scaleDivisionValue = 100f / scaleCapacity
                paint.color = getColor(R.styleable.CustomProgressBar_color, Color.BLACK)
            } finally {
                recycle()
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        widthMinusPadding = (width - paddingRight * 2).toFloat() / scaleCapacity
        widthMinusPadding10Percent = widthMinusPadding * 0.1f
        widthMinusPadding90Percent = widthMinusPadding - widthMinusPadding10Percent
        heightMinusPadding = (height - paddingTop).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (progress == 0f) return

        var i = 0
        var curProgress = progress

        while (curProgress >= scaleDivisionValue) {
            val left: Float = widthMinusPadding * i + paddingLeft + widthMinusPadding10Percent
            val right: Float = left + widthMinusPadding90Percent
            canvas.drawRect(
                left,
                paddingTop.toFloat(),
                right,
                heightMinusPadding,
                paint
            )
            curProgress -= scaleDivisionValue
            i++
        }

        if (curProgress > 0.9f) {
            val left: Float = widthMinusPadding * i + paddingLeft + widthMinusPadding10Percent
            val right: Float = left + (curProgress / scaleDivisionValue) * widthMinusPadding90Percent
            canvas.drawRect(
                left,
                paddingTop.toFloat(),
                right,
                heightMinusPadding,
                paint
            )
        }
    }
}