package com.event2go.app.features.event.data.repository;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.event2go.app.features.user.data.repository.UserDataProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.event2go.base.utils.JsonUtils;
import com.event2go.base.utils.Logger;
import com.event2go.app.AppApplication;
import com.event2go.app.features.event.data.Attendee;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.Parameter;
import com.event2go.app.data.ParseModel;
import com.event2go.app.features.user.data.User;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 6/30/15.
 */
public class EventsDataProvider {

    private static final String TAG = UserDataProvider.class.toString();

    private final Logger logger = new Logger();

    private static EventsDataProvider instance = new EventsDataProvider();

    private EventsDataProvider() {
    }

    public static EventsDataProvider getInstance() {
        return instance;
    }

    public Observable<List<ParseObject>> getEventsContains(final String substring) {

        Observable<List<ParseObject>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<ParseObject>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<ParseObject>> subscriber) throws Exception {


                if (TextUtils.isEmpty(substring)) {
                    subscriber.onNext(new ArrayList<ParseObject>());
                    subscriber.onComplete();
                } else {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseModel.CLASS_EVENT);
//                    query.whereContains("name", substring);
                    query.whereMatches("name", "(" + substring + ")", "i"); // case insensitive

                    query.findInBackground(new FindCallback<ParseObject>() {

                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {

                                Collections.sort(list, new Comparator<ParseObject>() {
                                    @Override
                                    public int compare(ParseObject lhs, ParseObject rhs) {
                                        return ((String) lhs.get("name")).compareTo((String) rhs.get("name"));
                                    }
                                });
                                subscriber.onNext(list);
                                subscriber.onComplete();
                            } else {
                                subscriber.onError(e);
                            }
                        }
                    });
                }
            }
        });

        return apiResultStream;
    }

    public Observable<List<Attendee>> getEventAttendees(final List<Event> events) {

        Observable<List<Attendee>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<Attendee>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Attendee>> subscriber) throws Exception {


                List<ParseQuery<ParseObject>> list = new ArrayList<ParseQuery<ParseObject>>();
                for (Event event : events) {


                    ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseModel.CLASS_ATTENDEE);
                    query.whereEqualTo("eventId", event.getId());

                    Date startDate;
                    if (!event.isRecur()) {
                        startDate = event.getStartDate();
                    } else {
                        startDate = event.getDtStartOccurrence();
                    }

                    query.whereEqualTo("event_start_date", startDate);
//                    query.include("user");
                    list.add(query);
//                query.findInBackground(new FindCallback<ParseObject>() {
//
//                    @Override
//                    public void done(List<ParseObject> list, ParseException e) {
//                        if (e == null) {
//                            logger.d(TAG, "Event get by id result " + list.size() + ", " + list);
//
//                            List<Attendee> result = (List<Attendee>) (List<?>) ParseModel.convertFromParseObject(list, Event.class);
//                            if (!result.isEmpty()) {
//                                subscriber.onNext(result);
//                            }
//                            subscriber.onCompleted();
//                        } else {
//                            logger.d(TAG, "getFeedEventById Error " + e);
//                            subscriber.onError(e);
//                        }
//                    }
//                });


                }

                if (list.size() > 0) {
                    ParseQuery mainQuery = ParseQuery.or(list);//.orderByAscending("eventId");
                    mainQuery.findInBackground(new FindCallback() {

                        @Override
                        public void done(Object o, Throwable throwable) {
                            if (throwable != null) {
                                // sort
                            }

                            subscriber.onComplete();
                        }

                        @Override
                        public void done(List objects, ParseException e) {
                            if (e != null) {

                                // sort

                            }

                            subscriber.onComplete();
                        }
                    });


                } else {
                    subscriber.onComplete();
                }
            }
        });

        return apiResultStream;
    }

    public Observable<List<Event>> getFeedEvents() {

        Observable<List<Event>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<Event>>() {
                                                                        @Override
                                                                        public void subscribe(final ObservableEmitter<List<Event>> subscriber) throws Exception {

                    final HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("test", true);

                    ParseCloud.callFunctionInBackground("getFeedEvents", params, new FunctionCallback<List<ParseObject>>() {

                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {

                                List<Event> events = new ArrayList<>();
                                for(ParseObject data : list) {
                                    Event event  = new Event();
                                    event.readFromParseObject(data);
                                    events.add(event);
                                }

                                subscriber.onNext(events);
                                subscriber.onComplete();


                            } else {
                                subscriber.onError(e);
                            }
                        }
                    });
                }
            }
        );

        return apiResultStream;
    }

    public Observable createEvent(final Event event) {

        event.addUser(AppApplication.getContext().getCurrentUser());

        final ParseObject parseEvent = new ParseObject(event.getParseCasssName());

        event.writeToParseObject(parseEvent);

//        final ParseObject parseEvent = event.toParseObject();
//        if (event.getRepeatType() != DurationUnit.NONE) {
//            RecurringEvent recurringEvent = RecurringEvent.createFromEvent(event);
//            event.addRecurringEvent(recurringEvent);
//        }

        List<Parameter> eventParameters = event.getEventParameters();

        final List<ParseObject> objectsToSave = new ArrayList<>();
        objectsToSave.add(parseEvent);

        // todo add parameters
//        List<ParseObject> eventParametersParseObjects = convertParametersToParseObjects(eventParameters);
//        objectsToSave.addAll(eventParametersParseObjects);

        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {

                ParseObject.saveAllInBackground(objectsToSave, new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            event.setId(parseEvent.getObjectId());
                            ParseObject alarmParseObject = event.getRsvpAlarmParseObject();
                            alarmParseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        if (subscriber != null) {
                                            subscriber.onNext(null);
                                            subscriber.onComplete();
                                        }
                                    } else {
                                        subscriber.onError(e);
                                    }


                                }
                            });
