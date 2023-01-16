package com.example.dzen_vk_cup_2022.task_2.dragging_options

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dzen_vk_cup_2022.databinding.ActivityDraggingVariantBinding
import com.example.dzen_vk_cup_2022.task_2.filling_gap.FillingGapAdapter
import com.example.dzen_vk_cup_2022.task_2.view_pager.RealizationViewPager


class DraggingVariantActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDraggingVariantBinding
    private lateinit var viewPager: ViewPager2
    private val draggingVariantRepository = DraggingVariantRepository()
    private val draggingVariantAlgorithm = DraggingVariantAlgorithm(this, ::disallowScrollingViewPager)
    private lateinit var realizationViewPager: RealizationViewPager<DraggingVariantAdapter.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDraggingVariantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewPager = binding.draggingVariantViewPager

        realizationViewPager = RealizationViewPager(
            viewPager,
            DraggingVariantAdapter(draggingVariantRepository),
            ::initViewsOnCurrentPage,
            binding.scrollElement
        )
    }

    private fun initViewsOnCurrentPage(position: Int, currentHolder: DraggingVariantAdapter.ViewHolder) {
        val currentTask = draggingVariantRepository.getTaskByPosition(position)
        draggingVariantAlgorithm.displayViewOnScreen(currentHolder, currentTask)
    }

   private fun disallowScrollingViewPager(active: Boolean){
        viewPager.isUserInputEnabled = active
   }
}