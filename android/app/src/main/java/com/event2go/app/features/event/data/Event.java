package com.event2go.app.features.event.data;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.event2go.base.data.Parsable;
import com.event2go.app.data.ParseModel;
import com.event2go.app.features.user.data.User;
import com.google.gson.annotations.SerializedName;
import com.event2go.app.AppApplication;
import com.event2go.app.BR;
import com.event2go.base.utils.DateUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Dur;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.VAlarm;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Iliya Gogolev on 9/22/15.
 */
public class Event extends BaseObservable implements Parsable {

    public final static int PARAM_TYPE_EVENT = 0;
    public final static int PARAM_TYPE_EVENT_INSTANCE_TEMPLATE = 1;
    public final static int PARAM_TYPE_EVENT_PLAYER_TEMPLATE = 2;
    public final static int PARAM_TYPE_EVENT_PLAYER_INSTANCE_TEMPLATE = 3;

    public void addAttendee(Attendee currentAttendee) {

        if (mAttendees == null) mAttendees = new ArrayList<>();
        mAttendees.add(currentAttendee);
    }

    public static class Counts extends BaseObservable {
        @SerializedName("coming")
        private int coming;
        @SerializedName("needsAction")
        private int needsAction;
        @SerializedName("tentative")
        private int tentative;
        @SerializedName("notComing")
        private int notComing;

        @Bindable
        public int getComing() {
            return coming;
        }

        public void setComing(int coming) {
            this.coming = coming;
            notifyPropertyChanged(BR.coming);
        }

        @Bindable
        public int getNeedsAction() {
            return needsAction;
        }

        public void setNeedsAction(int needsAction) {
            this.needsAction = needsAction;
            notifyPropertyChanged(BR.needsAction);
        }

        @Bindable
        public int getNotComing() {
            return notComing;
        }

        public void setNotComing(int notComing) {
            this.notComing = notComing;
            notifyPropertyChanged(BR.notComing);
        }

        @Bindable
        public int getTentative() {
            return tentative;
        }

        public void setTentative(int tentative) {
            this.tentative = tentative;
            notifyPropertyChanged(BR.tentative);
        }
    }

    @SerializedName("users")
    private List<User> mUsers = new ArrayList<>();
    private List<Parameter> mEventParameters = new ArrayList<Parameter>();

    public static final String ALARM_TYPE_RVSP_REMINDER = "RSVP";

    // one day before scheduled start of the event
    private final static int DEFAULT_RVSP_ALERT_DUR_DAYS = -1;

    private VAlarm defaultAlert;
//    private PeriodList mPeriodList;

    @SerializedName("objectId")
    private String mId;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("location")
    private String mLocation;
    //    @SerializedName("rrule")
//    private String mRRuleString;
    @SerializedName("attendees")
    private List<Attendee> mAttendees = new ArrayList<>();
    @SerializedName("counts")
    private Counts mCounts;
    @SerializedName("dtstart_occurrence")
    private Date mDtStartOccurrence;
    @SerializedName("dtStart")
    private Date mDtStart;
    @SerializedName("dtEnd")
    private Date mDtEnd;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("sequence")
    private String mSequence = "0";
    @SerializedName("users_count")
    private Integer mUsersCount;
    @SerializedName("recur_end")
    private Date mRecurEndDate;
    @SerializedName("recur_type")
    private int mRecurType = RecurType.NONE;

    //    private RecurRule mRRule;
    private PeriodList mPeriodList;
    // Used ical4j event class to help implement event alarm logic
    public VEvent mEventAlarm = new VEvent();


    public Event() {
        // set default rvsp alert
        Dur dur = new Dur(DEFAULT_RVSP_ALERT_DUR_DAYS, 0, 0, 0);
        setRsvpAlarmDur(dur);
        mCounts = new Counts();
    }

    final public String getId() {
        return mId;
    }

