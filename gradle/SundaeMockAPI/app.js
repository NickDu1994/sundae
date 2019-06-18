var express = require('express');
var app = express();
var fs = require("fs");
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({ extended: false });
var jsonParser = bodyParser.json();
const cors = require('cors');

app.use(cors());
 
//  GET 请求
app.get('/index', function (req, res) {
	console.log("接收到 GET 请求");
   fs.readFile( __dirname + "/response/" + "mock.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})

app.post('/getPhoneMessage', function (req, res) {
	console.log("接收到 GET 请求");
   fs.readFile( __dirname + "/response/" + "getPhoneMessage.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})

app.post('/getUserInfo', function (req, res) {
	console.log("接收到 GET 请求");
   fs.readFile( __dirname + "/response/" + "getUserInfo.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})
 
 
app.get('/news/getLastestNews', function (req, res) {
	console.log("接收到 GET 请求");
   fs.readFile( __dirname + "/response/" + "getNewsResponse.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})

app.get('/abbreviation/getRecommendedEntryList', function (req, res) {
	console.log("接收到 GET 请求");
   fs.readFile( __dirname + "/response/" + "recommend.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})
 
app.post('/abbreviation/getOneEntryDetail',jsonParser, function (req, res) {
   console.log("接收到 POST 请求," + "body=");
   console.dir(req.body);
   fs.readFile( __dirname + "/response/" + "searchdetail.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})
 
app.post('/abbreviation/searchAbbreviation',jsonParser, function (req, res) {
   console.log("接收到 POST 请求," + "body=");
   console.dir(req.body);
   fs.readFile( __dirname + "/response/" + "searchlist.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})


//  POST 请求
app.post('/list',jsonParser, function (req, res) {
   console.log("接收到 POST 请求," + "body=");
   console.dir(req.body);
   fs.readFile( __dirname + "/response/" + "mock.json", 'utf8', function (err, data) {
       console.log( "response:",data );
       res.send( data );
   });
})
 
 
// 对页面 abcd, abxcd, ab123cd, 等响应 GET 请求
app.get('/ab*cd', function(req, res) {   
   console.log("/ab*cd GET 请求");
   res.send('正则匹配');
})
 
 
var server = app.listen(3001, function () {
 
  var host = server.address().address
  var port = server.address().port
 
  console.log("应用实例启动，访问地址为 http://%s:%s", host, port)
 
})