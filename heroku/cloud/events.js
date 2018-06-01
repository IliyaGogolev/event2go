var RECUR_TYPE_NONE = 0;
var RECUR_TYPE_HOUR = 1;
var RECUR_TYPE_DAY = 2;
var RECUR_TYPE_WEEK = 3;
var RECUR_TYPE_MONTH = 4;
var RECUR_TYPE_COUNT = 5;


//Reply to an event request. Clients may set their
// status ("partstat") to ACCEPTED, DECLINED, TENTATIVE, or DELEGATED.
var PART_STAT_NEEDS_ACTION = "NEEDS-ACTION";
var PART_STAT_VALUE_ACCEPTED = "ACCEPTED";
var PART_STAT_VALUE_DECLINED = "DECLINED";
var PART_STAT_VALUE_TENTATIVE = "TENTATIVE";
var PART_STAT_VALUE_DELEGATED = "DELEGATED";
var PART_STAT_VALUE_COMPLETED = "COMPLETED";
var PART_STAT_VALUE_IN_PROCESS = "IN-PROCESS";

var attendee = require(__dirname + '/attendee.js');

Parse.Cloud.afterDelete("Event", function (request) {
    console.log('Event after save trigger');
    var eventId = request.object.id;

    attendee.removeAttendeesOfEvent(eventId);
});

//var groups = require(__dirname + '/groups.js');
Parse.Cloud.beforeSave("Event", function (request, response) {
    console.log('Event before save trigger');
    var event = request.object;

    // check if it's create or update. In case it's update, return
    if (!request.object.isNew()) {
        console.log("update event");
        response.success();
        return;
    }


    var Group = Parse.Object.extend("Groups");
    var group = new Group();

    //notification.set("expired", expiredDate);
    group.set("title", event.get("summary"));
    var relation = group.relation("users");
    relation.add(request.user);

    group.save(null).then(
        function(object) {
            console.log("group saved");
            // the object was saved.
            //response.success();
            var event = request.object;
            event.set("group", object);
            response.success();
        },
        function(error) {
            console.log("failed to create group");
            response.error(error);
            // saving the object failed.
            //response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
            //response.error("error create group");
        });;
});



function compareByDtStartOccurrence(a,b) {
    if (a.get("dtstart_occurrence") < b.get("dtstart_occurrence"))
        return -1;
    if (a.get("dtstart_occurrence") > b.get("dtstart_occurrence"))
        return 1;
    return 0;
}

//var groups = require(__dirname + '/groups.js');
//
//Parse.Cloud.define("addEvent", function (request, response) {
//
//    var groupId = request.params.groupId;
//
//    var promise;
//    promise = new Parse.Promise();
//    if (groupId == undefined || groupId == null) { // true if undefined, null, 0, or ""
//        // create group
//        //promise = groups.addGroup("Group", request.user);
//        var Group = Parse.Object.extend("Group");
//        var group = new Group();
//
//        //notification.set("expired", expiredDate);
//        group.set("title", "Group");
//        group.set("createdBy", request.user);
//
//        var relation = group.relation("users");
//        relation.add(request.user);
//
//        group.save(null).then(
//            function(object) {
//                // the object was saved.
//                //response.success();
//                promise.resolve( object.id );
//            },
//            function(error) {
//                // saving the object failed.
//                //response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
//                promise.reject(error);
//            });;
//
//    } else {
//        promise.resolve( groupId);
//    }
//
//    promise.then(function (groupId) {
//
//        console.log("group id " + groupId);
//        response.success();
//
//    }, function (error) {
//        response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
//    });
//
//    //var Test = Parse.Object.extend("Test");
//    //var testObj = new Test();
//    //
//    //var expiredDate = new Date();
//    //testObj.set("expired", expiredDate);
//    //testObj.set("interval", 60 * 60 * 24);
//    //
//    //
//    //testObj.save(null, {
//    //    success: function (notification) {
//    //        console.log('testObj object created with objectId: ' + notification.id);
//    //        response.success(notification);
//    //    },
//    //    error: function (invite, error) {
//    //        console.error('Failed to create new object, with error code: ' + error.message);
//    //        //response.error(error);
//    //        response.error(error);
//    //    }
//    //});
//
//});

