package com.event2go.app.features.user.domain;

import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.event2go.app.AppApplication;
import com.event2go.base.utils.AppError;
import com.event2go.app.features.user.data.User;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Iliya Gogolev on 6/5/15.
 */
public class GetFacebookUserInfoUseCase {

    public interface OnUserProfile {
        void onUserProfileReceived(User user);

        void onUserProfileFailed(Throwable error);
    }

    // retrieve a user's own profile.
    public static void loadUserInfo(final OnUserProfile listener) {

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        if (jsonObject != null) {
//                            JSONObject userProfile = new JSONObject();
                            User user = AppApplication.getContext().getCurrentUser();
                            try {
                                user.setFbId(jsonObject.getLong("id"));
                                user.setName(jsonObject.getString("name"));

                                if (jsonObject.getString("gender") != null) {
                                    user.setGender(jsonObject.getString("gender"));
                                }

                                if (jsonObject.getString("email") != null) {
                                    user.setEmail(jsonObject.getString("email"));
                                }

                                user.setAvatarUrl("http://graph.facebook.com/" + jsonObject.getLong("id") + "/picture?type=large");

                                ParseUser parseUser = ParseUser.getCurrentUser();
                                user.writeToParseObject(parseUser);
                                parseUser.saveInBackground();

                                listener.onUserProfileReceived(user);
//                                updateViewsWithProfileInfo();
                            } catch (JSONException e) {
                                Log.d("ProfileActivity", "Error parsing returned user data. " + e);
                                listener.onUserProfileFailed(e);
                            }

                        } else if (graphResponse.getError() != null) {

                            switch (graphResponse.getError().getCategory()) {
                                case LOGIN_RECOVERABLE:

                                    Log.d("ProfileActivity", "Authentication error: " + graphResponse.getError());
                                    listener.onUserProfileFailed(new AppError("Authentication error:" + graphResponse.getError()));
                                    break;

                                case TRANSIENT:
                                    Log.d("ProfileActivity", "Transient error. Try again. " + graphResponse.getError());
                                    listener.onUserProfileFailed(new AppError("Transient error:" + graphResponse.getError()));
                                    break;

                                case OTHER:
                                    Log.d("ProfileActivity", "Some other error: " + graphResponse.getError());
                                    listener.onUserProfileFailed(new AppError("Some other error:" + graphResponse.getError()));
                                    break;
                            }
                        }
                    }
                });

        request.executeAsync();
    }

//    private void updateViewsWithProfileInfo() {
//        ParseUser currentUser = ParseUser.getCurrentUser();
//        if (currentUser.has("profile")) {
//            JSONObject userProfile = currentUser.getJSONObject("profile");
//            try {
//
//                if (userProfile.has("facebookId")) {
//                    userProfilePictureView.setProfileId(userProfile.getString("facebookId"));
//                } else {
//                    // Show the default, blank user profile picture
//                    userProfilePictureView.setProfileId(null);
//                }
//
//                if (userProfile.has("name")) {
//                    userNameView.setText(userProfile.getString("name"));
//                } else {
//                    userNameView.setText("");
//                }
//
//                if (userProfile.has("gender")) {
//                    userGenderView.setText(userProfile.getString("gender"));
//                } else {
//                    userGenderView.setText("");
//                }
//
//                if (userProfile.has("email")) {
//                    userEmailView.setText(userProfile.getString("email"));
//                } else {
//                    userEmailView.setText("");
//                }
//
//            } catch (JSONException e) {
//                Log.d("ProfileActivity", "Error parsing saved user data.");
//            }
//        }
//    }
}
