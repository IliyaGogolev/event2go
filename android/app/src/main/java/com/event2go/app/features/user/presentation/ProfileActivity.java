package com.event2go.app.features.user.presentation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.event2go.app.R;
import com.event2go.app.features.login.dataProvider.LoginProvider;
import com.event2go.app.utils.NavUtils;

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
public class ProfileActivity extends AppCompatActivity {

    private ProfilePictureView userProfilePictureView;
    private TextView userNameView;
    private TextView userGenderView;
    private TextView userEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_user_profile);

        userProfilePictureView = (ProfilePictureView) findViewById(R.id.userProfilePicture);
        userNameView = (TextView) findViewById(R.id.userName);
        userGenderView = (TextView) findViewById(R.id.userGender);
        userEmailView = (TextView) findViewById(R.id.userEmail);

        // todo - load user by id or get it from cache
    }

    public void onLogoutClick(View v) {
        logout();
    }

    private void logout() {

        LoginProvider.logout();
        NavUtils.showLoginActivity(this);
    }
}