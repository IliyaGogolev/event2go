package com.event2go.app.features.login.presentation.presenter

import android.support.annotation.StringRes
import com.costar.lm.base.presentation.presenter.BaseView

/**
 * Created by Iliya Gogolev on 5/30/18.
 */
interface EnterCodeContract{

    interface Presenter {
        fun login(mPhoneNumber: String, code: Int)
        fun validateCode(code: String): Boolean
    }

    interface View : BaseView {
        fun enableSendCodeButton(enable:Boolean)
        fun showProgressBar(toShow: Boolean)
        fun showInvalidCodeToast()
        fun showMainActivity()
        fun showErrorLoginMessage(@StringRes stringResId: Int)

    }
}
