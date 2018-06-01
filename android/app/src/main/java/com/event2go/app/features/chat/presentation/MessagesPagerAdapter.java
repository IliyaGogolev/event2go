package com.event2go.app.features.chat.presentation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.event2go.app.features.chat.presentation.ChatsRecyclerFragment;
import com.event2go.app.features.user.presentation.ContactsRecyclerFragment;

/**
 * Created by Iliya Gogolev on 3/10/2016.
 */
public class MessagesPagerAdapter extends FragmentPagerAdapter {

    public MessagesPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ChatsRecyclerFragment();
        } else {
            fragment = new ContactsRecyclerFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
