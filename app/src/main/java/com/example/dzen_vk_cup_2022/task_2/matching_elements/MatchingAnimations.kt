package com.example.dzen_vk_cup_2022.task_2.matching_elements

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.example.dzen_vk_cup_2022.task_2.utils.AnimationTools

class MatchingAnimations {

    fun animationSwapViews(view1: View, view2: View, animationEnd: () -> Unit) {
        val v1Y = view1.y
        val v2Y = view2.y

        view1.animate().y(v2Y).duration = SWAP_TIME

        with(view2.animate()) {
            y(v1Y).duration = SWAP_TIME
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    animationEnd.invoke()
                    setListener(null)
                }
            })
        }
    }

    fun zoomAnimation(view: View) {
        AnimationTools.viewClip(view)
        val animBuf = view.animate().scaleX(1.05f).scaleY(1.1f).setDuration(50)
        animBuf.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.animate().scaleX(1.0f).scaleY(1.0f).duration = 50
                animBuf.setListener(null)
            }
        })
    }

    fun showAnimation(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate().alpha(1f).duration = SHOW_ARROW_TIME
    }

    companion object {
        const val SHOW_ARROW_TIME = 100L
        const val SWAP_TIME = 400L
    }
}