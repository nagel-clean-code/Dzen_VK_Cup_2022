package com.example.dzen_vk_cup_2022.task_2.matching_elements

import android.view.View
import android.widget.TextView
import com.github.javafaker.Faker

class MatchingElementRepository {

    private val faker = Faker()
    private val listMatchingElement = mutableMapOf<Int, List<ElementLine>>()

    fun getMatchingElementByPosition(position: Int): List<ElementLine> {
        return if (listMatchingElement[position] == null) {
            val result = generateMatchingElement()
            listMatchingElement[position] = result
            result
        } else {
            listMatchingElement[position]!!
        }
    }

    private fun generateMatchingElement(): List<ElementLine> {
        val countElements = (3..6).random()

        val lines = mutableListOf<ElementLine>()

        repeat(countElements) {
            val line = ElementLine(
                ElementLeft(faker.lorem().word()),
                ElementRight((it + 1).toString())
            )
            line.left.myLine = line
            line.right.myLine = line
            lines.add(line)
        }

        return lines
    }

    fun getCount() = 150

    class ElementLine(
        var left: ElementLeft,
        val right: ElementRight
    )

    class ElementRight(text: String) : Element(text) {
        var showArrow: Boolean = false
        lateinit var arrow: View
    }

    class ElementLeft(text: String) : Element(text)

    open class Element(val text: String) {
        lateinit var textView: TextView
        lateinit var myLine: ElementLine
    }
}