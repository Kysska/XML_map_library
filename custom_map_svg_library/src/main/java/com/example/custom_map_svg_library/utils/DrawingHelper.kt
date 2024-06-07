package com.example.custom_map_svg_library.utils

import android.graphics.Canvas
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.Log
import com.example.custom_map_svg_library.models.Marker
import com.example.custom_map_svg_library.models.Part
import com.example.custom_map_svg_library.models.Text

internal class DrawingHelper {

    private val paint = Paint()

    fun drawPart(
        canvas: Canvas,
        parts: List<Part>,
        selectedPart: Part?,
        markers: List<Marker>,
        titles: List<Text>,
        selectedFillColor: Int,
        selectedStrokeColor: Int,
        strokeWidth: Float?,
        strokeAlpha: Float?,
        fillAlpha: Float?,
        strokeLineCap: String?,
        strokeLineJoin: String?,
        strokeMiterLimit: Float?
    ){
        canvas.save()
        paint.isAntiAlias = true
        paint.pathEffect = CornerPathEffect(20f)

        for (part in parts) {
            val path = part.region.boundaryPath

            paint.run {
                style = Paint.Style.FILL
                color = if(selectedPart == part) selectedFillColor else part.color
                alpha = ((fillAlpha ?: part.fillAlpha) * 255).toInt()

                canvas.drawPath(path, this)

                style = Paint.Style.STROKE
                color = if(selectedPart == part) selectedStrokeColor else part.strokeColor
                setStrokeWidth(strokeWidth ?: part.strokeWidth)
                alpha = ((strokeAlpha ?: part.strokeAlpha) * 255).toInt()

                strokeCap = when (strokeLineCap ?: part.strokeLineCap) {
                    "butt" -> Paint.Cap.BUTT
                    "round" -> Paint.Cap.ROUND
                    "square" -> Paint.Cap.SQUARE
                    else -> strokeCap
                }

                strokeJoin = when (strokeLineJoin ?: part.strokeLineJoin) {
                    "miter" -> Paint.Join.MITER
                    "round" -> Paint.Join.ROUND
                    "bevel" -> Paint.Join.BEVEL
                    else -> strokeJoin
                }

                strokeMiter = strokeMiterLimit ?: part.strokeMiterLimit
                canvas.drawPath(path, this)
            }

        }

        for (marker in markers) {
            if (marker.bitmap != null) {
                canvas.drawBitmap(marker.bitmap, marker.x, marker.y, paint)
            } else {
                canvas.drawCircle(marker.x, marker.y, marker.radius, marker.paint ?: Paint())
            }
        }

        for (title in titles){
            canvas.drawText(title.title, title.x, title.y, title.paint)
        }
        canvas.restore()
    }

}