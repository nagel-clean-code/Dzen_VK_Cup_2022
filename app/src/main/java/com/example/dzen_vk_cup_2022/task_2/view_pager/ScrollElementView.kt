package com.example.dzen_vk_cup_2022.task_2.view_pager

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.widget.SeekBar
import androidx.core.view.isVisible
import com.example.dzen_vk_cup_2022.databinding.ScrollElementLayoutBinding

/**
 * @param funJumpOnPageViewPager - в этой функции обязательно должен быть включен функционал отключения скролинга viewPager
 */
class ScrollElementView(
    private val scrollElement: ScrollElementLayoutBinding,
    private val sizeSeekBar: Int,
    private val funJumpOnPageViewPager: (page: Int) -> Unit
) {

    init {
        initScrollBar()
    }

    private fun initScrollBar() {
        with(scrollElement) {
            currentNumberPage.alpha = 0f
            seekBar.max = sizeSeekBar
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    currentNumberPage.text = seekBar!!.progress.toString()
                    numberPageLeft.text = (seekBar.progress - 1).toString()
                    numberPageRight.text = (seekBar.progress + 1).toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    cancelPrevAnimationSeekBar()
                    animationStartOnTouchNumberPage()
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    funJumpOnPageViewPager.invoke(seekBar!!.progress)
                    animationStopOnTouchNumberPage()
                    hidingSeekBarAnimation()
                }
            })
            panelActivationSeekBar.setOnClickListener { appearanceSeekBarAnimation() }
        }
    }

    /**
     * Animation numberPage
     */
    private var bufPrevAnimationNumberPageRunnable: Runnable? = null
    private fun animationStopOnTouchNumberPage() {
        bufPrevAnimationNumberPageRunnable = Runnable() {
            cancelPrevAnimationNumberPage()
            scrollElement.currentNumberPage.animate().alpha(0f).duration = TIME_HIDING_NUMBER_PAGE
        }
        getHandler()?.postDelayed(bufPrevAnimationNumberPageRunnable!!, HIDING_NUMBER_PAGE_AFTER)
    }

    private fun animationStartOnTouchNumberPage() {
        cancelPrevAnimationNumberPage()
        scrollElement.currentNumberPage.animate().alpha(1f).duration = FADING_NUMBER_PAGE
    }

    private fun cancelPrevAnimationNumberPage() {
        scrollElement.currentNumberPage.animate().cancel()
        bufPrevAnimationNumberPageRunnable?.let {
            getHandler()?.removeCallbacks(it)
        }
    }

    /**
     * Animation seek bar
     */
    private var bafPrevAnimationSeekBar: Runnable? = null
    fun appearanceSeekBarAnimation() {
        cancelPrevAnimationSeekBar()
        scrollElement.panelActivationSeekBar.isVisible = false
        with(scrollElement.seekBar.animate()) {
            alpha(1f).duration = FADING_TIME_SEEK_BAR
            setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    setListener(null)
                    hidingSeekBarAnimation()
                }
            })
        }
    }

    fun hidingSeekBarAnimation() {
        bafPrevAnimationSeekBar = Runnable() {
            scrollElement.seekBar.animate().alpha(0f).duration = FADING_TIME_SEEK_BAR
            scrollElement.panelActivationSeekBar.isVisible = true
        }
        getHandler()?.postDelayed(bafPrevAnimationSeekBar!!, HIDING_SEEK_BAR_AFTER)
    }

    private fun cancelPrevAnimationSeekBar(){
        bafPrevAnimationSeekBar?.let { getHandler()?.removeCallbacks(it) }
        scrollElement.seekBar.animate().cancel()
    }

    private fun getHandler() = scrollElement.currentNumberPage.handler

    companion object {
        const val HIDING_SEEK_BAR_AFTER = 4000L
        const val FADING_TIME_SEEK_BAR = 500L
        const val TIME_HIDING_NUMBER_PAGE = 800L
        const val HIDING_NUMBER_PAGE_AFTER = 1000L
        const val FADING_NUMBER_PAGE = 200L
    }
}