package com.costar.lm.base.presentation.presenter

import android.arch.lifecycle.LifecycleOwner

/**
 * Created by Iliya Gogolev on 2/7/18.
 */
interface BaseView : LifecycleOwner {
    fun hideKeyboard()
}