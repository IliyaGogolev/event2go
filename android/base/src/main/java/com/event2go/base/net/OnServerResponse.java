package com.event2go.base.net;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */
public interface OnServerResponse<T> {
    void onSuccess(T result);
    void onFail(Throwable e);
}
