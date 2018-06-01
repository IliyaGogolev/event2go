package com.event2go.base.utils

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/**
 * Created by Iliya Gogolev on 5/29/18.
 */

class PermissionUtils(val listener: OnPermissionRequired) {

    interface OnPermissionRequired {
        fun onAllPermissionsGranted(requestCode: Int, permissions: Array<String>)
        fun onPermissionDisabled(requestCode: Int, permission: String)
        fun onShowRequestPermissionRationale(requestCode: Int, permission: String)
    }

    private fun getActivity() = if (listener is AppCompatActivity) listener else (listener as Fragment).activity
    private fun getContext() = if (listener is AppCompatActivity) listener else (listener as Fragment).context

    fun check(requestCode: Int, permissions: Array<String>): Boolean {

        if (permissions.size == 0 || checkAllPermissionsGranted(permissions)) {
            return true
        } else {
            requestPermissions(requestCode, permissions)
            return false
        }
    }

    fun requestPermissions(requestCode: Int, permissions: Array<String>) {

        if (listener is AppCompatActivity)
            ActivityCompat.requestPermissions(listener, permissions, requestCode)
        else if (listener is Fragment) {
            listener.requestPermissions(permissions, requestCode)
        }
    }

    private fun checkAllPermissionsGranted(permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(getContext()!!, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (checkAllPermissionsGranted(permissions)) {
            listener.onAllPermissionsGranted(requestCode, permissions)
        } else {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(getContext()!!, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity()!!, permission)) {
                        listener.onShowRequestPermissionRationale(requestCode, permission)
                        break
                    } else {
                        listener.onPermissionDisabled(requestCode, permission)
                        break
                    }
                }
            }
        }
    }
}