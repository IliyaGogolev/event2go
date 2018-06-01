package com.event2go.app;

import android.app.Application;

import com.event2go.app.data.AppModel;
import com.facebook.FacebookSdk;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.user.data.User;
import com.parse.Parse;
import com.parse.interceptors.ParseLogInterceptor;

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
public class AppApplication extends Application {

    private static AppApplication context;
    private AppModel mModel = new AppModel();
    private Event mCurrentEvent;
    private User mCurrentSenderUser;
    private AppNotification mCurrentNotification;

//  https://console.developers.google.com/home/dashboard?project=event2go-1234
//  google  Project id: 894550020348
    // server api key: AIzaSyBd7LUU1Ab6nONzMMZYx5vLaMUt7uWaWw4
    

    @Override
    public void onCreate() {

        super.onCreate();

        AppApplication.context = this;
        FacebookSdk.sdkInitialize(getApplicationContext());

//        ParseFacebookUtils.initialize(getListener());

//        Parse.addParseNetworkInterceptor(new ParseLogInterceptor());
//        Parse.initialize(this,
//                "MofV8rBJbmoa5QTbmyGt6KjfKHR0AcGZ0OprSAqf", // app id
//                "xfEBsXSUxr32SMPMzek1rzjvru2slpjOAGKH73kK"); // client id

        Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("MofV8rBJbmoa5QTbmyGt6KjfKHR0AcGZ0OprSAqf")
                .clientKey("xfEBsXSUxr32SMPMzek1rzjvru2slpjOAGKH73kK")
                .server("https://event2go.herokuapp.com/parse/")

//                .server("http://192.168.1.140:1337/parse/")
//                .server("http://172.17.120.44:1337/parse/")
//                .server("http://10.0.2.2:1337/parse/") // localhost for android AVD emulator
//                .server("http://10.0.3.2:1337/parse/") // localhost for android for Genymotion
//                .server("http://10.0.3.2:1337/parse/") // localhost for android for Genymotion

//                .server("http://172.17.120.44:1337/parse/")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .build()
        );


//        Parse.setLogLevel(BuildConfig.DEBUG ? \Parse.LOG_LEVEL_DEBUG : Parse.LOG_LEVEL_VERBOSE);

//        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);


//        ParsePush.subscribeInBackground(PushChannel.NOTIFICATION, new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("com.parse.push", "successfully subscribed to " + PushChannel.NOTIFICATION + " channel.");
//                } else {
//                    Log.e("com.parse.push", "failed to subscribe for push", e);
//                }
//            }
//        });
    }

    public static AppApplication getContext() {
        return (AppApplication) AppApplication.context;
    }

    public AppModel getModel() {
        return mModel;
    }

    public User getCurrentUser() {
        return mModel.getCurrentUser();
    }

    public void setCurrentEvent(Event event) {
        mCurrentEvent = event;
    }

    public Event getCurrentEvent() {
        return mCurrentEvent;
    }

    public void setCurrentSenderUser(User sender) {
        mCurrentSenderUser = sender;
    }

    public User getCurrentSenderUser() {
        return mCurrentSenderUser;
    }

    public AppNotification getCurrentNotification() {
        return mCurrentNotification;
    }

    public void setCurrentNotification(AppNotification notification) {
        mCurrentNotification = notification;
    }
}

