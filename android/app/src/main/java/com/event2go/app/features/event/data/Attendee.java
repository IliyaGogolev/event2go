package com.event2go.app.features.event.data;

import android.databinding.BaseObservable;

import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.google.gson.annotations.SerializedName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by Iliya Gogolev on 10/6/15.
 */
public class Attendee extends BaseObservable implements Parsable {

    private static final String ROLE_REQ_PARTICIPANT = "REQ-PARTICIPANT";

    // date start of participation event
    @SerializedName("objectId")
    private String mObjectId;
    @SerializedName("dtStart")
    private Date mDtStart;
    @SerializedName("userId")
    private String mUserId;
    @SerializedName("partstat")
    private String mPartStat;
    // participation role
    @SerializedName("role")
    private String mRole;
    @SerializedName("eventId")
    private String mEventId;

    public Attendee() {

//        net.fortuna.ical4j.model.property.Attendee mAttendee = new net.fortuna.ical4j.model.property.Attendee();
//        mAttendee.getParameters().add(Role.REQ_PARTICIPANT);
//        Role currentRole = (Role) mAttendee.getParameter(Parameter.ROLE);
//        String s = currentRole.toString();

        setRole(ROLE_REQ_PARTICIPANT);
    }


    // todo currently not supported
    public String getRole() {
        return mRole;
    }

    // todo currently not supported
    public void setRole(String role) {

        mRole = role;
//        net.fortuna.ical4j.model.property.Attendee mAttendee = new net.fortuna.ical4j.model.property.Attendee();
//        Role currentRole = (Role) mAttendee.getParameter(Parameter.ROLE);
//        if (currentRole != null) {
//            mAttendee.getParameters().remove(currentRole);
//        }
//
//        mAttendee.getParameters().add(role);
    }

    public String getEventId() {
        return mEventId;
    }

    public void setEventId(String id) {
        mEventId = id;
    }

    /**
     * Event start date - equals to event.startDate.
     * In case event is recurring event, the start date specify the instance of recurring event
     *
     * @param date
     */
    public void setEventStartDate(Date date) {
        mDtStart = date;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getPartStat() {
        return mPartStat;
    }

    public void setPartStat(String partstat) {
        mPartStat = partstat;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_ATTENDEE;
    }

    @Override
    public void writeToParseObject(ParseObject dest) {

        dest.setObjectId(mObjectId);
        dest.put("userId", mUserId);
        dest.put("eventId", mEventId);
        dest.put("dtStart", mDtStart);
        dest.put("partstat", mPartStat);
        dest.put("role", mRole);
    }

    @Override
    public void readFromParseObject(ParseObject source) {
        mObjectId = source.getObjectId();
        mUserId = (String) source.get("userId");
        mEventId = (String) source.get("eventId");
        mDtStart = (Date) source.get("dtStart");
        mPartStat = (String) source.get("partstat");
        mRole = (String) source.get("role");
    }


}
