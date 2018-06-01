package com.event2go.app.features.event.presentation;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.event2go.base.presentation.fragment.AlertDialogFragment;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.AppApplication;
import com.event2go.app.utils.OnCallbacListener;
import com.event2go.app.R;
import com.event2go.app.features.event.domain.EventUseCase;
import com.event2go.app.features.event.data.repository.EventsDataProvider;
import com.event2go.app.databinding.FragmentEventProfileBinding;
import com.event2go.app.databinding.ListItemEventUserBinding;
import com.event2go.app.features.event.data.Attendee;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.PartStatType;
import com.event2go.app.features.user.data.User;
import com.event2go.app.utils.NavUtils;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 6/3/15.
 */
public class EventProfileFragment extends BaseFragment {

    public static int RESULT_DELETED = 1;
    Event mEvent;

    TextView v;
    private String mTitle;
    //    private ArrayList<User> mUsers;
    private Subscription mSubscription;
    private FragmentEventProfileBinding mBind;
    private boolean mCameFromNotifiaction;
    // todo geo location
//  http://wptrafficanalyzer.in/blog/android-reverse-geocoding-using-geocoder-at-touched-location-in-google-map/
//  http://stackoverflow.com/questions/13598647/google-map-how-to-get-address-in-android


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // todo iliya last changes
//        ActionBar actionBar = getSupportedActionBar();
//        if (actionBar != null) {
//            actionBar.setTitle(getString(R.string.event));
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
//        }


        String eventId = null;
        try {
            Intent intent = getActivity().getIntent();
            eventId = intent.getStringExtra("event_id");
            mCameFromNotifiaction = !intent.getBooleanExtra("came_from_notification", false);
            if (!TextUtils.isEmpty(eventId)) {
                Observable<Event> observableEvent = EventsDataProvider.getInstance().getFeedEventById(eventId);
                observableEvent.subscribe(new Consumer<Event>() {
                    @Override
                    public void accept(Event event) throws Exception {
                        mEvent = event;
                        bindEvent(getView());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }

        } catch (Exception e) {
        }

        if (TextUtils.isEmpty(eventId)) {
            mEvent = AppApplication.getContext().getCurrentEvent();
            setTitle(mEvent.getSummary());
        }
    }

    public void setTitle(String title) {
        mTitle = title;
        if (v != null) {
            v.setText(title);
        }

    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_event_profile;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBind = (FragmentEventProfileBinding) DataBindingUtil.bind(view);
        bindEvent(view);
    }

    private void bindEvent(View view) {
        mBind.container.setVisibility(mEvent == null ? View.GONE : View.VISIBLE);

        if (mEvent == null) {
            return;
        }

        mBind.setEvent(mEvent);
        setTitle(mEvent.getSummary());

        buildUsersListLayout(mBind);

        ((TextView) view.findViewById(R.id.event_location)).setText(mEvent.getLocation());

        PartStatQuestionBarUtils.addClickListeners(mBind.includeQuestionBar, mEvent, new OnCallbacListener() {
            @Override
            public void onSuccess(Object object) {
                LinearLayout goingContainer = mBind.going.listContainer;
                LinearLayout notGoingContainer = mBind.notGoing.listContainer;
                LinearLayout maybeContainer = mBind.tentative.listContainer;
                LinearLayout needsActionContainer = mBind.needsAction.listContainer;

                // remove users, keep titles
                goingContainer.removeViews(1, goingContainer.getChildCount() - 1);
                notGoingContainer.removeViews(1, notGoingContainer.getChildCount() - 1);
                maybeContainer.removeViews(1, maybeContainer.getChildCount() - 1);
                needsActionContainer.removeViews(1, needsActionContainer.getChildCount() - 1);

                buildUsersListLayout(mBind);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void buildUsersListLayout(FragmentEventProfileBinding bind) {

        List<Attendee> attendees = mEvent.getAttendees();
        HashMap<String, Attendee> attendeeHashMap = new HashMap<>();
        for (Attendee attendee : attendees) {
            attendeeHashMap.put(attendee.getUserId(), attendee);
        }

        List<User> users = mEvent.getUsers();
        List<User> going = new ArrayList<>();
        List<User> notGoing = new ArrayList<>();
        List<User> maybe = new ArrayList<>();
        List<User> needsAction = new ArrayList<>();

        for (User user : users) {
            Attendee attendee = attendeeHashMap.get(user.getId());
            if (attendee != null) {
                String partStat = attendee.getPartStat();
                if (partStat.equalsIgnoreCase(PartStatType.ACCEPTED)) {
                    going.add(user);
                } else if (partStat.equalsIgnoreCase(PartStatType.DECLINED)) {
                    notGoing.add(user);
                } else if (partStat.equalsIgnoreCase(PartStatType.TENTATIVE)) {
                    maybe.add(user);
                } else if (partStat.equalsIgnoreCase(PartStatType.NEEDS_ACTION)) {
                    needsAction.add(user);
                }
            } else {
                needsAction.add(user);
            }
        }

        bind.going.container.setVisibility(going.size() == 0 ? View.GONE : View.VISIBLE);
        bind.notGoing.container.setVisibility(notGoing.size() == 0 ? View.GONE : View.VISIBLE);
        bind.tentative.container.setVisibility(maybe.size() == 0 ? View.GONE : View.VISIBLE);
        bind.needsAction.container.setVisibility(needsAction.size() == 0 ? View.GONE : View.VISIBLE);

        LinearLayout goingContainer = bind.going.listContainer;
        LinearLayout notGoingContainer = bind.notGoing.listContainer;
        LinearLayout maybeContainer = bind.tentative.listContainer;
        LinearLayout needsActionContainer = bind.needsAction.listContainer;

        addUsersToListContainer(going, goingContainer);
        addUsersToListContainer(notGoing, notGoingContainer);
        addUsersToListContainer(maybe, maybeContainer);
        addUsersToListContainer(needsAction, needsActionContainer);
    }

    private void buildUsersListLayout() {

    }

    private void addUsersToListContainer(List<User> users, LinearLayout container) {

        for (User user : users) {

            View listItemEventUser = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_event_user, null);
            container.addView(listItemEventUser);

            ListItemEventUserBinding binding = (ListItemEventUserBinding) DataBindingUtil.bind(listItemEventUser);
            binding.setUser(user);
        }
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
        if (mCameFromNotifiaction) {
            inflater.inflate(R.menu.menu_event_profile, menu);
        }
        // todo add menu - remove from event
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.invite_person:
                NavUtils.showInvitePersonActivity(getActivity());
                return true;

            case R.id.action_edit:
                NavUtils.showCreateEventFragmentActivity(this, true, mEvent);
                break;

            case R.id.action_delete:

                AlertDialogFragment alertDialogFragment = AlertDialogFragment.newInstance();
                alertDialogFragment.setMessage(getString(R.string.event_delete_dialog_message));
                alertDialogFragment.setPositiveButton(android.R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Observable observable = new EventUseCase().deleteEvent(mEvent);
                        observable.subscribe(new Consumer() {
                            @Override
                            public void accept(Object o) throws Exception {

//                                Intent returnIntent = new Intent();
                                getActivity().setResult(RESULT_DELETED);//, returnIntent);
                                getActivity().finish();

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Toast.makeText(getActivity(), R.string.event_deleted_failed, Toast.LENGTH_SHORT);
                            }
                        });
                    }
                });
                alertDialogFragment.setNegativeButton(android.R.string.no, null);
                alertDialogFragment.show(getActivity().getSupportFragmentManager(), "delete event");


                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