/**
 * @return event which include:
 *    * array of users, array of attendees of upcoming event from now,
 *    * counts: { coming: 0, notComing: 0, tentative: 0, needsAction: 1 }
 *    * dtstart_occurrence: { __type: 'Date', iso: '2015-12-16T19:00:25.000Z' },
 */
Parse.Cloud.define("getFeedEvent", function (request, response) {
    var senderUser = request.user;
    console.log("user " + senderUser);
    if (!request.user) {
        console.log("Must be signed in to call this Cloud Function");
        response.error(new Parse.Error(Parse.USERNAME_MISSING, "Must be signed in to call this Cloud Function."));
        //response.error("Must be signed in to call this Cloud Function.")
        return;
    }
    var now = new Date();

    //var query = new Parse.Query("Attendee");
    //query.containedIn("userId", usersIds);
    //query.equalTo("eventId", event.id);
    //query.equalTo("dtStart", dtStartOccurence);
    ////query.equalTo("dtStartMilliseconds", date);
    ////console.log("attendee date: " + JSON.stringify(dtStartOccurence));
    //console.log("attendee query: " + JSON.stringify(query));
    //return query.find();


    var eventId = request.params.eventId;

    var query = new Parse.Query("Event");
    query.equalTo("objectId", eventId);
    query.equalTo("users", senderUser); // verify this user part of the event
    query.find({
        success: function (event) {

            console.log("there was no error! " + event);

            var resultJson = {};
            if (event.length > 0) {

                //console.log(result);
                var promise = Parse.Promise.as();

                var event = event[0];

                var promise1 = Parse.Promise.as();
                promise = loadEventFeedData(promise1, event);

                promise.then(function () {

                    console.log("DONE");
                    var eventJson = event.toJSON();
                    console.log(eventJson);
                    response.success(eventJson);

                }, function (error) {
                    response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
                });

            } else {
                console.log("Event not found");
                response.error("Event not found");
            }

        },
        error: function (error) {

            console.log("There was an error code " + error.code + ", message: " + error.message);
            response.error(error);
        }
    });
});

//
//Parse.Cloud.define("getFeedEvents", function (request, response) {
//
//    console.log("getFeedEvents test ");
//    var Events = Parse.Object.extend("Event");
//    var query = new Parse.Query(Events);
//    query.find({
//        success: function (data) {
//                console.log("success");
//                response.success(data);
//        },
//        error: function (error) {
//            console.log("There was an error code " + error.code + ", message: " + error.message);
//            response.error(error);
//
//        }
//    });
//});


