package com.example.dzen_vk_cup_2022.task_2.dragging_options

import com.github.javafaker.Faker
import kotlin.random.Random

class DraggingVariantRepository {

    private val faker = Faker()
    private val listTask = mutableMapOf<Int, TaskDraggingVariant>()

    fun getTaskByPosition(position: Int): TaskDraggingVariant {
        if (listTask[position] == null) {
            listTask[position] = generateTask()
        }
        return listTask[position]!!
    }

    private fun generateTask(): TaskDraggingVariant {
        val listWordsText = getText()
        val answers = getAnswersOptions(listWordsText.count { it == TEXT_GAPS })
        return TaskDraggingVariant(listWordsText, answers)
    }

    private fun getText(): List<String> {
        val text = faker.lorem().sentences(1)[0].split(" ").toMutableList()
        return setGapsInSentence(text)
    }

    private fun getAnswersOptions(count: Int): List<String> {
        return faker.lorem().words(count)
    }

    private fun setGapsInSentence(sentences: MutableList<String>): List<String> {
        val listInsertPosition = (1 until (sentences.size - 1)).filter { Random.nextBoolean() }
        if (listInsertPosition.isEmpty()) {
            sentences.add(sentences.size - 1, TEXT_GAPS)
        }
        listInsertPosition.forEachIndexed { ix, countValue ->
            sentences.add(ix + countValue, TEXT_GAPS)
        }
        return sentences
    }


    fun getCount() = 150

    data class TaskDraggingVariant(
        val sentences: List<String>,
        val answerOptions: List<String>,
        val inserted: MutableMap<Int, String> = mutableMapOf() //<Порядковый номер, "Text">
    )


    companion object {
        const val TEXT_GAPS = "______"
    }
}