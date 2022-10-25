package com.example.alias.extensions

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.addListener
import androidx.viewpager2.widget.ViewPager2

// ViewPager Transition With Custom Duration
fun ViewPager2.animatePagerTransition(transitionDuration: Long) {
    val animator = ValueAnimator.ofInt(0, width)

    animator.addListener(
        onStart = {},
        onEnd = { endFakeDrag() },
        onCancel = { endFakeDrag() },
        onRepeat = {}
    )

    animator.interpolator = AccelerateInterpolator()

    var oldDragPosition = 0f
    animator.addUpdateListener {
        val dragPosition = (it.animatedValue as Int).toFloat()
        val dragOffset = dragPosition - oldDragPosition
        oldDragPosition = dragPosition
        fakeDragBy(-dragOffset)
    }

    animator.duration = transitionDuration
    if (beginFakeDrag())
        animator.start()
}