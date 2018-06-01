Parse.Cloud.define("getUsers", function(request, response) {
    var senderUser = request.user;

    var query = new Parse.Query(Parse.User);
    query.get(null, {
        success: function(users) {
            response.success(users);
        },
        error: function(object, error) {
            response.error(error);
        }
    });
});