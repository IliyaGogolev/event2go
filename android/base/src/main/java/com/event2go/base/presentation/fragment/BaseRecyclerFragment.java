package com.event2go.base.presentation.fragment;


import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewStubProxy;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.Toast;

import com.event2go.base.R;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;
import com.event2go.base.utils.Logger;
import com.event2go.base.databinding.FragmentBaseRecyclerViewBinding;
import com.event2go.base.presentation.decorator.DividerItemDecoration;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.base.presentation.viewmodel.RecyclerFragmentViewModel;

import java.util.List;


public abstract class BaseRecyclerFragment<T extends BaseObservable> extends BaseFragment
        implements BaseRecyclerAdapter.OnItemClickListener {

    // all possible internal states
    private static final int STATE_IDLE               = 0;
    private static final int STATE_LOADING            = 1;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mTargetState  = STATE_IDLE;


    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected BaseRecyclerViewModel<T> mRecyclerViewModel;
    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    private RecyclerFragmentViewModel mFragmentViewModel;
//    private FragmentBaseRecyclerViewBinding mBinding;
    private FragmentBaseRecyclerViewBinding mBinding;
    private BaseRecyclerAdapter<T> mAdapter;
    private boolean mLoadingData;
    /**
     * When a call to the View-Models data retrieval methods is completed,
     * this code will be executed
     */
    protected BaseRecyclerViewModel.OnLoadCompleteCallback<T> mCompletionCallback =
            new BaseRecyclerViewModel.OnLoadCompleteCallback<T>() {
                @Override
                public void onSuccess(List<T> items) {
                    setData(items);
                }

                @Override
                public void onFailure(Throwable t) {

                    onDataLoadFailure(t);
                }
            };
    /**
     * Code to be executed when user "pulls to refresh"
     */
    protected SwipeRefreshLayout.OnRefreshListener mRefreshListener =
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (!mLoadingData) {
                        mRecyclerViewModel.setEmptyTextVisibility(View.GONE);

                        if (!mAdapter.isEmpty()) {
                            mAdapter.clear();
                        }

                        mLoadingData = true;

                        // When the user initiates a refresh action we query the VM to download data
                        // We register a listener for completion of the request
                        mRecyclerViewModel.refresh(mCompletionCallback);

                        mRecyclerViewModel.setEmptyTextVisibility(View.GONE);
                    }
                }
            };
    private boolean mSwipeRefreshEnabled;
    protected boolean mLoadDataOnCreate = true;

    public void onDataLoadFailure(Throwable t) {
        if (!isAdded()) return;

        mLoadingData = false;
        mFragmentViewModel.notifyChange();

        mRecyclerViewModel.setEmptyTextVisibility(View.VISIBLE);

        setViewModelLoading(false);
        mSwipeRefreshLayout.setRefreshing(false);

        if (t != null) {
            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void setData(final List<T> items) {

        initializeViewModelAndAdapter();
        mAdapter.clear();
        mAdapter.addAll(items);

        mLoadingData = false;
        mFragmentViewModel.notifyChange();

        // Stop loading animation
        setViewModelLoading(false);
        mRecyclerViewModel.setEmptyTextVisibility(items.size() == 0 ? View.VISIBLE : View.GONE);

        if (!isAdded()) return;

        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void setViewModelLoading(boolean isLoading) {
        if (mRecyclerViewModel != null) {
            mRecyclerViewModel.setLoadingData(isLoading);
        }
    }

    @Override
    protected final int getLayoutId() {
        return R.layout.fragment_base_recycler_view;
    }

    @Override
    public void onResume() {
        mRecyclerViewModel.onViewResumed();
        super.onResume();
    }

    @Override
    public void onStop() {
        mRecyclerViewModel.onViewStopped();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            initializeViewModelAndAdapter();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Logger.d("Creating fragment of type: " + this.getClass().getSimpleName());

        if (savedInstanceState != null && mRecyclerViewModel == null) {
            initializeViewModelAndAdapter();
        }

        mAdapter = mRecyclerViewModel.getAdapter();
        setItemClickListenerOnAdapter(mAdapter);

        mBinding = DataBindingUtil.bind(view);
        mBinding.setViewModel(mFragmentViewModel);

        mRecyclerView = mBinding.recyclerView;
        mSwipeRefreshLayout = mBinding.swipeContainer;

        onSetLayoutManager(mRecyclerView);
        onSetRecycledViewPool(mRecyclerView);

        // Add item dividers
        onAddItemDecoration(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);

        // Swipe to refresh is disabled by default, must call setSwipeRefreshEnabled to enable
        mSwipeRefreshLayout.setEnabled(mSwipeRefreshEnabled);
        mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

        // load items only there's no data yet
        if (mAdapter.isEmpty() && mLoadDataOnCreate) {
            mLoadingData = true;

            reload();
        }
    }

    private void setItemClickListenerOnAdapter(BaseRecyclerAdapter adapter) {
        if (adapter == null) return;

        if (!adapter.hasOnItemClickListener()
                // If adapter has existing reference to another fragment, overwrite it
                || (adapter.getItemClickListener() instanceof BaseRecyclerFragment)) {
            adapter.setOnItemClickListener(this);
        }
    }

    private void initializeViewModelAndAdapter() {

        Logger.d("init adapter");
        if (mRecyclerViewModel == null) {

            mRecyclerViewModel = getRecyclerViewModel();
            if (mRecyclerViewModel != null) {
                mFragmentViewModel = new RecyclerFragmentViewModel(mRecyclerViewModel);
                mAdapter = mRecyclerViewModel.getAdapter();
            }

        }
        setItemClickListenerOnAdapter(mAdapter);
    }

    public void reload() {
        setViewModelLoading(true);

        if (isVisible()) {
            mRecyclerViewModel.load(mCompletionCallback);
            mTargetState = STATE_IDLE;
        } else {
            mTargetState = STATE_LOADING;
        }
    }

    @Override
    protected void onBecomeVisible() {
        if (mTargetState == STATE_LOADING) {
            mRecyclerViewModel.load(mCompletionCallback);
            mTargetState = STATE_IDLE;
        }
    }

    @NonNull
    protected abstract BaseRecyclerViewModel<T> getRecyclerViewModel();

    /**
     * Sets the {@linkplain RecyclerView.LayoutManager} for the {@linkplain RecyclerView}.
     * Calling to super is not necessary.
     *
     * @param recyclerView The RecyclerView on which to change the layout manager.
     */
    protected void onSetLayoutManager(RecyclerView recyclerView) {
        mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    /**
     * @param recyclerView
     */
    protected void onSetRecycledViewPool(RecyclerView recyclerView) {

    }

    /**
     * Sets the {@linkplain RecyclerView.ItemDecoration} for the {@linkplain RecyclerView}.
     * Calling to super is not necessary.
     *
     * @param recyclerView The RecyclerView on which to change the item decoration.
     */
    protected void onAddItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(
                new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL)
        );
    }

    /**
     * Used if the ability to swipe refresh is required by your listener. If enabled the provided {@linkplain BaseRecyclerViewModel}'s
     * refresh method will be invoked.
     *
     * @param enabled set to true if swipe to refresh is desired.
     */
    public void setSwipeRefreshEnabled(boolean enabled) {
        mSwipeRefreshEnabled = enabled;
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(mSwipeRefreshEnabled);
        }
    }

    public void setLoadDataOnCreate(boolean loadDataOnCreate) {
        this.mLoadDataOnCreate = loadDataOnCreate;
    }

    protected BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    protected View inflateHeaderLayout(@LayoutRes int layoutRes) {
        // This is a data binding issue. Not sure why it resolves to a proxy.
        // It can be ignored for the time being
        View headerView = inflateViewStubProxy(mBinding.headerViewStub, layoutRes);
        mBinding.headerShadowView.setVisibility(View.VISIBLE);

        return headerView;
    }

    protected void setHeaderShadowVisibility(int visibility) {
        mBinding.headerShadowView.setVisibility(visibility);
    }

    protected void setFooterShadowVisibility(int visibility) {
        mBinding.footerShadowView.setVisibility(visibility);
    }

    protected View inflateFooterLayout(@LayoutRes int layoutRes) {
        // This is a data binding issue. Not sure why it resolves to a proxy.
        // It can be ignored for the time being
        View footerView = inflateViewStubProxy(mBinding.footerViewStub, layoutRes);
        mBinding.footerShadowView.setVisibility(View.VISIBLE);

        return footerView;
    }

    @Nullable
    private View inflateViewStubProxy(ViewStubProxy viewStubProxy, @LayoutRes int layoutRes) {
        final View view;

        if (viewStubProxy.isInflated()) {
            view = viewStubProxy.getRoot();
        } else {
            final ViewStub viewStub = viewStubProxy.getViewStub();
            viewStub.setLayoutResource(layoutRes);
            view = viewStub.inflate();
        }
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void scrollToEndPosition() {
        int size = mRecyclerViewModel.getAdapter().getItems().size();
        scrollToPosition(size);
    }

    public void scrollToPosition(int position) {
        int firstPosition = mLayoutManager.findFirstVisibleItemPosition();

        if (Math.abs(firstPosition - position) < 20) {
            mRecyclerView.smoothScrollToPosition(position);
        } else {

            if (position < firstPosition) { // scroll up
                mRecyclerView.scrollToPosition(position + 10);
            } else {
                mRecyclerView.scrollToPosition(position - 10);
            }
            mRecyclerView.smoothScrollToPosition(position);
        }
    }

    public boolean isRefreshing() {
        return mSwipeRefreshLayout.isRefreshing();
    }
}

