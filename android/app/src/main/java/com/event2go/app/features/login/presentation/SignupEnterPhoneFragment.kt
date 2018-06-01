package com.event2go.app.features.login.presentation

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.event2go.app.R
import com.event2go.app.databinding.FragmentSignupBinding
import com.event2go.app.features.login.presentation.presenter.EnterPhoneContract
import com.event2go.app.features.login.presentation.presenter.EnterPhonePresenter
import com.event2go.app.utils.DialogUtils
import com.event2go.base.extension.lastIndexOfString
import com.event2go.base.presentation.activity.BaseFragmentActivity
import com.event2go.base.presentation.fragment.BaseFragment
import com.event2go.base.utils.PhoneUtils

class SignupEnterPhoneFragment : BaseFragment(), EnterPhoneContract.View {

    private var mBind: FragmentSignupBinding? = null

    private var progressBar: ProgressBar? = null

    private val presenter = EnterPhonePresenter(this)

    private val phoneNumberFromUI: String
        get() {

            var selectedCode = mBind!!.spinner.selectedItem as String
            val from = selectedCode.lastIndexOfString("(")
            val to = selectedCode.lastIndexOfString(")")
            selectedCode = selectedCode.subSequence(from + 1, to) as String

            return "${selectedCode} ${phoneFieldNumber}"
        }

    private val phoneFieldNumber: String
        get() = mBind!!.phoneNumberField.text.toString()

    override fun getLayoutId(): Int {
        return R.layout.fragment_signup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBind = DataBindingUtil.bind(view)

        mBind!!.phoneNumberField.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        progressBar = mBind!!.loadingSpinner

        mBind!!.codeLabel.visibility = View.GONE
        mBind!!.sendCodeButton.setOnClickListener { presenter.onSendCodeButtonClicked(phoneNumberFromUI) }
        mBind!!.phoneNumberField.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        mBind!!.enterCodeButton.setOnClickListener {
            val phoneNumber = phoneNumberFromUI
            if (!PhoneUtils.isPhoneValid(phoneNumber)) {
                Toast.makeText(context, getString(R.string.error_invalid_phone),
                        Toast.LENGTH_LONG).show()
                mBind!!.sendCodeButton.isClickable = true
            } else {
                addEnterCodeFragment(phoneNumber)
            }
        }
    }

    override fun showProgressBar(toShow: Boolean) {
        progressBar!!.visibility = if (toShow) View.VISIBLE else View.GONE
    }

    override fun showSendCodeButton(toShow: Boolean) {
        mBind!!.sendCodeButton.visibility = if (toShow) View.VISIBLE else View.GONE
    }

    override fun addEnterCodeFragment(phoneNumber: String) {
        val fragment = SignupEnterCodeFragment.getInstance(phoneNumber)
        (activity as BaseFragmentActivity).addFragment(fragment, SignupEnterCodeFragment::class.simpleName)
    }

    override fun showErrorInvalidPhoneNumber() {
        Toast.makeText(context, getString(R.string.error_invalid_phone), Toast.LENGTH_LONG).show()
        mBind!!.sendCodeButton.isClickable = true
    }

    override fun showVerificationProgressDialog(phoneNumber: String) {
        DialogUtils.showDialog(fragmentManager,
                getString(R.string.phone_num_verification),
                getString(R.string.is_correct_number, phoneNumber),
                R.string.ok, View.OnClickListener { presenter.requestSMSCodeByPhoneNumber(phoneNumber) },
                R.string.action_edit, null)

    }

    override fun showToastMessage(message: String) {
        Toast.makeText(context,
                message,
                Toast.LENGTH_LONG).show()
    }

    override fun showToastMessage(stringResId: Int) {
        Toast.makeText(context,
                stringResId,
                Toast.LENGTH_LONG).show()
    }

}
