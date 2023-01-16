package com.example.dzen_vk_cup_2022.task_1.views

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Поиск центра третьего круга
 *
 * Он равен нижнему пересечению окружности с радиусом = R самого нижнего круга + R нового круга (пересечение с прямой Y=Const)
 *
 * @return Нижнее пересечение окружности и прямой Y=x
 * @param k - Const из (Y=Const)
 * @param radius - радиус круга с центром в AB
 */
fun lowerIntersectionStraightLineAndCircle(AB: Pair<Float, Float>, radius: Float, k: Float): Pair<Float, Float> {
    val rSquared = radius * radius
    val kMinusASquared = (k - AB.first) * (k - AB.first)
    val y = AB.second + sqrt(abs(rSquared - kMinusASquared))
    return Pair(AB.first, y)
}

fun belongingOfCirclePoint(circle: CircleMaker.Circle, pointX: Float, pointY: Float): Boolean {
    val rSquared = circle.r * circle.r
    val xMinusASquared = (pointX - circle.a) * (pointX - circle.a)
    val yMinusBSquared = (pointY - circle.b) * (pointY - circle.b)
    return rSquared > xMinusASquared + yMinusBSquared
}


