package com.event2go.app.features.user.domain;

import com.event2go.app.features.user.data.repository.UserDataProvider;
import com.event2go.app.features.user.data.User;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */
public class GetUserInfoUseCase {

    public Observable<List<User>> getPersonContains(String substring) {
        return UserDataProvider.getInstance().getUserContains(substring);
    }

    public Observable<List<User>> getAllUsers() {
        return UserDataProvider.getInstance().getAllUsers();
    }

    public Observable<User> getUserById(String userId) {
        return UserDataProvider.getInstance().getUserById(userId);
    }

}
