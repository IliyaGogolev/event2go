package com.event2go.app.utils;

/**
 * Created by Iliya Gogolev on 12/5/15.
 */
public interface OnCallbacListener<T> {
    /** Successful HTTP response. */
    void onSuccess(T object);

    /** Invoked when a network or unexpected exception occurred during the HTTP request. */
    void onFailure(Throwable t);
}

