package com.example.dzen_vk_cup_2022.task_2.evaluation_article

import com.github.javafaker.Faker
import kotlin.random.Random

class EvaluationArticleRepository {

    private val faker = Faker()
    private val listTask = mutableMapOf<Int, TaskFillingDto>()

    fun getTaskByPosition(position: Int): TaskFillingDto {
        if (listTask[position] == null) {
            listTask[position] = TaskFillingDto(generateTask())
        }
        return listTask[position]!!
    }

    private fun generateTask(): String {
        return faker.lorem().sentences(1)[0]
    }

    fun getCount() = 150

    data class TaskFillingDto(
        val sentences: String,
        var currentEvaluation: Int = -1
    )
}