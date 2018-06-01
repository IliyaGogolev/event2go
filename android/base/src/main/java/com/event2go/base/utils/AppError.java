package com.event2go.base.utils;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */
public class AppError extends Throwable {

    public AppError(String detailMessage) {
        super(detailMessage);
    }
}
