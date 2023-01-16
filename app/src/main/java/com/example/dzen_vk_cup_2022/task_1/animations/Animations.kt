package com.example.dzen_vk_cup_2022.task_1.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import androidx.core.view.isInvisible
import com.example.dzen_vk_cup_2022.R
import java.util.concurrent.atomic.AtomicBoolean

//TODO Хотел сделать скрытие панели через передвижения её вниз https://habr.com/ru/post/309200/
class Animations(private val context: Context) {

    private val displayWidth: Int
    private val displayHeight: Int
    private var prevCoordinate: Pair<Float, Float>? = null
    var prevView: View? = null

    init {
        val dm = DisplayMetrics()
        context.display?.getMetrics(dm)
        displayWidth = dm.widthPixels
        displayHeight = dm.heightPixels
    }

    fun bringBackLastOne() {
        prevView?.setBackgroundResource(R.drawable.rect_round_10_grey)
        prevCoordinate?.let { animateFlying(prevView!!, it.first, it.second, 200) }
        prevCoordinate = null
        prevView = null
    }

    fun fly(view: View) {
        view.setBackgroundResource(R.drawable.category_pressed)
        val viewGroup = view.parent as ViewGroup
        viewGroup.clipChildren = false
        viewGroup.clipToPadding = false
        val viewGroup2 = viewGroup.parent as ViewGroup
        viewGroup2.clipChildren = false
        viewGroup2.clipToPadding = false

        val toY = (displayHeight / 9)
        val toX = (displayWidth / 2) - (view.width / 2)
        animateFlying(view, toX.toFloat(), -toY.toFloat(), 500)
    }


    private fun animateFlying(view: View, toX: Float, toY: Float, duration: Long) {
        prevCoordinate = Pair(view.x, view.y)
        prevView = view
        view.animate().x(toX).y(toY).duration = duration
    }

    fun animateFading(view: View) {
        view.animate().alpha(0f).setDuration(500).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.isInvisible = true
            }
        })
    }

    fun animateFadingBoard(board: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.pop_enter)
        board.visibility = View.VISIBLE
        board.startAnimation(animation)
    }

    fun animateAppearanceBoard(board: View) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.pop_exit)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                board.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        board.startAnimation(animation)
    }

    fun animateAppearance(view: View) {
        view.animate().alpha(1f).setDuration(500).setListener(null)
    }

    var stateHidePatterns = AtomicBoolean(false)
    fun hidePatterns(mainCategoriesLayout: ConstraintLayout) {
        mainCategoriesLayout.forEach { currentFrame ->
            if (currentFrame is FrameLayout) {
                currentFrame.tag?.let {
                    if (it != "board" && it != prevView?.tag) {
                        currentFrame.animate()
                            .alpha(0f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    super.onAnimationEnd(animation)
                                    currentFrame.isInvisible = true
                                    inProcessOfAnimationShow.set(false)
                                }
                            })
                    }
                }
            }
        }
    }

    var inProcessOfAnimationShow = AtomicBoolean(false)
    fun showPatterns(mainCategoriesLayout: ConstraintLayout) {
        inProcessOfAnimationShow.set(true)
        mainCategoriesLayout.forEach { currentFrame ->
            if (currentFrame is FrameLayout) {
                currentFrame.tag?.let {
                    if (it != "board" && it != prevView?.tag) {
                        currentFrame.isInvisible = false
                        currentFrame.animate().alpha(1f).setDuration(200).setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                super.onAnimationEnd(animation)
                                inProcessOfAnimationShow.set(false)
                            }
                        })
                    }
                }
            }
        }
    }
}

