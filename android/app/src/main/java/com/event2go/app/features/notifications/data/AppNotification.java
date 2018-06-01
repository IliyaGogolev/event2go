package com.event2go.app.features.notifications.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Typeface;
import android.support.annotation.IntDef;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import com.event2go.app.features.event.data.Invite;
import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.google.gson.annotations.SerializedName;
import com.event2go.app.AppApplication;
import com.event2go.app.BR;
import com.event2go.app.R;
import com.parse.ParseObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Created by Iliya Gogolev on 9/4/15.
 */
public class AppNotification extends BaseObservable implements Parsable {

    @IntDef({NOTIFICATION_TYPE_INVITE, NOTIFICATION_TYPE_CHAT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationType {
    }

    public static final int NOTIFICATION_TYPE_INVITE = 0;
    public static final int NOTIFICATION_TYPE_CHAT = 1;

    @IntDef({STATUS_NEW, STATUS_READ})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationStatus {
    }
    public static final int STATUS_NEW = 0;
    public static final int STATUS_READ = 1;


    private static final int INVITE = 0;
    @SerializedName("type")
    private int mType;
    @SerializedName("data_object_id")
    private String mDataObjectId;
    @SerializedName("status")
    private int mStatus;
    @SerializedName("user_id")
    private String mUserId;
    @SerializedName("objectId")
    private String mId;
    private
    @NotificationType
    int type;
    private Invite mInvite;
    @SerializedName("createdAt")
    private Date mCreatedAt;

    public void setDataObjectId(String dataObjectId) {
        this.mDataObjectId = dataObjectId;
    }

    public String getmDataObjectId() {
        return mDataObjectId;
    }

    public void setmDataObjectId(String mDataObjectId) {
        this.mDataObjectId = mDataObjectId;
    }

    public void setStatus(@NotificationStatus  int status) {
        this.mStatus = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public int getStatus() {
        return mStatus;
    }

    public void setUserId(String userId) {
        this.mUserId = userId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setInvite(Invite invite) {
        this.mInvite = invite;
    }


    public void setObjectId(String id) {
        this.mId = id;
    }

//    "data_object": {
//        "updatedAt": "2015-12-09T01:54:05.734Z",
//                "objectId": "0R96gaYJV9",
//                "event": {
//            "__type": "Relation",
//                    "className": "Event"
//        },
//        "createdAt": "2015-12-09T01:54:05.734Z",
//                "user_name_invited_by": "korkag",
//                "event_id": "kP827a3Edz",
//                "user_id_invited_by": "7kMAjgbTE4",
//                "user_id": "7kMAjgbTE4",
//                "event_summary": "test",
//                "user_invited_by": {
//            "__type": "Relation",
//                    "className": "_User"
//        }
//    },
//    "data_object_id": "0R96gaYJV9",
//    "status": 0,
//    "objectId": "q1nfiWekVd",
//    "createdAt": "2015-12-09T01:54:05.833Z",
//    "user_id": "7kMAjgbTE4",
//    "type": 0


//  private ChatMessge mChatMessage; // this's example, todo: add it


    public int getType() {
        return mType;
    }

    @Bindable
    public String getUserName() {
        switch (getType()) {
            case INVITE:
                return mInvite.getInvitedByUserName();
        }

        return "no user name";
    }

    @Bindable
    public CharSequence getNotificationText() {

        SpannableStringBuilder builder = new SpannableStringBuilder();

        addContent(builder);

//        CharSequence created = android.text.format.DateUtils.getRelativeTimeSpanString(mCreatedAt.getTime());
//        builder.append(created);

        return builder;
    }


    public void addContent(SpannableStringBuilder builder) {

        String userName = getUserName();
        SpannableString spannable = new SpannableString(userName);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, userName.length(), 0);
        builder.append(spannable);
        builder.append(" ");

        String string = null;
        switch (getType()) {
            case INVITE:
                string = AppApplication.getContext().getString(R.string.notification_invite_content);
                builder.append(string);
                builder.append(" ");
                break;
        }

        String eventName = mInvite.getEventSummary();
        spannable = new SpannableString(eventName);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, eventName.length(), 0);
        builder.append(spannable);

//        builder.append(" ");
//        string = AppApplication.getListener().getString(R.string.notification_invite_suffix);
//        builder.append(string);

    }

    @Bindable
    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date time) {
        mCreatedAt = time;
    }

    public String getAvatarUrl() {
//        switch (getType()) {
//            case INVITE:
//                return mInvite.getInvitedByUserName();
//        }

        return "";
    }


    public Invite getInvite() {
        return mInvite;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_NOTIFICATION;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {

        dest.setObjectId(mId);
        dest.put("status", mStatus);
        dest.put("data_object_id", mDataObjectId);
        dest.put("type", mType);
        dest.put("user_id", mUserId);

    }

    @Override
    public void readFromParseObject(ParseObject source) {
        mType = (Integer) source.get("type");
        mInvite = new Invite();
        mInvite.readFromParseObject((ParseObject) source.get("data"));
    }
}
