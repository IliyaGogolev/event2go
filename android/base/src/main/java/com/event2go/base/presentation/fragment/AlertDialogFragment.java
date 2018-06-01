package com.event2go.base.presentation.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.event2go.base.R;
import com.event2go.base.databinding.FragmentAlertDialogBinding;

/**
 * Created by Iliya Gogolev on 4/27/15.
 */
public class AlertDialogFragment extends DialogFragment {

    private static final String PARAM_TITLE = "TITLE";
    private static final String PARAM_MESSAGE = "MESSAGE";
    private static final String PARAM_TEXT_OK = "OK";
    private static final String PARAM_TEXT_CANC = "CANCEL";

    private
    @StringRes
    int mTitleResId;
    private
    @StringRes
    int mMessageResId;
    private
    @StringRes
    int mButtonPositiveTextResId;
    private
    @StringRes
    int mButtonNegativeTextResId;

    private String mTitle;
    private String mMessage;
    private String mButtonPositiveText;
    private String mButtonNegativeText;

    private View.OnClickListener mNegativeButtonListener;
    private View.OnClickListener mPositiveButtonListener;
    private DialogInterface.OnDismissListener mOnDismissListener;
    private View mCustomView;
    private boolean mCancelable = true;

    protected boolean isAutoDismiss() {
        return true;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setTitle(@StringRes int resId) {
        this.mTitleResId = resId;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public void setMessage(@StringRes int resId) {
        this.mMessageResId = resId;
    }

    public void setPositiveButton(String text, final View.OnClickListener listener) {
        mButtonPositiveText = text;
        mPositiveButtonListener = listener;
    }

    public void setPositiveButton(@StringRes int resId, View.OnClickListener listener) {
        mButtonPositiveTextResId = resId;
        mPositiveButtonListener = listener;
    }

    public void setNegativeButton(String text, final View.OnClickListener listener) {
        mButtonNegativeText = text;
        mNegativeButtonListener = listener;
    }

    public void setNegativeButton(@StringRes int resId, View.OnClickListener listener) {
        mButtonNegativeTextResId = resId;
        mNegativeButtonListener = listener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void setCancelable(boolean cancelable) {
        mCancelable = cancelable;
    }

    public void setCustomContentView(View view) {
        mCustomView = view;
    }

    public static AlertDialogFragment newInstance() {
        AlertDialogFragment frag = new AlertDialogFragment();
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PARAM_TITLE, mTitle);
        outState.putString(PARAM_MESSAGE, mMessage);
        outState.putString(PARAM_TEXT_OK, mButtonPositiveText);
        outState.putString(PARAM_TEXT_CANC, mButtonNegativeText);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mTitle = savedInstanceState.getString(PARAM_TITLE);
            mMessage = savedInstanceState.getString(PARAM_MESSAGE);
            mButtonPositiveText = savedInstanceState.getString(PARAM_TEXT_OK);
            mButtonNegativeText = savedInstanceState.getString(PARAM_TEXT_CANC);
        }

        setCancelable(mCancelable);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_alert_dialog, null);
        builder.setView(view);
        builder.setOnDismissListener(mOnDismissListener);

        FragmentAlertDialogBinding bind = (FragmentAlertDialogBinding) DataBindingUtil.bind(view);
        mTitle = getString(view.getContext(), mTitleResId, mTitle);
        bind.setTitle(mTitle);

        if (TextUtils.isEmpty(mTitle)) {
            bind.alertTitle.setVisibility(View.GONE);
        } else {
            bind.setTitle(mTitle);
        }

        mMessage = getString(view.getContext(), mMessageResId, mMessage);
        if (TextUtils.isEmpty(mMessage)) {
            bind.messageTextView.setVisibility(View.GONE);
        } else {
            bind.setMessage(mMessage);
        }

        setupButtons(view.getContext(), bind);

        if (mCustomView != null) {

            FrameLayout fl = bind.customPanel;
            fl.addView(mCustomView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            DataBindingUtil.bind(mCustomView);

            bind.customPanel.setVisibility(View.VISIBLE);
            bind.messageTextView.setVisibility(View.GONE);
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    private String getString(Context context, @StringRes int resId, String text) {
        if (!TextUtils.isEmpty(text)) return text;

        if (resId != -1) return context.getString(resId);

        return "";
    }

    private void setupButtons(Context context, final FragmentAlertDialogBinding bind) {

        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int whichButtons = 0;

        mButtonPositiveText = getString(context, mButtonPositiveTextResId, mButtonPositiveText);
        if (TextUtils.isEmpty(mButtonPositiveText)) {
            bind.button1.setVisibility(View.GONE);
        } else {
            bind.button1.setText(mButtonPositiveText);
            bind.button1.setVisibility(View.VISIBLE);
            bind.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPositiveButtonListener != null) {
                        mPositiveButtonListener.onClick(v);
                    }
                    if (isAutoDismiss()) dismiss();
                }
            });
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegativeText = getString(context, mButtonNegativeTextResId, mButtonNegativeText);
        if (TextUtils.isEmpty(mButtonNegativeText)) {
            bind.button2.setVisibility(View.GONE);
        } else {
            bind.button2.setText(mButtonNegativeText);
            bind.button2.setVisibility(View.VISIBLE);
            bind.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mNegativeButtonListener != null) {
                        mNegativeButtonListener.onClick(v);
                    }
                    if (isAutoDismiss()) dismiss();
                }
            });
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        final boolean hasButtons = whichButtons != 0;
        if (!hasButtons) {
            bind.buttonsBar.setVisibility(View.GONE);
        }
    }
}

