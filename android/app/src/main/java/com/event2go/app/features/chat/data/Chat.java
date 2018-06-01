package com.event2go.app.features.chat.data;

import android.databinding.BaseObservable;

import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Iliya Gogolev on 3/11/16.
 */
public class Chat extends BaseObservable implements Parsable {

    @SerializedName("objectId")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("lastMessage")
    private String mLastMessage;
    @SerializedName("time")
    private Date mLastTime;

    public Chat() {
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

    public void setName(String name) {
        mName = name;
    }

    public String getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(String lastMessage) {
        mLastMessage = lastMessage;
    }

    public Date getLastTime() {
        return mLastTime;
    }

    public void setLastTime(Date mLastTime) {
        this.mLastTime = mLastTime;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_CHAT;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {

        dest.setObjectId(mId);
        dest.put("name", mName);
        dest.put("lastMessage", mLastMessage == null ? "" : mLastMessage);
        dest.put("time", mLastTime == null ? "" : mLastTime);
    }

    @Override
    public void readFromParseObject(ParseObject object) {
        setId(object.getObjectId());
        setName((String) object.get("name"));
        setLastMessage((String) object.get("lastMessage"));
        setLastTime((Date) object.get("time"));
    }
}
