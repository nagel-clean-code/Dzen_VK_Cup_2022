package com.example.dzen_vk_cup_2022.task_2.matching_elements

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dzen_vk_cup_2022.databinding.ActivityMatchingElementsBinding
import com.example.dzen_vk_cup_2022.task_2.view_pager.RealizationViewPager

class MatchingElementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchingElementsBinding
    private val matchingElementRepository = MatchingElementRepository()
    private lateinit var realizationViewPager: RealizationViewPager<MatchingElementAdapter.ViewHolder>

    private val matchingAlgorithm = MatchingAlgorithm()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchingElementsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realizationViewPager = RealizationViewPager(
            binding.matchingElementViewPager,
            MatchingElementAdapter(matchingElementRepository),
            ::initViewsOnCurrentPage,
            binding.scrollElement
        )
    }

    private fun initViewsOnCurrentPage(position: Int, currentHolder: MatchingElementAdapter.ViewHolder) {
        val currentMatchingElement = matchingElementRepository.getMatchingElementByPosition(position)
        matchingAlgorithm.addListElements(currentHolder, currentMatchingElement)
    }

}