package com.event2go.app.features.event.domain;

import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.repository.EventsDataProvider;
import com.parse.ParseObject;
import java.util.List;

import io.reactivex.Observable;

//import rx.parse.ParseObservable;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */
public class EventUseCase {


////    public Observable createEvent(String title, Calendar startDate, Calendar endDate, int repeatType, String location, int acceptReminderAmount, int acceptReminderDuration) {
////
////        Event event = new Event();
////        event.setTitle(title);
////        event.setStartTime(startDate);
////        event.setEndTime(endDate);
////        event.setRepeatType(repeatType);
////        event.setLocation(location);
////        event.setNotificationReminderDurationAmount(acceptReminderAmount);
////        event.setNotificationReminderDurationType(acceptReminderDuration);
////        return EventsDataProvider.getInstance().createEvent(event);
////
////    }
//
//    public Observable<Throwable>  save´Event(Event event) {
//        return EventsDataProvider.getInstance().createEvent(event);
//    }

    public Observable<List<ParseObject>> getEventsContains(String substring) {

        return EventsDataProvider.getInstance().getEventsContains(substring);
    }

    public Observable<Event> getEventById(String eventId) {

        return EventsDataProvider.getInstance().getFeedEventById(eventId);
    }

//    public Observable<List<Event>> getAllEvents() {
//
//        return EventsDataProvider.getInstance().getFeedEvents();
////        return DataProvider.getInstance().getEventsContains("occ");
//    }

    public Observable deleteEvent(Event mEvent) {
        return EventsDataProvider.getInstance().removeEvent(mEvent);
    }
}
