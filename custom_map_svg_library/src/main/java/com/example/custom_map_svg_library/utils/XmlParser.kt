package com.example.custom_map_svg_library.utils

import android.content.Context
import android.graphics.RectF
import android.graphics.Region
import android.util.Xml
import androidx.core.graphics.PathParser
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toRect
import com.example.custom_map_svg_library.DefaultValues
import com.example.custom_map_svg_library.models.Part
import org.xmlpull.v1.XmlPullParser
import java.util.UUID
import kotlin.math.max
import kotlin.math.min

internal class XmlParser(private val context: Context) {

    fun parse(xmlName : Int) : List<Part>{
        val list = context.resources
            .openRawResource(xmlName)
            .use {
                val parser = Xml.newPullParser().apply {
                    setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
                    setInput(it, null)
                }

                val list = mutableListOf<Part>()
                while (parser.eventType != XmlPullParser.END_DOCUMENT) {
                    parser.run {
                        if (eventType == XmlPullParser.START_TAG && name == "path") {

                            val id = getAttributeValue(null, "android:id") ?: UUID.randomUUID().toString()

                            val title = getAttributeValue(null, "android:name") ?: getAttributeValue(null, "android:title") ?: DefaultValues.DEFAULT_TITLE

                            val pathString = getAttributeValue(null, "android:pathData") ?: getAttributeValue(null, "android:d") ?: throw IllegalArgumentException("Path data is missing")
                            val region = computeRegion(pathString)

                            val color = try {
                                getAttributeValue(null, "android:fillColor")?.toColorInt() ?: DefaultValues.DEFAULT_COLOR
                            } catch (e: IllegalArgumentException) {
                                DefaultValues.DEFAULT_COLOR
                            }

                            val strokeColor = try {
                                getAttributeValue(null, "android:strokeColor")?.toColorInt() ?: DefaultValues.DEFAULT_STROKE_COLOR
                            } catch (e: IllegalArgumentException) {
                                DefaultValues.DEFAULT_STROKE_COLOR
                            }

                            val strokeWidth = getAttributeValue(null, "android:strokeWidth")?.toFloat() ?: DefaultValues.DEFAULT_STROKE_WIDTH

                            val strokeAlpha = getAttributeValue(null, "android:strokeAlpha")?.toFloat() ?: DefaultValues.DEFAULT_STROKE_ALPHA

                            val fillAlpha = getAttributeValue(null, "android:fillAlpha")?.toFloat() ?: DefaultValues.DEFAULT_FILL_ALPHA

                            val strokeLineCap = getAttributeValue(null, "android:strokeLineCap") ?: DefaultValues.DEFAULT_STROKE_LINE_CAP

                            val strokeLineJoin = getAttributeValue(null, "android:strokeLineJoin") ?: DefaultValues.DEFAULT_STROKE_LINE_JOIN

                            val strokeMiterLimit = getAttributeValue(null, "android:strokeMiterLimit")?.toFloat() ?: DefaultValues.DEFAULT_STROKE_MITER_LIMIT

                            list.add(Part(id, title, color, region, strokeColor, strokeWidth, strokeAlpha, fillAlpha, strokeLineCap, strokeLineJoin, strokeMiterLimit, DefaultValues.DEFAULT_STATE_SELECTED))
                        }
                        next()
                    }
                }
                list
            }
        return list
    }

    private fun computeRegion(pathString: String): Region {
        val path = PathParser.createPathFromPathData(pathString)
        val rectF = RectF()
        path.computeBounds(rectF, true)

        svgTop = min(svgTop, rectF.top)
        svgBottom = max(svgBottom, rectF.bottom)
        svgLeft = min(svgLeft, rectF.left)
        svgRight = max(svgRight, rectF.right)

        return Region().apply { setPath(path, Region(rectF.toRect())) }
    }

    companion object{
        var svgTop = Float.MAX_VALUE
        var svgBottom = Float.MIN_VALUE
        var svgLeft = Float.MAX_VALUE
        var svgRight = Float.MIN_VALUE
    }
}