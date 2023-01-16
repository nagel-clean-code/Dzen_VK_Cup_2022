package com.example.dzen_vk_cup_2022

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.example.dzen_vk_cup_2022.databinding.ActivityMainBinding
import com.example.dzen_vk_cup_2022.task_1.Task1Activity
import com.example.dzen_vk_cup_2022.task_2.dragging_options.DraggingVariantActivity
import com.example.dzen_vk_cup_2022.task_2.evaluation_article.EvaluationArticleActivity
import com.example.dzen_vk_cup_2022.task_2.filling_gap.FillingGapActivity
import com.example.dzen_vk_cup_2022.task_2.matching_elements.MatchingElementsActivity
import com.example.dzen_vk_cup_2022.task_2.multi_stage.MultiStageActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            initListeners()
        }
    }

    private fun initListeners() {
        binding.listButtons.forEach { currentView ->
            if (currentView is TextView) {
                initButton(currentView)
            }
        }
    }

    private fun openActivity(name: String) {
        var intent: Intent? = null
        if (name == resources.getString(R.string.multi_stage)) {
            intent = Intent(this, MultiStageActivity::class.java)
        } else if (name == resources.getString(R.string.prev_task)) {
            intent = Intent(this, Task1Activity::class.java)
        } else if (name == resources.getString(R.string.matching)) {
            intent = Intent(this, MatchingElementsActivity::class.java)
        } else if (name == resources.getString(R.string.dragging_variant)) {
            intent = Intent(this, DraggingVariantActivity::class.java)
        } else if (name == resources.getString(R.string.completion)) {
            intent = Intent(this, FillingGapActivity::class.java)
        } else if (name == resources.getString(R.string.estimation)) {
            intent = Intent(this, EvaluationArticleActivity::class.java)
        } else {
            Toast.makeText(this, "Не сделал", Toast.LENGTH_LONG).show()
        }
        intent?.let { startActivity(intent) }
    }

    private fun initButton(currentView: TextView) {
        currentView.setOnClickListener {
            downButton(currentView)
            currentView.postDelayed({
                upButton(currentView)
            }, 200)
            openActivity(currentView.text.toString())
        }
    }

    private fun downButton(currentView: TextView) {
        val params = currentView.layoutParams as LinearLayout.LayoutParams
        params.height = getPixelsFromDP(60f).toInt()
        params.width = getPixelsFromDP(241f).toInt()
        params.bottomMargin = getPixelsFromDP(7f).toInt()
        currentView.layoutParams = params
        currentView.setTextColor(resources.getColor(R.color.turquoise))
        currentView.background = resources.getDrawable(R.drawable.button_down)
    }

    private fun upButton(currentView: TextView) {
        val params = currentView.layoutParams as LinearLayout.LayoutParams
        params.height = getPixelsFromDP(67f).toInt()
        params.width = getPixelsFromDP(246f).toInt()
        params.bottomMargin = 0
        currentView.layoutParams = params
        currentView.setTextColor(resources.getColor(R.color.golden))
        currentView.background = resources.getDrawable(R.drawable.background_button_menu_item)
    }

    private fun getPixelsFromDP(dp: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

}

