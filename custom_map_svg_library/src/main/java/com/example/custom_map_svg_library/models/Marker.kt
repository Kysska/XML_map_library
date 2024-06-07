package com.example.custom_map_svg_library.models

import android.graphics.Bitmap
import android.graphics.Paint

data class Marker(
    val x : Float,
    val y : Float,
    val radius : Float,
    val paint : Paint? = Paint(),
    val bitmap: Bitmap? = null
)