Parse.Cloud.define("getFeedEvents", function (request, response) {

    var senderUser = request.user;
    console.log("user " + senderUser);

    if (!request.user) {
        console.log("Must be signed in to call this Cloud Function");
        response.error(new Parse.Error(Parse.USERNAME_MISSING, "Must be signed in to call this Cloud Function."));
        //response.error("Must be signed in to call this Cloud Function.")
        return;
    }

    console.log("user not null");

    var now = new Date();

    var groupQuery = new Parse.Query("Groups");
    groupQuery.equalTo("users", senderUser);
    groupQuery.find({
        success: function (groups) {
            // events not recurring that not finished
            var query1 = new Parse.Query("Event");
            query1.equalTo("recur_type", 0);
            query1.containedIn("group", groups);
            //query1.equalTo("users", senderUser);
            query1.greaterThan("dtEnd", now);

            console.log("query1 query: " + JSON.stringify(query1));

            // events recurring no endkeramica

            var query2 = new Parse.Query("Event");
            query2.containedIn("group", groups);
            //query2.equalTo("users", senderUser);
            query2.notEqualTo("recur_type", 0);
            query2.doesNotExist("recur_end");
            console.log("query2 query: " + JSON.stringify(query2));
            //
            // events recurring with until date
            var query3 = new Parse.Query("Event");
            query3.containedIn("group", groups);
            //query3.equalTo("users", senderUser);
            query3.notEqualTo("recur_type", 0);
            query3.exists("recur_end");
            query3.greaterThan("recur_end", now);
            console.log("query3 query: " + JSON.stringify(query3));

            var result = [];

            query1.find({
                success: function (queryResult) {
                    console.log("there was no error, found events: " + queryResult.length);
                    //result.push(queryResult);
                    Array.prototype.push.apply(result, queryResult);
                    query2.find({
                        success: function (queryResult) {
                            Array.prototype.push.apply(result, queryResult);
                            query3.find({
                                success: function (queryResult) {
                                    console.log("there was no error, found events: " + queryResult.length);
                                    //console.log(result);

                                    //result.push(queryResult);
                                    Array.prototype.push.apply(result, queryResult);

                                    var promise = loadFeedEventsData(result);
                                    promise.then(function () {
                                        console.log("DONE");

                                        result.sort(compareByDtStartOccurrence)

                                        var resultsJson = [];
                                        for (var i = 0; i < result.length; i++) {
                                            var eventJson = result[i].toJSON();
                                            console.log(eventJson);
                                            resultsJson.push(eventJson);
                                        }
                                        response.success(resultsJson);
                                    }, function (error) {
                                        console.log("script failed with error.code: " + error.code + " error.message: " + error.message);
                                        response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
                                    });
                                },
                                error: function (error) {

                                    console.log("There was an error code " + error.code + ", message: " + error.message);
                                    response.error(error);

                                }
                            });
                        },
                        error: function (error) {

                            console.log("There was an error code " + error.code + ", message: " + error.message);
                            response.error(error);

                        }});
                },
                error: function (error) {

                    console.log("There was an error code " + error.code + ", message: " + error.message);
                    response.error(error);

                }
            });
        },
        error: function (error) {
            response.error(error);

        }
    });





            //var compoundQuery = Parse.Query.or(query1);//, query2, query3);
    //query2.find({
    //    success: function (result) {
    //        console.log("there was no error, found events: " + result.length);
    //        //console.log(result);
    //
    //        var promise = loadFeedEventsData(result);
    //        promise.then(function () {
    //            console.log("DONE");
    //
    //            result.sort(compareByDtStartOccurrence)
    //
    //            var resultsJson = [];
    //            for (var i = 0; i < result.length; i++) {
    //                var eventJson = result[i].toJSON();
    //                console.log(eventJson);
    //                resultsJson.push(eventJson);
    //            }
    //            response.success(resultsJson);
    //        }, function (error) {
    //            console.log("script failed with error.code: " + error.code + " error.message: " + error.message);
    //            response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
    //        });
    //
    //        //response.success(result);
    //
    //        //return the new modified JSON instead of the array of PFObjects
    //        //var resultsJson = buildFeedEventsResponce(result)
    //        //response.success(resultsJson);
    //    },
    //    error: function (error) {
    //
    //        console.log("There was an error code " + error.code + ", message: " + error.message);
    //        response.error(error);
    //
    //    }
    //});
});

var _ = require('underscore');
var RRule = require(__dirname + '/rrule').RRule;

/**
 * Return promise to event users query
 * @param event
 * @returns {*}
 */
var findEventUsers = function (event) {

    var group = event.get("group");

    var usersRelation = group.relation("users");
    return usersRelation.query().find();

    //var usersRelation = event.relation("users");
    //return usersRelation.query().find();
};

