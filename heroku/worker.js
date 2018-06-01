var Parse = require('parse/node');
//Parse.initialize('app-id', 'js-key','master-key');
Parse.initialize('MofV8rBJbmoa5QTbmyGt6KjfKHR0AcGZ0OprSAqf', 'MKyfgvrYhuOFdBY0XPs8DCHz0rQWL6osDAkAJK34', 'Ea4JKv504lBVQiw3MXak37hYwsu0aCg7qaMlXxCe');
Parse.serverURL = 'https://event2go.herokuapp.com/parse/';
Parse.Cloud.useMasterKey();
//
//function saveSomething(){
//    var MyClass = Parse.Object.extend("MyClass");
//    var myclass = new MyClass();
//    myclass.set("columnName", "value");
//    myclass.save({
//        success: function(place){
//            console.log("Success!!");
//        },
//        error: function(place, error){
//            console.log("Fail: " + error.message);
//        }
//    });
//}
//
//saveSomething();


//var RRule = require(__dirname + '/cloud/rrule').RRule;
//require('parse')
//require(__dirname + '/cloud/notifications.js')
// require('/app/cloud/jobs.js');
//require(__dirname + '/cloud/invite.js');
//require(__dirname + '/cloud/events.js');
//require(__dirname + '/cloud/users.js');
//require(__dirname + '/cloud/attendee.js')


function sayHello() {
    console.log('Hello');

    console.log("getFeedEvents test ");
    var Events = Parse.Object.extend("Event");
    console.log("getFeedEvents test 1 ");
    var query = new Parse.Query(Events);
    query.find({
        success: function (result) {
            console.log("success " + result.length);
            for (var i = 0; i < result.length; i++) {
                var eventJson = result[i].toJSON();
                console.log(eventJson);
            }
            //response.success(result);
        },
        error: function (error) {
            console.log("There was an error code " + error.code + ", message: " + error.message);
            //response.error(error);
        }
    });
}

function sendNotification() {
    var UserClass = Parse.Object.extend("_User");
    var userQuery = new Parse.Query(UserClass);
    //userQuery.equalTo("_id", recipientUserId);


    var pushQuery = new Parse.Query(Parse.Installation);
    //pushQuery.matchesQuery('user', userQuery);
    //pushQuery.equalTo('_p_user', "_User$" + recipientUserId);
    //pushQuery.equalTo('channels', 'Notification'); // Set our channel
    console.log("pushQuery = " + pushQuery);

    // Send the push notification to results of the query

    Parse.Push.send({
        where: pushQuery,
        data:{
            //time: new Date(timeStamp).toISOString(),
            //push_id: pushId,
            title: "title",
            alert: "alertText",
            command: "com.event2go.general",
            notification: "dataObject"
        }
    }, {
        useMasterKey: true
    }).then(function () {
        console.log("Notification push was sent successfully.");
    }, function (error) {
        console.error("Push failed to send with error: " + error.message);
    });
}

//sayHello();
//sendNotification();

require(__dirname + '/cloud/notifications.js');