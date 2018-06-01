package com.event2go.app.features.main;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.event2go.app.AppApplication;
import com.event2go.app.utils.NavUtils;
import com.event2go.app.R;
import com.parse.ParseUser;

/**
 * Created by Iliya Gogolev on 6/26/15.
 */
public class NavigationViewManager {

    MainActivity mMainActivity;
    DrawerLayout mDrawerLayout;

    public NavigationViewManager(MainActivity mainActivity, DrawerLayout drawerLayout, NavigationView navigationView) {
        mMainActivity = mainActivity;
        mDrawerLayout = drawerLayout;
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {

                        // unselected menu item
                        navigationView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                menuItem.setChecked(false);
                            }
                        }, 100);


                        mDrawerLayout.closeDrawers();

                        NavigationViewManager.this.onNavigationItemSelected(menuItem.getItemId());
                        return true;
                    }
                }
        );
    }

    private void onNavigationItemSelected(int menuId) {

        switch (menuId) {
//            case R.id.nav_home:
//                Fragment fragment = new CreateEventFragment();
//                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.content_frame, fragment).addToBackStack(null);
//                ft.commit();
//                break;

//            case R.id.nav_create_event:
//                NavUtils.showCreateEventFragmentActivity(mMainActivity);

//                EventUseCase eventController = new EventUseCase();
//                eventController.createEvent("Event_name_test", new OnServerResponse() {
//                    @Override
//                    public void onSuccess(Object result) {
//                        Log.d("aa", "done");
//                        Snackbar.make(findViewById(R.id.main_content), "Event created", Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
//
//                    }
//
//                    @Override
//                    public void onFail(Throwable e) {
//                        Log.d("aa", "fail");
//                    }
//                });
//                break;

//            case R.id.nav_find_event:
//                NavUtils.showSearchEventActivity(mMainActivity);
//                break;





//            case R.id.nav_add_tab_event:
//                mMainActivity.addEvent();
//                NavUtils.showSearchEventActivity(mMainActivity);
//                break;
//
//            case R.id.nav_switch_events:
//                mMainActivity.switchEvents();
//                break;
//
//            case R.id.nav_remove_event:
//                mMainActivity.removeEvent(0);
//                break;

//            case R.id.nav_events:
//                NavUtils.showEvents(mMainActivity);
//                break;
//
//            case R.id.nav_invite_person:
//                NavUtils.showInvitePersonActivity(mMainActivity);
//                break;

            case R.id.nav_notifications:
                if (AppApplication.getContext().getCurrentUser() == null) {
                    NavUtils.showLoginActivity(mMainActivity);
                } else {
                    NavUtils.showNotificationsActivity(mMainActivity);
                }
                break;
            case R.id.nav_conversation:
                if (AppApplication.getContext().getCurrentUser() == null) {
                    NavUtils.showLoginActivity(mMainActivity);
                } else {
                    NavUtils.showConversationActivity(mMainActivity);
                }
                break;

//            case R.id.nav_settings:
//                NavUtils.showUserDetailsActivity(mMainActivity);

//                Snackbar.make(mDrawerLayout, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                            }
//                        }).show();
//
//                break;
            case R.id.nav_singout:
                ParseUser.logOut();
                NavUtils.showSigninActivity(mMainActivity);
                break;

        }

    }
}
