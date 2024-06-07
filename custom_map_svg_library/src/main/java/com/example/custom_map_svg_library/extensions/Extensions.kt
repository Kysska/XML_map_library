package com.example.custom_map_svg_library.extensions

import android.view.View


internal fun View.initState(){
    translationX = 0f
    translationY = 0f
    pivotX = (width/2).toFloat()
    pivotY = (height/2).toFloat()
}
