package com.nickolay.dellme.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.nickolay.android2.R
import kotlin.math.min


class TempHimbView:
    View,
    SensorEventListener {
    var charTemp = "\uD83C\uDF21" //нет конечного решения это переменная или изменяемая константа
    var charHimb = "\uD83D\uDCA7" //нет конечного решения это переменная или изменяемая константа
    var color = Color.BLACK
    private var realWidth = 2f
    private var realHeigh = 2f
    private var paint = Paint().apply {
                    style = Paint.Style.FILL
                    textSize = 30f
                }
    private var autohide = false
    private var yTemp = 0f
    private var yHimd = 0f
    private var xText = 0f
    private var val_temp = 0f
    private var val_himd = 0f

    var sensorTemperature: Sensor? = null
        set(value) {
            Log.d("myLOG", "ST $value")
            field = value
            visibility = if (value == null && sensorHimidity == null && autohide)
                GONE
            else
                VISIBLE
        }

    var sensorHimidity: Sensor? = null
        set(value) {
            field = value
            visibility = if (value == null && sensorHimidity == null && autohide)
                GONE
            else
                VISIBLE
        }

    constructor(context: Context) : this(context, null) {
        paint.color = color
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs, 0) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TempHimbView,0, 0)
        try {
            paint.color = styledAttributes.getColor(R.styleable.TempHimbView_color, Color.BLACK)
            autohide    = styledAttributes.getBoolean(R.styleable.TempHimbView_autohide, false)
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        realWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        realHeigh = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        paint.textSize = min(realWidth, realHeigh) / 3

        yTemp = paint.textSize
        if (sensorTemperature == null) {
            yHimd = paint.textSize * 1.2f
        } else{
            yHimd = paint.textSize * 2.5f
            xText = paint.textSize * 1.2f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (sensorTemperature != null || !autohide){
            canvas.drawText(charHimb, 0f, paint.textSize, paint)
            canvas.drawText(String.format("%.1f°C", val_temp), xText, yTemp, paint)
        }
        if (sensorHimidity != null || !autohide){
        canvas.drawText(charTemp, 0f, paint.textSize * 2.5f, paint)
        canvas.drawText("%.1f".format(val_himd) + " %", xText, yHimd, paint)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE ->
                if (val_temp != event.values[0]) {
                    val_temp = event.values[0]
                    invalidate()
                }
            Sensor.TYPE_RELATIVE_HUMIDITY ->
                if (val_himd != event.values[0]) {
                    val_himd = event.values[0]
                    invalidate()
                }
        }
    }

}