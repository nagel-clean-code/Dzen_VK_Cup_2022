package com.example.dzen_vk_cup_2022.task_2.filling_gap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dzen_vk_cup_2022.databinding.ActivityFillingGapBinding
import com.example.dzen_vk_cup_2022.task_2.view_pager.RealizationViewPager


class FillingGapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFillingGapBinding
    private val fillingGapRepository = FillingGapRepository()
    private val fillingGapAlgorithm = FillingGapAlgorithm(this)
    private lateinit var realizationViewPager: RealizationViewPager<FillingGapAdapter.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFillingGapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realizationViewPager = RealizationViewPager(
            binding.draggingVariantViewPager,
            FillingGapAdapter(fillingGapRepository),
            ::initViewsOnCurrentPage,
            binding.scrollElement
        )
    }

    private fun initViewsOnCurrentPage(position: Int, currentHolder: FillingGapAdapter.ViewHolder) {
        val currentTask = fillingGapRepository.getTaskByPosition(position)
        fillingGapAlgorithm.displayViewOnScreen(currentHolder, currentTask)
    }
}