function loadEventFeedData(promise, event) {
    promise = promise.then(function () {
        /**
         * Load Event Users
         */

            //get relation for each state
            //console.log(event.get("summary"));
        console.log("event json data");
        console.log(event.toJSON());
        console.log("call findEventUsers");
        return findEventUsers(event);

    }).then(function (userResults) {

        /**
         * Load Event Users
         */

        var usersData = [];
        var usersIds = [];
        for (var i = 0; i < userResults.length; i++) {
            console.log("users ids: " + userResults[i].id);
            usersData.push(userResults[i].toJSON());
            usersIds.push(userResults[i].id);
        }
        event.set("users", usersData);

        var recurType = event.get("recur_type");
        var eventDtStart = event.get("dtStart");
        var eventDtEnd = event.get("dtEnd");
        var duration = eventDtEnd.getTime() - eventDtStart.getTime();

        if (recurType !== undefined && recurType !== RECUR_TYPE_NONE) {
            // recurring event

            // Create a rule:
            var rule = null;
            switch (recurType) {

                case RECUR_TYPE_HOUR:
                    rule = new RRule({
                        freq: RRule.HOURLY,
                        dtstart: eventDtStart
                    });
                    break;

                case RECUR_TYPE_DAY:
                    rule = new RRule({
                        freq: RRule.DAILY,
                        dtstart: eventDtStart
                    });
                    break;
                
                case RECUR_TYPE_WEEK:
                    rule = new RRule({
                        freq: RRule.WEEKLY,
                        dtstart: eventDtStart
                    });
                    break;

                case RECUR_TYPE_MONTH:
                    rule = new RRule({
                        freq: RRule.MONTHLY,
                        dtstart: eventDtStart
                    });
                    break;

                //case RECUR_TYPE_YEAR:
                //    untilDate.setDate(now.getDate());
                //    untilDate.setYear(now.getYear() + 1);
                //    break;

            }

            //var options = RRule.parseString(recurType)
            //options.dtstart = eventDtStart
            //var rule = new RRule(options)

            var now = new Date();
            var fromDate = new Date();
            fromDate.setTime(now.getTime() - duration);
            var untilDate = new Date();
            // todo - now - minus event duration, maybe still occurring
            switch (recurType) {
                case RECUR_TYPE_DAY:
                    untilDate.setDate (now.getDate() + 1); // plus 1 day from now
                    break;
                //case RECUR_TYPE_YEAR:
                //    untilDate.setDate(now.getDate());
                //    untilDate.setYear(now.getYear() + 1);
                //    break;
                case RECUR_TYPE_MONTH:
                    untilDate.setDate(now.getDate());
                    untilDate.setMonth(now.getMonth() + 1);
                    break;
                case RECUR_TYPE_WEEK:
                    untilDate.setDate(now.getDate() + 7); // plus 7 day, since the event occurs every day
                    break;
            }
            var periods = rule.between(fromDate, untilDate, true);
            if (periods.length > 0) {

                // round up
                var date =  Math.floor(periods[0].getTime()/1000);
                date = date*1000;
                var dtStartOccurence = new Date(date);
                var date1 = new Date();
                var date2 = new Date();

                event.set("dtstart_occurrence", dtStartOccurence);

                var query = new Parse.Query("Attendee");
                query.containedIn("userId", usersIds);
                query.equalTo("eventId", event.id);
                var offset = 60000; // one minute +/-
                date1.setTime(dtStartOccurence.getTime() - offset);
                date2.setTime(dtStartOccurence.getTime() + offset);

                query.greaterThan("dtStart", date1);
                query.lessThan("dtStart", date2);

                //query.equalTo("dtStart", dtStartOccurence);
                //query.equalTo("dtStartMilliseconds", date);
                //console.log("attendee date: " + JSON.stringify(dtStartOccurence));
                console.log("attendee query: " + JSON.stringify(query));
                return query.find();
            } else {
                // can't happen, bug (this code just in case)
                event.set("attendees", null);
                event.set("dtstart_occurrence", eventDtStart);
                //var eventJson = event.toJSON();
                //console.log(eventJson);
                //resultsJson.push(eventJson);
            }
        } else {
            event.set("dtstart_occurrence", eventDtStart);

            var date1 = new Date();
            var date2 = new Date();
            var offset = 60000;
            date1.setTime(eventDtStart.getTime() - offset);
            date2.setTime(eventDtStart.getTime() + offset);

            // not recurring event
            var query = new Parse.Query("Attendee");
            query.equalTo("eventId", event.id);
            query.containedIn("userId", usersIds);
            query.greaterThan("dtStart", date1);
            query.lessThan("dtStart", date2);

            console.log("attendee query not recurring event: " + JSON.stringify(query));

            return query.find();
        }

    }).then(function (attendees) {

        if (attendees !== undefined) {

            addAttendeesToEvent(event, attendees);

        } else {

            addAttendeesToEvent(event, null);



        }

        //var eventJson = event.toJSON();
        //console.log(eventJson);
        //resultsJson.push(eventJson);
    });

    return promise;

}
function loadFeedEventsData(events) {

    var promise = Parse.Promise.as();

    _.each(events, function (event) {

        // for each one waiting for the previous to finish
        promise = loadEventFeedData(promise, event);
        return promise;

    });


    return promise;
}

