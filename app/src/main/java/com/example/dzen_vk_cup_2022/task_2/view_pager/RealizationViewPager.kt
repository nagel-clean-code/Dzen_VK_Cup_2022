package com.example.dzen_vk_cup_2022.task_2.view_pager

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dzen_vk_cup_2022.databinding.ScrollElementLayoutBinding
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @param scrollElementBinding - опционально, если нужен seekBar
 */
class RealizationViewPager<T : RecyclerView.ViewHolder?>(
    private val viewPager: ViewPager2,
    private val adapterViewPager: AdapterViewPager<T>,
    private val initCurrentPage: (pos: Int, holder: T) -> Unit,
    private val scrollElementBinding: ScrollElementLayoutBinding? = null
) {

    private var scrollElementView: ScrollElementView? = null

    private val seekBarEvent = AtomicBoolean(false)
    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        /**
         * 1. Существует бага ViewPager2 - при открытии клавиатуры иногда вызывается onPageSelected
         * решил проблему сохранением предыдущей позиции
         *
         * 2. findViewHolderForAdapterPosition не безопасен, по этому получаю холдер напрямую из адаптера, преждевременно добавив в кэш.
         *
         * 3. При пролистывании используя seekBar адаптер не успевает сбилдить страницы, на это у него уходит обычно 160млс
         *
         */
        private var prevPosition = -1
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            if (position != prevPosition) {
                prevPosition = position
                scrollElementBinding?.seekBar!!.progress = position + 1
                scrollElementView?.appearanceSeekBarAnimation()

                val movingAroundSeekBar = seekBarEvent.get()
                seekBarEvent.set(false)

                viewPager.postDelayed({
                    startInitCurrentPage(position)
                }, if (movingAroundSeekBar) 160 else 0)

            }
        }
    }

    init {
        viewPager.adapter = adapterViewPager
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        scrollElementBinding?.let {
            scrollElementView = ScrollElementView(
                it,
                adapterViewPager.itemCount,
                ::jumpOnPageViewPager
            )
        }
    }

    private fun startInitCurrentPage(position: Int) {
        val currentHolder = adapterViewPager.getViewHolderByPosition(position)  //Сначала убеждаемся что холдер сбилдился
        if (currentHolder == null) {
            //Для этого случая есть корутины, но мы пока без библиотек справляемся :D
            viewPager.postDelayed({ startInitCurrentPage(position) }, 20)
            return
        }
        initCurrentPage.invoke(position, currentHolder)
    }

    private fun jumpOnPageViewPager(numberPage: Int) {
        seekBarEvent.set(true)
        viewPager.post {
            viewPager.setCurrentItem(numberPage, true)
        }
    }
}