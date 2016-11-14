var express = require('express');
app = express();
console.log('dir name ' + __dirname);
//app.use(express.static(__dirnam))
port = Number(3100);
const RapidAPI = require('rapidapi-connect');
const rapid = new RapidAPI('MusicGo', '03f0fe63-84e3-42e7-942b-765915f9bcfe');
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended:false});
var auth_code = "BQBhY61m2f3YtTCCTvaRI7xOFRXRmO6VAssptO0wMEX9PfO22nUPwCDhqIcmOvpv74ZtPjZn8gVBG5ybr5aKLitnc8nOxhCjYmEyllQuULQBisK1fafixtuRvvwArivQQuEZqvPhfFm2a5MH2fRU2ReJNBVMkl082lq1HMcIeazhA-Gaj9X2Gdyf40cFFMfYAN_goNF0fIz2d5F0QY_58M2cbBwwSsB_ynUj9q68XG7L";
app.get('/',function(req,res){
   console.log("get message received "); 
});
app.get('/query/:id',function(req,res){
    console.log("query = "+req.params.id);
    if(req.params.id == 0){
        rapid.call('SpotifyUserAPI', 'getFeaturedPlaylists', { 
	'access_token': auth_code,
	'locale': 'en',
	'country': 'US',
	'timestamp': ''
 
}).on('success', (payload)=>{
	var str = JSON.stringify(payload);
    var obj = '{"result":'+str+'}';
    res.send(obj);
}).on('error', (payload)=>{
	 /*YOUR CODE GOES HERE*/ 
});
    } else if(req.params.id == 1){
        rapid.call('SpotifyUserAPI', 'getUserSavedTracks', { 
	'access_token':auth_code,
	'market': 'US'
 
}).on('success', (payload)=>{
	var str = JSON.stringify(payload);
    var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
    res.send(obj);
    
      
}).on('error', (payload)=>{
	 /*YOUR CODE GOES HERE*/ 
});

    }
});


app.listen(port,'10.11.18.202');
