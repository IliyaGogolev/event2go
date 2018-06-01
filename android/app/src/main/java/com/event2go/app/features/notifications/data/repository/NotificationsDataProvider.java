package com.event2go.app.features.notifications.data.repository;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.event2go.base.utils.JsonUtils;
import com.event2go.app.features.notifications.data.gsondeserializer.NotificationJsonDeserialiser;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.data.ParseModel;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 9/5/15.
 */
public class NotificationsDataProvider {

    public static final String TAG = NotificationsDataProvider.class.getSimpleName();

    private static final NotificationsDataProvider ourInstance = new NotificationsDataProvider();

    public static NotificationsDataProvider getInstance() {
        return ourInstance;
    }

    private NotificationsDataProvider() {
    }

    public Observable<List<AppNotification>> getNotifications() {

        Observable<List<AppNotification>> apiResultStream

                = Observable.create(new ObservableOnSubscribe<List<AppNotification>>() {
                @Override
                public void subscribe(final ObservableEmitter<List<AppNotification>> subscriber) throws Exception {


                    final HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("test", true);

                    ParseCloud.callFunctionInBackground("userNotifications", params, new FunctionCallback<List<HashMap>>() {


                        @Override

                        public void done(List<HashMap> list, ParseException e) {

                            if (e == null) {

                                //                        Log.d("aa", "aa");
                                //                        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                //                        f.setTimeZone(TimeZone.getTimeZone("UTC"));
                                //                        String format = f.format(new Date());
                                //                        Log.d("bbb", format);

                                //http://stackoverflow.com/questions/15402321/how-to-convert-hashmap-to-json-array-in-android
                                JSONArray jsonArray = null;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    jsonArray = JsonUtils.collectionToJson(list);
                                } else {
                                    jsonArray = new JSONArray(list);
                                }
                                //                            Gson gson = new GsonBuilder().registerTypeAdapter(Id.class, new IdDeserializer()).create();


                                Gson gson = new GsonBuilder().registerTypeAdapter(AppNotification.class, new NotificationJsonDeserialiser()).setDateFormat("EEE MMM dd HH':'mm':'ss z yyyy").create();

                                //                        jsonArray.toString(4)
                                // for iOS use
                                // https://parse.com/questions/nsdateformatter-problem-with-dates-from-parse
                                List<AppNotification> notifications = null;
                                try {
                                    Type listType = new TypeToken<List<AppNotification>>() {
                                    }.getType();
                                    notifications = gson.fromJson(jsonArray.toString(), listType);
                                    // workaround of post processor

                                } catch (JsonSyntaxException jsonEx) {
                                    Log.e(TAG, jsonEx.getMessage());
                                }

                                subscriber.onNext(notifications);
                                subscriber.onComplete();


                            } else {
                                subscriber.onError(e);
                            }
                        }
                    });
                }
            }
        );

        return apiResultStream;
    }

    public Observable update(final AppNotification notification) {

        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {

                ParseObject obj = new ParseObject(ParseModel.CLASS_NOTIFICATION);
                notification.writeToParseObject(obj);

                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            subscriber.onNext(notification);
                            subscriber.onComplete();
                        } else {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });

        return apiResultStream;
    }
}
