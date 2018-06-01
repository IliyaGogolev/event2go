package com.event2go.app.features.user.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;
import com.event2go.base.presentation.fragment.AlertDialogFragment;
import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.event.data.repository.InviteDataProvider;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.Invite;
import com.event2go.app.features.user.data.User;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * Created by Iliya Gogolev on 6/25/15.
 */
public class InviteUsersRecycleViewFragment extends BaseRecyclerFragment<User> {

    //    public static final String LOAD_PARAM_USER_SUBSTRING = "user_substring";
    private static String DIALOG_TAG = "Confirm invite";

    private InviteUsersViewModel mInviteUsersViewModel;

    @NonNull
    @Override
    protected BaseRecyclerViewModel<User> getRecyclerViewModel() {
        mInviteUsersViewModel = new InviteUsersViewModel();
        return mInviteUsersViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadDataOnCreate(false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSwipeRefreshEnabled(false);

        final BaseRecyclerAdapter<User> adapter = mInviteUsersViewModel.getAdapter();
        adapter.setOnItemClickListener(
                new BaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        final User currentUser = AppApplication.getContext().getModel().getCurrentUser();
                        final User userSendTo = adapter.getItem(position);
                        final Event event = AppApplication.getContext().getCurrentEvent();

                        AlertDialogFragment dialog = AlertDialogFragment.newInstance();
                        dialog.setTitle(getString(R.string.invite_user_title));
                        String text = String.format(getString(R.string.invite_user_into_event), userSendTo.getName(), event.getSummary());
                        dialog.setMessage(text);
                        dialog.setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                hideKeyboard();

                                final User currentUser = AppApplication.getContext().getModel().getCurrentUser();
                                final Event event = AppApplication.getContext().getCurrentEvent();


                                Invite invite = new Invite();
                                invite.setEvent(event);
                                invite.setUserInvitedBy(currentUser);
                                invite.setUserSentTo(userSendTo);

                                Observable<Object> observable = InviteDataProvider.sendInvite(invite);
                                observable.subscribe(new Consumer<Object>() {
                                    @Override
                                    public void accept(Object o) {

                                        Context context = getActivity();
                                        String successText = context.getString(R.string.invite_user_success_toast, userSendTo.getName());
                                        Snackbar.make(getView(), successText, Snackbar.LENGTH_SHORT).show();

                                        getView().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                getActivity().finish();
                                            }
                                        }, 1000);


                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) {

                                        Log.e("Invite", throwable.getMessage());

                                        Context context = getActivity();
                                        String errorText = context.getString(R.string.invite_user_error_toast, userSendTo.getName());
                                        Snackbar.make(getView(), errorText, Snackbar.LENGTH_SHORT).show();

                                    }
                                });


//                                Observable<Object> observable = InviteDataProvider.sendInvite(getActivity(), currentUser, userSendTo, event);
//                                observable.subscribe(new Action1<Object>() {
//                                    @Override
//                                    public void call(Object o) {
//
//                                        Context listener = getActivity();
//                                        String successText = listener.getString(R.string.invite_user_success_toast, userSendTo.getName());
//                                        Toast.makeText(getActivity(), successText, Toast.LENGTH_LONG).show();
//
//                                    }
//                                }, new Action1<Throwable>() {
//                                    @Override
//                                    public void call(Throwable throwable) {
//
//                                        Context listener = getActivity();
//                                        String errorText = listener.getString(R.string.invite_user_error_toast, userSendTo.getName());
//                                        Toast.makeText(listener, errorText, Toast.LENGTH_LONG).show();
//                                    }
//                                });
                            }
                        });
                        dialog.setNegativeButton(android.R.string.cancel, null);
                        dialog.show(getActivity().getSupportFragmentManager(), DIALOG_TAG);
                    }
                }
        );
    }

    public void showUsersContains(String string) {

        mInviteUsersViewModel.setUserNameSubstring(string);
    }

}
