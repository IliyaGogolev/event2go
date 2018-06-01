package com.event2go.app.features.user.data

import android.databinding.BaseObservable
import android.text.TextUtils

import com.event2go.app.data.AppModel
import com.event2go.app.data.ParseModel
import com.event2go.base.data.Parsable
import com.google.gson.annotations.SerializedName
import com.event2go.app.AppApplication
import com.parse.ParseObject
import com.parse.ParseUser

/**
 * Created by Iliya Gogolev on 6/4/15.
 */
//    public User(String name, String email,  String username, Date createdAt, String objectId, Date updatedAt) {
//        this.createdAt = createdAt;
//        this.id = objectId;
//        this.updatedAt = updatedAt;
//        this.email = email;
//        this.name = name;
//        this.username = username;
//    }


class User : BaseObservable(), Parsable {

    companion object {

        fun initCurrentUser(): User? {

            if (ParseUser.getCurrentUser() == null) return null

            val model = AppApplication.getContext().model
            val currentUser = User()
            currentUser.readFromParseObject(ParseUser.getCurrentUser())
            model.currentUser = currentUser
            return currentUser
        }
    }


    @SerializedName("objectId")
    var id: String? = null
    var name: String? = null
    var email: String? = null
    // used phone number as username
    var username: String? = null
        private set
    //    private String phoneNumber;

    private val updatedAt: String? = null
    private val createdAt: String? = null

    //    @SerializedName("aa")
    //    private Date updatedAt;
    //    @SerializedName("bb")
    //    private  Date createdAt;

    var gender: String? = null
    var fbId: Long = 0
    var avatarUrl: String? = null

    //    public void setPhoneNumber(String phoneNumber) {
    //        this.phoneNumber = phoneNumber;
    //    }
    //
    //    public String getPhoneNumber() {
    //        return this.phoneNumber;
    //    }

    override fun getParseCasssName(): String {
        return ParseModel.CLASS_USER
    }

    override fun writeToParseObject(parseUser: ParseObject) {

        val dest = parseUser as ParseUser
        dest.username = username
        if (!TextUtils.isEmpty(email)) {
            dest.email = email
        }
        dest.put("name", name)
        if (!TextUtils.isEmpty(gender)) {
            dest.put("gender", gender)
        }
        //        dest.add("fb_id", facebookId);
        if (!TextUtils.isEmpty(avatarUrl)) {
            dest.put("avatar_url", avatarUrl)
        }

        if (!TextUtils.isEmpty(id)) {
            dest.objectId = id
        }
    }

    override fun readFromParseObject(parseUser: ParseObject) {

        val source = parseUser as ParseUser
        id = source.objectId
        username = source.username as? String
        name = source.get("name") as? String
        gender = source.get("gender") as? String
        email = source.email
        //        Long fb_id = (Long) source.get("fb_id");
        //        if (fb_id != null) facebookId = fb_id.longValue();
        avatarUrl = source.get("avatar_url") as? String
//        username = source.username
//        name = source.get("name") as String
//        gender = source.get("gender") as String
//        email = source.email
//        //        Long fb_id = (Long) source.get("fb_id");
//        //        if (fb_id != null) facebookId = fb_id.longValue();
//        avatarUrl = source.get("avatar_url") as String
        //        updatedAt = source.getUpdatedAt();
    }


}
