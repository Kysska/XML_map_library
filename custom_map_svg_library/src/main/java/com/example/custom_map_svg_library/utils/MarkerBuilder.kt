package com.example.custom_map_svg_library.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import com.example.custom_map_svg_library.models.Marker

class MarkerBuilder(private val x: Float, private val y: Float, private val radius: Float) {
    private var drawable: Drawable? = null
    private var paint: Paint? = null

    fun setDrawable(drawable: Drawable?): MarkerBuilder {
        this.drawable = drawable
        return this
    }

    fun setPaint(paint: Paint?): MarkerBuilder {
        this.paint = paint
        return this
    }

    fun build(): Marker {
        return if (drawable != null) {
            val width = drawable!!.intrinsicWidth
            val height = drawable!!.intrinsicHeight

            val scaledWidth = if (width > MAX_RADIUS) MAX_RADIUS else width
            val scaledHeight = if (height > MAX_RADIUS) MAX_RADIUS else height

            val bitmap = Bitmap.createBitmap(
                scaledWidth,
                scaledHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable!!.setBounds(0, 0, radius.toInt(), radius.toInt())
            drawable!!.draw(canvas)


            Marker( x, y, radius, bitmap = bitmap, paint = paint)
        } else if (paint != null) {
            Marker( x, y, radius, paint!!)
        } else {
            Marker( x, y, radius)
        }
    }

    companion object{
        private const val MAX_RADIUS = 50
    }
}
