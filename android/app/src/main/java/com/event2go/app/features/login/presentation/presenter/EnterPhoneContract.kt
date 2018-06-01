package com.event2go.app.features.login.presentation.presenter

import com.costar.lm.base.presentation.presenter.BaseView

/**
 * Created by Iliya Gogolev on 5/30/18.
 */
interface EnterPhoneContract{

    interface Presenter {
        fun onSendCodeButtonClicked(phoneNumber: String)
        fun requestSMSCodeByPhoneNumber(phoneNumber:String)
    }

    interface View : BaseView {
        fun showErrorInvalidPhoneNumber()
        fun addEnterCodeFragment(phoneNumber: String)
        fun showVerificationProgressDialog(phoneNumber: String)
        fun showToastMessage(message: String)
        fun showToastMessage(stringResId: Int)
        fun showProgressBar(toShow:Boolean)
        fun showSendCodeButton(toShow: Boolean)
    }
}
