package com.event2go.app.features.event.presentation.dialog;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.event2go.base.presentation.fragment.AlertDialogFragment;
import com.event2go.app.R;
import com.event2go.app.databinding.DialogAddParamBinding;
import com.event2go.app.features.event.data.Parameter;

/**
 * Created by Iliya Gogolev on 8/19/15.
 */
public class AddParameterDialog extends AlertDialogFragment {

    private View.OnClickListener mPositiveButtonClickListener;

    private Parameter parameter = new Parameter();
    private DialogAddParamBinding mParamBidning;

    public static AddParameterDialog newInstance(Parameter param, Activity activity) {
        AddParameterDialog f = new AddParameterDialog();
        f.parameter = param;

        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_add_param, null);
        DialogAddParamBinding mParamBidning = DataBindingUtil.bind(view);
        mParamBidning.setParam(param);
        f.setCustomContentView(view);
        return f;
    }


    public void setPositiveButton(int textId, View.OnClickListener listener) {
        mPositiveButtonClickListener = listener;

        super.setPositiveButton(textId, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Parameter param = mParamBidning.getParam();
                param.setName(mParamBidning.txtName.getText().toString().trim());
                param.setValue(mParamBidning.txtValue.getText().toString().trim());
                mPositiveButtonClickListener.onClick(v);
            }
        });
    }


    public Parameter getParameter() {
        return mParamBidning.getParam();
    }
}
