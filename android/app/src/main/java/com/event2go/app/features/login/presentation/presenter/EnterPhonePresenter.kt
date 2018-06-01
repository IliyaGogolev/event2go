package com.event2go.app.features.login.presentation.presenter

import com.event2go.base.utils.PhoneUtils

/**
 * Created by Iliya Gogolev on 5/30/18.
 */
class EnterPhonePresenter
constructor(val view: EnterPhoneContract.View) : EnterPhoneContract.Presenter {

    override fun onSendCodeButtonClicked(phoneNumber: String) {

        view.hideKeyboard()
        if (!PhoneUtils.isPhoneValid(phoneNumber)) {
            view.showErrorInvalidPhoneNumber()
        } else {
            view.showVerificationProgressDialog(phoneNumber)
        }
    }

    override fun requestSMSCodeByPhoneNumber(phoneNumber: String) {

        /**
         * DEMO
         **/
        view.showProgressBar(false)
        view.addEnterCodeFragment(phoneNumber)

        /*
        val objectTask = LoginProvider.requestSMSCodeByPhoneNumber(phoneNumber)
        objectTask.continueWithTask<Any>(Continuation<Any, Any> { task ->
            view.showSendCodeButton(false)
            if (task.isFaulted) {
                view.showToastMessage(R.string.error_something_went_wrong)
            } else {
                view.showProgressBar(false)
                view.addEnterCodeFragment(phoneNumber)
            }
            null
        })
        */
    }
}