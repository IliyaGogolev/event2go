var addGroup = function (title, user) {

    var Group = Parse.Object.extend("Groups");
    var group = new Group();

    //notification.set("expired", expiredDate);
    group.set("title", title);
    group.set("createdBy", user);

    var relation = group.relation("users");
    relation.add(user);

    return group.save({});

    //    (null,{
    //    success: function (object) {
    //        //response.success(object);
    //    },
    //    error: function (object, error) {
    //        response.error(error);
    //    }
    //});//(event);

    //group.save(null, {
    //    success: function (group) {
    //        console.log('Notification object created with objectId: ' + notification.id);
    //
    //        promise.resolve(grop);
    //
    //    },
    //    error: function (group, error) {
    //        console.error('Failed to create new Group object, error code: ' + error.message);
    //        response.error(error);
    //}
    //});
    //
    //return promise;
};

//
//var promise1 = Parse.Promise.as();
//promise = loadEventFeedData(promise1, event);
//
//promise.then(function () {
//
//    console.log("DONE");
//    var eventJson = event.toJSON();
//    console.log(eventJson);
//    response.success(eventJson);
//
//}, function (error) {
//    response.error("script failed with error.code: " + error.code + " error.message: " + error.message);
//});


module.exports.addGroup = addGroup;