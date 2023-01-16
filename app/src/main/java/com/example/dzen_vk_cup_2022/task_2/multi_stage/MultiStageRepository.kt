package com.example.dzen_vk_cup_2022.task_2.multi_stage

import android.view.View
import android.widget.TextView
import com.example.dzen_vk_cup_2022.R
import com.github.javafaker.Faker


class MultiStageRepository {

    private val faker = Faker()
    private var sumPercent = 0
    private val listMultiStage = mutableMapOf<Int, Question>()

    fun getCurrentMultiStage(currentPosition: Int): Question {
        return if (listMultiStage[currentPosition] != null) {
            listMultiStage[currentPosition]!!
        } else {
            val resultQuestion = generateQuestion()
            listMultiStage[currentPosition] = resultQuestion
            resultQuestion
        }
    }

    private fun generateQuestion(): Question {
        var text = faker.lorem().sentences(1).joinToString(" ")
        text = text.dropLast(1) + "?"
        val answers = mutableListOf<Pair<String, String>>()
        val countAnswers = (2..5).random()
        repeat(countAnswers) {
            val percentValue = getPercent()
            answers.add(
                Pair(
                    faker.lorem().words(3).joinToString(" "),
                    "$percentValue%"
                )
            )
        }
        sumPercent = 0
        return Question(text, answers)
    }

    private fun getPercent(): Int {
        return if (sumPercent < 100) {
            var result = (0..100).random()
            if (sumPercent + result <= 100) {
                sumPercent += result
            } else {
                result = 100 - sumPercent
                sumPercent = 100
            }
            result
        } else {
            0
        }
    }

    fun getCount(): Int {
        return 150
    }

    class Question(
        val text: String,
        val answersWithPercent: List<Pair<String, String>>,
        val listAnswerView: MutableList<View> = mutableListOf(),
    ) {

        fun showPercents() {
            listAnswerView.forEach {
                it.findViewById<TextView>(R.id.percent).visibility = View.VISIBLE
            }
        }
    }
}