function addAttendeesToEvent(event, attendees) {



    var coming = 0, notCoiming = 0, maybeComing = 0;
    var users = event.get("users");

    var attendeesJson = [];
    if (attendees != null) {
        console.log("attendee length: " + attendees.length);

        for (var i = 0; i < attendees.length; i++) {
            var attendee = attendees[i];
            console.log("attendee id: " + attendee.id);

            var attendeeJson = attendee.toJSON();
            console.log("AA");
            console.log(attendeeJson);
            //console.log(attendee.get("dtStart").toJSON());
            attendeesJson.push(attendeeJson);

            // PARTSTAT - based to iCal rfs 2446 http://ietfreport.isoc.org/idref/rfc2446/
            var partstat = attendee.get("partstat");
            if (partstat == PART_STAT_VALUE_ACCEPTED) {
                coming++;
            } else if (partstat == PART_STAT_VALUE_DECLINED) {
                notCoiming++;
            } else if (partstat == PART_STAT_VALUE_TENTATIVE) {
                maybeComing ++;
            }
        }
    } else {
        console.log("attendee length: 0");
    }

    var needsAction = users.length - (coming + notCoiming + maybeComing);

    var counts = {}
    counts["coming"] = coming;
    counts["notComing"] = notCoiming;
    counts["tentative"] = maybeComing;
    counts["needsAction"] = needsAction;

    //var attendeesResponce = {};
    event.set("attendees",attendeesJson);
    event.set("counts",counts);

    console.log("counts: " + counts);

    //event.set("comingCount", coming);
    //event.set("notComingCount", notCoiming);
    //event.set("maybeComingAmount", maybeComing);
    //event.set("needsAction", needsAction);
}
//
//function buildFeedEventsResponce(events) {
//
//    var successful = new Parse.Promise();
//
//    var resultsJson = [];
//    for (var i = 0; i < events.length; i++) {
//        var event = events[i];
//        var eventJson = event.toJSON();
//        event.relation("users").query().find({
//            success: function (results) {
//                console.log("got users");
//                //response.success(results);
//            },
//            error: function (err) {
//                console.log("failed to get users");
//                //response.error(err);
//            }
//        }).then(function (users) {
//
//        });
//
//
//        resultsJson.push(eventJson);
//    }
//
//
//    return successful;
//}
//
//function setCurrentRecurringEventsDates(events) {
//
//    var resultsJson = [];
//    var now = new Date();
//
//    for (var i = 0; i < events.length; i++) {
//
//        var event = events[i];
//
//
//        //console.log("event");
//        //console.log(event);
//
//        if (event.get("repeat_type") != RECUR_TYPE_NONE) {
//            var startDate = event.get("start_date");
//            var endDate = event.get("end_date");
//            var repeatType = event.get("repeat_type");
//
//            console.log("startDate");
//            console.log(startDate);
//
//            var duration = endDate.getTime() - startDate.getTime();
//
//
//            //Calendar lastStartDate = Calendar.getInstance();
//
//            switch (repeatType) {
//                case RECUR_TYPE_DAY:
//                    endDate.setFullYear(now.getFullYear(), now.getMonth(), now.getDate());
//                    if (dates.compare(now, endDate) > 0) { // now after endDate
//                        endDate.setDate(now.getDate() + 1); // plus one day, since the event occurs every day
//                        startDate.setFullYear(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());
//
//                        event.set("start_date", startDate);
//                        event.set("end_date", endDate);
//                    } else {
//                        startDate.setTime(endDate.getTime() - duration);
//
//                        event.set("start_date", startDate);
//                        event.set("end_date", endDate);
//
//                    }
//                    break;
//                //case DurationUnit.RECUR_TYPE_WEEK:
//                //    int nowWeekDay = now.get(Calendar.DAY_OF_WEEK);
//                //
//                //    int startWeekDay = startDate.get(Calendar.DAY_OF_WEEK);
//                //    // calculate last event from today, check if it still going
//                //
//                //    lastStartDate.set(Calendar.HOUR, startDate.get(Calendar.HOUR));
//                //    lastStartDate.set(Calendar.MINUTE, startDate.get(Calendar.MINUTE));
//                //    lastStartDate.set(Calendar.SECOND, startDate.get(Calendar.SECOND));
//                //    lastStartDate.add(Calendar.DATE, (nowWeekDay < startWeekDay) ? -(nowWeekDay + 7 - startWeekDay) : -(nowWeekDay - startWeekDay));
//                //
//                //    Calendar lastEndDate = Calendar.getInstance();
//                //    lastEndDate.setTime(new Date(lastStartDate.getTime().getTime() + duration));
//                //
//                //    if (now.after(lastEndDate)) {
//                //        lastStartDate.add(Calendar.DATE, 7); // add week
//                //        lastEndDate.setTime(new Date(lastStartDate.getTime().getTime() + duration));
//                //
//                //        event.setStartDate(lastStartDate);
//                //        event.setEndDate(lastEndDate);
//                //    } else { // still going
//                //        event.setStartDate(lastStartDate);
//                //        event.setEndDate(lastEndDate);
//                //    }
//                //    break;
//                //case DurationUnit.RECUR_TYPE_MONTH:
//                //
//                //    startDate.set(Calendar.YEAR, now.get(Calendar.YEAR));
//                //    startDate.set(Calendar.MONTH, now.get(Calendar.MONTH));
//                //
//                //    endDate.setTime(new Date(startDate.getTime().getTime() + duration));
//                //
//                //    if (now.after(endDate)) { // ended, otherwise still going
//                //        startDate.add(Calendar.MONTH, 1); // add week
//                //        endDate.setTime(new Date(startDate.getTime().getTime() + duration));
//                //
//                //        event.setStartDate(startDate);
//                //        event.setEndDate(endDate);
//                //    }
//                //    break;
//            }
//        }
//
//        resultsJson.push(event.toJSON());
//    }
//
//    return resultsJson;
//}
//
//Parse.Cloud.define("addUserToEvent", function (request, response) {
//    var senderUser = request.user;
//    var eventId = request.params.eventId;
//
//    var Event = Parse.Object.extend("Event");
//    var query = new Parse.Query(Event);
//    query.get(eventId, {
//        success: function (event) {
//
//        },
//        error: function (object, error) {
//            response.error(error);
//        }
//    });
//    // Find devices associated with the recipient user
//    var query = new Parse.Query(Parse.User);
//    query.equalTo('objectId', recipientUserId);
//
//    var pushQuery = new Parse.Query(Parse.Installation);
//    pushQuery.matchesQuery('user', query);
//    pushQuery.equalTo("channels", "Invite"); // Set our channel
//
//    // Send the push notification to results of the query
//    Parse.Push.send({
//        where: pushQuery,
//        data: {
//            alert: message,
//            command: command,
//            senderId: senderUserId,
//            eventId: offeredEventId
//        }
//    }).then(function () {
//        response.success("Push was sent successfully.")
//    }, function (error) {
//        response.error("Push failed to send with error: " + error.message);
//    });
//});

