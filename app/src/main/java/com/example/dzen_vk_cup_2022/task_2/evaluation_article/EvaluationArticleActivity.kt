package com.example.dzen_vk_cup_2022.task_2.evaluation_article

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dzen_vk_cup_2022.databinding.ActivityEvaluationArticleBinding
import com.example.dzen_vk_cup_2022.task_2.view_pager.RealizationViewPager

class EvaluationArticleActivity : AppCompatActivity() {

    private val evaluationArticleRepository = EvaluationArticleRepository()
    private lateinit var binding: ActivityEvaluationArticleBinding
    private lateinit var realizationViewPager: RealizationViewPager<EvaluationArticleAdapter.ViewHolder>
    private val animationStars = AnimationStars(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realizationViewPager = RealizationViewPager(
            binding.draggingVariantViewPager,
            EvaluationArticleAdapter(evaluationArticleRepository),
            ::initViewsOnCurrentPage,
            binding.scrollElement
        )
    }

    private fun initViewsOnCurrentPage(position: Int, currentHolder: EvaluationArticleAdapter.ViewHolder) {
        val currentTask = evaluationArticleRepository.getTaskByPosition(position)
        currentHolder.textView.text = currentTask.sentences

        currentHolder.linearStars.removeAllViews()
        animationStars.addStarsView(currentHolder, currentTask)
    }

}