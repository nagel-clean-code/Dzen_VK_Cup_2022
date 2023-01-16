package com.example.dzen_vk_cup_2022.task_2.dragging_options

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.example.dzen_vk_cup_2022.R
import com.example.dzen_vk_cup_2022.task_2.dragging_options.DraggingVariantRepository.Companion.TEXT_GAPS

class DraggingVariantAlgorithm(
    private val context: Context,
    private val disallowScrollingViewPager: (Boolean) -> Unit
) : View.OnTouchListener {
    private val animation = AnimationDragging()
    private val listViewAll = mutableListOf<View>()
    private val listElementsGaps = mutableListOf<TextView>()
    private val listElementsAnswers = mutableListOf<TextView>()
    private lateinit var currentTask: DraggingVariantRepository.TaskDraggingVariant

    fun displayViewOnScreen(holder: DraggingVariantAdapter.ViewHolder, task: DraggingVariantRepository.TaskDraggingVariant) {
        currentTask = task
        cancelOldView(holder)
        addToTheFlowText(holder, currentTask.sentences)
        addToFlowAnswers(holder, currentTask.answerOptions)

        initSaveElements()
    }

    private fun initSaveElements() {
        val viewInserted = mutableListOf<TextView>()
        currentTask.inserted.forEach() {
            val currentView = listElementsGaps[it.key]
            currentView.text = it.value
            currentView.setTextColor(context.resources.getColor(R.color.golden))
            viewInserted.add(currentView)

            val textViewAnswer = listElementsAnswers.find { view -> view.text == it.value }
            textViewAnswer!!.x = 200f
            textViewAnswer.y = -200f
            insertInEmptyTextView(currentView, textViewAnswer)
        }
        listElementsGaps.removeAll(viewInserted)
    }

    private fun cancelOldView(holder: DraggingVariantAdapter.ViewHolder) {
        listElementsGaps.clear()
        listElementsAnswers.clear()
        listViewAll.forEach {
            holder.containerFlows.removeView(it)
        }
    }

    private fun addToTheFlowText(holder: DraggingVariantAdapter.ViewHolder, sentences: List<String>) {
        sentences.forEach {
            val textView = createTextView(it)
            holder.containerFlows.addView(textView)
            holder.flowText.addView(textView)
        }
    }

    private fun addToFlowAnswers(holder: DraggingVariantAdapter.ViewHolder, answers: List<String>) {
        answers.forEach {
            val textView = createButtonAnswer(it, holder.containerFlows)
            holder.containerFlows.addView(textView)
            holder.flowButtons.addView(textView)
            textView.setOnTouchListener(this)
            textViewClip(textView)
        }
    }

    private fun createTextView(text: String): TextView {
        val lParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val view = TextView(context)
        view.id = View.generateViewId()
        view.setTextColor(context.resources.getColor(R.color.super_light_turquoise))
        view.textSize = 18f
        view.typeface = ResourcesCompat.getFont(context, R.font.montserrat_semibold)
        view.layoutParams = lParams
        view.text = text

        listViewAll.add(view)
        if (text == TEXT_GAPS) {
            //Сохраняем порядковый номер посутого элемента, чтобы потом сохранять вставленные слова по этому индексу
            view.tag = listElementsGaps.size
            listElementsGaps.add(view)
        }
        return view
    }

    private fun createButtonAnswer(textAnswer: String, containerFlows: ConstraintLayout): TextView {
        val viewElement = LayoutInflater.from(context).inflate(R.layout.dragging_answer_element_item, containerFlows, false)
        val textView = viewElement.findViewById<TextView>(R.id.answer_element)
        textView.id = View.generateViewId()
        textView.text = textAnswer
        textView.tag = Pair(textView.x, textView.y)
        listElementsAnswers.add(textView)
        listViewAll.add(textView)
        return textView
    }

    private fun textViewClip(textView: TextView) {
        val viewGroup2 = textView.parent as ViewGroup
        viewGroup2.clipChildren = false
        viewGroup2.clipToPadding = false
        val viewGroup3 = viewGroup2.parent as ViewGroup
        viewGroup3.clipChildren = false
        viewGroup3.clipToPadding = false
    }

    private var dX = 0f
    private var dY = 0f
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                dX = view.x - event.rawX
                dY = view.y - event.rawY
            }
            MotionEvent.ACTION_UP -> {
                tryInsertText(view)
                disallowScrollingViewPager.invoke(true)
            }
            MotionEvent.ACTION_MOVE -> {
                disallowScrollingViewPager.invoke(false)
                view.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY - MARGIN_FROM_ABOVE_MOVE_BUTTON)
                    .setDuration(0)
                    .start()
            }
            else -> return false
        }
        return true
    }

    private fun tryInsertText(buttonView: View) {
        val intersectionAreaList = mutableListOf<Pair<Float, TextView>>()   //Pair<Площадь, TextView>

        listElementsGaps.forEach {
            val currentIntersectionArea = getIntersectionArea(buttonView, it)
            if (currentIntersectionArea > 0) {
                intersectionAreaList.add(Pair(currentIntersectionArea, it))
            }
        }
        if (intersectionAreaList.isEmpty()) {
            animation.animationOnOldPositionView(buttonView)
        } else {
            //Вставляем в textView площадь пересечения с которым самая большая
            val crossedTextView: TextView = intersectionAreaList.maxBy { it.first }.second

            listElementsGaps.remove(crossedTextView)
            insertInEmptyTextView(crossedTextView, (buttonView as TextView))
        }
    }

    private fun insertInEmptyTextView(initializedView: TextView, buttonView: TextView) {
        initializedView.text = buttonView.text
        initializedView.setTextColor(context.resources.getColor(R.color.golden))
        currentTask.inserted[initializedView.tag as Int] = buttonView.text.toString()

        initializedView.setOnClickListener {
            returnStartStateAnswer(initializedView, buttonView)
        }
        buttonView.visibility = View.GONE
    }

    private fun returnStartStateAnswer(initializedView: TextView, buttonView: TextView){
        initializedView.text = TEXT_GAPS
        initializedView.setTextColor(context.resources.getColor(R.color.super_light_turquoise))
        buttonView.visibility = View.VISIBLE

        animation.animationOnOldPositionView(buttonView)
        initializedView.setOnClickListener(null)
        listElementsGaps.add(initializedView)
        removeFromInserted(buttonView.text.toString())
    }

    private fun removeFromInserted(text: String) {
        if (currentTask.inserted.isEmpty()) return

        var answerIdFound = 0
        currentTask.inserted.forEach { (id, value) ->
            if (value == text) {
                answerIdFound = id
                return@forEach
            }
        }
        currentTask.inserted.remove(answerIdFound)
    }

    private fun getIntersectionArea(buttonView: View, textView: TextView): Float {
        val cordBV = animation.getCoordinateView(buttonView)
        val cordTV = animation.getCoordinateView(textView)

        val x1TV = cordTV.first
        val y1TV = cordTV.second
        val x2TV = cordTV.first + textView.width
        val y2TV = cordTV.second + textView.height

        val x1BV = cordBV.first
        val y1BV = cordBV.second
        val x2BV = cordBV.first + textView.width
        val y2BV = cordBV.second + textView.height

        val left = Math.max(x1BV, x1TV)
        val right = Math.min(x2BV, x2TV)

        val width = right - left
        if (width <= 0) return 0f

        val top = Math.max(y1BV, y1TV)
        val bottom = Math.min(y2BV, y2TV)
        val height = bottom - top
        if (height <= 0) return 0f

        return height * width
    }

    companion object {
        const val MARGIN_FROM_ABOVE_MOVE_BUTTON = 50f
    }
}