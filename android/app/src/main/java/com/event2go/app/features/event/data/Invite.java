package com.event2go.app.features.event.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.event2go.app.features.user.data.User;
import com.google.gson.annotations.SerializedName;
import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

/**
 * Created by Iliya Gogolev on 9/3/15.
 */
public class Invite extends BaseObservable implements Parsable { //Parcelable { //}, Parsable {

    @SerializedName("object_id")
    private String mId;

    private Event mEvent;
    private User mUserInvitedBy;
    private User mUserSentTo;

    @SerializedName("event_id")
    private String mEventId;
    @SerializedName("user_id")
    private String mUserIdSentTo;
    @SerializedName("user_id_invited_by")
    private String mUserIdInvitedBy;
    @SerializedName("event_summary")
    private String mEventSummary;
    @SerializedName("user_name_invited_by")
    private String mUserNameInvitedBy;

    public Invite() {
        super();
    }

    public String getId() {
        return mId;
    }

    public void setEvent(Event event) {
        mEvent = event;
    }

    public void setUserInvitedBy(User user) {
        mUserInvitedBy = user;
    }

    @Bindable
    public User getUserInvitedBy() {
        return mUserInvitedBy;
    }

    public String getEventId() {
        return mEventId;
    }

    public String getUserId() {
        return mUserIdSentTo;
    }

    public String getInvitedByUserName() {
        return mUserNameInvitedBy;
    }

    public String getEventSummary() {
       return mEventSummary;
    }



    public void setUserSentTo(User user) {
        this.mUserSentTo = user;
    }

    public User getUserSentTo() {
        return mUserSentTo;
    }

    @Bindable
    public CharSequence getSummary() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        addContent(builder);
        return builder;
    }

    public void addContent(SpannableStringBuilder builder) {

        SpannableString spannable = new SpannableString(getInvitedByUserName());
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, getInvitedByUserName().length(), 0);
        builder.append(spannable);
        builder.append(" ");

        String string = AppApplication.getContext().getString(R.string.notification_invite_content);
        builder.append(string);
        builder.append(" ");

        String eventName = getEventSummary();
        spannable = new SpannableString(eventName);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, eventName.length(), 0);
        builder.append(spannable);
    }

    public Event getEvent() {
        return mEvent;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_INVITE;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {

        if (!TextUtils.isEmpty(mId)) {
            dest.setObjectId(mId);
        }
        dest.put("event_id", mEvent.getId());
        dest.put("user_id", mUserSentTo.getId());
        dest.put("user_id_invited_by", mUserInvitedBy.getId());
        dest.put("event_summary", mEvent.getSummary());
        dest.put("user_name_invited_by", mUserInvitedBy.getName());
        if (!TextUtils.isEmpty(mUserInvitedBy.getAvatarUrl())) {
            dest.put("user_avatar_url_invited_by", mUserInvitedBy.getAvatarUrl());
        }

        ParseObject eventParseObject = new ParseObject(ParseModel.CLASS_EVENT);
        ParseRelation<ParseObject> event = dest.getRelation("event");
        mEvent.writeToParseObject(eventParseObject);
        event.add(eventParseObject);

        ParseUser userInviteByParseObject = new ParseUser();
        mUserInvitedBy.writeToParseObject(userInviteByParseObject);
        ParseRelation<ParseObject> userInvitedByRelation = dest.getRelation("user_invited_by");
        userInvitedByRelation.add(userInviteByParseObject);
    }

    @Override
    public void readFromParseObject(ParseObject source) {

        // TODO didn't check yet

        mId = (String) source.get("objectId");
        mEventId = (String) source.get("event_id");
        mUserIdSentTo = (String) source.get("user_id");
        mUserIdInvitedBy  = (String) source.get("invited_by");
        mEvent = new Event();
        mEvent.setId(((ParseObject) source.get("event")).getObjectId());
        mUserInvitedBy = new User();
        mUserInvitedBy.setId(((ParseObject) source.get("user_invited_by")).getObjectId());
        mEventSummary = (String) source.get("event_summary");
        mUserNameInvitedBy = (String) source.get("user_name_invited_by");
    }
}