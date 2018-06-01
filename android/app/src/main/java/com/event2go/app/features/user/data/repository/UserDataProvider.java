package com.event2go.app.features.user.data.repository;

import android.text.TextUtils;

import com.event2go.base.utils.Logger;
import com.event2go.app.data.ParseModel;
import com.event2go.app.features.user.data.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;
import bolts.TaskCompletionSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 6/12/15.
 */
public class UserDataProvider {

    private static final String TAG = UserDataProvider.class.toString();

    private final Logger logger = new Logger();

    private static UserDataProvider instance = new UserDataProvider();

    private UserDataProvider() {
    }

    public static UserDataProvider getInstance() {
        return instance;
    }


    public Observable<List<User>> getUserContains(final String substring) {

        Observable<List<User>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<User>> subscriber) throws Exception {

                if (TextUtils.isEmpty(substring)) {
                    subscriber.onNext(new ArrayList<User>());
                    subscriber.onComplete();
                } else {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.whereMatches("name", "(" + substring + ")", "i"); // case insensitive
                    query.findInBackground(new FindCallback<ParseObject>() {

                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                Logger.d("User search result " + list.size() + ", " + list);
                                //                            Observable.from(list).subscribe(p->)

                                List<User> result = (List<User>) (List<?>) ParseModel.convertFromParseObject(list, User.class);
                                Collections.sort(result, new Comparator<User>() {
                                    @Override
                                    public int compare(User lhs, User rhs) {
                                        return lhs.getName().compareTo(rhs.getName());
                                    }
                                });
                                subscriber.onNext(result);
                                subscriber.onComplete();
                            } else {
                                Logger.d("getUserContains Error " + e);
                                subscriber.onError(e);
                            }
                        }
                    });
                }
            }
        });

        return apiResultStream;
    }

    public Observable<User> getUserById(final String userId) {
        Observable<User> apiResultStream = Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(final ObservableEmitter<User> subscriber) throws Exception {

                if (TextUtils.isEmpty(userId)) {
                    subscriber.onComplete();
                } else {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.whereEqualTo("objectId", userId);
                    query.findInBackground(new FindCallback<ParseObject>() {

                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            if (e == null) {
                                Logger.d("User get by id result " + list.size() + ", " + list);

                                List<User> result = (List<User>) (List<?>) ParseModel.convertFromParseObject(list, User.class);
                                if (!result.isEmpty()) {
                                    subscriber.onNext(result.get(0));
                                }
                                subscriber.onComplete();
                            } else {
                                Logger.d("getUserById Error " + e);
                                subscriber.onError(e);
                            }
                        }
                    });
                }
            }
        });

        return apiResultStream;
    }

    public Observable<List<User>> getAllUsers() {

        Observable<List<User>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<User>> subscriber) throws Exception {

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (e == null) {

                            List<User> result = (List<User>) (List<?>) ParseModel.convertFromParseObject(list, User.class);
//                            Collections.sort(result, new Comparator<User>() {
//                                @Override
//                                public int compare(User lhs, User rhs) {
//                                    return lhs.getName().compareTo(rhs.getName());
//                                }
//                            });
                            subscriber.onNext(result);
                            subscriber.onComplete();


                        } else {
                            Logger.d("getUserContains Error " + e);
//                            subscriber.onError(e);
                        }
                    }
                });
            }
        });

        return apiResultStream;
    }


//    public void getAllUsers(final OnServerResponse listener) {
//
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.findInBackground(new FindCallback<ParseUser>() {
//            @Override
//            public void done(List<ParseUser> list, ParseException e) {
//                if (e == null) {
//
//                    List<User> result = (List<User>) ParseModel.convert(list, User.class);
//                    listener.onSuccess(result);
//
//                } else {
//                    listener.onFail(e);
//                }
//            }
//        });
//    }

//    public Observable<List<ParseObject>> getEvent(final String id) {
//
//        Observable<List<ParseObject>> apiResultStream = Observable.create(new Observable.OnSubscribe<List<ParseObject>>() {
//            @Override
//            public void call(final Subscriber<? super List<ParseObject>> subscriber) {
//
//                if (TextUtils.isEmpty(id)) {
//                    subscriber.onNext(new ArrayList<ParseObject>());
//                    subscriber.onCompleted();
//                } else {
//
//                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
////                    query.whereContains("name", substring);
//                    query.whereContains("name", substring); // case insensitive
//                    query.findInBackground(new FindCallback<ParseObject>() {
//
//                        @Override
//                        public void done(List<ParseObject> list, ParseException e) {
//                            if (e == null) {
//                                Logger.d( "User search result "+ list.size()+", "+list);
//                                //                            Observable.from(list).subscribe(p->)
//
//                                Collections.sort(list, new Comparator<ParseObject>() {
//                                    @Override
//                                    public int compare(ParseObject lhs, ParseObject rhs) {
//                                        return ((String) lhs.get("name")).compareTo((String) rhs.get("name"));
//                                    }
//                                });
//                                subscriber.onNext(list);
//                                subscriber.onCompleted();
//                            } else {
//                                Logger.d( "getUserContains Error "+ e);
//                                subscriber.onError(e);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//
//        return apiResultStream;
//    }


    public Observable<List<ParseObject>> getUsers() {

        Observable<List<ParseObject>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<ParseObject>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<ParseObject>> subscriber) throws Exception {

                ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseModel.CLASS_USER);
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            Logger.d("getUsers " + list.size() + ", " + list);
//                            Observable.from(list).subscribe(p->)

                            Collections.sort(list, new Comparator<ParseObject>() {
                                @Override
                                public int compare(ParseObject lhs, ParseObject rhs) {
                                    return ((String) lhs.get("name")).compareTo((String) rhs.get("name"));
                                }
                            });
                            subscriber.onNext(list);
                            subscriber.onComplete();
                        } else {
                            Logger.d("getUsers Error " + e);
                            subscriber.onError(e);
                        }
                    }
                });
            }
        });

        return apiResultStream;
    }

    public static Task updateCurrentUser(final User currentUser) {

        final TaskCompletionSource tcs = new TaskCompletionSource<>();

        Task.call(new Callable<Object>() {
            @Override
            public Object call() throws Exception {

                ParseUser parseUser = ParseUser.getCurrentUser();
                currentUser.writeToParseObject(parseUser);

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            tcs.setResult(null);
                        } else {
                            tcs.setError(e);
                        }
                    }
                });

                return new Object();
            }
        });

        return tcs.getTask();

    }
}
