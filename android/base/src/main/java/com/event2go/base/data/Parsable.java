package com.event2go.base.data;

import android.databinding.BaseObservable;

import com.parse.ParseObject;

/**
 * Created by Iliya Gogolev on 10/9/15.
 */
public interface Parsable {

    /**
     * Describe the parse object class
     */
    String getParseCasssName();

    /**
     * Flatten this object in to a ParseObject.
     *
     * @param dest The ParseObject in which the object should be written.
     */
    void writeToParseObject(ParseObject dest);

    /**
     * Initialize  a current instance of the ParseObject class, instantiating it
     * from the given ParseObject whose data had previously been written by
     *
     * @param source The ParseObject to read the object's data from.
     */
    void readFromParseObject(ParseObject source);
}