package com.example.dzen_vk_cup_2022.task_2.multi_stage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dzen_vk_cup_2022.databinding.ActivityMultiStageBinding
import com.example.dzen_vk_cup_2022.task_2.view_pager.RealizationViewPager

class MultiStageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMultiStageBinding

    private val multiStageRepository = MultiStageRepository()
    private val multiStageAlgorithm = MultiStageAlgorithm()
    private lateinit var realizationViewPager: RealizationViewPager<MultiStageAdapter.ViewHolder>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMultiStageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realizationViewPager = RealizationViewPager(
            binding.multiStageViewPager,
            MultiStageAdapter(multiStageRepository),
            ::initViewsOnCurrentPage,
            binding.scrollElement
        )
    }

    private fun initViewsOnCurrentPage(position: Int, currentHolder: MultiStageAdapter.ViewHolder) {
        val currentMultiStage = multiStageRepository.getCurrentMultiStage(position)
        multiStageAlgorithm.addListAnswers(currentHolder, currentMultiStage)
    }

}