package com.event2go.app.features.user.presentation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;

import com.event2go.base.presentation.activity.BaseFragmentActivity;
import com.event2go.app.R;

/**
 * Created by Iliya Gogolev on 6/25/15.
 */
public class InviteUserActivity extends BaseFragmentActivity {

    private SearchView mSearchView;
    private InviteUsersRecycleViewFragment mInviteUserFragment;
    private Handler h = new Handler();
    private Runnable r;

    @Override
    protected Fragment createFragment(Bundle savedInstanceState) {
        mInviteUserFragment = new InviteUsersRecycleViewFragment();
        return mInviteUserFragment;
    }

    @Override
    protected String getToolbarTitle() {
        return null;
    }

    protected boolean isDisplayShowToolbarTitleEnabled() {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_user, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search); // get my MenuItem with placeholder submenu
        MenuItemCompat.expandActionView(searchMenuItem);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                onBackPressed();
                return true;
            }
        });

        searchMenuItem.expandActionView(); // Expand the search menu item in order to show by default the query
        mSearchView = (SearchView) searchMenuItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_user_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                deferredSearch(newText);
                return false;
            }
        });

        AutoCompleteTextView searchText = (AutoCompleteTextView) mSearchView.findViewById(R.id.search_src_text);
        searchText.setHintTextColor(getResources().getColor(android.R.color.white));
        searchText.setTextColor(getResources().getColor(android.R.color.white));

        return true;
    }

    private void deferredSearch(final String query) {
        if (r != null) {
            h.removeCallbacks(r);
        }

        if (query.length() > 1) {
            r = new Runnable() {

                @Override
                public void run() {
                    mInviteUserFragment.showUsersContains(query);
                }
            };
            h.postDelayed(r, 500);
        } else {
            mInviteUserFragment.showUsersContains("");
        }
    }
}