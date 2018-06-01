package com.event2go.base.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.event2go.base.R;


/**
 * Created by Iliya Gogolev on 7/20/15.
 */
public abstract class BaseTabbedFragment extends BaseFragment {

    protected TabAdapter mTabAdapter;
    protected ViewPager mViewPager;
    private ProgressBar progress;
    private TabLayout mTabLayout;

    abstract protected Fragment getTabAdapterItem(int position);

    abstract protected int getTabCount();

    abstract protected String getTabPageTitle(int position);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tab_base;
    }

    @Override
    public int getToolbarMode() {

        return TOOLBAR_MODE_TABS;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress = (ProgressBar) view.findViewById(R.id.progress);

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(0);

        if (mViewPager != null) {

            mTabLayout = (TabLayout) view.findViewById(R.id.tabs);

            mTabAdapter = new TabAdapter(getChildFragmentManager());
            mViewPager.setAdapter(mTabAdapter);
            mTabLayout.setupWithViewPager(mViewPager);

            for (int i = 0; i < mTabLayout.getTabCount(); i++) {

                final TabLayout.Tab tab = mTabLayout.getTabAt(i);
                final View customTabView = getCustomTabView(i);
                if (tab != null && customTabView != null) {
                    tab.setCustomView(customTabView);

                    if (i == 0) {
                        customTabView.setSelected(true);
                    }
                }
            }
        }

        view.findViewById(R.id.toolbar_shadow).setVisibility(showToolbarShadow() ? View.VISIBLE : View.GONE);
    }

    public View getCustomTabView(int position) {
        return null;
    }

    protected ViewPager getViewPager() {
        return mViewPager;
    }

    public boolean showToolbarShadow() {
        return true;
    }

    public void showProgress(boolean show) {
        if (show)
            progress.setVisibility(View.VISIBLE);
        else
            progress.setVisibility(View.GONE);
    }

    public class TabAdapter extends FragmentStatePagerAdapter {

        SparseArray<Fragment> mRegisteredFragments = new SparseArray<Fragment>();

        public TabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public Fragment getItem(int position) {
            return BaseTabbedFragment.this.getTabAdapterItem(position);
        }

        @Override
        public int getCount() {
            return BaseTabbedFragment.this.getTabCount();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return BaseTabbedFragment.this.getTabPageTitle(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            mRegisteredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            mRegisteredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getFragment(int position) {
            return mRegisteredFragments.get(position);
        }

    }
}