    public void setId(final String id) {
        this.mId = id;
    }


//    private String getRRuleString() {
//        return mRRuleString;
//    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDtStartOccurrence() {
        return mDtStartOccurrence;
    }

    @Bindable
    public Integer getUsersCount() {
        return mUsersCount;
    }

    public void setUsersCount(Integer usersCount) {
        mUsersCount = usersCount;
    }

    @Bindable
    public Date getStartDate() {
        return mDtStart;
    }

    @Bindable
    public Date getEndDate() {
        return mDtEnd;
    }

    public void addUser(User user) {

        mUsers.add(user);
    }

    public List<User> getUsers() {
        return mUsers;
    }

    public void setStartTime(Date time) {
        mDtStart = time;
        notifyPropertyChanged(BR.startDate);
    }

    public void setEndTime(Date time) {
        mDtEnd = time;
        notifyPropertyChanged(BR.endDate);
    }

    private VAlarm getRsvpReminderAlarm() {

        ComponentList alarms = mEventAlarm.getAlarms();
        for (final Iterator i = alarms.iterator(); i.hasNext(); ) {
            final VAlarm c = (VAlarm) i.next();
            if (c.getDescription().getValue().equals(ALARM_TYPE_RVSP_REMINDER)) {
                return c;
            }
        }

        return null;
    }

    public List<Parameter> getEventParameters() {
        return mEventParameters;
    }

    public Dur getRsvpAlarmDuration() {
        return getRsvpReminderAlarm().getTrigger().getDuration();
    }

    public void setRsvpAlarmDur(Dur dur) {

        VAlarm alarm = getRsvpReminderAlarm();
        if (alarm == null) {
            alarm = new VAlarm(dur);
            alarm.getProperties().add(new Description(ALARM_TYPE_RVSP_REMINDER));
            mEventAlarm.getAlarms().add(alarm);
        } else {
            alarm.getTrigger().setDuration(dur);
        }
    }

    /**
     * Represents VALARM for rsvp
     * Example
     * BEGIN:VALARM
     * ACTION:DISPLAY
     * DESCRIPTION: This is an event reminder
     * TRIGGER:-P0DT7H10M0S
     * END:VALARM
     */
    public ParseObject getRsvpAlarmParseObject() {

        VAlarm alarm = getRsvpReminderAlarm();

        ParseObject parseObject = new ParseObject(ParseModel.CLASS_VALARM);
        parseObject.put("vEventId", getId());
        if (alarm.getAction() != null) {
            parseObject.put("action", alarm.getAction().getValue());
        }
        if (alarm.getDescription() != null) {
            parseObject.put("description", alarm.getDescription().getValue());
        }
        parseObject.put("trigger", alarm.getTrigger().toString());

        return parseObject;
    }

    public List<Attendee> getAttendees() {
        return mAttendees;
    }

    public Counts getCounts() {
        return mCounts;
    }

    public boolean isRecur() {
        return !(mRecurType == RecurType.NONE);
    }

    @Bindable
    public String getDislayStartDate() {

        if (!isRecur()) {
            return DateUtils.getDateString(mDtStart);
        }

        return DateUtils.getDateString(mDtStartOccurrence);
    }

    @Bindable
    public String getDisplayTimeRange() {

        String timeRange;
        if (isRecur()) {

            timeRange = DateUtils.getTimeString(getStartDate()) + " - " +
                    DateUtils.getTimeString(getEndDate());
        } else {
//            initCurrentNextPeriodList();

            Date startDateCal = mDtStartOccurrence;
            Date endDateCal = new Date();
            long duration = (mDtEnd.getTime() - mDtStart.getTime());
            endDateCal.setTime(mDtStartOccurrence.getTime() + duration);

            timeRange = DateUtils.getTimeString(startDateCal) + " - " +
                    DateUtils.getTimeString(endDateCal);
        }

        return timeRange;
    }

    @Override
    public String getParseCasssName() {
        return ParseModel.CLASS_EVENT;
    }

    private String dateToUtc(Date date) {
        Log.d("aa", "aa");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        String format = f.format(new Date());
        Log.d("bbb", format);
        return format;
    }

    public void setRecurEndDate(Date date) {
        mRecurEndDate = date;
    }

    public Date getRecurEndDate() {
        return mRecurEndDate;
    }


    @Override
    public void writeToParseObject(ParseObject dest) {

        dest.setObjectId(mId);
        dest.put("summary", mSummary);
//        dest.put("dtStart", dateToUtc(mDtStart));
//        dest.put("dtEnd", dateToUtc(mDtEnd));

        dest.put("dtStart", mDtStart);
        dest.put("dtEnd", mDtEnd);

        // new Event
        if (TextUtils.isEmpty(mId)) {
            ParseUser currentParseUser = ParseUser.getCurrentUser();
            User currentUser = AppApplication.getContext().getCurrentUser();
            currentUser.writeToParseObject(currentParseUser);
            dest.put("organizer", currentParseUser);
        }

        if (!TextUtils.isEmpty(mSequence)) dest.put("sequence", mSequence);

        // currently not used
        dest.put("description", mDescription == null ? "" : mDescription);


//        ParseRelation<ParseObject> attendees = dest.getRelation(ParseModel.RELATION_USERS);
//        for (User user : mUsers) {
//            ParseUser parseUser = new ParseUser();
//            user.writeToParseObject(parseUser);
//            attendees.add(parseUser);
//        }

        // todo
//        parseObject.put("uid", mEvent.getUid().toString());
        if (!TextUtils.isEmpty(mStatus)) {
            dest.put("status", mStatus);
        }

        dest.put("location", mLocation);

        dest.put("recur_type", mRecurType);
        if (isRecur() && mRecurEndDate != null) {
            dest.put("recur_end", mRecurEndDate);
        }
        // todo
//        parseObject.put("duration", "");
//        parseObject.put("geoLat", );
//        parseObject.put("geoLng", );

    }

    @Override
    public void readFromParseObject(ParseObject object) {

        setId(object.getObjectId());
        setSummary((String) object.get("summary"));
        setDescription((String) object.get("description"));
        setStartTime((Date) object.get("dtStart"));
        mDtStartOccurrence = (Date) object.get("dtStart");
        setEndTime((Date) object.get("dtEnd"));
        setSequence((String) object.get("sequence"));
        setLocation((String) object.get("location"));
        setUsersCount((Integer) object.get("users_count"));

        mRecurType = ((Number) object.get("recur_type")).intValue();
        mRecurEndDate = (Date) object.get("recur_end");


        // todo init users (?)
    }


    public void setSequence(String sequence) {
        this.mSequence = sequence;
    }

    public String getmSequence() {
        return mSequence;
    }

    public void setmSequence(String mSequence) {
        this.mSequence = mSequence;
    }

    @Bindable
    public int getShowQuestionBar() {
//        User currentUser = AppApplication.getView().getCurrentUser();
//        for (Attendee attendee : mAttendees) {
//            if (currentUser.getId().equalsIgnoreCase(attendee.getUserId()))
//                return View.GONE;
//        }
        return View.VISIBLE;
    }

    /**
     * Update counters when user changes participation status. Used counts setter method,
     * since layout uses binding!
     *
     * @param prevPartstat
     * @param newPartstate
     */
    public void updateCountsByPrevstat(String prevPartstat, String newPartstate) {

        if (!TextUtils.isEmpty(prevPartstat)) {
            if (prevPartstat.equalsIgnoreCase(PartStatType.ACCEPTED)) {
                mCounts.coming = (mCounts.coming == 0) ? 0 : mCounts.coming - 1;
                mCounts.setComing(mCounts.coming);
            } else if (prevPartstat.equalsIgnoreCase(PartStatType.DECLINED)) {
                mCounts.notComing = (mCounts.notComing == 0) ? 0 : mCounts.notComing - 1;
                mCounts.setNotComing(mCounts.notComing);
            } else if (prevPartstat.equalsIgnoreCase(PartStatType.TENTATIVE)) {
                mCounts.tentative = (mCounts.tentative == 0) ? 0 : mCounts.tentative - 1;
                mCounts.setTentative(mCounts.tentative);
            } else if (prevPartstat.equalsIgnoreCase(PartStatType.NEEDS_ACTION)) {
                mCounts.needsAction = (mCounts.needsAction == 0) ? 0 : mCounts.needsAction - 1;
                mCounts.setNeedsAction(mCounts.needsAction);
            }
        } else {
            mCounts.needsAction = (mCounts.needsAction == 0) ? 0 : mCounts.needsAction - 1;
            mCounts.setNeedsAction(mCounts.needsAction);
        }

        if (newPartstate.equalsIgnoreCase(PartStatType.ACCEPTED)) {
            mCounts.setComing(++mCounts.coming);
        } else if (newPartstate.equalsIgnoreCase(PartStatType.DECLINED)) {
            mCounts.setNotComing(++mCounts.notComing);
        } else if (newPartstate.equalsIgnoreCase(PartStatType.TENTATIVE)) {
            mCounts.setTentative(++mCounts.tentative);
        }

    }

    @Bindable
    public String getCurrentUserPartStat() {
        Attendee currentAttendee = getCurrentAttendee();
        if (currentAttendee != null) {
            return currentAttendee.getPartStat();
        }

        return PartStatType.NEEDS_ACTION;
    }


    public void setCurrentUserPartStat(String partstat) {
        Attendee currentAttendee = getCurrentAttendee();
        if (currentAttendee == null) {
            String currentUserId = AppApplication.getContext().getCurrentUser().getId();

            currentAttendee = new Attendee();
            currentAttendee.setUserId(currentUserId);
            currentAttendee.setEventStartDate(getDtStartOccurrence());
            currentAttendee.setEventId(getId());
            addAttendee(currentAttendee);
        }

        currentAttendee.setPartStat(partstat);
        notifyPropertyChanged(BR.currentUserPartStat);
    }

    public Attendee getCurrentAttendee() {
        User currentUser = AppApplication.getContext().getCurrentUser();
        for (Attendee attendee : mAttendees) {
            if (currentUser.getId().equalsIgnoreCase(attendee.getUserId()))
                return attendee;
        }

        return null;
    }

    public void setFrequancy(int frequancy) {
        mRecurType = frequancy;
        notifyPropertyChanged(BR.frequencyText);
    }

    public
    @RecurType.Recurrency
    int getFrequency() {
        return mRecurType;
    }

    @Bindable
    public String getFrequencyText() {
        return RecurUtils.getFrequencyText(mRecurType, mRecurEndDate);
    }

