package com.event2go.app.features.event.data;

import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.event2go.app.BR;
import com.event2go.app.data.ParseModel;
import com.event2go.base.data.BaseParseObject;

/**
 * Created by Iliya Gogolev on 8/14/15.
 */
public class Parameter extends BaseParseObject implements Parcelable {

//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({PARAMETER_TYPE_GLOBAL, PARAMETER_TYPE_INSTANCE})
//    public @interface ParameterType {
//    }
//
//    public static final int PARAMETER_TYPE_GLOBAL= 0;
//    public static final int PARAMETER_TYPE_INSTANCE = 1;

    private String mDialogTitle = "";

    public Parameter() {

    }

    @Override
    protected String getParseCasssName() {
        return ParseModel.CLASS_PARAMETER;
    }

    @Bindable
    public String getName() {
        return (String) get("name");
    }

    public void setName(String name) {
        put("name", name);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getValue() {
        return (String) get("value");
    }

    public void setValue(String value) {
        put("value", value);
        notifyPropertyChanged(BR.value);
    }

    public void setDialogTitle(String title){
        mDialogTitle = title;
    }

    public String getDialogTitle() {
        return mDialogTitle;
    }

    // parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                getName(),
                getValue()});
    }

    public void setEvent(Event event) {
        // todo
//        put("event", event.toParseObject());

    }

    protected Parameter(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        setName(data[0]);
        setValue(data[1]);
    }

    public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
        @Override
        public Parameter createFromParcel(Parcel in) {
            return new Parameter(in);
        }

        @Override
        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };


}
