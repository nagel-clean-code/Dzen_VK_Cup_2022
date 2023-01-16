package com.example.dzen_vk_cup_2022.task_2.matching_elements

import android.widget.TextView
import com.example.dzen_vk_cup_2022.R
import java.util.concurrent.atomic.AtomicBoolean

class MatchingAlgorithm {

    private val matchingAnimations = MatchingAnimations()

    private var leftSelected: MatchingElementRepository.ElementLeft? = null
    private var rightSelected: MatchingElementRepository.ElementRight? = null
    private val linesSelected = mutableListOf<MatchingElementRepository.ElementLine>()

    private val isCurrentlyAnimating = AtomicBoolean(false)

    fun addListElements(holder: MatchingElementAdapter.ViewHolder, lines: List<MatchingElementRepository.ElementLine>) {
        leftSelected = null
        rightSelected = null
        holder.layoutRight.removeAllViews()
        holder.layoutLeft.removeAllViews()

        lines.forEach { currentLine ->
            holder.initLene(currentLine)
            initButtonsLine(currentLine)
        }
    }

    private fun initButtonsLine(line: MatchingElementRepository.ElementLine) {
        initOnClickButtons(line)
        initColorButtonsInLine(line)
    }

    private fun initOnClickButtons(line: MatchingElementRepository.ElementLine) {
        val leftElement = line.left
        val rightElement = line.right
        leftElement.textView.setOnClickListener {
            if (isCurrentlyAnimating.get())
                return@setOnClickListener

            matchingAnimations.zoomAnimation(it)
            tryCancelPrevSelected(leftElement)
            pressButton(leftElement)
        }
        rightElement.textView.setOnClickListener {
            if (isCurrentlyAnimating.get())
                return@setOnClickListener

            matchingAnimations.zoomAnimation(it)
            tryCancelPrevSelected(rightElement)
            pressButton(rightElement)
        }
    }

    private fun initColorButtonsInLine(line: MatchingElementRepository.ElementLine) {
        //Если стрелка показана, то два элемента считаются сопоставленными - выделенными жёлтым
        setupColorButton(line.right.textView, line.right.showArrow)
        setupColorButton(line.left.textView, line.right.showArrow)
    }

    private fun pressButton(currentElementPressed: MatchingElementRepository.Element) {
        var clickingOnAlreadySelected = false
        checkLineSelectedAndCancel(currentElementPressed.myLine)

        //Если элемент уже был выбран, то отменяем его выделение
        if (currentElementPressed == leftSelected || currentElementPressed == rightSelected) {
            clickingOnAlreadySelected = true
            leftSelected = null
        }

        setupColorButton(currentElementPressed.textView, !clickingOnAlreadySelected)
        if (!clickingOnAlreadySelected) {
            tryCollate(currentElementPressed)
        }
    }

    private fun checkLineSelectedAndCancel(line: MatchingElementRepository.ElementLine) {
        if (linesSelected.contains(line)) {
            linesSelected.remove(line)
            lineCancelSelected(line)
        }
    }

    private fun lineCancelSelected(line: MatchingElementRepository.ElementLine) {
        setupColorButton(line.left.textView, false)
        setupColorButton(line.right.textView, false)
        line.right.arrow.alpha = 0f
    }

    private fun setupColorButton(view: TextView, selected: Boolean) {
        if (selected) {
            view.setTextColor(view.context.resources.getColor(R.color.turquoise))
            view.background = view.context.resources.getDrawable(R.drawable.matching_element_selected_background)
        } else {
            view.setTextColor(view.context.resources.getColor(R.color.golden))
            view.background = view.context.resources.getDrawable(R.drawable.matching_element_background)
        }
    }

    private fun tryCollate(currentElementPressed: MatchingElementRepository.Element) {
        if (currentElementPressed is MatchingElementRepository.ElementLeft) {
            leftSelected = currentElementPressed
            if (rightSelected != null) {
                acceptLine()
            }
        }
        if (currentElementPressed is MatchingElementRepository.ElementRight) {
            rightSelected = currentElementPressed
            if (leftSelected != null) {
                acceptLine()
            }
        }
    }

    private fun acceptLine() {
        if (linesSelected.contains(leftSelected!!.myLine)) {
            lineCancelSelected(leftSelected!!.myLine)
        } else {
            rightSelected!!.myLine.right.showArrow = true
            linesSelected.add(rightSelected!!.myLine)
            animationSwapAndShowArrow()
        }
    }

    private fun animationSwapAndShowArrow() {
        val arrowView = rightSelected!!.arrow
        val swapTextView1 = leftSelected!!.textView
        val swapTextView2 = rightSelected!!.myLine.left.textView

        swapLinks()

        isCurrentlyAnimating.set(true)
        matchingAnimations.animationSwapViews(swapTextView1, swapTextView2) {
            isCurrentlyAnimating.set(false)
            matchingAnimations.showAnimation(arrowView)
            rightSelected = null
            leftSelected = null
        }
    }

    private fun swapLinks() {
        val rlBuf = rightSelected!!.myLine.left
        rlBuf.myLine = leftSelected!!.myLine

        rightSelected!!.myLine.left = leftSelected!!
        leftSelected!!.myLine.left = rlBuf

        leftSelected!!.myLine = rightSelected!!.myLine
    }

    private fun tryCancelPrevSelected(element: MatchingElementRepository.Element) {
        if (element is MatchingElementRepository.ElementLeft) {
            if (leftSelected != null) {             //Если уже был выбран элемент с левой стороны
                if (element != leftSelected) {      //Если это не этот элемент, то отменяем выделение у прошлого
                    setupColorButton(leftSelected!!.textView, false)
                }
            }
        } else if (element is MatchingElementRepository.ElementRight) {
            if (rightSelected != null) {            //Если уже был выбран элемент с левой стороны
                if (element != rightSelected) {     //Если это не этот элемент, то отменяем выделение у прошлого
                    setupColorButton(rightSelected!!.textView, false)
                }
            }
        }
    }
}