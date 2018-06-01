package com.event2go.app.features.notifications.repository;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.event2go.app.features.event.presentation.InviteResponseFragment;
import com.event2go.base.presentation.fragment.BaseRecyclerFragment;
import com.event2go.base.presentation.viewmodel.BaseRecyclerViewModel;
import com.event2go.app.features.notifications.data.repository.NotificationsDataProvider;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.data.RequestCode;
import com.event2go.app.utils.NavUtils;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 6/29/15.
 */
public class NotificationsRecyclerFragment extends BaseRecyclerFragment<AppNotification> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(View view, int position) {
        final AppNotification notification = getAdapter().getItem(position);
        switch (notification.getType()) {
            case AppNotification.NOTIFICATION_TYPE_INVITE:
                notification.setStatus(AppNotification.STATUS_READ);
                Observable update = NotificationsDataProvider.getInstance().update(notification);
                update.subscribe(new Consumer() {
                    @Override
                    public void accept(Object object) {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        notification.setStatus(AppNotification.STATUS_READ);
                    }
                });
                NavUtils.showResponseToInviteActivity(NotificationsRecyclerFragment.this.getActivity(), notification, RequestCode.INVITE_ANSWER.ordinal());
                getActivity().finish();
                break;
            case AppNotification.NOTIFICATION_TYPE_CHAT:
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // todo iliya base
//        ActionBar actionBar = getSupportedActionBar();
//        actionBar.setTitle(getString(R.string.nav_notifications));
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    @NonNull
    @Override
    protected BaseRecyclerViewModel<AppNotification> getRecyclerViewModel() {
        return new NotificationsViewModel();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.INVITE_ANSWER.ordinal()) {
            if (resultCode == InviteResponseFragment.RESULT_ACCEPTED) {
                NotificationsViewModel.getInstance().load(null);
            }
            if (resultCode == InviteResponseFragment.RESULT_REJECTED) {
                NotificationsViewModel.getInstance().load(null);
            }
        }
    }
}
