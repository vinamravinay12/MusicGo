var express = require('express');
app = express();
console.log('dir name ' + __dirname);
//app.use(express.static(__dirnam))
port = Number(process.env.POST || 3100);
const RapidAPI = require('rapidapi-connect');
const rapid = new RapidAPI('MusicGo', '03f0fe63-84e3-42e7-942b-765915f9bcfe');
var bodyParser = require('body-parser');
var urlencodedParser = bodyParser.urlencoded({extended:false});
app.use(bodyParser.json());
app.use(urlencodedParser);
var auth_code;
app.get('/',function(req,res){
   console.log("get message received "); 
});

app.post('/auth',function(req,res){
    auth_code = req.body.token;
    console.log(auth_code);
   var jsonString = '{"message":"Received Token"}';
   var responseObject = JSON.parse(jsonString);
   console.log(responseObject);
   res.send(responseObject);
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
    var obj = JSON.parse(str);
    res.send(obj);
}).on('error', (payload)=>{
	 /*YOUR CODE GOES HERE*/ 
});
    } else if(req.params.id == 1){
        rapid.call('SpotifyUserAPI', 'getUserSavedTracks', { 
	'access_token':auth_code,
	'market': 'US'
 
}).on('success', (payload)=>{
    var result = '{"result":[';
	var str = JSON.stringify(payload);
    var obj = JSON.parse(str);
    console.log(obj[0].items[0].track.album.name);
    var updatedJson;
    for(var i = 0;i<obj[0].items.length;i++){
        var songName = obj[0].items[i].track.album.name;
        songName = songName.replace(/"/g,"");
        var songUri = obj[0].items[i].track.album.uri;
        var artistName = obj[0].items[i].track.album.artists[0].name;
        var imageArrayLength = obj[0].items[i].track.album.images.length;
        var songImage = JSON.stringify(obj[0].items[i].track.album.images[imageArrayLength -2].url);
        
        if(i == (obj[0].items.length -1)){
            var jsonObject = '{"name":"'+songName+'","artist":"'+artistName+'","imageurl":'+songImage+',"songurl":"'+songUri+'"}';
            updatedJson  = updatedJson +jsonObject;
            
        } else if(i == 0){
            var jsonObject ='{"name":"'+songName+'","artist":"'+artistName+'","imageurl":'+songImage+',"songurl":"'+songUri+'"},';
            updatedJson = result + jsonObject;
        }
        else{
            var jsonObject = '{"name":"'+songName+'","artist":"'+artistName+'","imageurl":'+songImage+',"songurl":"'+songUri+'"},';
            updatedJson = updatedJson + jsonObject;
        }
    
    }
    updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';


   // var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
    res.send(updatedJson);
    
      
}).on('error', (payload)=>{
	 /*YOUR CODE GOES HERE*/ 
});

    }
});


app.listen(port,'192.168.0.18');
