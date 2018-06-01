package com.event2go.base.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.event2go.base.R;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.base.utils.Logger;

/**
 * Created by Iliya Gogolev on 6/9/15.
 */

public class BaseFragmentActivity extends BaseActivity {

    public static final String CLASS_NAME = "BaseFragmentActivity.class_name";
    public static final String USE_TOOLBAR = "BaseFragmentActivity.use_toolbar";
    public static final String TOOLBAR_TITLE = "BaseFragmentActivity.toolbar_title";

    public static int USE_TOOLBAR_NONE = 0;
    public static int USE_TOOLBAR_INSIDE_ACITIVY = 1;
    public static int USE_TOOLBAR_INSIDE_FRAGMENT = 2;

    private Class<?> mCls;
    private Fragment mFragment;
    private String mClassName;
    private int mUseToolbar;
    private String mToolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mUseToolbar = USE_TOOLBAR_INSIDE_ACITIVY;
        if (savedInstanceState != null) {
            mUseToolbar = savedInstanceState.getInt(USE_TOOLBAR, USE_TOOLBAR_NONE);
            mToolbarTitle = savedInstanceState.getString(TOOLBAR_TITLE);
        } else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                mUseToolbar = extras.getInt(USE_TOOLBAR, USE_TOOLBAR_INSIDE_ACITIVY);
                if (mUseToolbar != USE_TOOLBAR_NONE) {
                    mToolbarTitle = extras.getString(TOOLBAR_TITLE, null);
                }
            }
        }

        if (mUseToolbar != USE_TOOLBAR_NONE) {
            setTheme(R.style.BaseAppTheme_NoActionBar);
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_activity);


        if (mUseToolbar == USE_TOOLBAR_INSIDE_ACITIVY) {
            final ViewStub viewStub = (ViewStub) findViewById(R.id.toolbar_view);
            Toolbar toolbar = (Toolbar) viewStub.inflate();
            setSupportActionBar(toolbar);

            // hide/remove toolbar title
            getSupportActionBar().setDisplayShowTitleEnabled(isDisplayShowToolbarTitleEnabled());
            setToolbarTitle(getToolbarTitle());

            @DrawableRes int resId = getHomeAsUpIndicatorResId();
            if (resId > 0) {
                ActionBar ab = getSupportActionBar();
                ab.setHomeAsUpIndicator(resId);
                ab.setDisplayHomeAsUpEnabled(true);
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {

            mFragment = createFragment(savedInstanceState);
            if (mFragment != null) {
                fm.beginTransaction()
                        .add(R.id.content_frame, mFragment)
                        .commit();
            }

        } else {
            mFragment = fm.findFragmentById(R.id.content_frame);
        }
    }

    public void addFragment(BaseFragment fragment, String tag) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .add(R.id.content_frame, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    protected Fragment createFragment(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            mClassName = intent.getExtras().getString(CLASS_NAME);
            if (mClassName == null && savedInstanceState != null) {
                mClassName = savedInstanceState.getString(CLASS_NAME);
            }
            Logger.d("className " + mClassName);
            try {
                mCls = Class.forName(mClassName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        Fragment fragment = null;
        try {
            fragment = (Fragment) mCls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return fragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(CLASS_NAME, mClassName);
        outState.putInt(USE_TOOLBAR, mUseToolbar);
        outState.putString(TOOLBAR_TITLE, mToolbarTitle);
    }


    protected String getToolbarTitle() {
        return mToolbarTitle;
    }

    protected int getHomeAsUpIndicatorResId() {
        return R.drawable.ic_arrow_back_white_24dp; // example R.drawable.ic_menu;
    }

    protected boolean isDisplayShowToolbarTitleEnabled() {
        return true;
    }

    protected void setToolbarTitle(String actionBarTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;

        if (actionBarTitle != null) {
            actionBar.setTitle(actionBarTitle);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


