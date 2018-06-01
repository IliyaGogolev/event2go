package com.event2go.app.features.event.presentation;

import android.databinding.BaseObservable;

import com.event2go.app.utils.OnCallbacListener;
import com.event2go.app.R;
import com.event2go.app.databinding.ListItemEventBinding;
import com.event2go.app.features.event.data.Event;
import com.event2go.base.presentation.adapter.BaseRecyclerAdapter;

/**
 * Created by Iliya Gogolev on 6/29/15.
 */
public class EventsRecycleViewAdapter extends BaseRecyclerAdapter<Event> {

    private OnCallbacListener mCallbackListener = new OnCallbacListener() {
        @Override
        public void onSuccess(Object object) {

        }

        @Override
        public void onFailure(Throwable t) {

        }
    };


    @Override
    protected int getViewType(BaseObservable item) {
        return 0;
    }

    @Override
    protected int getLayoutIdByViewType(int viewType) {
        return R.layout.list_item_event;
    }

    @Override
    protected void onBindViewHolderByViewType(int viewType, ViewBindingHolder holder, final BaseObservable item) {
        Event event = (Event) item;

        final ListItemEventBinding binding = (ListItemEventBinding) holder.binding;
        binding.setEvent(event);

        PartStatQuestionBarUtils.addClickListeners(binding.includeQuestionBar, (Event) item, mCallbackListener);

    }
}
