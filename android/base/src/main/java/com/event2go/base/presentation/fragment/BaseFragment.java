package com.event2go.base.presentation.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.event2go.base.R;
import com.event2go.base.utils.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Ilya Gogolev on 4/20/15.
 */
public abstract class BaseFragment extends Fragment {

    public static final int TOOLBAR_MODE_NONE = -1;
    public static final int TOOLBAR_MODE_STANDARD = 0;
    public static final int TOOLBAR_MODE_CUSTOM = 1;
    public static final int TOOLBAR_MODE_TABS = 2;

    @LayoutRes
    protected abstract int getLayoutId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the hosting activity has a parent and actionbar display
        // the home button
        if (canNavigateUpFromSameTask() && getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutId(), container, false);
        initToolbar(view);

        return view;
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }

        switch (getToolbarMode()) {

            case TOOLBAR_MODE_STANDARD: {
                getSupportActivity().setSupportActionBar(toolbar);

                final ActionBar actionBar = getActionBar();
                if (actionBar != null) {
                    if (showHomeUpIndicatorIcon()) {

                        if (canNavigateUpFromSameTask()) {
                            Drawable upArrow = DrawableCompat.wrap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
                            actionBar.setHomeAsUpIndicator(upArrow);
                            actionBar.setDisplayHomeAsUpEnabled(true);
                        }
                    }

                    actionBar.setTitle(getToolbarTitle());
                }
                break;
            }
            case TOOLBAR_MODE_TABS:
                getSupportActivity().setSupportActionBar(toolbar);

                final ActionBar actionBar = getActionBar();
                if (actionBar != null) {
                    if (showHomeUpIndicatorIcon()) {
                        Drawable upArrow = DrawableCompat.wrap(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, null));
                        actionBar.setHomeAsUpIndicator(upArrow);
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                    actionBar.setTitle(getToolbarTitle());
                }
                break;

            case TOOLBAR_MODE_CUSTOM:
                View customToolbar = onCreateCustomToolbarView(getActivity().getLayoutInflater());
                ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
                toolbar.addView(customToolbar, layoutParams);
//                toolbar.setBackgroundResource(R.color.primary_color);
                toolbar.setContentInsetsAbsolute(0, 0);
                break;

            case TOOLBAR_MODE_NONE:
                ((ViewGroup) toolbar.getParent()).removeView(toolbar);
                break;
        }
    }

    protected boolean showHomeUpIndicatorIcon() {
        return true;
    }

    @ToolbarMode
    public int getToolbarMode() {
        return TOOLBAR_MODE_NONE;
    }

    /**
     * @return
     */
    public AppCompatActivity getSupportActivity() {
        return (AppCompatActivity) (getActivity());
    }

    @Nullable
    public ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    protected View onCreateCustomToolbarView(LayoutInflater inflater) {
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // set tinting color for pre lollipop version.
        Context context = getContext();
        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null && context != null) {
                drawable = DrawableCompat.wrap(drawable);
                DrawableCompat.setTint(
                        drawable,
                        ContextCompat.getColor(context, R.color.base_icon_tint)
                );
                menu.getItem(i).setIcon(drawable);
            }
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (canNavigateUpFromSameTask()) {
                    NavUtils.navigateUpFromSameTask(getSupportActivity());
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * @return true if the activity has a parent defined.
     */
    protected boolean canNavigateUpFromSameTask() {
        return NavUtils.getParentActivityName(getSupportActivity()) != null;
    }

    @StringRes
    public int getToolbarTitle() {
        return R.string.empty;
    }

    public void hideKeyboard() {

        if (getView() != null) {
            InputMethodManager methodManager = (InputMethodManager) (getSupportActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
            methodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    protected void setSoftInputAdjustResize() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisible()) {
            onBecomeVisible();
        } else {
            onBecomeInvisible();
        }
    }

    // Called when the Fragment is visible to the user.
    @Override
    public void onStart() {
        super.onStart();

        onBecomeVisible();
    }

    @Override
    public void onStop() {
        super.onStop();
        onBecomeInvisible();
    }

    protected void onBecomeVisible() {
        Logger.d("onBecomeVisible");
    }

    protected void onBecomeInvisible() {
        Logger.d("onBecomeInvisible");
    }

    /**
     * Using a toolbar in the fragment, requiring to include layout="@layout/toolbar"
     */
    @IntDef({TOOLBAR_MODE_NONE, TOOLBAR_MODE_STANDARD, TOOLBAR_MODE_CUSTOM, TOOLBAR_MODE_TABS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ToolbarMode {
    }
}
