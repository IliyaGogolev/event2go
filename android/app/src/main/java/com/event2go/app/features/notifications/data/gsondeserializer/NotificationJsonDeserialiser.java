package com.event2go.app.features.notifications.data.gsondeserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.features.event.data.Invite;
import com.event2go.base.utils.DateUtils;

import java.lang.reflect.Type;

/**
 * Created by Iliya Gogolev on 12/8/15.
 */
public class NotificationJsonDeserialiser implements JsonDeserializer<AppNotification> {

    private static final String TAG = NotificationJsonDeserialiser.class.getSimpleName();

    @Override
    public AppNotification deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jobject = (JsonObject) json;

        AppNotification notification = new AppNotification();
//        notification.setObjectId(jobject.get("objectId").getAsString());


        // todo
//        "createdAt":"2015-12-09T01:54:05.833Z",

        notification.setObjectId(jobject.get("objectId").getAsString());
        notification.setDataObjectId(jobject.get("data_object_id").getAsString());
        notification.setStatus(jobject.get("status").getAsInt() == 0 ?
                AppNotification.STATUS_NEW :
                AppNotification.STATUS_READ);
        notification.setUserId(jobject.get("user_id").getAsString());
        notification.setType(jobject.get("type").getAsInt());

        /*try {
            Gson timeGson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").create();
            Date date = timeGson.fromJson(jobject.get("createdAt").getAsString(), Date.class);
            notification.setCreatedAt(date);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }*/
        notification.setCreatedAt(DateUtils.parseDateFromJSonString(jobject.get("createdAt").getAsString()));

        int type = notification.getType();
        switch (type) {
            case AppNotification.NOTIFICATION_TYPE_INVITE:
//                Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH':'mm':'ss z yyyy").create();
                Gson gson = new GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss.SSS'Z'").create();
                try {
                    Invite invite = gson.fromJson(jobject.get("data_object").toString(), Invite.class);
                    notification.setInvite(invite);
                } catch (JsonSyntaxException jsonEx) {
                    Log.e(TAG, jsonEx.getMessage());
                }


                break;
        }

        return notification;
    }
}
