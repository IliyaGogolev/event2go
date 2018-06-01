package com.event2go.app.features.event.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.data.RequestCode;
import com.event2go.app.utils.NavUtils;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;

/**
 * Created by Iliya Gogolev on 6/29/15.
 */
public class EventsRecyclerFragment extends BaseRecyclerFragment<Event> {


    @NonNull
    @Override
    protected BaseRecyclerViewModel<Event> getRecyclerViewModel() {
        return EventsViewModel.getInstance();
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);

        Event event = getAdapter().getItem(position);
        NavUtils.showEventProfileActivity(EventsRecyclerFragment.this, event, RequestCode.EVENT_PROFILE.ordinal());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.EVENT_PROFILE.ordinal()) {
            if (resultCode == EventProfileFragment.RESULT_DELETED) {

                EventsViewModel.getInstance().load(null);
            }
        }

        if (requestCode == RequestCode.EVENT_CREATE.ordinal()) {
            if (resultCode == Activity.RESULT_OK) {
                EventsViewModel.getInstance().load(null);
            }
        }


    }


//    @Override
//    protected void onItemClick(Event item) {
//
//        Snackbar.make(getListener(), "OnItemClicked", Snackbar.LENGTH_SHORT).show();
//
//
//        NavUtils.showEventProfileActivity(this, item, RequestCode.EVENT_PROFILE);
//    }
//

}
