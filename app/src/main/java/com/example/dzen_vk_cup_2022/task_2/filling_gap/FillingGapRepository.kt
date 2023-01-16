package com.example.dzen_vk_cup_2022.task_2.filling_gap

import com.github.javafaker.Faker
import org.apache.commons.lang3.tuple.MutablePair
import kotlin.random.Random

class FillingGapRepository {

    private val faker = Faker()
    private val listTask = mutableMapOf<Int, TaskFillingDto>()

    fun getTaskByPosition(position: Int): TaskFillingDto {
        if (listTask[position] == null) {
            listTask[position] = generateTask()
        }
        return listTask[position]!!
    }

    private fun generateTask(): TaskFillingDto {
        val listWordsText = getText()
        return TaskFillingDto(listWordsText)
    }

    private fun getText(): List<MutablePair<String, Boolean>> {
        val text = faker.lorem().sentences(1)[0].split(" ").toMutableList()
        return setGapsInSentence(text.map { MutablePair(it, false) } as MutableList<MutablePair<String, Boolean>>)
    }

    private fun setGapsInSentence(sentences: MutableList<MutablePair<String, Boolean>>): List<MutablePair<String, Boolean>> {
        val listInsertPositionsNumbers = (1 until (sentences.size - 1)).filter { Random.nextBoolean() }
        if (listInsertPositionsNumbers.isEmpty()) {
            sentences.add(sentences.size - 1, MutablePair("", true))
        }
        listInsertPositionsNumbers.forEachIndexed { ix, countValue ->
            sentences.add(ix + countValue, MutablePair("", true))
        }
        return sentences
    }

    fun getCount() = 150

    data class TaskFillingDto(
        val sentences: List<MutablePair<String, Boolean>>  //List<Pair<Text, isEditText>>
    )
}