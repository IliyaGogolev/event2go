package com.event2go.app.features.chat.data.repository;

import com.event2go.base.utils.Logger;
import com.event2go.app.features.chat.data.Chat;
import com.event2go.app.features.chat.data.ChatMessage;
import com.event2go.app.features.chat.data.Contact;
import com.event2go.app.data.ParseModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class MessagesProvider {

    private static final String TAG = MessagesProvider.class.toString();

    private final Logger logger = new Logger();

    private static MessagesProvider instance = new MessagesProvider();

    private MessagesProvider() {
    }

    public static MessagesProvider getInstance() {
        return instance;
    }


    public Observable<List<Chat>> getChats() {

        Observable<List<Chat>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<Chat>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Chat>> subscriber) throws Exception {


                subscriber.onNext(createTestChatDataList());
                subscriber.onComplete();

                /*ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.findInBackground(new FindCallback<ParseObject>() {

                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            logger.d(TAG, "User search result " + list.size() + ", " + list);
                            //                            Observable.from(list).subscribe(p->)

                            List<Chat> result = (List<Chat>) (List<?>) ParseModel.convertFromParseObject(list, User.class);
                            Collections.sort(result, new Comparator<Chat>() {
                                @Override
                                public int compare(Chat lhs, Chat rhs) {
                                    return lhs.getId().compareTo(rhs.getId());
                                }
                            });
                            subscriber.onNext(result);
                            subscriber.onCompleted();
                        } else {
                            logger.d(TAG, "getUserContains Error " + e);
                            subscriber.onError(e);
                        }
                    }
                });*/
            }
        });

        return apiResultStream;
    }

    public Observable<List<Contact>> getContacts() {

        Observable<List<Contact>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<Contact>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<Contact>> subscriber) throws Exception {


                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (e == null) {

                            List<Contact> result = (List<Contact>) (List<?>) ParseModel.convertFromParseObject(list, Contact.class);
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

    private List<Chat> createTestChatDataList() {
        Chat chat1 = new Chat();
        chat1.setId("1");
        chat1.setLastMessage("We have found you.");
        chat1.setName("Fedor Petrov");
        chat1.setLastTime(new Date());

        List<Chat> chatList = new ArrayList<>();
        chatList.add(chat1);

        return chatList;
    }

    private List<ChatMessage> createTestChatMessagesDataList() {

        List<ChatMessage> messages = new ArrayList<>();

        ChatMessage msg = new ChatMessage();
        msg.setId(1);
        msg.setIsMe(false);
        msg.setMessage("Hi!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(3);
        msg.setIsMe(false);
        msg.setMessage("How are you doing?");
        msg.setDate(new Date());
        messages.add(msg);


        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(3);
        msg.setIsMe(false);
        msg.setMessage("How are you doing?");
        msg.setDate(new Date());
        messages.add(msg);


        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(2);
        msg.setIsMe(true);
        msg.setMessage("Hello!");
        msg.setDate(new Date());
        messages.add(msg);

        msg = new ChatMessage();
        msg.setId(3);
        msg.setIsMe(false);
        msg.setMessage("How are you doing?");
        msg.setDate(new Date());
        messages.add(msg);


        return messages;
    }

    public Observable<List<ChatMessage>> getChatMessages() {
        Observable<List<ChatMessage>> apiResultStream = Observable.create(new ObservableOnSubscribe<List<ChatMessage>>() {
            @Override
            public void subscribe(final ObservableEmitter<List<ChatMessage>> subscriber) throws Exception {

                subscriber.onNext(createTestChatMessagesDataList());
                subscriber.onComplete();
            }
        });
        return apiResultStream;
    }
}
