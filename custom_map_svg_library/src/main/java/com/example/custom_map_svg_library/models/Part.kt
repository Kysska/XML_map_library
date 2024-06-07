package com.example.custom_map_svg_library.models

import android.graphics.Region

data class Part(
    val id : String,
    val name : String,
    var color : Int,
    val region : Region,
    val strokeColor: Int,
    val strokeWidth: Float,
    val strokeAlpha: Float,
    val fillAlpha: Float,
    val strokeLineCap: String,
    val strokeLineJoin: String,
    val strokeMiterLimit: Float,
    var isEnabledPart : Boolean
)
