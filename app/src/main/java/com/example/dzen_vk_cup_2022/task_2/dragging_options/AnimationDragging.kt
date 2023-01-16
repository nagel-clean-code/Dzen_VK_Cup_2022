package com.example.dzen_vk_cup_2022.task_2.dragging_options

import android.view.View

class AnimationDragging {
    fun getCoordinateView(view: View): Pair<Float, Float> {
        val point = IntArray(2)
        view.getLocationOnScreen(point)
        val x = point[0]
        val y = point[1]
        return Pair(x.toFloat(), y.toFloat())
    }

    fun animationOnOldPositionView(view: View) {
        val originalCoordinate = (view.tag as Pair<*, *>)
        view.animate()
            .translationX(originalCoordinate.first as Float)
            .translationY(originalCoordinate.second as Float)
            .setDuration(200)
            .start()
    }
}