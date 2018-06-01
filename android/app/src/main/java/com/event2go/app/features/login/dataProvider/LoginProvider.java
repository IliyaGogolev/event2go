package com.event2go.app.features.login.dataProvider;

import android.util.Log;

import com.event2go.app.AppApplication;
import com.event2go.app.data.AppModel;
import com.event2go.app.features.user.data.User;
import com.event2go.base.utils.Logger;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.concurrent.Callable;

import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * Created by Iliya Gogolev on 3/24/16.
 */
public class LoginProvider {

    public static Task requestSMSCodeByPhoneNumber(String phoneNumber) {

        final HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("phoneNumber", phoneNumber);
        params.put("language", "en");
        params.put("debug", 1);

        final TaskCompletionSource tcs = new TaskCompletionSource<>();

         Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                ParseCloud.callFunctionInBackground("onSendCodeButtonClicked", params, new FunctionCallback<HashMap>() {
                    public void done(HashMap response, ParseException e) {
                        if (e == null) {
                            Logger.d("There were no exceptions! ");
                            tcs.setResult(null);
                        } else {
                            Logger.d("Exception: " + (response != null ? response.toString() : "") + e);
                            tcs.setError(e);
                        }
                    }
                });

                return new Object();
            }
        });

        return tcs.getTask();
    }


    public static Task loginByPhoneNumber(String phoneNumber, int code) {
        final HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("phoneNumber", phoneNumber);
        params.put("codeEntry", code);

        final TaskCompletionSource tcs = new TaskCompletionSource<>();

        Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                ParseCloud.callFunctionInBackground("logIn", params, new FunctionCallback<String>() {
                    public void done(String response, ParseException e) {

                        if (e == null) {
                            String token = response;
                            Log.d("Cloud Response", "There were no exceptions! " + response);
                            ParseUser.becomeInBackground(token, new LogInCallback() {
                                @Override
                                public void done(ParseUser parseUser, ParseException e) {
                                    if (e == null) {
                                        Log.d("Cloud Response", "There were no exceptions! ");
                                        User.Companion.initCurrentUser();
                                        tcs.setResult(null);
                                    } else {
                                        Log.d("Cloud Response", "Exception: " + e);
                                        tcs.setError(e);
                                    }
                                }
                            });
                        } else {
                            Log.d("Cloud Response", "Exception: " + response + e);
                            tcs.setError(e);
                        }
                    }
                });

                return new Object();
            }
        });

        return tcs.getTask();

    }

    public static Task loginByUsername(String username, String password) {

        final HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);

        final TaskCompletionSource tcs = new TaskCompletionSource<>();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    tcs.setResult(user);
                } else {
                    Log.d("Cloud Response", "Exception: " + e);
                    tcs.setError(e);
                }
            }
        });

        return tcs.getTask();

    }


    public static void logout() {
        ParseUser.logOut();
    }
}
