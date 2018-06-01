package com.event2go.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.user.presentation.InviteUserActivity;
import com.event2go.app.features.main.MainActivity;
import com.event2go.app.features.user.presentation.ProfileActivity;
import com.event2go.app.features.main.SplashActivity;
import com.event2go.base.presentation.activity.BaseFragmentActivity;
import com.event2go.app.features.chat.presentation.ConversationFragment;
import com.event2go.app.features.chat.presentation.CreateChatFragment;
import com.event2go.app.features.event.presentation.CreateEventFragment;
import com.event2go.app.features.event.presentation.EventProfileFragment;
import com.event2go.app.features.event.presentation.EventsRecyclerFragment;
import com.event2go.app.features.chat.presentation.MessagesTabFragment;
import com.event2go.app.features.notifications.repository.NotificationsRecyclerFragment;
import com.event2go.app.features.login.presentation.SignupEnterPhoneFragment;
import com.event2go.app.features.user.presentation.UserProfileEditFragment;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.features.chat.data.Contact;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.data.RequestCode;
import com.parse.ui.ParseLoginBuilder;

import java.util.Arrays;

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
public class NavUtils {

    public static void showUserDetailsActivity(Context context) {

        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    public static void showUserProfileEditFragment(AppCompatActivity activity) {
        Intent intent = new Intent(activity, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, UserProfileEditFragment.class.getName());
        activity.startActivityForResult(intent, RequestCode.LOGIN_EDIT_PROFILE.ordinal());
    }


    public static void showLoginActivity(AppCompatActivity activity) {

        /**
         * Option to open login by email and password
         * Plus option to create new user
         */
//        showParseLoginActivity(activity);


        showLoginByPhoneActivity(activity);
    }

    private static void showLoginByPhoneActivity(AppCompatActivity activity) {
        Intent intent = new Intent(activity, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, SignupEnterPhoneFragment.class.getName());
        intent.putExtra(BaseFragmentActivity.USE_TOOLBAR, BaseFragmentActivity.USE_TOOLBAR_NONE);
        activity.startActivityForResult(intent, RequestCode.LOGIN.ordinal());
    }

    public static void showParseLoginActivity(Activity activity) {
        ParseLoginBuilder builder = new ParseLoginBuilder(activity);
        Intent parseLoginIntent = builder.setParseLoginEnabled(true)
                .setParseLoginButtonText("Log In")
                .setParseSignupButtonText("Register")
                .setParseLoginHelpText("Forgot password?")
                .setParseLoginInvalidCredentialsToastText("You email and/or password is not correct")
                .setParseLoginEmailAsUsername(true)
                .setParseSignupSubmitButtonText("Submit registration")
                .setFacebookLoginEnabled(true)
                .setFacebookLoginButtonText("Facebook")
                .setFacebookLoginPermissions(Arrays.asList("user_status", "read_stream"))
//                .setTwitterLoginEnabled(true)
//                .setTwitterLoginButtontext("Twitter")
                .build();

        activity.startActivityForResult(parseLoginIntent, RequestCode.LOGIN.ordinal());
    }

    public static void showMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void showSigninActivity(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        activity.startActivity(intent);
    }


//    public static void showSearchEventActivity(AppCompatActivity activity) {
//
//        Intent intent = new Intent(activity, SearchActivity.class);
//        activity.startActivity(intent);
//
////        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
////        ft.add(R.id.content_frame, new SearchEventFragment());
////        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out);
////        ft.addToBackStack(null);
////        ft.commit();
//
//
//    }


    public static void showCreateEventFragmentActivity(BaseFragment fragment) {

        showCreateEventFragmentActivity(fragment, false, null);
    }


    public static void showCreateEventFragmentActivity(BaseFragment fragment, boolean editMode, Event event) {

        AppApplication.getContext().setCurrentEvent(event);

        Intent intent = new Intent(fragment.getActivity(), BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, CreateEventFragment.class.getName());
        intent.putExtra("edit_mode", editMode);
        int requestCode = editMode ? RequestCode.EVENT_EDIT.ordinal() : RequestCode.EVENT_CREATE.ordinal();
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void showInvitePersonActivity(Context context) {
        Intent intent = new Intent(context, InviteUserActivity.class);
        context.startActivity(intent);
    }

    public static void showEvents(MainActivity context) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, EventsRecyclerFragment.class.getName());
        context.startActivity(intent);

    }

    public static void showEventProfileActivity(BaseFragment fragment, Event event, int requestCode) {
        AppApplication.getContext().setCurrentEvent(event);

        Intent intent = new Intent(fragment.getActivity(), BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, EventProfileFragment.class.getName());
        fragment.startActivityForResult(intent, requestCode);

    }

    public static void showResponseToInviteActivity(Activity activity, AppNotification notification, int requestCode) {
        AppApplication.getContext().setCurrentNotification(notification);
        AppApplication.getContext().setCurrentEvent(null);

        Intent intent = new Intent(activity, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, EventProfileFragment.class.getName());
        intent.putExtra("event_id", notification.getInvite().getEventId());
        intent.putExtra("came_from_notification", true);
        activity.startActivityForResult(intent, requestCode);

    }

    public static void showNotificationsActivity(Context context) {

        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, NotificationsRecyclerFragment.class.getName());
        intent.putExtra(BaseFragmentActivity.TOOLBAR_TITLE, context.getString(R.string.nav_notifications));
        context.startActivity(intent);
    }

    public static void showActivityFromPushNotification(Context context, String className) {

        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, className);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showConversationActivity(MainActivity context) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, MessagesTabFragment.class.getName());
        intent.putExtra(BaseFragmentActivity.USE_TOOLBAR, BaseFragmentActivity.USE_TOOLBAR_INSIDE_FRAGMENT);
        context.startActivity(intent);
    }

    public static void showCreateChatActivity(Context context) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, CreateChatFragment.class.getName());
        context.startActivity(intent);
    }

    public static void showOpenChatActivity(Context context, Contact contact) {
        Intent intent = new Intent(context, BaseFragmentActivity.class);
        intent.putExtra(BaseFragmentActivity.CLASS_NAME, ConversationFragment.class.getName());
//        intent.putExtra(BaseFragmentActivity.CLASS_NAME, OneOnOneChatRecyclerFragment.class.getName());
        context.startActivity(intent);
    }

}
