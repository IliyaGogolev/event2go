Parse.Cloud.job("job", function(request, status) {
  // Test function
  status.success("TestJob completed successfully.");


 // sendRVSPs();
// [today - (start_date - alarm_duration - job_interval/2)] % rrule_interval <= job_interval/2



});

var _ = require('underscore');

function sendRVSPs() {

    console.log("sendRVSPs");

    var now = new Date();

    // events not recurring that not finished
    var query1 = new Parse.Query("Event");
    query1.equalTo("recur_type", 0);
    query1.greaterThan("dtEnd", now);

    //console.log("query1 query: " + JSON.stringify(query1));

    // events recurring no end
    var query2 = new Parse.Query("Event");
    query2.equalTo("users", senderUser);
    query2.notEqualTo("recur_type", 0);
    query2.doesNotExist("recur_end");
    //console.log("query2 query: " + JSON.stringify(query2));

    // events recurring with until date
    var query3 = new Parse.Query("Event");
    query3.equalTo("users", senderUser);
    query3.notEqualTo("recur_type", 0);
    query3.exists("recur_end");
    query3.greaterThan("recur_end", now);
    //console.log("query3 query: " + JSON.stringify(query3));



// events not recurring
//  var query1 = new Parse.Query("Event");
//  query1.doesNotExist("rrule");
//  query1.greaterThan("dtEnd", now);
//
//  console.log("query1");
//
//  // events recurring
//  var query2 = new Parse.Query("Event");
//  query2.exists("rrule");
//  query2.equalTo("users", senderUser);
//
//  console.log("query2");
//
//  var compoundQuery = Parse.Query.or(query1, query2);
//  compoundQuery.find({
//    success: function (result) {
//      console.log("there was no error! " + result.length);
//      //console.log(result);
//
//      var promise = loadFeedEventsData(result);
//      promise.then(function () {
//        console.log("DONE");
//
//        result.sort(compareByDtStartOccurrence)
//
//        var resultsJson = [];
//        for (var i = 0; i < result.length; i++) {
//          var eventJson = result[i].toJSON();
//          console.log(eventJson);
//          resultsJson.push(eventJson);
//        }
//        response.success(resultsJson);
//      }, function (error) {
//        response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
//      });
//
//      //response.success(result);
//
//      //return the new modified JSON instead of the array of PFObjects
//      //var resultsJson = buildFeedEventsResponce(result)
//      //response.success(resultsJson);
//    },
//    error: function (error) {
//
//      console.log("There was an error code " + error.code + ", message: " + error.message);
//      response.error(error);
//
//    }
//  });

}
