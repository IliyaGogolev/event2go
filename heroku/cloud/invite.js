Parse.Cloud.define("sendInvite", function(request, response) {
  var senderUser = request.user;
  var eventId = request.params.eventId;
  var senderUserId = request.params.senderId;
  var recipientUserId = request.params.recipientId;
  var command = request.params.command;

  // Save invite to DB
  var Invite = Parse.Object.extend("Invite");
  var invite = new Invite();

  invite.set("event_id", eventId);
  invite.set("user_id", recipientUserId);
  invite.set("invited_by", senderUserId);

  invite.save(null, {
    success: function(invite) {
      console.log('Invite object created with objectId: ' + invite.id);
      response.success(invite);
    },
    error: function(invite, error) {
      console.log('Failed to create new Invite object, with error code: ' + error.message);
      response.error(error);
    }
  });
});


Parse.Cloud.define("loadInvite", function(request, response) {
  var senderUser = request.user;
  var inviteId = request.params.inviteId;

  var Invite = Parse.Object.extend("Invite");
  var query = new Parse.Query(Invite);
  query.get(inviteId, {
    success: function(invite) {
      // The object was retrieved successfully.
      response.success(invite);
    },
    error: function(object, error) {
      // The object was not retrieved successfully.
      // error is a Parse.Error with an error code and message.
      response.error(error);
    }
  });
});

Parse.Cloud.define("loadAllInvites", function(request, response) {
  var senderUser = request.user;

  var Invite = Parse.Object.extend("Invite");
  var query = new Parse.Query(Invite);

  query.equalTo('userId', senderUser.objectId);
  query.get(null, {
    success: function(invites) {
      response.success(invites);
    },
    error: function(object, error) {
      response.error(error);
    }
  });
});

Parse.Cloud.define("deleteInvite", function(request, response) {
  var senderUser = request.user;
  var inviteId = request.params.inviteId;

  var Invite = Parse.Object.extend("Invite");
  var query = new Parse.Query(Invite);
  query.get(inviteId, {
    success: function(invite) {
      invite.destroy()({
        success: function(invite) {
          response.success(invite);
        },
        error: function(invite, error) {
          response.error(error);
        }
      });
    },
    error: function(object, error) {
      response.error(error);
    }
  });
});

Parse.Cloud.define("acceptInvite", function(request, response) {
  var senderUser = request.user;
  var inviteId = request.params.inviteId;

  var Invite = Parse.Object.extend("Invite");
  var query = new Parse.Query(Invite);
  query.get(inviteId, {
    success: function(invite) {
      invite.destroy()({
        success: function(invite) {
          response.success(invite);
        },
        error: function(invite, error) {
          response.error(error);
        }
      });
    },
    error: function(object, error) {
      response.error(error);
    }
  });
});

Parse.Cloud.define("declineInvite", function(request, response) {
  var senderUser = request.user;
  var inviteId = request.params.inviteId;

  var Invite = Parse.Object.extend("Invite");
  var query = new Parse.Query(Invite);
  query.get(inviteId, {
    success: function(invite) {
      invite.destroy()({
        success: function(invite) {
          response.success(invite);
        },
        error: function(invite, error) {
          response.error(error);
        }
      });
    },
    error: function(object, error) {
      response.error(error);
    }
  });
});

var notification = require(__dirname + '/notifications.js');
var event = require(__dirname + '/events.js');

/**
 * After invite has been saved, add invited user to event and send him notification
 */
Parse.Cloud.afterSave("Invite", function(request) {
  console.log('invite after save trigger');
  var invite = request.object;

  //var eventId = invite.get("event_id");
  var userId = invite.get("user_id");
  notification.addNotification(userId, notification.NOTIFICATION_TYPE_INVITE, invite.id);
  event.addUserToEvent(invite.get("event_id"), userId);

});
