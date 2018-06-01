package com.event2go.app.features.login.presentation.presenter

import bolts.Continuation
import com.event2go.app.AppApplication
import com.event2go.app.R
import com.event2go.app.features.login.dataProvider.LoginProvider
import com.event2go.app.features.user.data.User

/**
 * Created by Iliya Gogolev on 5/30/18.
 */
class EnterCodePresenter
constructor(val view: EnterCodeContract.View) : EnterCodeContract.Presenter {


    override fun validateCode(code: String): Boolean = code.length == 4

    override fun login(mPhoneNumber: String, code: Int) {

        /**
         * DEMO
         */
        fakeLogin()

//        loginByPhoneAndCode(mPhoneNumber, code)

    }

    private fun loginByPhoneAndCode(mPhoneNumber: String, code: Int) {
        val loginTask = LoginProvider.loginByPhoneNumber(mPhoneNumber, code)
        loginTask.continueWithTask<Any>(Continuation<Any, Any> { task ->

            view.enableSendCodeButton(true)
            view.showProgressBar(false)

            if (task.isFaulted) {
                view.showErrorLoginMessage(R.string.error_signup_code_lenght)
            } else {

                view.showMainActivity()
            }

            null
        })
    }


    /**
     * DEMO, using test account
     * */
    private fun fakeLogin() {

//        val objectTask = LoginProvider.loginByUsername("test@test.com", "123456")
        val objectTask = LoginProvider.loginByUsername("korkag2@gmail.com", "123456")
        objectTask.continueWithTask<Any>(Continuation<Any, Any> { task ->

            if (task.isFaulted) {
                view.showErrorLoginMessage(R.string.error_signup_code_lenght)
            } else {
                User.initCurrentUser()
                view.showProgressBar(false)
                view.showMainActivity()
            }
            null
        })
    }


}