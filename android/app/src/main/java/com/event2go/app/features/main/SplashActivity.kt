package com.event2go.app.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils

import com.event2go.app.AppApplication
import com.event2go.app.data.RequestCode
import com.event2go.app.features.user.data.User
import com.event2go.app.utils.NavUtils
import com.parse.ParseException
import com.parse.ParseInstallation
import com.parse.ParseUser
import com.parse.SaveCallback

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
class SplashActivity : AppCompatActivity() {

    private var mShowLogin = false

    override fun onStart() {
        super.onStart()

        var user:User? = null
        if (!mShowLogin) {
            user = User.initCurrentUser()
        }

        if (user != null) {
            verifyUserData(user!!)
        } else {
            NavUtils.showLoginActivity(this)
        }
    }

    private fun verifyUserData(user: User) {

        if (TextUtils.isEmpty(user.name)) {
            NavUtils.showUserProfileEditFragment(this)
        } else {
            NavUtils.showMainActivity(this)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode.LOGIN.ordinal) {

            if (resultCode == Activity.RESULT_OK) {
                val user = User.initCurrentUser()
                verifyUserData(user!!)
                registerForPushNotifications()
            } else if (requestCode == Activity.RESULT_CANCELED) {
                finish()
            }

        } else if (requestCode == RequestCode.LOGIN_EDIT_PROFILE.ordinal) {
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            } else if (resultCode == Activity.RESULT_FIRST_USER) {
                mShowLogin = true
            }
        }
    }

    private fun registerForPushNotifications() {

        val installation = ParseInstallation.getCurrentInstallation()
        installation.put("user", ParseUser.getCurrentUser())
        installation.saveInBackground { e ->
            // todo
            if (e != null) {
            } else {
            }
        }
    }


}
