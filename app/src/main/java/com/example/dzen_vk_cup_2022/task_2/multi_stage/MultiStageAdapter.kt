package com.example.dzen_vk_cup_2022.task_2.multi_stage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.view_pager.AdapterViewPager

class MultiStageAdapter(
    private val multiStageRepository: MultiStageRepository
) : AdapterViewPager<MultiStageAdapter.ViewHolder>() {

    private val count = multiStageRepository.getCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.multi_stage_survey_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMultiStage = multiStageRepository.getCurrentMultiStage(position)

        val questionNumberText = "Вопрос ${position + 1}/$count"
        holder.questionNumber.text = questionNumberText
        holder.textQuestion.text = currentMultiStage.text
        holderCache[position] = holder
    }

    override fun getItemCount(): Int = count

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionNumber: TextView = itemView.findViewById(R.id.question_number)
        val textQuestion: TextView = itemView.findViewById(R.id.text_question)
        val layoutAnswers: LinearLayout = itemView.findViewById(R.id.layout_answers)
    }
}