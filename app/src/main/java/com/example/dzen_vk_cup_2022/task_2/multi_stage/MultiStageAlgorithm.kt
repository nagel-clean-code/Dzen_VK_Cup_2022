package com.example.dzen_vk_cup_2022.task_2.multi_stage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.utils.AnimationTools

class MultiStageAlgorithm {

    private var countView = 0

    fun addListAnswers(holder: MultiStageAdapter.ViewHolder, currentMultiStage: MultiStageRepository.Question) {
        countView = 0
        holder.layoutAnswers.removeAllViews()   //На новую форму попадают ответы со старой формы, приходится очищать
        if (currentMultiStage.listAnswerView.isNotEmpty()) {
            currentMultiStage.listAnswerView.forEach {
                (it.parent as? ViewGroup)?.removeView(it)
                holder.layoutAnswers.addView(it)
            }
        } else {
            currentMultiStage.listAnswerView.clear()
            currentMultiStage.answersWithPercent.forEach {
                val view =
                    LayoutInflater.from(holder.layoutAnswers.context).inflate(R.layout.multi_stage_answer_item, holder.layoutAnswers, false)
                view.findViewById<TextView>(R.id.text_answer_item).text = it.first

                val viewPercent = view.findViewById<TextView>(R.id.percent)
                viewPercent.text = it.second
                view.tag = NOT_SELECTED

                initAnswerButton(view, currentMultiStage)
                currentMultiStage.listAnswerView.add(view)
                holder.layoutAnswers.addView(view)
                animationButton(view)
            }
        }
    }

    private fun animationButton(button: View) {
        button.alpha = 0f
        button.postDelayed({
            button.animate().alpha(1f).duration = 700
        }, (100 * countView++).toLong())
    }

    private fun initAnswerButton(view: View, question: MultiStageRepository.Question) {
        view.setOnClickListener {
            val isSelected = it.tag
            returnStateNotSelectedViews(question)
            if (isSelected == NOT_SELECTED) {
                val answerTextView = it.findViewById<TextView>(R.id.text_answer_item)
                answerTextView.setTextColor(view.context.resources.getColor(R.color.golden))
                view.background = view.context.resources.getDrawable(R.drawable.multi_stage_background_answer_selected_item)

                question.showPercents()
                it.findViewById<TextView>(R.id.percent).setTextColor(it.context.resources.getColor(R.color.golden))
                it.tag = SELECTED
                zoomAnimation(it)
            }
        }
    }

    private fun returnStateNotSelectedViews(question: MultiStageRepository.Question) {
        question.listAnswerView.forEach { view ->
            if (view.tag == SELECTED) {
                view.tag = NOT_SELECTED
                val answerTextView = view.findViewById<TextView>(R.id.text_answer_item)

                answerTextView.setTextColor(view.context.resources.getColor(R.color.turquoise))
                view.background = view.context.resources.getDrawable(R.drawable.multi_stage_background_answer_item)
                view.findViewById<TextView>(R.id.percent).setTextColor(view.context.resources.getColor(R.color.turquoise))
            }
        }
    }

    private fun zoomAnimation(view: View) {
        AnimationTools.viewClip(view)
        val animBuf = view.animate().scaleX(1.05f).scaleY(1.1f).setDuration(50)
        animBuf.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.animate().scaleX(1.0f).scaleY(1.0f).duration = 50
                animBuf.setListener(null)
            }
        })
    }

    companion object {
        const val SELECTED = true
        const val NOT_SELECTED = false
    }
}