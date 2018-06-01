package com.event2go.app.features.user.presentation;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.user.data.repository.UserDataProvider;
import com.event2go.app.databinding.FragmentUserProfileEditBinding;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.features.user.data.User;
import com.event2go.app.utils.NavUtils;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
public class UserProfileEditFragment extends BaseFragment {


    private User mCurrentUser;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_profile_edit;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // defautl finish state
        getActivity().setResult(Activity.RESULT_CANCELED);

        mCurrentUser = AppApplication.getContext().getModel().getCurrentUser();
        final FragmentUserProfileEditBinding bind = (FragmentUserProfileEditBinding) DataBindingUtil.bind(view);
        bind.setUser(mCurrentUser);

        bind.signUpAnotherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // currently RESULT_FIRST_USER used only in this case
                FragmentActivity activity = getActivity();
                activity.setResult(Activity.RESULT_FIRST_USER, null);
                activity.finish();
            }
        });

        bind.saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(bind.nameField.getText())) {
                    bind.textInputLayoutName.requestFocus();
                    bind.textInputLayoutName.setError(getString(R.string.error_no_name));
                } else {

                    bind.saveSettingsButton.setEnabled(false);

                    mCurrentUser.setName(bind.nameField.getText().toString().trim());
                    mCurrentUser.setEmail(bind.editTextEmail.getText().toString());

                    Task task = UserDataProvider.updateCurrentUser(mCurrentUser);
                    task.continueWithTask(new Continuation() {
                        @Override
                        public Object then(Task task) throws Exception {

                            bind.saveSettingsButton.setEnabled(true);

                            if (task.isFaulted()) {
                                Toast.makeText(getContext(), R.string.error_network_title, Toast.LENGTH_LONG).show();
                            } else {
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                                NavUtils.showMainActivity(getActivity());
                            }
                            return null;
                        }
                    });
                }
            }
        });
    }
}
