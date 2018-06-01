package com.costar.lm.base.presentation.presenter

import android.support.annotation.CallSuper

/**
 * Created by Iliya Gogolev on 2/26/18.
 */
interface BasePresenter<T> {

    var view: T?
    fun onStart() {}

    @CallSuper
    fun onDestroy() {
        view = null
    }
}