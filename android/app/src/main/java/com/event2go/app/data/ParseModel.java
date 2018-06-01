package com.event2go.app.data;

import com.event2go.base.data.Parsable;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.event2go.base.data.BaseParseObject;

/**
 * Created by Iliya Gogolev on 6/26/15.
 */
public class ParseModel {

    public static final String CLASS_USER = "_User";
    public static final String CLASS_EVENT = "Event";
    public static final String RECURRING_EVENT = "Recurring";
    public static final String CLASS_PARAMETER = "Parameter";
    public static final String CLASS_INVITE = "Invite";
    public static final String CLASS_NOTIFICATION = "Notification";
    public static final String CLASS_CHAT = "Chat";
    public static final String CLASS_CONTACT = "Contact";

    public static final String CLASS_VALARM = "Alarm";
    public static final String CLASS_ATTENDEE = "Attendee";

    public static final String RELATION_USER = "user";
    public static final String RELATION_USERS = "users";
//    public static final String RELATION_ATTENDEES = "attendees";
    public static final String RELATION_EVENT = "event";
    public static final String RELATION_RECURRING_EVENT = "recurring_event";


    public static List<BaseParseObject> convertFromParseObject(List<? extends ParseObject> list, Class<? extends Parsable> objClass) {
        List result = new ArrayList();

        for (ParseObject obj : list) {

            try {
                Parsable modelObject = objClass.newInstance();
                modelObject.readFromParseObject(obj);
                result.add(modelObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static List<? extends BaseParseObject> convert(List<? extends ParseObject> list, Class<? extends BaseParseObject> objClass) {
        List result = new ArrayList();

        for (ParseObject obj : list) {
            try {
                BaseParseObject modelObject = objClass.newInstance();
                modelObject.setParseObject(obj);
                modelObject.readParseObject(obj);

                result.add(modelObject);

            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static BaseParseObject convertObject(ParseObject obj, Class<? extends BaseParseObject> objClass) {

        try {
            BaseParseObject modelObject = objClass.newInstance();
            modelObject.setParseObject(obj);
            modelObject.readParseObject(obj);
            return modelObject;

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ParseObject copy(ParseObject sourceObject, ParseObject targetObject) {
        for (Iterator it = sourceObject.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            targetObject.put(key.toString(), sourceObject.get(key.toString()));
        }

        return targetObject;
    }

// todo
//    public static ArrayList<ParseUser> convertToParse(List<User> users) {
//        List<ParseUser> result = new ArrayList<ParseUser>();
//        for (User user : users) {
//            result.add(user.getParseUser());
//        }
//
//        return result;
//    }

}
