package com.example.dzen_vk_cup_2022.task_2.filling_gap

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.example.dzen_vk_cup_2022.R
import org.apache.commons.lang3.tuple.MutablePair

class FillingGapAlgorithm(
    private val context: Context
) {
    private val listViewAll = mutableListOf<View>()
    private val listEditTextElements = mutableListOf<TextView>()

    fun displayViewOnScreen(holder: FillingGapAdapter.ViewHolder, task: FillingGapRepository.TaskFillingDto) {
        cancelOldView(holder)
        addToFlowText(holder, task.sentences)
    }

    private fun cancelOldView(holder: FillingGapAdapter.ViewHolder) {
        listEditTextElements.forEach {
            val pair = it.tag as MutablePair<String, Boolean>
            pair.left = it.text.toString()
        }
        listEditTextElements.clear()
        listViewAll.forEach {
            holder.viewGroupText.removeView(it)
        }
    }

    private fun addToFlowText(holder: FillingGapAdapter.ViewHolder, sentences: List<MutablePair<String, Boolean>>) {
        sentences.forEach {
            val view = if (it.right) {
                val editTextView = createEditText(it.left)
                listEditTextElements.add(editTextView)
                editTextView.tag = it
                editTextView
            } else {
                createTextView(it.left)
            }
            listViewAll.add(view)
            holder.viewGroupText.addView(view)
            holder.placeholderForText.addView(view)
        }
    }

    private fun createTextView(text: String): TextView {
        val lParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val view = TextView(context)
        view.id = View.generateViewId()
        view.setTextColor(context.resources.getColor(R.color.super_light_turquoise))
        view.textSize = 18f
        view.typeface = ResourcesCompat.getFont(context, R.font.montserrat_semibold)
        view.layoutParams = lParams
        view.text = text
        return view
    }

    private fun createEditText(text: String): EditText {
        val view = EditText(context)
        view.setPadding(20, 0, 20, 0)
        view.id = View.generateViewId()
        view.setTextColor(context.resources.getColor(R.color.turquoise))
        view.setBackgroundResource(R.drawable.dragging_answer_element_background)
        view.textSize = 17f
        view.typeface = ResourcesCompat.getFont(context, R.font.montserrat_semibold)
        view.setText(text)

        val lParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lParams.matchConstraintMinWidth = 170
        lParams.matchConstraintMaxHeight = 70
        view.layoutParams = lParams
        return view
    }

}