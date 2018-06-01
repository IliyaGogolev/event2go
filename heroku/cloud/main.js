//console.log("dirname: " + __dirname);
var RRule = require(__dirname + '/rrule').RRule;
require(__dirname + '/notifications.js');
// require('/app/cloud/jobs.js');
require(__dirname + '/invite.js');
require(__dirname + '/events.js');
require(__dirname + '/users.js');
require(__dirname + '/attendee.js')
require(__dirname + '/groups.js')


Parse.Cloud.define('hello', function (req, res) {
    res.success('Hi Dennis & Iliya  1 !!!');
});


//https://github.com/ParsePlatform/AnyPhone

var twilioAccountSid = 'AC088112f3619f35cc44aeae61990310da';
var twilioAuthToken = '0e72b46778b592f898a1281d541ec8b1';
var twilioPhoneNumber = '+14243200808';
var secretPasswordToken = 'B60Zlk=x1g3N';

var language = "en";
var languages = ["en", "es", "ja", "kr", "pt-BR"];

var client = require('twilio')(twilioAccountSid, twilioAuthToken);

Parse.Cloud.define("sendCode", function (req, res) {

    console.log("SEND CODE");

    var phoneNumber = req.params.phoneNumber;
    phoneNumber = phoneNumber.replace(/\D/g, '');

    var lang = req.params.language;
    if (lang !== undefined && languages.indexOf(lang) != -1) {
        language = lang;
    }

    if (!phoneNumber || (phoneNumber.length != 10 && phoneNumber.length != 11)) return res.error('Invalid Parameters');

    var num;
    if (req.params.debug == 1) num = 1855;

    Parse.Cloud.useMasterKey();
    var query = new Parse.Query(Parse.User);
    query.equalTo('username', phoneNumber + "");
    query.first().then(function (result) {
        var min = 1000;
        var max = 9999;
        if (!(num !== undefined && num >= 1000 && num <= 9999)) {
            num = Math.floor(Math.random() * (max - min + 1)) + min;
        }

        if (result) {
            result.setPassword(secretPasswordToken + num);
            result.set("language", language);
            result.save().then(function () {
                return sendCodeSms(phoneNumber, num, language);
            }).then(function () {
                res.success({});
            }, function (err) {
                res.error(err);
            });
        } else {
            var user = new Parse.User();
            user.setUsername(phoneNumber);
            user.setPassword(secretPasswordToken + num);
            user.set("language", language);
            //user.setACL({});
            user.save().then(function (a) {
                return sendCodeSms(phoneNumber, num, language);
            }).then(function () {
                res.success({});
            }, function (err) {
                res.error(err);
            });
        }
    }, function (err) {
        res.error(err);
    });
});

Parse.Cloud.define("logIn", function (req, res) {
    Parse.Cloud.useMasterKey();

    var phoneNumber = req.params.phoneNumber;
    try {
        phoneNumber = phoneNumber.replace(/\D/g, '');
    } catch(err) {
        res.error(err);
        return;
    }

    if (phoneNumber && req.params.codeEntry) {
        Parse.User.logIn(phoneNumber, secretPasswordToken + req.params.codeEntry).then(function (user) {
            res.success(user.getSessionToken());
        }, function (err) {
            res.error(err);
        });
    } else {
        res.error('Invalid parameters.');
    }
});

function sendCodeSms(phoneNumber, code, language) {
    var prefix = "+1";
    if (typeof language !== undefined && language == "ja") {
        prefix = "+81";
    } else if (typeof language !== undefined && language == "kr") {
        prefix = "+82";
        phoneNumber = phoneNumber.substring(1);
    } else if (typeof language !== undefined && language == "pt-BR") {
        prefix = "+55";
    }

    var promise = new Parse.Promise();
    var fullNumberTo = prefix + phoneNumber.replace(/\D/g, '');
    var fullNumberFrom = twilioPhoneNumber;
    var message = 'Your login code for AnyPhone is ' + code;

    console.log("send sms code to " + fullNumberTo + ", from " + fullNumberFrom + ", message: " + message);

    return client.messages.create({
        to: fullNumberTo,
        from: fullNumberFrom,
        body: message
    }, function (err, message) {
        console.log("err " + err);
        console.log("message: " + message);
        if (err) {
            console.log(err);
            promise.reject(err.message);
        } else {
            console.log("message: " + message.sid);
            promise.resolve();
        }
    });

    return promise;
}