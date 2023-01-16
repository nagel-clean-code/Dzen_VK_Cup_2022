package com.example.dzen_vk_cup_2022.task_1

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.isInvisible
import com.example.dzen_vk_cup_2022.databinding.ActivityTask1Binding
import com.example.dzen_vk_cup_2022.task_1.animations.Animations
import com.example.dzen_vk_cup_2022.task_1.repositories.CategoriesRepository

/**
 * Идея:
 *  Человеческую жизнь можно разделить на 6 главных категорий, из которых вытекаю подкатегории
 *
 *  1. Кухня
 *  2. Отдых и Развлечения
 *  3. Семья
 *  4. Быт
 *  5. Интересы
 *  6. Красота и Здоровье
 *
 *  Большинство людей найдут интересующие их темы в этих 6 паттернах, остальные могут найти через поиск
 */

class Task1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityTask1Binding
    private lateinit var animations: Animations
    private val repository = CategoriesRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTask1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
        initAnimations()
    }

    override fun onBackPressed() {
        if (binding.searchLayout.isInvisible) {
            returnToInitialState()
        } else {
            super.onBackPressed()
        }
    }

    private fun initAnimations() {
        animations = Animations(this)
    }

    private fun initListeners() {
        binding.mainCategories.forEach {
            if (it is FrameLayout) {
                initClickPattern(it)
            }
        }
        initScrollingView()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initScrollingView() {
        binding.scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            if (animations.inProcessOfAnimationShow.get()) return@setOnScrollChangeListener

            if (!animations.stateHidePatterns.get()) {
                animations.stateHidePatterns.set(true)
                if (scrollY > 200) {
                    animations.hidePatterns(binding.mainCategories)
                }
            } else {
                animations.stateHidePatterns.set(false)
                if (scrollY < 200) {
                    animations.showPatterns(binding.mainCategories)
                }
            }
        }
    }

    private fun returnToInitialState() {
        binding.searchLayout.isInvisible = false
        binding.scrollView.scrollTo(0, 0)
        animations.animateAppearance(binding.searchLayout)
        animations.animateAppearanceBoard(binding.board)
        animations.bringBackLastOne()
        animations.showPatterns(binding.mainCategories)
        firstClickPattern = true
    }

    private fun initClickPattern(view: FrameLayout) {
        view.setOnClickListener() {
            val nameFrame = view.tag?.toString()
            if (nameFrame == null || nameFrame == "" || view == animations.prevView) return@setOnClickListener
            repository.mapCategories[nameFrame]?.let {
                binding.soapBubblesView.listCategory = it
                animationClickPattern(view)
            }
        }
    }

    private var firstClickPattern = true
    private fun animationClickPattern(patternView: View) {
        animations.bringBackLastOne()
        animations.fly(patternView)
        if (firstClickPattern) {
            animations.animateFading(binding.searchLayout)
            animations.animateFadingBoard(binding.board)
            firstClickPattern = false
        }
    }

}