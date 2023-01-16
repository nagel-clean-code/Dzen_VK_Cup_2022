package com.example.dzen_vk_cup_2022.task_2.utils

import android.view.View
import android.view.ViewGroup

class AnimationTools {

    companion object {
        fun viewClip(view: View) {
            val viewGroup2 = view.parent as ViewGroup
            viewGroup2.clipChildren = false
            viewGroup2.clipToPadding = false
            val viewGroup3 = viewGroup2.parent as ViewGroup
            viewGroup3.clipChildren = false
            viewGroup3.clipToPadding = false
        }
    }
}