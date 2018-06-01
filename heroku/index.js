// Example express application adding the parse-server module to expose Parse
// compatible API routes.

var express = require('express');
var ParseServer = require('parse-server').ParseServer;

//var DATABASE_URI = "mongodb://heroku_12zphtkk:pl30a8gie2ed646lj4ph05cad2@ds061325.mongolab.com:61325/heroku_12zphtkk";
//var databaseUri = DATABASE_URI;
var databaseUri = process.env.DATABASE_URI || process.env.MONGOLAB_URI;// || DATABASE_URI;

if (!databaseUri) {
  console.log('DATABASE_URI not specified, falling back to localhost.');
  databaseUri =  'mongodb://localhost:27017/event2go';
}

console.log('DATABASE_URI ' + databaseUri);
console.log('process.env.CLOUD_CODE_MAIN ' + process.env.CLOUD_CODE_MAIN);
console.log("__dirname " + __dirname);

//var OneSignalPushAdapter = require('parse-server/lib/Adapters/Push/OneSignalPushAdapter');
//var oneSignalPushAdapter = new OneSignalPushAdapter({
//  oneSignalAppId:"ebdc78e0-5369-44f5-8831-a39290a03769",
//  oneSignalApiKey:"NTI1YTdkM2EtNzk1YS00NTg5LWJlMDQtNWJhYmU5MjQ5ZTdk"
//});



var api = new ParseServer({
  databaseURI: databaseUri,
  //databaseURI: databaseUri || 'mongodb://localhost:27017/event2go',
  //databaseURI: 'mongodb://localhost:27017/event2go',
  cloud: process.env.CLOUD_CODE_MAIN || __dirname + '/cloud/main.js',
  appId: 'MofV8rBJbmoa5QTbmyGt6KjfKHR0AcGZ0OprSAqf',
  masterKey: 'Ea4JKv504lBVQiw3MXak37hYwsu0aCg7qaMlXxCe', //Add your master key here. Keep it secret!
  serverURL: 'http://localhost:1337/parse',
  //serverURL: 'https://event2go.herokuapp.com/parse/',
  //push: {
  //  adapter: oneSignalPushAdapter
  //},
  push: {
    android: {
      senderId: '894550020348',
      apiKey: 'AIzaSyBd7LUU1Ab6nONzMMZYx5vLaMUt7uWaWw4'
    },
    //ios: {
    //  pfx: '', // The filename of private key and certificate in PFX or PKCS12 format from disk
    //  cert: '', // If not using the .p12 format, the path to the certificate PEM to load from disk
    //  key: '', // If not using the .p12 format, the path to the private key PEM to load from disk
    //  bundleId: '', // The bundle identifier associate with your app
    //  production: false // Specifies which environment to connect to: Production (if true) or Sandbox
    //}
  }
});


// https://github.com/ParsePlatform/parse-server/wiki/Push#2-configure-parse-server
// https://developers.google.com/cloud-messaging/android/client

//Parse.serverURL =
//Parse.Cloud.useMasterKey();

//Parse.Cloud.useMasterKey();
// Client-keys like the javascript key or the .NET key are not necessary with parse-server
// If you wish you require them, you can set them as options in the initialization above:
// javascriptKey, restAPIKey, dotNetKey, clientKey

var app = express();

//Serve the Parse API at /parse URL prefix
//app.use('/parse', api);

// Serve the Parse API on the /parse URL prefix
var mountPath = process.env.PARSE_MOUNT || '/parse';
app.use(mountPath, api);

// Parse Server plays nicely with the rest of your web routes
app.get('/', function(req, res) {
  res.status(200).send('I dream of being a web site.');
});

var port = process.env.PORT || 1337;
app.listen(port, function() {
    console.log('parse-server-example running on port ' + port + '.');
});
