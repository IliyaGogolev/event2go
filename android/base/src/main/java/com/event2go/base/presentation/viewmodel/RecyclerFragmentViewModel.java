package com.event2go.base.presentation.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

/**
 * Created by Iliya Gogolev on 10/06/2015.
 */
public class RecyclerFragmentViewModel extends BaseObservable {

    private final BaseRecyclerViewModel<? extends BaseObservable> mRecyclerViewModel;

    public RecyclerFragmentViewModel(@NonNull BaseRecyclerViewModel<? extends BaseObservable> viewModel) {
        mRecyclerViewModel = viewModel;
    }

    public BaseRecyclerViewModel<? extends BaseObservable> getBaseRecyclerViewModel() {
        return mRecyclerViewModel;
    }

    /**
     * @return The value to display in the {@link android.support.v7.widget.RecyclerView}
     * when there is no data in the view model.
     */
    @Bindable
    public int getEmptyListStringId() {
        return mRecyclerViewModel.getEmptyListStringId();
    }

    /**
     * @return The quantity of data items contained in this view model.
     */
    @Bindable
    public int getItemCount() {
        return mRecyclerViewModel.getItemCount();
    }
}
