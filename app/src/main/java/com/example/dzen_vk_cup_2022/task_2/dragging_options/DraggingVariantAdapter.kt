package com.example.dzen_vk_cup_2022.task_2.dragging_options

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.filling_gap.FillingGapAdapter
import com.example.dzen_vk_cup_2022.task_2.view_pager.AdapterViewPager

class DraggingVariantAdapter(
    private val draggingVariantRepository: DraggingVariantRepository
) : AdapterViewPager<DraggingVariantAdapter.ViewHolder>() {
    private val count = draggingVariantRepository.getCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dragging_variant_item, parent, false)
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
        val containerFlows: ConstraintLayout = itemView.findViewById(R.id.container_flows)
        val flowText: Flow = containerFlows.findViewById(R.id.flow_text)
        val flowButtons: Flow = containerFlows.findViewById(R.id.flow_buttons)
    }
}