Parse.Cloud.define("addUserToEvent", function (request, response) {

    var eventId = request.params.eventId;
    var userId = request.params.userId;

    console.log("addUserToEvent eventId " + eventId + " userId " + userId);

    addUserToEvent(eventId, userId);

});

/**
 * Add user to event
 */
var addUserToEvent = function (eventId, userId) {
    console.log("addUserToEvent");

    var Event = Parse.Object.extend("Event");
    var query = new Parse.Query(Event);
    query.include("group");
    query.get(eventId, {
        success: function (event) {

            var User = Parse.Object.extend("User");
            var user = new User();
            user.id = userId;

            //var relation = event.relation("users");
            //relation.add(user);
            //event.save();

            var group = event.get("group");
            var relation = group.relation("users");
            relation.add(user);
            group.save();


        },
        error: function (object, error) {
            console.log("failed to add userId: " + userId + " to eventId: " + eventId);
        }
    });


    // new code

    //var relation = group.relation("users");
    //relation.add(request.user);
    //
    //group.save(null).then(
    //    function(object) {
    //        console.log("group saved");
    //        // the object was saved.
    //        //response.success();
    //        var event = request.object;
    //        event.set("group", object);
    //        response.success();
    //    },
    //    function(error) {
    //        console.log("failed to create group");
    //        response.error(error);
    //        // saving the object failed.
    //        //response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
    //        //response.error("error create group");
    //    });;

}

