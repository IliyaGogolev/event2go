package com.event2go.app.features.event.data.repository;

import android.content.Context;
import android.util.Log;

import com.event2go.base.net.OnServerResponse;
import com.event2go.app.features.event.data.Invite;
import com.event2go.app.data.ParseModel;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 7/10/2015.
 */
public class InviteDataProvider {

    private static final String TAG = InviteDataProvider.class.getName();

    public static void loadInvite(final Context context, String inviteId, final OnServerResponse<Invite> serverResponse) {
        Map<String, Object> params = new HashMap<>();
        params.put("inviteId", inviteId);

        ParseCloud.callFunctionInBackground("loadInvite", params, new FunctionCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Invite invite = new Invite();
                    invite.readFromParseObject(object);
                    Log.d(TAG, invite.getId());
                    serverResponse.onSuccess(invite);
                } else {
                    Log.e(TAG, e.getMessage());
                    serverResponse.onFail(e);
                }
            }
        });
    }


    public static void deleteInvite(String inviteId, final OnServerResponse<Invite> serverResponse) {
        Map<String, Object> params = new HashMap<>();
        params.put("inviteId", inviteId);

        ParseCloud.callFunctionInBackground("deleteInvite", params, new FunctionCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Invite invite = new Invite();
                    invite.readFromParseObject(object);
                    Log.d(TAG, "Removed Invite " + invite.getId());
                    serverResponse.onSuccess(invite);
                } else {
                    Log.e(TAG, e.getMessage());
                    serverResponse.onFail(e);
                }
            }
        });
    }

    // test add user to event
    public static Observable<Object> sendInvite2(final Invite invite) {

        return EventsDataProvider.getInstance().addUserToEvent(invite.getEvent(), invite.getUserSentTo());
    }

    public static Observable<Object> sendInvite(final Invite invite) {

        Observable<Object> apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {


                final ParseObject parseObject = new ParseObject(ParseModel.CLASS_INVITE);
                invite.writeToParseObject(parseObject);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            if (e == null) {
                                subscriber.onNext(null);
                                subscriber.onComplete();
                            } else {
                                subscriber.onError(e);
                            }
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
