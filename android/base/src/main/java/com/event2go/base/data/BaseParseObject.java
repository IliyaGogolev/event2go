package com.event2go.base.data;

import android.databinding.BaseObservable;

import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 6/30/15.
 */
public abstract class BaseParseObject extends BaseObservable {

    private ParseObject mParseObject;

    protected abstract String getParseCasssName();

    public BaseParseObject() {
        mParseObject = ParseObject.create(getParseCasssName());
    }

    public void saveInBackground() {
        mParseObject.saveInBackground();
    }

    protected void put(String key, Object value) {
        mParseObject.put(key, value);
    }


    protected Object get(String key) {
        return mParseObject.get(key);
    }

    public void setId(String objectId) {
        mParseObject.setObjectId(objectId);
    }

    public String getId() {
        return mParseObject.getObjectId();
    }

    public ParseObject getParseObject() {
        return mParseObject;
    }

    public void setParseObject(ParseObject mParseObject) {
        this.mParseObject = mParseObject;
    }

    public Observable deleteInBackground() {

        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {


                mParseObject.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            subscriber.onNext(null);
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

    public void readParseObject(ParseObject object) {

    }

    public ParseObject toParseObject() {
        return mParseObject;
    }

}
