var NOTIFICATION_TYPE_INVITE = 0;
var NOTIFICATION_TYPE_USER_JOINED_TO_EVENT = 1;
var NOTIFICATION_TYPE_MESSAGE = 2;

var NOTIFICATION_STATUS_UNREAD = 0;
var NOTIFICATION_STATUS_READ = 1;


var NOTIFICATION_COMMAND_GENERAL = "com.event2go.general";
var NOTIFICATION_COMMAND_INVITE_USER = "com.event2go.invite_user";
var NOTIFICATION_COMMAND_USER_JOINED_TO_EVENT = "com.event2go.user_joined_to_event";
var NOTIFICATION_COMMAND_MESSAGE = "com.event2go.message";


var addNotification = function (userId, notification_type, id) {
    var Notification = Parse.Object.extend("Notification");
    var notification = new Notification();

    var expiredDate = new Date();
    if (notification_type == NOTIFICATION_TYPE_INVITE) {
        expiredDate.setDate(expiredDate.getDate + 3);
    } else {
        expiredDate.setDate(expiredDate.getDate + 7)
    }

    //notification.set("expired", expiredDate);
    notification.set("status", NOTIFICATION_STATUS_UNREAD)
    notification.set("data_object_id", id);
    notification.set("type", notification_type);
    notification.set("user_id", userId);


    //notification.set("invite", invite);
    //notification.set("event", event);

    notification.save(null, {
        success: function (notification) {
            console.log('Notification object created with objectId: ' + notification.id);
        },
        error: function (invite, error) {
            console.error('Failed to create new Invite object, with error code: ' + error.message);
            //response.error(error);
        }
    });
};

var _ = require('underscore');

Parse.Cloud.define("userNotifications", function (request, response) {

    var senderUser = request.user;
    console.log("user " + senderUser);

    if (!request.user) {
        console.log("Must be signed in to call this Cloud Function");
        response.error(new Parse.Error(Parse.USERNAME_MISSING, "Must be signed in to call this Cloud Function."));
        return;
    }

    console.log("user not null");

    var expired = new Date();

    var Notification = Parse.Object.extend("Notification");
    var query = new Parse.Query(Notification);

    query.equalTo('userId', senderUser.objectId);
    query.descending("createdAt");
    query.find({
        success: function (notifications) {


            var types = [];
            var objectIds = {};
            for (var i = 0; i < notifications.length; i++) {
                var notification = notifications[i];
                console.log("notifications ids: " + notification.id);
                var type = notification.get("type");

                var typeIds = [];
                if (type in objectIds) {
                    typeIds = objectIds[type];
                } else {
                    objectIds[type] = typeIds;
                    types.push(type);
                }
                typeIds.push(notification.get("data_object_id"));
            }
            //
            //
            var promises = [];
            var promise = Parse.Promise.as();

            var dataObjectsMap = {};

            _.each(types, function (type) {
                console.log("type: " + type);

                var query;
                if (type == NOTIFICATION_TYPE_INVITE) {
                    query = new Parse.Query("Invite");
                } else if (type == NOTIFICATION_TYPE_USER_JOINED_TO_EVENT) {
                    //query = new Parse.Query("Messages");
                } else if (type == NOTIFICATION_TYPE_MESSAGE) {
                    query = new Parse.Query("Messages");
                }
                query.containedIn("objectId", objectIds[type]);
                promise = promise.then(function() {
                    return query.find();
                }).then(function (dataObjects) {
                    for (var i = 0; i < dataObjects.length; i++) {
                        var dataObj = dataObjects[i];
                        console.log("dataObj: " + dataObj.toJSON());
                        var key = (dataObj.id + "_" + type);
                        dataObjectsMap[key] = dataObj;
                    }
                });
            });

            promise.then(function() {
                var notificationJsonArr = [];
                for (var i = 0; i < notifications.length; i++) {
                    var notification = notifications[i];
                    var key = (notification.get("data_object_id") + "_" + notification.get("type"));
                    var dataObj = dataObjectsMap[key];
                    var notificationJson = notification.toJSON();
                    if (dataObj != undefined) {
                        notificationJson.data_object = dataObj.toJSON();
                        notificationJsonArr.push(notificationJson);
                    } // todo remove notification without data object
                }
                console.log("Result notifications: " + notificationJsonArr);
                response.success(notificationJsonArr);
            });


            //response.success(notifications);

            //for (var type in objectIds) {
            //    if (objectIds.hasOwnProperty(type)) {
            //        console.log(type + " -> " + objectIds[type]);
            //
            //        var ids = objectIds[type];
            //
            //        if (type == NOTIFICATION_TYPE_INVITE) {
            //            var query = new Parse.Query("Invite");
            //            query.containedIn("objectId", ids);
            //            console.log("attendee query: " + JSON.stringify(query));
            //            return query.find();
            //        }
            //    }
            //}


            //for (var i = 0; i < objectIds.length; i++) {
            //    objectIds.get
            //}


            //for (var i = 0; i < notifications.length; i++) {
            //    notifications.get("type");
            //}
            ///*******/
            //for (var i = 0; i < notifications.length; i++) {
            //    var eventJson = notifications[i].toJSON();
            //    console.log(eventJson);
            //    resultsJson.push(eventJson);
            //}
            //
            //var promise = Parse.Promise.as();
            //promise.then(function () {
            //    console.log("DONE");
            //
            //    result.sort(compareByDtStartOccurrence)
            //
            //    var resultsJson = [];
            //    for (var i = 0; i < result.length; i++) {
            //        var eventJson = result[i].toJSON();
            //        console.log(eventJson);
            //        resultsJson.push(eventJson);
            //    }
            //    response.success(resultsJson);
            //}, function (error) {
            //    response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
            //});
        },
        error: function (object, error) {
            response.error(error);
        }
    });
});

