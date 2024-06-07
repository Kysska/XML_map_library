package com.example.custom_map_svg_library.utils

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.View
import com.example.custom_map_svg_library.extensions.initState

internal class EventHandler(private val view : View) {

    fun zoomInRegion(relativeX: Float, relativeY: Float, widthRegion: Int, heightRegion: Int, centerX: Int, centerY: Int) {
        view.initState()
        val initialScale = view.scaleX
        val targetScale = if (widthRegion + heightRegion < 100) 4.5f else 3f

        val animatorScale = ValueAnimator.ofFloat(initialScale, targetScale)
        animatorScale.duration = 500
        animatorScale.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            view.scaleX = animatedValue
            view.scaleY = animatedValue
        }

        view.pivotX = relativeX
        view.pivotY = relativeY
        view.translationX = ((view.width / 2) - centerX).toFloat()
        view.translationY = ((view.height / 2) - centerY).toFloat()
        animatorScale.start()
    }



    fun zoomToScale(targetScale: Float) {
        view.initState()
        val initialScale = view.scaleX
        val animatorScale = ValueAnimator.ofFloat(initialScale, targetScale)
        animatorScale.duration = 500
        animatorScale.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Float
            view.scaleX = animatedValue
            view.scaleY = animatedValue
        }
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorScale)
        animatorSet.start()
    }

    fun updateTranslation(dx: Float, dy: Float) {
        val newTranslationX = view.translationX + dx
        val newTranslationY = view.translationY + dy

        val maxTranslationX = (view.width * (view.scaleX - 1)) / 2
        val minTranslationX = -maxTranslationX
        view.translationX = newTranslationX.coerceIn(minTranslationX, maxTranslationX)

        val maxTranslationY = (view.height * (view.scaleY - 1)) / 2
        val minTranslationY = -maxTranslationY
        view.translationY = newTranslationY.coerceIn(minTranslationY, maxTranslationY)
    }
}