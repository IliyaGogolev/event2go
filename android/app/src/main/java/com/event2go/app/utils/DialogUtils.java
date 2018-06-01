package com.event2go.app.utils;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.event2go.base.presentation.fragment.AlertDialogFragment;
import com.event2go.app.R;

/**
 * Created by Iliya Gogolev on 1/8/16.
 */
public class DialogUtils {


    public static void showNetworkErrorAlertDialog(AppCompatActivity activity, String message, View.OnClickListener onDismissListener) {

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            showNetworkErrorAlertDialog(fragmentManager, message, onDismissListener);
        }
    }


    public static void showNetworkErrorAlertDialog(FragmentManager fragmentManager, String message, View.OnClickListener onDismissListener) {

        AlertDialogFragment prevAlert = (AlertDialogFragment) fragmentManager.findFragmentByTag(AlertDialogFragment.class.getSimpleName());
        if (prevAlert != null) {
            prevAlert.setMessage(message);
            return;
        }

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(R.string.error_network_title);
        dialog.setMessage(message);
        dialog.setPositiveButton(R.string.ok, onDismissListener);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(dialog, AlertDialogFragment.class.getSimpleName());
        ft.commitAllowingStateLoss();
    }


    public static void showAlertDialog(FragmentManager manager, String title, String message, String positive) {
        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton(positive, null);
        dialog.show(manager, "fragment_alert");
    }

    public static void showAlertDialog(FragmentManager manager, @StringRes int titleResId, @StringRes int messageResId, @StringRes int positiveResId) {

        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(titleResId);
        dialog.setMessage(messageResId);
        dialog.setPositiveButton(positiveResId, null);
        dialog.show(manager, "fragment_alert");
    }

    public static void showAlertDialog(FragmentManager manager, @StringRes int titleResId, @StringRes int messageResId, @StringRes int positiveResId, boolean cancelable) {

        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(titleResId);
        dialog.setMessage(messageResId);
        dialog.setPositiveButton(positiveResId, null);
        dialog.setCancelable(cancelable);
        dialog.show(manager, "fragment_alert");
    }


    public static void showConfirmDialog(FragmentManager manager, String title, String message, View.OnClickListener onDismissListener) {
        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(android.R.string.ok, onDismissListener);
        dialog.setPositiveButton(android.R.string.cancel, null);
        dialog.show(manager, "fragment_alert");
    }

    public static void showDialog(FragmentManager manager, String title, String message,
                                  @StringRes int positiveButtonId, View.OnClickListener onPositivesListener,
                                  @StringRes int negativeButtonId, View.OnClickListener onDismissListener) {
        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setNegativeButton(positiveButtonId, onPositivesListener);
        dialog.setPositiveButton(negativeButtonId, onDismissListener);
        dialog.show(manager, "fragment_alert");
    }


    public static void showConfirmDialog(FragmentManager manager, @StringRes int titleResId, @StringRes int messageResId, View.OnClickListener onDismissListener) {
        if (manager == null) return;

        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.setTitle(titleResId);
        dialog.setMessage(messageResId);
        dialog.setNegativeButton(android.R.string.ok, onDismissListener);
        dialog.setPositiveButton(android.R.string.cancel, null);
        dialog.show(manager, "fragment_alert");
    }

}

