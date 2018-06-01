package com.event2go.app.features.user.presentation;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.features.chat.data.Contact;

/**
 * Created by Iliya Gogolev on 3/10/16.
 */
public class ContactViewModel extends BaseRecyclerViewModel<Contact> {

    public ContactViewModel() {
        super(new ContactRecyclerViewAdapter());
    }

//    @Override
//    protected BaseRecyclerAdapter<Contact> getRecyclerAdapter() {
//        return new ContactRecyclerViewAdapter();
//    }
//
//    @Override
//    protected void refreshData(final OnLoadCompleteCallback<Contact> callback) {
//        Observable<List<Contact>> userCall = MessagesProvider.getInstance().getContacts();
//        userCall.subscribe(new Action1<List<Contact>>() {
//
//            @Override
//            public void call(List<Contact> data) {
//                callback.onSuccess(data);
//            }
//        }, new Action1<Throwable>() {
//            @Override
//            public void call(Throwable t) {
//
//                callback.onFailure(t);
//            }
//        });
//    }

}
