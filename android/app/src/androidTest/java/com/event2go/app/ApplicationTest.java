package com.event2go.app;

import android.app.Application;
import android.test.ApplicationTestCase;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }


//    private void testVEvent() {
//        VEvent vEvent = new VEvent();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(java.util.Calendar.MONTHLY, java.util.Calendar.DECEMBER);
//        calendar.set(java.util.Calendar.DAY_OF_MONTH, 25);
//
//        Date time = calendar.getTime();
//        // initialise as an all-day event..
//        net.fortuna.ical4j.model.Date date = new net.fortuna.ical4j.model.Date(time);
//        VEvent christmas = new VEvent(date, "Christmas Day");
//
//        // Generate a UID for the event..
//        UidGenerator ug = null;
//        try {
//            ug = new UidGenerator("1");
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
//        christmas.getProperties().add(ug.generateUid());
//
//
//        // TEST FILTER TODAY EVENTS
//        java.util.Calendar today = java.util.Calendar.getInstance();
//        today.set(java.util.Calendar.HOUR_OF_DAY, 0);
//        today.clear(java.util.Calendar.MINUTE);
//        today.clear(java.util.Calendar.SECOND);
//
//// create a period starting now with a duration of one (1) day..
//        Period period = new Period(new DateTime(today.getTime()), new Dur(1, 0, 0, 0));
//        Rule rule = new PeriodRule(period);
//        Rule[] rules = {rule};
//        Filter filter = new Filter(rules, Filter.MATCH_ANY);
//
//    }
}