var dates = {
    convert: function (d) {
        // Converts the date in d to a date-object. The input can be:
        //   a date object: returned without modification
        //  an array      : Interpreted as [year,month,day]. NOTE: month is 0-11.
        //   a number     : Interpreted as number of milliseconds
        //                  since 1 Jan 1970 (a timestamp)
        //   a string     : Any format supported by the javascript engine, like
        //                  "YYYY/MM/DD", "MM/DD/YYYY", "Jan 31 2009" etc.
        //  an object     : Interpreted as an object with year, month and date
        //                  attributes.  **NOTE** month is 0-11.
        return (
            d.constructor === Date ? d :
                d.constructor === Array ? new Date(d[0], d[1], d[2]) :
                    d.constructor === Number ? new Date(d) :
                        d.constructor === String ? new Date(d) :
                            typeof d === "object" ? new Date(d.year, d.month, d.date) :
                                NaN
        );
    },
    compare: function (a, b) {
        // Compare two dates (could be of any type supported by the convert
        // function above) and returns:
        //  -1 : if a < b
        //   0 : if a = b
        //   1 : if a > b
        // NaN : if a or b is an illegal date
        // NOTE: The code inside isFinite does an assignment (=).
        return (

            isFinite(a = this.convert(a).valueOf()) &&
            isFinite(b = this.convert(b).valueOf()) ?
            (a > b) - (a < b) :
                NaN
        );
    },
    inRange: function (d, start, end) {
        // Checks if date in d is between dates in start and end.
        // Returns a boolean or NaN:
        //    true  : if d is between start and end (inclusive)
        //    false : if d is before start or after end
        //    NaN   : if one or more of the dates is illegal.
        // NOTE: The code inside isFinite does an assignment (=).
        return (
            isFinite(d = this.convert(d).valueOf()) &&
            isFinite(start = this.convert(start).valueOf()) &&
            isFinite(end = this.convert(end).valueOf()) ?
            start <= d && d <= end :
                NaN
        );
    }
}

module.exports.addUserToEvent = addUserToEvent;