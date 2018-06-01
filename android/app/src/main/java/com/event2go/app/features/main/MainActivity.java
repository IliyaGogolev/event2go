package com.event2go.app.features.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.event2go.base.presentation.activity.BaseActivity;
import com.event2go.app.AppApplication;
import com.event2go.app.features.event.presentation.EventsRecyclerFragment;
import com.event2go.app.utils.NavUtils;
import com.event2go.app.R;
import com.event2go.app.features.user.data.User;


public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private EventsRecyclerFragment mEventListFragment;
    private NavigationViewManager mNavigationViewManager;
//    private Adapter mPageAdapter;
//    private ViewPager mViewPager;
//    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getString(R.string.main_title));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        mNavigationViewManager = new NavigationViewManager(this, (DrawerLayout) findViewById(R.id.drawer_layout), navigationView);



//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//        if (mViewPager != null) {
//            setupViewPager(mViewPager);
//        }
//
//
//        mTabLayout = (TabLayout) findViewById(R.id.tabs);
//        mTabLayout.setupWithViewPager(mViewPager);

        mEventListFragment = new EventsRecyclerFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_event);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavUtils.showCreateEventFragmentActivity(mEventListFragment);
//                Snackbar.make(listener, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        User currentUser = AppApplication.getContext().getCurrentUser();

//         todo chekc why it's happening
        if (currentUser != null) {
            String userName = currentUser.getName();
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.username)).setText(userName);;
            ((TextView) navigationView.getHeaderView(0).findViewById(R.id.email)).setText(currentUser.getEmail());
        }


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.content_frame,  mEventListFragment)
                .commit();
    }

//    @Override
//    public View onCreateView(String name, Context listener, AttributeSet attrs) {
//        View listener =  super.onCreateView(name, listener, attrs);
//
//        NavigationView navigationView = (NavigationView) listener.findViewById(R.id.navigation_drawer);
//
//
//        return listener;
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.invite_person:
                NavUtils.showInvitePersonActivity(this);
                break;

            case R.id.add_event:
                NavUtils.showCreateEventFragmentActivity(mEventListFragment);
                break;
        }
        return super.onOptionsItemSelected(item);
    }




//    private void setupViewPager(ViewPager viewPager) {
//
//
//        mPageAdapter = new Adapter(getSupportFragmentManager());
//        mPageAdapter.addFragment(new EventProfileFragment(), "Event 1");
//        mPageAdapter.addFragment(new EventProfileFragment(), "Event 2");
////        mPageAdapter.addFragment(new EventProfileFragment(), "Event 3");
////        mPageAdapter.addFragment(new EventProfileFragment(), "Event 4");
////        mPageAdapter.addFragment(new EventProfileFragment(), "Event 5");
//        viewPager.setAdapter(mPageAdapter);
//
//
//    }

//    public void addEvent() {
//
////        mPageAdapter.clearAll();
////        mPageAdapter = new Adapter(getSupportFragmentManager());
//        EventProfileFragment eventProfileFragment = new EventProfileFragment();
//        eventProfileFragment.setTitle("Event 3");
//        mPageAdapter.addFragment(eventProfileFragment, "Event 3");
//       mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));
////        mViewPager.setAdapter(mPageAdapter);
//        mPageAdapter.notifyDataSetChanged();
//
//    }
//
//    public void switchEvents() {
//
//        mPageAdapter.switchFragments(0,1);
//        CharSequence text = mTabLayout.getTabAt(0).getText();
//        mTabLayout.getTabAt(0).setText(mTabLayout.getTabAt(1).getText());
//        mTabLayout.getTabAt(1).setText(text);
////        mPageAdapter.notifyDataSetChanged();
//
//    }
//
//
//    public void removeEvent(int i) {
//
//        mTabLayout.removeTab(mTabLayout.getTabAt(i));
//        mPageAdapter.removeFragment(i);
//        mPageAdapter.notifyDataSetChanged();
//    }




//    static class Adapter extends FragmentStatePagerAdapter {
//        private final List<EventProfileFragment> mFragments = new ArrayList<>();
//        private final List<String> mFragmentTitles = new ArrayList<>();
//
//        public Adapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        public void addFragment(EventProfileFragment fragment, String title) {
//            fragment.setTitle(title);
//            mFragments.add(fragment);
//            mFragmentTitles.add(title);
//        }
//
//        public void clearAll() {
//            mFragments.clear();
//            mFragmentTitles.clear();
//        }
//
//        public void switchFragments(int pos1, int pos2) {
//
//            String title = mFragments.get(pos1).getName();
//            mFragments.get(pos1).setTitle(mFragments.get(pos2).getName());
//            mFragments.get(pos2).setTitle(title);
//
//            Collections.swap(mFragmentTitles, pos1, pos2);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            mFragments.get(position).setTitle(mFragmentTitles.get(position));
//
//            return mFragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragments.size();
//        }
//
//        public int getItemPosition(Object item) {
//            EventProfileFragment fragment = (EventProfileFragment) item;
//            String title = fragment.getName();
//            int position = mFragmentTitles.indexOf(title);
//
//            if (position >= 0) {
//                return position;
//            } else {
//                return POSITION_NONE;
//            }
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitles.get(position);
//        }
//
//
//        public void removeFragment(int i) {
//            mFragments.remove(i);
//            mFragmentTitles.remove(i);
//        }
//    }

}
