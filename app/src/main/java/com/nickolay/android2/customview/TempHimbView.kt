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
    var charTemp = "\uD83C\uDF21"
    var charHimb = "\uD83D\uDCA7"
    var color = Color.BLACK
    private var realWidth = 2f
    private var realHeigh = 2f
    private var paint = Paint()
    private var autohide = false
    private var yTemp = 0f
    private var yHimd = 0f
    private var xText = 0f
    private var valTemp = 0f
    private var valHimd = 0f

    var SensorTemperature: Sensor? = null
        set(value) {
            Log.d("myLOG", "ST $value")
            field = value
            visibility = if (value == null && SensorHimidity == null && autohide)
                GONE
            else
                VISIBLE
        }

    var SensorHimidity: Sensor? = null
        set(value) {
            field = value
            visibility = if (value == null && SensorHimidity == null && autohide)
                GONE
            else
                VISIBLE
        }

    constructor(context: Context) : this(context, null) {
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.textSize = 30f
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
        if (SensorTemperature == null) {
            yHimd = paint.textSize * 1.2f
        } else{
            yHimd = paint.textSize * 2.5f
            xText = paint.textSize * 1.2f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (SensorTemperature != null || !autohide){
            canvas.drawText(charHimb, 0f, paint.textSize, paint)
            canvas.drawText(String.format("%.1f°C", valTemp), xText, yTemp, paint)
        }
        if (SensorHimidity != null || !autohide){
        canvas.drawText(charTemp, 0f, paint.textSize * 2.5f, paint)
        canvas.drawText("%.1f".format(valHimd) + " %", xText, yHimd, paint)
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_AMBIENT_TEMPERATURE ->
                if (valTemp != event.values[0]) {
                    valTemp = event.values[0]
                    invalidate()
                }
            Sensor.TYPE_RELATIVE_HUMIDITY ->
                if (valHimd != event.values[0]) {
                    valHimd = event.values[0]
                    invalidate()
                }
        }
    }

}