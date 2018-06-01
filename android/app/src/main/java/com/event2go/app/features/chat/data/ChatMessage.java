package com.event2go.app.features.chat.data;

import android.databinding.BaseObservable;

import com.event2go.base.data.Parsable;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Iliya Gogolev on 3/18/2016.
 */
public class ChatMessage extends BaseObservable implements Parsable {
    private long id;
    private boolean isMe;
    private String message;
    private Long userId;
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date dateTime) {
        this.date = dateTime;
    }

    @Override
    public String getParseCasssName() {
        return null;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {

    }

    @Override
    public void readFromParseObject(ParseObject source) {

    }
}