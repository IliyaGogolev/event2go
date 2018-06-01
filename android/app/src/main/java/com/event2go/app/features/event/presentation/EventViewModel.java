package com.event2go.app.features.event.presentation;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.event2go.app.AppApplication;
import com.event2go.app.BR;
import com.event2go.app.R;
import com.event2go.app.features.event.data.repository.EventsDataProvider;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.Parameter;
import com.event2go.app.features.event.data.RecurType;
import com.event2go.app.features.event.data.RecurUtils;

import net.fortuna.ical4j.model.Dur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import io.reactivex.Observable;

/**
 * Created by Iliya Gogolev on 8/14/15.
 */
public class EventViewModel extends BaseObservable {

    private Resources mResources;
    public int isVisibleAddInstanceParam = View.VISIBLE;

    private String test = "Add global parameter";

    private Event mEvent;

    // template parameteres will be stored in event table as json. Then event or playar parameteres should be displayed,
    // first it will use template date and override it by data from EventParams/UserParams accordingly
    private List<Parameter> mEventParameters = new ArrayList<Parameter>();
    private List<Parameter> mEventInstanceTemplateParameters = new ArrayList<Parameter>();
    private List<Parameter> mUserEventTemplateParameters = new ArrayList<Parameter>();
    private List<Parameter> mUserEventInstanceTemplateParameter = new ArrayList<Parameter>();
    //    private int mRepeatType;
    private boolean mIsEditMode;

    public EventViewModel(Event event) {
        mEvent = event;
    }

    public EventViewModel(Resources resources, boolean isEditMode) {

        mResources = resources;
        mIsEditMode = isEditMode;

        if (isEditMode) {
            mEvent = AppApplication.getContext().getCurrentEvent();
        } else {


            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(Calendar.HOUR_OF_DAY, startCalendar.get(Calendar.HOUR_OF_DAY) + 1);
            startCalendar.set(Calendar.MINUTE, 0);

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(Calendar.HOUR_OF_DAY, endCalendar.get(Calendar.HOUR_OF_DAY) + 2);
            endCalendar.set(Calendar.MINUTE, 0);

            mEvent = new Event();
            mEvent.setStartTime(startCalendar.getTime());
            mEvent.setEndTime(endCalendar.getTime());
//            mEvent.setRRule(new RecurUtils());
            mEvent.setLocation(resources.getString(R.string.location));
            //        mEvent.setNotificationReminderDurationAmount(2);
            //        mEvent.setNotificationReminderDurationType(DurUnits.DAILY);

        }

//        Dur dur = mEvent.getRsvpAlarmDuration();
//        setRsvpReminderAlarmDuration(dur);

    }

    public Event getEvent() {
        return mEvent;
    }

    @Bindable
    public int getIsVisibleAddInstanceParam() {
        return isVisibleAddInstanceParam;
    }

    public void setIsVisibleAddInstanceParam(int isVisibleAddInstanceParam) {
        this.isVisibleAddInstanceParam = isVisibleAddInstanceParam;
        notifyPropertyChanged(BR.isVisibleAddInstanceParam);
    }

    public void setRsvpReminderAlarmDuration(Dur dur) {
//        if (dur.getHours() != 0) {
//
//            mReminderAmount = dur.getHours();
//            mReminderDurationType = DurUnits.HOURLY;
//
//        } else if (dur.getDays() != 0) {
//
//            mReminderAmount = dur.getDays();
//            mReminderDurationType = DurUnits.DAILY;
//
//        } else if (dur.getWeeks() != 0) {
//            mReminderAmount = dur.getWeeks();
//            mReminderDurationType = DurUnits.WEEKLY;
//        }

        mEvent.setRsvpAlarmDur(dur);
        notifyPropertyChanged(BR.rsvmReminderAlertDuration);
    }

