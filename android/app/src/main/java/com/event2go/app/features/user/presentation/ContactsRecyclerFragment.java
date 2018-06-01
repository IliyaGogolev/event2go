package com.event2go.app.features.user.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.app.features.chat.data.Contact;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class ContactsRecyclerFragment extends BaseRecyclerFragment<Contact> {

    public interface ContactClickedHandler {
        void onContactClicked(Contact contact);
    }

    private ContactViewModel mContactViewModel;

    private ContactClickedHandler contactClickedHandler;

    @NonNull
    @Override
    protected BaseRecyclerViewModel<Contact> getRecyclerViewModel() {
        mContactViewModel = new ContactViewModel();
        return mContactViewModel;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setSwipeRefreshEnabled(false);
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);

        if (contactClickedHandler != null) {
            Contact contact = getAdapter().getItem(position);
            contactClickedHandler.onContactClicked(contact);
        }
    }

    public void setContactClickedHandler(ContactClickedHandler handler) {
        this.contactClickedHandler = handler;
    }
}
