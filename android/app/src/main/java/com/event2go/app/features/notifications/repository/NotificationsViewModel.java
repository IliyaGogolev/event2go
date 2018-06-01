package com.event2go.app.features.notifications.repository;

import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.features.notifications.data.repository.NotificationsDataProvider;
import com.event2go.app.features.notifications.data.AppNotification;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 10/6/15.
 */
public class NotificationsViewModel extends BaseRecyclerViewModel<AppNotification> {

    private static NotificationsViewModel ourInstance = new NotificationsViewModel();

    public static NotificationsViewModel getInstance() {
        return ourInstance;
    }

    public NotificationsViewModel() {
        super(new AppNotificationsRecyclerViewAdapter());
    }

    @Override
    public void load(final OnLoadCompleteCallback<AppNotification> callback) {

        Observable<List<AppNotification>> call = NotificationsDataProvider.getInstance().getNotifications();
        call.subscribe(new Consumer<List<AppNotification>>() {
            @Override
            public void accept(List<AppNotification> appNotifications) {
                callback.onSuccess(appNotifications);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                callback.onFailure(throwable);
            }
        });

    }
}