//    @Bindable
//    public Drawable getEventPartStatResId(){
//        return UIUtils.getEventPartStatResId(getCurrentUserPartStat());
//    }


    //    public void test() {
//        java.util.Calendar today = java.util.Calendar.getInstance();
//        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
//        today.clear(java.util.Calendar.MINUTE);
//        today.clear(java.util.Calendar.SECOND);
//
//// create a period starting now with a duration of one (1) day..
//        Period period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
//        Filter filter = new Filter(new Rule[] { new PeriodRule(period) }, Filter.MATCH_ANY);
//
//        net.fortuna.ical4j.model.Calendar calendar;
//        List eventsToday = filter.filter(calendar.getComponents(Component.VEVENT));
//
//
//
////        Calendar instance = Calendar.getInstance();
////        net.fortuna.ical4j.model.Date date = new net.fortuna.ical4j.model.Date(instance.getTime());
////        PeriodList consumedTime = mEvent.getConsumedTime(date, date);
////        Log.d("AA", consumedTime.toString());
//    }

//     TODO change
//    public void setEventParameter(List<Parameter> parameters) {
//
//        mEventParameters = parameters;
//    }
//
//    public void setEventInstaceTemplateParameters(JSONArray parameters) {
//
//        put("event_instance_params", parameters);
//    }
//
//    public void setUserEventTemplateParameters(JSONArray parameters) {
//
//        put("user_event_template_params", parameters);
//    }
//
//    public void setUserEventInstanceTemplateParameters(JSONArray parameters) {
//        put("user_event_instance_template_params", parameters);
//    }