//                                    subscriber.onNext(null);
//                            subscriber.onCompleted();

                        } else {
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });

        return apiResultStream;
    }

    private List<ParseObject> convertParametersToParseObjects(List<Parameter> eventParameters) {
        List<ParseObject> result = new ArrayList<>();
        for (Parameter param : eventParameters) {
            result.add(param.toParseObject());
        }
        return result;
    }

    public Observable removeEvent(final Event event) {

        final ParseObject object = new ParseObject(event.getParseCasssName());
        event.writeToParseObject(object);
        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {


                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            subscriber.onNext(null);
                            subscriber.onComplete();

                        } else {
                            subscriber.onError(e);
                        }

                    }
                });
            }
        });

        return apiResultStream;
    }

    public Observable<Event> getFeedEventById(final String eventId) {
        Observable<Event> apiResultStream = Observable.create(new ObservableOnSubscribe<Event>() {
                                                                  @Override
                                                                  public void subscribe(final ObservableEmitter<Event> subscriber) throws Exception {


                                                                      final HashMap<String, Object> params = new HashMap<String, Object>();
                                                                      params.put("eventId", eventId);

                                                                      ParseCloud.callFunctionInBackground("getFeedEvent", params, new FunctionCallback<HashMap>() {

                                                                          @Override
                                                                          public void done(HashMap eventHashMap, ParseException e) {
                                                                              if (e == null) {

                                                                                  Gson gson = new GsonBuilder().setDateFormat("EEE MMM dd HH':'mm':'ss z yyyy").create();
                                                                                  Event event = null;
                                                                                  try {
                                                                                      JSONObject jsonObject = JsonUtils.mapToJson(eventHashMap);
                                                                                      event = gson.fromJson(jsonObject.toString(), Event.class);
                                                                                      // workaround of post processor
                                                                                      //                              event.initRRule();
                                                                                  } catch (JsonSyntaxException jsonEx) {
                                                                                      Log.e(TAG, jsonEx.getMessage());
                                                                                  }

                                                                                  subscriber.onNext(event);
                                                                                  subscriber.onComplete();


                                                                              } else {
                                                                                  subscriber.onError(e);
                                                                              }
                                                                          }
                                                                      });
                                                                  }
                                                              }
        );

        return apiResultStream;
    }

    public Observable addUserToEvent(final Event event, final User user) {

        Observable apiResultStream = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(final ObservableEmitter<Object> subscriber) throws Exception {

                event.addUser(user);
                ParseObject object = new ParseObject(event.getParseCasssName());

                event.writeToParseObject(object);
                object.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            subscriber.onNext(null);
                            subscriber.onComplete();
                        } else {
                            subscriber.onError(e);
                        }
                    }
                });

            }
        });

        return apiResultStream;

    }
}

