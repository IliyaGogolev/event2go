package com.event2go.app.features.event.presentation;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.AppApplication;
import com.event2go.app.R;
import com.event2go.app.features.event.data.repository.EventsDataProvider;
import com.event2go.app.databinding.FragmentInviteResponseBinding;
import com.event2go.app.features.notifications.data.AppNotification;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.Invite;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 9/10/15.
 */
public class InviteResponseFragment extends BaseFragment {

    public static int RESULT_ACCEPTED = 1;
    public static int RESULT_REJECTED = 2;
    private AppNotification mNotification;

    TextView v;
    private String mTitle;
    private Subscription mSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        mNotification = AppApplication.getContext().getCurrentNotification();

        // todo iliya base
//        ActionBar actionBar = getSupportedActionBar();
//        actionBar.setTitle(getString(R.string.invitation));
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

//    public void setTitle(String title) {
//        mTitle = title;
//        if (v != null) {
//            v.setText(title);
//        }
//    }

    public String getTitle() {
        return mTitle;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_response;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentInviteResponseBinding bind = DataBindingUtil.bind(view);

        final Invite invite = mNotification.getInvite();
        Observable<Event> observableEvent = EventsDataProvider.getInstance().getFeedEventById(invite.getEventId());
        observableEvent.subscribe(new Consumer<Event>() {
            @Override
            public void accept(Event event) throws Exception {
                invite.setEvent(event);
                bind.setInvite(invite);
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        bind.setNotification(mNotification);

        bind.includeQuestionBar.buttonMaybe.setVisibility(View.GONE);

        bind.includeQuestionBar.buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_ACCEPTED);
                getActivity().finish();
            }
        });
        bind.includeQuestionBar.buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_REJECTED);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDetach() {
        if (mSubscription != null) {
            mSubscription.cancel();
        }
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.event_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
