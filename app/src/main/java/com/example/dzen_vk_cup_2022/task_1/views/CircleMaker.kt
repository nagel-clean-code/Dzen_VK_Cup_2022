package com.example.dzen_vk_cup_2022.task_1.views

import android.content.res.Resources
import android.util.TypedValue

class CircleMaker(
    private val resources: Resources,
    private val listCategory: List<String>,
    private var delta: Float,
    private val displayWidth: Int,
    private val displayHeight: Int
) {

    private val listCircleResult = mutableListOf<Circle>()

    init {
        delta = getPixelsFromDP(delta)
        calculate()
    }

    private fun calculate() {
        listCategory.forEach {
            listCircleResult.add(createCircle(it))
        }
    }

    private lateinit var lowestCenter: Circle
    private var centerPrevCircle1: Circle? = null
    private var centerPrevCircle2: Circle? = null
    private fun createCircle(currentCategoryName: String): Circle {
        if (centerPrevCircle2 == null) {
            val r = getRadius(currentCategoryName)
            centerPrevCircle2 = Circle(r + delta, r + delta, r, currentCategoryName)
            return centerPrevCircle2!!
        }
        if (centerPrevCircle1 == null) {
            val r = getRadius(currentCategoryName)    //Очень сложно, но нужно, потому что проблема со вторым кружком
            val k = (r / 2 + delta)
            var b = centerPrevCircle2!!.r * 2.5f + delta
            val a = displayWidth - if (k - displayWidth < r - delta) {
                b += delta
                k + delta / 2
            } else {
                k
            }
            centerPrevCircle1 = Circle(a, b, r, currentCategoryName)
            return centerPrevCircle1!!
        }

        val r = getRadius(currentCategoryName)
        val centerAxisX = getCenterAxis(r + delta)   //Ось цента нового круга

        //теперь нужно найти нижнее пересечение радиуса = (самого нижнего круга + радиус нового круга)
        val y = lowerIntersectionStraightLineAndCircle(
            Pair(lowestCenter.a, lowestCenter.b),
            r + delta + lowestCenter.r,
            centerAxisX
        ).second    //Нам важна только координата Y, x уже известна
        val result = Circle(centerAxisX, y, r, currentCategoryName)
        centerPrevCircle2 = centerPrevCircle1
        centerPrevCircle1 = result
        return result
    }

    /**
     * @return координату X - она определяет прямую Y=X
     */
    private fun getCenterAxis(radius: Float): Float {
        //Сначала определяем с какого края будет находится круг
        //С края протевоположному самому нижнему кругу
        if (centerPrevCircle1!!.b > centerPrevCircle2!!.b) {
            lowestCenter = centerPrevCircle1!!
            if (centerPrevCircle1!!.a < displayWidth / 2)  //Если слева от цента экрана самый нижний круг
                return displayWidth - radius                //То центр новой окружности будет справа
            else
                return radius                               //Иначе слева
        } else {
            lowestCenter = centerPrevCircle2!!
            if (centerPrevCircle2!!.a < displayWidth / 2)  //Если слева от цента экрана самый нижний круг
                return displayWidth - radius                //То центр новой окружности будет справа
            else
                return radius                               //Иначе слева
        }
    }

    private fun getRadius(currentCategory: String): Float {
        var radius = currentCategory.length.toFloat() * (4..15).random()
        if (radius > MAX_CIRCLE_RADIUS_DP) radius = MAX_CIRCLE_RADIUS_DP
        if (radius < MIN_CIRCLE_RADIUS_DP) radius = MIN_CIRCLE_RADIUS_DP
        return getPixelsFromDP(radius)
    }

    fun getCircles(): List<Circle> = listCircleResult

    data class Circle(val a: Float, val b: Float, val r: Float, val text: String)

    private fun getPixelsFromDP(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

    companion object {
        private const val MAX_CIRCLE_RADIUS_DP = 100f
        private const val MIN_CIRCLE_RADIUS_DP = 70f
    }
}