//    public void setRRule(RecurRule rrule) {
//        mRRule = rrule;
//        notifyPropertyChanged(BR.frequencyText);
//    }

//    public RecurRule getRRule() {
////        return mRRule;
////    }

//    public void initRRule() {
//
//        if (TextUtils.isEmpty(mRRuleString)) {
//            mRRule = null;
//        } else {
//            mRRule = new RecurRule(mRRuleString);
////            initCurrentNextPeriodList();
//        }
//    }

//    public void initCurrentNextPeriodList() {
//
//        Calendar today = Calendar.getInstance();
//
//        if (mRRule != null) {
//
//            try {
//
//                Period period = null;
//                int frequency = mRRule.getFrequency();
//                switch (frequency) {
//                    case RecurRule.HOURLY:
//                        period = new Period(new DateTime(today.getTime()), new Dur(0, 1, 0, 0));
//                        break;
//                    case RecurRule.DAILY:
//                        period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
//                        break;
//                    case RecurRule.WEEKLY:
//                        period = new Period(new DateTime(today.getTime()), new Dur(7, 0, 0, 0));
//                        break;
//                    case RecurRule.MONTHLY:
//                        period = new Period(new DateTime(today.getTime()), new Dur(30, 0, 0, 0));
//                        break;
//                }
//
//
//                VEvent vevent = new VEvent();
//
//                DtStart startTime = new DtStart(new DateTime(mDtStart));
//                vevent.getProperties().add(startTime);
//
//                DtEnd endTime = new DtEnd(new DateTime(mDtEnd));
//                vevent.getProperties().add(endTime);
//
//                RRule rRule = new RRule(mRRule.getValue());
//                vevent.getProperties().add(rRule);
//
//                mPeriodList = vevent.calculateRecurrenceSet(period);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }

//    public Date getRecurrnceStartDate() {
//        if (mPeriodList == null) return null;
//
//        Iterator iterator = mPeriodList.iterator();
//        Period next = (Period) iterator.next();
//        return next.getStart();
//    }

//    public Date getRecurrnceEndDate() {
//
//        if (mPeriodList == null) return null;
//
//        Iterator iterator = mPeriodList.iterator();
//        Period next = (Period) iterator.next();
//        return next.getEnd();
//    }

}
