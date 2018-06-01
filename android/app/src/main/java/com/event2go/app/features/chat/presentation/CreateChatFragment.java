package com.event2go.app.features.chat.presentation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.event2go.app.R;
import com.event2go.app.databinding.FragmentCreateChatBinding;
import com.event2go.app.features.user.presentation.ContactsRecyclerFragment;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.features.chat.data.Contact;
import com.event2go.app.utils.NavUtils;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class CreateChatFragment extends BaseFragment implements ContactsRecyclerFragment.ContactClickedHandler {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // todo iliya base
//        ActionBar actionBar = getSupportedActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(getString(R.string.messages));
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_chat;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentCreateChatBinding bind = DataBindingUtil.bind(view);

        ContactsRecyclerFragment contactsRecyclerFragment = new ContactsRecyclerFragment();
        contactsRecyclerFragment.setContactClickedHandler(this);

        getChildFragmentManager().beginTransaction().add(R.id.frame_layout_container, contactsRecyclerFragment).commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.event_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContactClicked(Contact contact) {
        NavUtils.showOpenChatActivity(getContext(), contact);
    }
}
