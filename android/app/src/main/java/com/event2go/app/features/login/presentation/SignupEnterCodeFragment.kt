package com.event2go.app.features.login.presentation

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.StringRes
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.event2go.app.R
import com.event2go.app.databinding.FragmentSignupEnterCodeBinding
import com.event2go.app.features.login.presentation.presenter.EnterCodeContract
import com.event2go.app.features.login.presentation.presenter.EnterCodePresenter
import com.event2go.app.utils.DialogUtils
import com.event2go.app.utils.NavUtils
import com.event2go.base.presentation.activity.BaseFragmentActivity
import com.event2go.base.presentation.fragment.BaseFragment

class SignupEnterCodeFragment : BaseFragment(), EnterCodeContract.View {

    companion object {

        private val ARG_PHONE_NUMBER = "phone_number"

        fun getInstance(phoneNumber: String): SignupEnterCodeFragment {

            val fragment = SignupEnterCodeFragment()
            val arg = Bundle()
            arg.putString(ARG_PHONE_NUMBER, phoneNumber)
            fragment.arguments = arg
            return fragment
        }
    }

    private lateinit var mPhoneNumber: String
    private lateinit var mBind: FragmentSignupEnterCodeBinding
    private var progressBar: ProgressBar? = null
    private val presenter: EnterCodeContract.Presenter = EnterCodePresenter(this)

    override fun getLayoutId(): Int {
        return R.layout.fragment_signup_enter_code
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBind = DataBindingUtil.bind(view)!!

        mPhoneNumber = arguments!!.getString(ARG_PHONE_NUMBER)
        mBind.phoneNumber = mPhoneNumber
        progressBar = mBind.loadingSpinner

        mBind.sendCodeButton.isEnabled = true
        mBind.codeLabel.visibility = View.GONE
        mBind.sendCodeButton.setOnClickListener {

            val codeStr = mBind.codeField.text.toString()
            if (!presenter.validateCode(codeStr)) {
                showInvalidCodeToast()
                enableSendCodeButton(true)
            } else {

                enableSendCodeButton(false)
                showProgressBar(true)
                val code = Integer.parseInt(codeStr)

                presenter.login(mPhoneNumber, code)
            }
        }

        mBind.backButton.setOnClickListener { (activity as BaseFragmentActivity).onBackPressed() }
    }

    override fun enableSendCodeButton(enable: Boolean) {
        mBind.sendCodeButton.isEnabled = enable
    }

    override fun showInvalidCodeToast() {
        Toast.makeText(context,
                R.string.error_signup_code_lenght,
                Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar(toShow: Boolean) {
        progressBar!!.visibility = if (toShow) View.VISIBLE else View.GONE
    }

    override fun showErrorLoginMessage(@StringRes stringResId: Int) {
        if (isAdded) {
            DialogUtils.showAlertDialog(activity!!.supportFragmentManager, getString(R.string.error), getString(stringResId), getString(R.string.ok))
        }
    }

    override fun showMainActivity() {
        NavUtils.showMainActivity(activity)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }
}
