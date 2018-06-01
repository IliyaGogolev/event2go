package com.event2go.base.presentation.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.view.View;

import com.event2go.base.R;
import com.event2go.base.BR;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

import java.util.List;

public abstract class BaseRecyclerViewModel<T extends BaseObservable> extends BaseObservable {

    /**
     * Contains the list of objects that represent the data inside of the provided adapter.
     * Can be manipulated directly if preferred but must notify the adapter of a data change
     * manually. Contents within should be fully initialized. If they are not fully initialized
     * NullPointerExceptions may occur during asynchronous transactions.
     */
    protected final ObservableArrayList<T> mItems;
    /**
     * The adapter to be used with a {@link android.support.v7.widget.RecyclerView}.
     * Items added or removed through the adapter automatically notify the bounded view.
     */
    protected final BaseRecyclerAdapter<T> mAdapter;
    protected boolean mLoadingData;
    private int mEmptyTextVisibility = View.GONE;

    /**
     * An adapter is required by all subclasses. It can have items upon construction or
     * after.
     *
     * @param adapter The adapter desired for use with the view model
     */
    protected BaseRecyclerViewModel(@NonNull BaseRecyclerAdapter<T> adapter) {
        mAdapter = adapter;
        mItems = mAdapter.getItems();
    }

    /**
     * @return The value to display in the {@link android.support.v7.widget.RecyclerView}
     * when there is no data in the view model.
     */
    public int getEmptyListStringId() {
        return R.string.default_empty_list_string;
    }

    /**
     * @return The quantity of data items contained in this view model.
     */
    public int getItemCount() {
        return mAdapter.getItemCount();
    }

    public ObservableList<T> getItems() {
        return mAdapter.getItems();
    }

    /**
     * Re-retrieves the entire data set or updates it incrementally
     */
    public void refresh(@NonNull OnLoadCompleteCallback<T> callback) {
    }

    /**
     * Retrieve entire data set
     */
    public void load(@NonNull OnLoadCompleteCallback<T> callback) {
    }

    /**
     * Loads next page of data if pagination is supported by the api
     */
    public void loadNextPage(@NonNull OnLoadCompleteCallback<T> callback) {
    }

    /**
     * Called when the view loses focus
     */
    public void onViewStopped() {
    }

    /**
     * Called when the view is back in focus.
     */
    public void onViewResumed() {
        // Force refresh of bound data otherwise data will remain stale until view is recycled
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Returns the data adapter being used by this View-AppModel if a reference to the adapter is
     * required.
     *
     * @return The instance of the adapter being used
     */
    public final BaseRecyclerAdapter<T> getAdapter() {
        return mAdapter;
    }

    @Bindable
    public boolean isLoadingData() {
        return mLoadingData;
    }

    public void setLoadingData(boolean value) {
        mLoadingData = value;
        notifyPropertyChanged(BR.loadingData);
    }

    @Bindable
    public int getEmptyTextVisibility() {
        return mEmptyTextVisibility;
    }

    public void setEmptyTextVisibility(int visibility) {
        mEmptyTextVisibility = visibility;
        notifyPropertyChanged(BR.emptyTextVisibility);
    }

    /**
     * Interface providing completion feedback after an HTTP request for a set of data,
     * in this case, a list.
     *
     * @param <T>
     */
    public interface OnLoadCompleteCallback<T extends BaseObservable> {
        /**
         * Called upon successful data response.
         */
        void onSuccess(List<T> items);

        /**
         * Invoked when a network or unexpected exception occurred during the HTTP request.
         */
        void onFailure(Throwable t);
    }
}