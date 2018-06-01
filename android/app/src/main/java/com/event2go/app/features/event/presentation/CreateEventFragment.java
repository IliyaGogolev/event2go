package com.event2go.app.features.event.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.event2go.base.presentation.fragment.AlertDialogFragment;
import com.event2go.base.presentation.fragment.BaseFragment;
import com.event2go.app.R;
import com.event2go.app.databinding.FragmentCreateEventBinding;
import com.event2go.app.databinding.IncludeAddParameterTextviewBinding;
import com.event2go.app.databinding.IncludeEventTimeBinding;
import com.event2go.app.features.event.presentation.dialog.AddParameterDialog;
import com.event2go.app.features.event.presentation.dialog.DurationUnitDialogFragment;
import com.event2go.app.features.event.presentation.dialog.EventFrequencyDialogFragment;
import com.event2go.app.features.event.data.Event;
import com.event2go.app.features.event.data.Parameter;

import net.fortuna.ical4j.model.Dur;

import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by Iliya Gogolev on 6/3/15.
 * todo: important: no overlap between start and end days, maximum event length repeat type
 */
public class CreateEventFragment extends BaseFragment {

    private EventViewModel mEventViewModel;
    private FragmentCreateEventBinding mBinding;

    // todo geo location
//  http://wptrafficanalyzer.in/blog/android-reverse-geocoding-using-geocoder-at-touched-location-in-google-map/
//  http://stackoverflow.com/questions/13598647/google-map-how-to-get-address-in-android


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        boolean isEditMode = false;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            isEditMode = extras.getBoolean("edit_mode");
        }
        mEventViewModel = new EventViewModel(getResources(), isEditMode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_create_event;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding = DataBindingUtil.bind(view);

        mBinding.setEvent(mEventViewModel.getEvent());
        mBinding.setEventViewModel(mEventViewModel);

//        mBinding.eventAddGlobalParams.title.setOnClickListener(getAddParamClickListener(mBinding.eventAddGlobalParams.paramsLayout, Event.PARAM_TYPE_EVENT));
//        mBinding.eventAddInstanceParams.title.setOnClickListener(getAddParamClickListener(mBinding.eventAddInstanceParams.paramsLayout, Event.PARAM_TYPE_EVENT_INSTANCE_TEMPLATE));
//        mBinding.userAddParams.title.setOnClickListener(getAddParamClickListener(mBinding.userAddParams.paramsLayout, Event.PARAM_TYPE_EVENT_PLAYER_TEMPLATE));
//        mBinding.userAddInstanceParams.title.setOnClickListener(getAddParamClickListener(mBinding.userAddInstanceParams.paramsLayout, Event.PARAM_TYPE_EVENT_PLAYER_INSTANCE_TEMPLATE));


        addDatePickerToViewClickListener(getStartDateTextView(), true);
        addDatePickerToViewClickListener(getEndDateTextView(), false);
        addTimerPickerToViewClickListener(getStartTimeTextView(), true);
        addTimerPickerToViewClickListener(getEndTimeTextView(), false);
        addRepeaTypeChooserDialogClickListener(mBinding.eventRepeat);
        addEventLocationClickListener();
        addRsvpReminderAlertClickListener();

        initActionBar();
    }

    private void initActionBar() {

        // todo iliya base
//        ActionBar ab = getSupportedActionBar();
//        if (mEventViewModel.isEditMode()) {
//            ab.setTitle(getString(R.string.event_edit));
//        } else {
//            ab.setTitle(getString(R.string.create_event));
//        }
//        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
//        ab.setDisplayHomeAsUpEnabled(true);
    }

    private void addRsvpReminderAlertClickListener() {
        mBinding.eventNotificationConfirmPresence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationUnitDialogFragment dialog = new DurationUnitDialogFragment();
                dialog.setInitialDur(mEventViewModel.getRsvmAlertDuration());
                dialog.setOnDoneClickListener(new DurationUnitDialogFragment.OnDoneClickListener() {
                    @Override
                    public void onDoneClicked(Dur duration) {
                        mEventViewModel.setRsvpReminderAlarmDuration(duration);
                    }
                });
                dialog.show(getActivity().getSupportFragmentManager(), "time_confirm_present");

            }
        });
    }

    private void addEventLocationClickListener() {
        mBinding.eventLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setMessage(getString(R.string.enter_location));

                final EditText input = new EditText(getActivity());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                if (!mBinding.eventLocation.getText().equals(getString(R.string.location))) {
                    input.setText(mBinding.eventLocation.getText());
                }
                input.setLayoutParams(lp);
                input.setSingleLine();
                alertDialog.setView(input);

                alertDialog.setPositiveButton(getString(android.R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mBinding.eventLocation.setText(input.getText().toString());
                            }
                        });

                alertDialog.setNegativeButton(getString(android.R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();

            }
        });

    }

    private void addRepeaTypeChooserDialogClickListener(TextView eventRepeat) {
//        todo

        final Event event = mEventViewModel.getEvent();
        AlertDialogFragment dialog = new AlertDialogFragment();
//        dialog.setTitle(R.string.dialog_upgrade_to_premium_title);
//        dialog.setPositiveButton(R.string.dialog_upgrade_to_premium_button, positiveClickListener);


//        final SortedMap<Integer, String> eventRepeatTypes = RecurRule.getFrequencyTypeStringMap(getActivity());
//        final String[] names = eventRepeatTypes.values().toArray(new String[eventRepeatTypes.size()]);
        eventRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//
//                View listener = LayoutInflater.from(AppApplication.getListener()).inflate(R.layout.dialog_event_frequency, null);
//                final DialogEventFrequencyBinding bind = DataBindingUtil.bind(listener);
//                bind.setRecurring(rRule);
//
                EventFrequencyDialogFragment dialog = new EventFrequencyDialogFragment();
                dialog.setCustomContentView(event);

//                AlertDialogFragment dialog = new AlertDialogFragment();
//                dialog.setTitle("Title");
//                dialog.setCustomContentView(listener);
//
//                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        int postion =  bind.frequencySpinner.getSelectedItemPosition();
//                        Object value = bind.frequencySpinner.getSelectedItem();
//                        Log.d("AA", postion + " " + value);
//                    }
//                });

                dialog.show(getFragmentManager(), "fragment_alert");


//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setItems(names, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            Integer[] keys = eventRepeatTypes.keySet().toArray(new Integer[eventRepeatTypes.size()]);
//                            @RecurRule.Frequency int frequency = keys[which];
//
//
//                            mEventViewModel.setEventFrequency(frequency);
//
//
//                        } catch (Resources.NotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });
    }

    @NonNull
    private View.OnClickListener getAddParamClickListener(final LinearLayout paramsLayout, final int paramType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                final AddParameterDialog addParameterDialog = new AddParameterDialog();
                addParameterDialog.show(supportFragmentManager, "add parameter");
                addParameterDialog.setPositiveButton(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onNewParameterSaveButtonClick(addParameterDialog.getParameter(), paramsLayout, paramType);
                    }
                });
                addParameterDialog.setNegativeButton(android.R.string.cancel, null);
            }
        };
    }

    private void onNewParameterSaveButtonClick(final Parameter param, final LinearLayout paramsLayout, final int paramType) {

        mEventViewModel.addParameter(param, paramType);

        final View newParamLayout = getActivity().getLayoutInflater().inflate(R.layout.include_add_parameter_textview, paramsLayout, false);
        paramsLayout.addView(newParamLayout, 0);
        final IncludeAddParameterTextviewBinding newParamTextViewBinding = (IncludeAddParameterTextviewBinding) DataBindingUtil.bind(newParamLayout);
        newParamTextViewBinding.setParam(param);

        newParamLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditParameterDialog(param, newParamTextViewBinding, paramsLayout, newParamLayout, paramType);
            }
        });
        ((IncludeAddParameterTextviewBinding) DataBindingUtil.bind(newParamLayout)).setParam(param);
    }

    private void showEditParameterDialog(final Parameter param, final IncludeAddParameterTextviewBinding newParamTextViewBinding, final ViewGroup parent, final View parameterLayout, final int paramType) {
        FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
        final AddParameterDialog editParamDialog = AddParameterDialog.newInstance(param, getActivity());
        editParamDialog.show(supportFragmentManager, "edit parameter");
        editParamDialog.setPositiveButton(R.string.save, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                newParamTextViewBinding.invalidateAll();
            }
        });
        editParamDialog.setNegativeButton(R.string.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEventViewModel.removeParameter(param, paramType);
                parent.removeView(parameterLayout);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:

                String title = mBinding.title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(getActivity(), getString(R.string.error_enter_title), Toast.LENGTH_SHORT).show();
                    return true;
                }

                mBinding.title.clearFocus();
                hideKeyboard();
                item.setEnabled(false);
                onSaveButtonClicked(item);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDatePickerToViewClickListener(final TextView textView, final boolean isStartDate) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = getCalendar(isStartDate);

                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        calendar.set(year, month, day);
                        // todo
                        if (isStartDate) {
                            mEventViewModel.setStartTime(calendar.getTime());
                            mEventViewModel.setEndTime(calendar.getTime());
                        } else {

                            final Calendar startDateCal = getCalendar(true);
                            if (startDateCal.after(calendar)) {
                                startDateCal.set(calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH));

                                mEventViewModel.setStartTime(calendar.getTime());
                            }

                            mEventViewModel.setEndTime(calendar.getTime());
                        }
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });
    }

    private void addTimerPickerToViewClickListener(final TextView textView, final boolean isStartDate) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = getCalendar(isStartDate);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hours, int minutes) {

                        calendar.set(Calendar.HOUR_OF_DAY, hours);
                        calendar.set(Calendar.MINUTE, minutes);
                        if (isStartDate) {
                            mEventViewModel.setStartTime(calendar.getTime());

                            Calendar endDate = Calendar.getInstance();
                            endDate.setTime(mEventViewModel.getEvent().getEndDate());
                            endDate.set(Calendar.HOUR_OF_DAY, hours + 2);
                            endDate.set(Calendar.MINUTE, minutes);
                            mEventViewModel.setEndTime(endDate.getTime());

                        } else {
                            mEventViewModel.setEndTime(calendar.getTime());
                        }
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });
    }

    private Calendar getCalendar(boolean isStartCalendar) {

        Calendar calendar = Calendar.getInstance();
        if (isStartCalendar) {
            calendar.setTime(mEventViewModel.getEvent().getStartDate());
        } else {
            calendar.setTime(mEventViewModel.getEvent().getEndDate());
        }

        return calendar;
    }

    public TextView getStartDateTextView() {
        return ((IncludeEventTimeBinding) mBinding.includeEventTime).eventStartDate;
    }

    public TextView getEndDateTextView() {
        return ((IncludeEventTimeBinding) mBinding.includeEventTime).eventEndDate;
    }

    public TextView getStartTimeTextView() {
        return ((IncludeEventTimeBinding) mBinding.includeEventTime).eventStartTime;
    }

    public TextView getEndTimeTextView() {
        return ((IncludeEventTimeBinding) mBinding.includeEventTime).eventEndTime;
    }

    private void onSaveButtonClicked(final MenuItem item) {

        mEventViewModel.setSummary(mBinding.title.getText().toString());
        mEventViewModel.setLocation(mBinding.eventLocation.getText().toString());
        Observable event = mEventViewModel.save();
        event.subscribe(new Consumer() {

            @Override
            public void accept(Object object) {

                item.setEnabled(true);
                Snackbar.make(getStartDateTextView(), "Event created", Snackbar.LENGTH_LONG).show();
                getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                }, 1000);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                View rootView = getActivity().getWindow().getDecorView().getRootView();
                Snackbar.make(rootView, "Failed " + throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
                item.setEnabled(true);
            }
        });
    }

}
