package com.example.dzen_vk_cup_2022.task_2.evaluation_article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.view_pager.AdapterViewPager

class EvaluationArticleAdapter(
    private val draggingVariantRepository: EvaluationArticleRepository
) : AdapterViewPager<EvaluationArticleAdapter.ViewHolder>() {
    private val count = draggingVariantRepository.getCount()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.evaluation_article_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionNumberText = "Текст ${position + 1}/$count"
        holder.taskNumber.text = questionNumberText
        holderCache[position] = holder
    }

    override fun getItemCount(): Int = count

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNumber: TextView = itemView.findViewById(R.id.task_number)
        val textView: TextView = itemView.findViewById(R.id.text)
        val linearStars: LinearLayout = itemView.findViewById(R.id.linear_stars)
    }
}