package com.example.dzen_vk_cup_2022.task_2.filling_gap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.view_pager.AdapterViewPager

class FillingGapAdapter(
    private val fillingGapRepository: FillingGapRepository
) : AdapterViewPager<FillingGapAdapter.ViewHolder>() {

    private val count = fillingGapRepository.getCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.filling_gap_item, parent, false)
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
        val viewGroupText: ConstraintLayout = itemView.findViewById(R.id.container_flows)
        val placeholderForText: Flow = viewGroupText.findViewById(R.id.flow_text)
    }

}