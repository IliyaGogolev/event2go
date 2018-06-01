package com.event2go.app.features.event.data.repository;

import android.content.Context;

import com.event2go.app.features.event.data.Attendee;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.data.ParseModel;
import com.event2go.base.utils.Logger;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 6/30/15.
 */
public class AttendeeDataProvider {

    private static final String TAG = AttendeeDataProvider.class.toString();

    private final Logger logger = new Logger();

    private static AttendeeDataProvider instance = new AttendeeDataProvider();

    private AttendeeDataProvider() {
    }

    public static AttendeeDataProvider getInstance() {
        return instance;
    }

    /**
     * Save participation status of current user for specific event
     * @param context
     * @param event - event of participation status
     * @param partstat - participation status
     * @return
     */
    public static Observable saveParticipationStatus(final Context context, final Event event, final String partstat) {

        final String prevPartstat = event.getCurrentUserPartStat();
        event.setCurrentUserPartStat(partstat);
        Attendee currentAttendee = event.getCurrentAttendee();

//        todo if (partstat.equalsIgnoreCase(prevPartstat)) return;

        final ParseObject parseObject = new ParseObject(ParseModel.CLASS_ATTENDEE);
        currentAttendee.writeToParseObject(parseObject);


        final Attendee finalCurrentAttendee = currentAttendee;
        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {


                parseObject.saveInBackground( new SaveCallback() {

                    @Override
                    public void done(ParseException e) {

                        if (e == null) {

                            event.setCurrentUserPartStat(partstat);
                            event.updateCountsByPrevstat(prevPartstat, partstat);
                            // update object id in case it was new object
                            finalCurrentAttendee.readFromParseObject(parseObject);

                            subscriber.onNext(null);
                            subscriber.onComplete();

                        } else {

                            event.setCurrentUserPartStat(prevPartstat);
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });

        return apiResultStream;

    }

}