    @Bindable
    public String getRsvmReminderAlertDuration() {

        Dur dur = mEvent.getRsvpAlarmDuration();
        int reminderAmount = getRSVPAlertValue(dur);
        int durType = getAlertDurUnit(dur);

        SortedMap<Integer, String> values;
        if (reminderAmount == 1) {
            values = RecurUtils.getAlertDurUnitNames(true);
        } else {
            values = RecurUtils.getAlertDurUnitNames(false);
        }

        String s = values.get(durType);

        return String.format(AppApplication.getContext().getString(R.string.create_event_rsvp_alarm), reminderAmount + " " + s.toLowerCase());
    }

    public static
    @RecurType.Recurrency
    int getAlertDurUnit(Dur dur) {

        if (dur.getHours() != 0) return RecurType.HOURLY;
        if (dur.getDays() != 0) return RecurType.DAILY;
        if (dur.getWeeks() != 0) return RecurType.WEEKLY;

        return RecurType.NONE;
    }


    public static int getRSVPAlertValue(Dur dur) {

        if (dur.getHours() != 0) return dur.getHours();
        if (dur.getDays() != 0) return dur.getDays();
        if (dur.getWeeks() != 0) return dur.getWeeks();

        return 0;
    }

    public Observable<Throwable> save() {

        // TODO
//        mEvent.setNotificationReminderDurationAmount(mReminderAmount);
//        mEvent.setNotificationReminderDurationType(mReminderDurationType);
//
//        mEvent.setEventParameter(mEventParameters);
//        mEvent.setEventInstaceTemplateParameters(convertParametersToJson(mEventInstanceTemplateParameters));
//        mEvent.setUserEventTemplateParameters(convertParametersToJson(mUserEventTemplateParameters));
//        mEvent.setUserEventInstanceTemplateParameters(convertParametersToJson(mUserEventInstanceTemplateParameter));

        return EventsDataProvider.getInstance().createEvent(mEvent);
    }

    private JSONArray convertParametersToJson(List<Parameter> params) {

        JSONArray array = new JSONArray();
        for (Parameter parameter : params) {
            JSONObject newParameter = new JSONObject();
            try {

                newParameter.put("name", parameter.getName());
                newParameter.put("value", parameter.getValue());
                array.put(newParameter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    public void addParameter(Parameter parameter, int paramType) {

        switch (paramType) {
            case Event.PARAM_TYPE_EVENT:
                parameter.setEvent(mEvent);
                mEventParameters.add(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_INSTANCE_TEMPLATE:
                mEventInstanceTemplateParameters.add(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_PLAYER_TEMPLATE:
                mUserEventTemplateParameters.add(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_PLAYER_INSTANCE_TEMPLATE:
                mUserEventInstanceTemplateParameter.add(parameter);
                break;
        }
    }

    public void removeParameter(Parameter parameter, int paramType) {

        switch (paramType) {
            case Event.PARAM_TYPE_EVENT:
                mEventParameters.remove(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_INSTANCE_TEMPLATE:
                mEventInstanceTemplateParameters.remove(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_PLAYER_TEMPLATE:
                mUserEventTemplateParameters.remove(parameter);
                break;
            case Event.PARAM_TYPE_EVENT_PLAYER_INSTANCE_TEMPLATE:
                mUserEventInstanceTemplateParameter.remove(parameter);
                break;
        }
    }

    public void setEventFrequency(@RecurType.Recurrency int frequency) {
        mEvent.setFrequancy(frequency);
    }

//    public void setRRule(RRUle rrule) {
//
//        mEvent.setRRule(rrule);
//        setIsVisibleAddInstanceParam(TextUtils.isEmpty(rrule) ? View.GONE : View.VISIBLE);
//
//    }

    public boolean isEditMode() {
        return mIsEditMode;
    }

    public void setStartTime(Date startDate) {
        mEvent.setStartTime(startDate);
    }

    public void setEndTime(Date endDate) {
        mEvent.setEndTime(endDate);
    }

    public Dur getRsvmAlertDuration() {
        return mEvent.getRsvpAlarmDuration();
    }

    public void setSummary(String text) {
        mEvent.setSummary(text);
    }

    public void setLocation(String location) {
        mEvent.setLocation(location);
    }
}