function sendPushNotification(user, dataObject, type, eventName) {
    var recipientUserId = user.id;
    console.log("recipientUserId: " + recipientUserId);
    //recipientUserId = user.get("id");
    //console.log("recipientUserId 1 : " + recipientUserId);
    // Find devices associated with the recipient user
    //var userQuery = new Parse.Query(Parse.User);
    //userQuery.equalTo('objectId', recipientUserId);

    var UserClass = Parse.Object.extend("_User");
    var userQuery = new Parse.Query(UserClass);
    userQuery.equalTo("_id", recipientUserId);


    var command = NOTIFICATION_COMMAND_GENERAL;
    var title = "Event2go";
    var alertText = "";

    switch (type) {
        case NOTIFICATION_TYPE_INVITE:
            command = NOTIFICATION_COMMAND_INVITE_USER;
            alertText = user.get("name") + " invites you to join " + eventName;
            break;
        case NOTIFICATION_TYPE_USER_JOINED_TO_EVENT:
            command = NOTIFICATION_COMMAND_USER_JOINED_TO_EVENT;
            title = "New user";
            break;

        case NOTIFICATION_TYPE_MESSAGE:
            command = NOTIFICATION_COMMAND_MESSAGE;
            title = "New message"
            break;
    }
    console.log("push title: " + alertText);
    console.log("push alert: " + alertText);

    var pushQuery = new Parse.Query(Parse.Installation);
    pushQuery.matchesQuery('user', userQuery);
    //pushQuery.equalTo('_p_user', "_User$" + recipientUserId);
    //pushQuery.equalTo('channels', 'Notification'); // Set our channel
    console.log("pushQuery = " + pushQuery);

    // Send the push notification to results of the query

    Parse.Push.send({
        where: pushQuery,
        data:{
            //time: new Date(timeStamp).toISOString(),
            //push_id: pushId,
            title: title,
            alert: alertText,
            command: command,
            notification: dataObject
        }
    }, {
        useMasterKey: true
    }).then(function () {
        console.log("Notification push was sent successfully.");
    }, function (error) {
        console.error("Push failed to send with error: " + error.message);
    });
};

Parse.Cloud.afterSave("Notification", function (request) {
    console.log("Notification afterSave trigger");
    var notification = request.object;
    var type = notification.get("type");

    // check if it's create or update. In case it's update, return
    if (notification.existed()) return;

    if (type == 0) {


        var invite_id = notification.get("data_object_id");

        console.log("invite = " + invite_id);
        var user_id = notification.get("user_id");
        console.log("user = " + user_id);

        //var event = invite.get("event");
        //console.log("event = " + event);
        //
        //console.log("event summary = " + event.get("summary"));
        console.log("************");

        var InviteClass = Parse.Object.extend("Invite");
        var invite = new Parse.Query(InviteClass);
        invite.equalTo("_id", invite_id);
        invite.find({
        //invite.fetch({
            success: function (invite) {
                console.log("Invite fetched");
                var UserClass = Parse.Object.extend("_User");
                var userQuery = new Parse.Query(UserClass);
                userQuery.equalTo("_id", user_id);
                userQuery.find({
                    success: function (user) {
                        console.log("User fetched size: " + user.length);
                        if (user.length > 0) {
                            sendPushNotification(user[0], notification, NOTIFICATION_TYPE_INVITE, invite[0].get("event_summary"));
                        }
                    },
                    error: function (user, error) {
                        console.error("Can't fetch user. " + error.message);
                        response.error(error);
                    }
                });
            },
            error: function (event, error) {
                console.error("Can't fetch invite. " + error.message);
                response.error(error);
            }
        });

    }
});

Parse.Cloud.define("loadUserNotifications", function (request, response) {
    var senderUser = request.user;
    console.log("user " + senderUser);

    if (!request.user) {
        console.log("Must be signed in to call this Cloud Function");
        response.error(new Parse.Error(Parse.USERNAME_MISSING, "Must be signed in to call this Cloud Function."));
        return;
    }

    var Notification = Parse.Object.extend("Notification");
    var query = new Parse.Query(Notification);

    query.equalTo('userId', senderUser.objectId);
    query.get(null, {
        success: function (notifications) {
            response.success(notifications);
        },
        error: function (object, error) {
            response.error(error);
        }
    });
});

Parse.Cloud.define("test", function (request, response) {

    var Test = Parse.Object.extend("Test");
    var testObj = new Test();

    var expiredDate = new Date();
    testObj.set("expired", expiredDate);
    testObj.set("interval", 60 * 60 * 24);


    testObj.save(null, {
        success: function (notification) {
            console.log('testObj object created with objectId: ' + notification.id);
            response.success(notification);
        },
        error: function (invite, error) {
            console.error('Failed to create new object, with error code: ' + error.message);
            //response.error(error);
            response.error(error);
        }
    });
});

Parse.Cloud.define("test1", function (request, response) {
    var Test = Parse.Object.extend("Test");
    var query = new Parse.Query(Test);

    query.equals('ex', senderUser.objectId);
    query.get(null, {
        success: function (notifications) {
            response.success(notifications);
        },
        error: function (object, error) {
            response.error(error);
        }
    });
});

module.exports.NOTIFICATION_TYPE_INVITE = NOTIFICATION_TYPE_INVITE;
module.exports.NOTIFICATION_TYPE_USER_JOINED_TO_EVENT = NOTIFICATION_TYPE_USER_JOINED_TO_EVENT;
module.exports.NOTIFICATION_TYPE_MESSAGE = NOTIFICATION_TYPE_MESSAGE;
module.exports.addNotification = addNotification;
