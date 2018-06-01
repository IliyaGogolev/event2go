package com.event2go.app.features.event.presentation;

import android.support.annotation.NonNull;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.features.event.data.repository.EventsDataProvider;
import com.event2go.app.features.event.data.Event;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 10/6/15.
 */
public class EventsViewModel extends BaseRecyclerViewModel<Event> {


    private static EventsViewModel ourInstance = new EventsViewModel();

    public static EventsViewModel getInstance() {
        return ourInstance;
    }

    private EventsViewModel() {
        super(new EventsRecycleViewAdapter());
    }


    @Override
    public void load(@NonNull final OnLoadCompleteCallback<Event> callback) {
        super.load(callback);

        Observable<List<Event>> upcomingEvents = EventsDataProvider.getInstance().getFeedEvents();
        upcomingEvents.subscribe(new Consumer<List<Event>>() {
            @Override
            public void accept(List<Event> events) {
                callback.onSuccess(events);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });


    }
}
