package com.example.dzen_vk_cup_2022.task_2.matching_elements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.view_pager.AdapterViewPager

class MatchingElementAdapter(
    private val matchingElementRepository: MatchingElementRepository
) : AdapterViewPager<MatchingElementAdapter.ViewHolder>() {
    private val count = matchingElementRepository.getCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.matching_element_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionNumberText = "Задача ${position + 1}/$count"
        holder.taskNumber.text = questionNumberText
        holderCache[position] = holder
    }

    override fun getItemCount(): Int = count

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNumber: TextView = itemView.findViewById(R.id.task_number)
        private val containerLine: LinearLayout = itemView.findViewById(R.id.container_line)
        val layoutLeft: LinearLayout = containerLine.findViewById(R.id.layout_placeholder_matching_element_left)
        val layoutRight: LinearLayout = containerLine.findViewById(R.id.layout_placeholder_matching_element_right)

        fun initLene(currentLine: MatchingElementRepository.ElementLine) {
            initLeftElement(currentLine)
            initRightElement(currentLine)
        }

        private fun initLeftElement(currentLine: MatchingElementRepository.ElementLine) {
            val viewElement = LayoutInflater.from(layoutLeft.context).inflate(R.layout.matching_left_element_item, layoutLeft, false)
            currentLine.left.textView = viewElement.findViewById(R.id.left_element)
            currentLine.left.textView.text = currentLine.left.text
            layoutLeft.addView(viewElement)
        }

        private fun initRightElement(currentLine: MatchingElementRepository.ElementLine) {
            val viewElement = LayoutInflater.from(layoutRight.context).inflate(R.layout.matching_right_element_item, layoutRight, false)
            currentLine.right.textView = viewElement.findViewById(R.id.right_element)
            currentLine.right.textView.text = currentLine.right.text

            currentLine.right.arrow = viewElement.findViewById<ImageView>(R.id.arrow)
            if (currentLine.right.showArrow) {
                currentLine.right.arrow.isVisible = true
            }
            layoutRight.addView(viewElement)
        }
    }
}