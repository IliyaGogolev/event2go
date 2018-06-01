package com.event2go.app.features.user.presentation;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.R;
import com.event2go.app.features.user.data.repository.UserDataProvider;
import com.event2go.app.features.user.data.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 10/6/15.
 */
public class InviteUsersViewModel extends BaseRecyclerViewModel<User> {

    private String mUserNameSubstring = null;

    public InviteUsersViewModel() {
        super(new UsersRecyclerViewAdapter());
    }

    @Override
    public void load(@NonNull final OnLoadCompleteCallback<User> callback) {
        super.load(callback);

        if (TextUtils.isEmpty(mUserNameSubstring)) {
            callback.onSuccess(new ArrayList<User>());
        } else {
            Observable<List<User>> userCall = UserDataProvider.getInstance().getUserContains(mUserNameSubstring);
            userCall.subscribe(new Consumer<List<User>>() {

                @Override
                public void accept(List<User> data) {
                    callback.onSuccess(data);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable t) {

                    callback.onFailure(t);
                }
            });
        }
    }

    public void setUserNameSubstring(String substring) {

        mUserNameSubstring = substring;
        load(null);
    }

    @Override
    public int getEmptyListStringId() {
        return R.string.empty;
    }

}
