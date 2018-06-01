/**
 * After attendee has been saved, remove extra data for exactly same event instance
 */
Parse.Cloud.afterSave("Attendee", function (request) {
    console.log('Attendee after save trigger');
    var attendee = request.object;

    //var eventId = invite.get("event_id");
    var id = attendee.id;
    var userId = attendee.get("userId");
    var eventId = attendee.get("eventId");
    var dtStart = attendee.get("dtStart");
    var date1 = new Date();
    var date2 = new Date();
    var offset = 60000;
    date1.setTime(dtStart.getTime() - offset);
    date2.setTime(dtStart.getTime() + offset);

    var query = new Parse.Query("Attendee");
    query.notEqualTo("objectId", request.object.id);
    query.equalTo("userId", userId);
    query.equalTo("eventId", eventId);
    //query.equalTo("dtStart", dtStart);
    query.greaterThan("dtStart", date1);
    query.lessThan("dtStart", date2);

    console.log("attendee query: " + JSON.stringify(query));

    query.find().then(function (data) {
        console.log("found extra attendee size: " + data.length);
        for (var i = 0; i < data.length; i++) {
            console.log(data.id);
        }
        if (data.length > 0) {
            console.log("remove extra attendee");
            return Parse.Object.destroyAll(data);
        }
    }).then(function (success) {
        console.log("extra attendees were deleted");

    }, function (error) {
        console.error("Error deleting related attendees " + error.code + ": " + error.message);
    });
});


var removeAttendeesOfEvent = function (eventId) {

    console.log("removeAttendeeOfEvent: " + eventId);

    var query = new Parse.Query("Attendee");
    query.equalTo("eventId", eventId);
    query.find({
        success: function (data) {
            console.log("found attendees to remove, size: " + data.length);
            Parse.Object.destroyAll(data, {
                success: function () {
                },
                error: function (error) {
                    console.error("Error deleting removeAttendeesOfEvent" + error.code + ": " + error.message);
                }
            });
        },
        error: function (error) {
            console.error("Error deleting removeAttendeeOfEvent" + error.code + ": " + error.message);
        }
    });
}

module.exports.removeAttendeesOfEvent = removeAttendeesOfEvent;
//
//var query = new Parse.Query("Attendee");
//query.containedIn("userId", usersIds);
//query.equalTo("eventId", event.id);
//query.equalTo("dtStart", dtStartOccurence);
////query.equalTo("dtStartMilliseconds", date);
////console.log("attendee date: " + JSON.stringify(dtStartOccurence));
//console.log("attendee query: " + JSON.stringify(query));
//return query.find();

Parse.Cloud.define("allAttendee", function (request, response) {
    var query = new Parse.Query("Attendee");
    //var query = new Parse.Query("Event");

    //query.equals('ex', senderUser.objectId);
    query.find({
        success: function (data) {
            response.success(data);
        },
        error: function (object, error) {
            response.error(error);
        }
    });
});
