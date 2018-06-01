package com.event2go.app.features.chat.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.event2go.app.features.chat.presentation.ChatsRecyclerFragment;
import com.event2go.app.features.user.presentation.ContactsRecyclerFragment;
import com.event2go.base.presentation.fragment.BaseTabbedFragment;
import com.event2go.app.R;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class MessagesTabFragment extends BaseTabbedFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo iliya base
//        ActionBar actionBar = getSupportedActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(getString(R.string.messages));
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
//        }
    }

    @Override
    protected Fragment getTabAdapterItem(int position) {
        if (position == 0) return new ChatsRecyclerFragment();
        return new ContactsRecyclerFragment();
    }

    @Override
    protected int getTabCount() {
        return 2;
    }

    @Override
    protected String getTabPageTitle(int position) {

        if (position == 0) return getString(R.string.chat);

        return getString(R.string.contacts);
    }

    @Override
    public int getToolbarTitle() {
        return R.string.messages;
    }
}
