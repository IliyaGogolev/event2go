package com.event2go.app.features.chat.data;

import android.databinding.BaseObservable;

import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseObject;

/**
 * Created by Iliya Gogolev on 9/22/15.
 */
public class Contact extends BaseObservable implements Parsable {

    @SerializedName("objectId")
    private String mId;
    @SerializedName("name")
    private String mName;

    public Contact() {
    }

    final public String getId() {
        return mId;
    }

    public void setId(final String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String summary) {
        mName = summary;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_CONTACT;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {
        dest.setObjectId(mId);
        dest.put("name", mName);
    }

    @Override
    public void readFromParseObject(ParseObject object) {
        setId(object.getObjectId());
        setName((String) object.get("name"));
    }
}
