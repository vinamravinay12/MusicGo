 var express = require('express');
app = express();
console.log('dir name ' + __dirname);
//app.use(express.static(__dirnam))
url = '192.168.0.13';
port = Number(process.env.POST || 3100);
var SpotifyWebApi = require('spotify-web-api-node');

// credentials are optional
var spotifyApi = new SpotifyWebApi({
  clientId : '5e41ef158c894daca97c62ca2e033d9a',
  clientSecret : '0f49a1393359471c868ab1d90aa227ad',
  redirectUri : 'http://localhost:1337/callback'
});
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
    spotifyApi.setAccessToken(auth_code);
    console.log(auth_code);
   var jsonString = '{"message":"Received Token"}';
   var responseObject = JSON.parse(jsonString);
   spotifyApi.getMe()
   .then(function(data) {
  // console.log('Some information about the authenticated user', data.body);
     var userJson = JSON.stringify(data.body);
     var obj = JSON.parse(userJson);
     this.userId = obj.id;
     console.log("user id is " + this.userId);
   }, function(err) {
     console.log('Something went wrong in getting auth code!', err);
   });
   console.log(responseObject);
   res.send(responseObject);
});

app.get('/query/:id',function(req,res){
    console.log("query = "+req.params.id);
    if(req.params.id == 0){

      spotifyApi.getUserPlaylists(this.userId,{
    limit : 50,
    offset: 0
  }).then(function(data) {
        var updatedJson = '{"result":[';
        var str = JSON.stringify(data.body);
        var userPlaylists = JSON.parse(str);
        //  console.log('Retrieved playlists', userPlaylists);
        if(userPlaylists.items != null){

          var itemsLength = userPlaylists.items.length;
          for(var i = 0; i <itemsLength;i++){
            var tracksStr  = JSON.stringify(userPlaylists.items[i]);
            var tracks = JSON.parse(tracksStr);
            var playlistId = tracks.id;
            var playlistName = tracks.name;
            var ownerId = tracks.owner.id;
            var imageStr = "";
            //console.log("images length " + tracks.images.length);
              if(tracks.images.length > 1){
                imageStr = JSON.stringify(tracks.images[1].url);
              }else {
                imageStr = JSON.stringify(tracks.images[0].url);
              }
              var itemLength = userPlaylists.items.length;
              itemLength = itemLength-1;
             //console.log("length here is " +itemLength + " :: " +i);
              if(i === itemLength){
              //  console.log("image is " +playlistId+" :: "+playlistName);

                  var jsonObject = '{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"}';
                  updatedJson  = updatedJson +jsonObject;

              } else if(i === 0){
              //  console.log("image is " +playlistId+" :: "+playlistName);
                  var jsonObject ='{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"},';
                  updatedJson = updatedJson + jsonObject;
              }
              else{
                  var jsonObject = '{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"},';
                  updatedJson = updatedJson + jsonObject;
              }
          }
        }
          updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';

          //  var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
          // console.log("sending user play json "+updatedJson);
          res.send(updatedJson);
//  console.log("hello I am fine");
          //console.log('tracks are ',tracks);
        },function(err) {
          console.log('Something went wrong in downloading user playlists!', err);
        });
    }else if(req.params.id == 1){
      console.log("inside getMySavedTracks");
      spotifyApi.getMySavedTracks({
    limit : 50,
    offset: 0
  })
  .then(function(data) {

    var updatedJson = '{"result":[';
  var str = JSON.stringify(data.body);
  // console.log("Songs data is " +str);
    var obj = JSON.parse(str);
    var itemsList = obj.items;
    for(var i = 0;i<itemsList.length;i++){
      var trackStr = JSON.stringify(itemsList[i].track);
      var trackDetails = JSON.parse(trackStr);
        var albumName = trackDetails.album.name;
        var songName = trackDetails.name;
        var songId = trackDetails.id;
      //  console.log("albums "+ albumName+" :: "+songName+" :: "+songId);
        albumName = albumName.replace(/"/g,"");
        songName = songName.replace(/"/g,"");
        var songUri = trackDetails.uri;
        var artistName = trackDetails.album.artists[0].name;
        var imageArrayLength = trackDetails.album.images.length;
        var smallLength;
        var mediumLength;
      //  console.log("image arr "+ imageArrayLength);
       if(imageArrayLength >1){
          smallLength = imageArrayLength-1;
          mediumLength = imageArrayLength-2;
      } else if(imageArrayLength ==1){
         smallLength = imageArrayLength-1;
         mediumLength = imageArrayLength-1;
}
    //    console.log("lengths "+ smallLength+ " :: "+mediumLength);
    var songImageStr = JSON.stringify(trackDetails.album.images);
  //  var song = JSON.stringify(songImageStr[0]);
  var song = JSON.parse(songImageStr);
  //var song1 = JSON.stringify(song[0]);
  //  console.log("song images "+song1);

      var songImage = JSON.stringify(song[mediumLength].url);
        var songImageSmall = JSON.stringify(song[smallLength].url);
      //  console.log("image urls "+songImage+" :: "+songImageSmall);
        var duration = trackDetails.duration;
        //  console.log("song name is ",songImage);
        var itemLength = itemsList.length;
        itemLength = itemLength -1;
        if(i === itemLength){
            var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"}';
            updatedJson  = updatedJson +jsonObject;

        } else if(i === 0){
            var jsonObject ='{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
            updatedJson = updatedJson + jsonObject;
        }
        else{
            var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
            updatedJson = updatedJson + jsonObject;
        }

}

      updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';


   // var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
  //  console.log("sending user songs json "+updatedJson);
    res.send(updatedJson);

  }, function(err) {
    console.log('Something went wrong in downloading user songs!', err);
  });

} else{
  spotifyApi.getFeaturedPlaylists(  {
limit : 50,
offset: 0
}).then(function(data) {
    var updatedJson = '{"result":[';
    var str = JSON.stringify(data.body);
    var playlistsMain= JSON.parse(str);
    //  console.log('Retrieved playlists', str);
    var userPlaylists = playlistsMain.playlists;
    var itemsLength = userPlaylists.items.length;
  //  console.log("featured lenth "+ itemsLength);
      for(var i = 0; i<itemsLength;i++){
        var tracksStr  = JSON.stringify(userPlaylists.items[i]);
    //    console.log("featured playlists "+ userPlaylists.items[i]);
        var tracks = JSON.parse(tracksStr);
        var playlistId = tracks.id;
        var playlistName = tracks.name;
        var ownerId = tracks.owner.id;
        var imageStr = "";
        //console.log("images length " + tracks.images.length);
          if(tracks.images.length > 1){
            imageStr = JSON.stringify(tracks.images[1].url);
          }else {
            imageStr = JSON.stringify(tracks.images[0].url);
          }
          var itemLength = userPlaylists.items.length;
          itemLength = itemLength-1;
         //console.log("length here is " +itemLength + " :: " +i);
          if(i === itemLength){
          //  console.log("image is " +playlistId+" :: "+playlistName);

              var jsonObject = '{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"}';
              updatedJson  = updatedJson +jsonObject;

          } else if(i === 0){
          //  console.log("image is " +playlistId+" :: "+playlistName);
              var jsonObject ='{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"},';
              updatedJson = updatedJson + jsonObject;
          }
          else{
              var jsonObject = '{"playlist_id":"'+playlistId+'","playlist_name":"'+playlistName+'","playlist_image":'+imageStr+',"playlist_owner":"'+ownerId+'"},';
              updatedJson = updatedJson + jsonObject;
          }

    }
      updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';

       // var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
      //  console.log("sending featured play json "+updatedJson);
      res.send(updatedJson);
//  console.log("hello I am fine");
      //console.log('tracks are ',tracks);
    },function(err) {
      console.log('Something went wrong in downloading featured playlists!', err);
    });

}
});

app.get('/query/:id/:playlistId/:ownerId',function(req,res){
  console.log("query here= " +req.params.id );
  //console.log("query = "+req.params.playlistId);
    if(req.params.id == 3){
      spotifyApi.getPlaylistTracks(req.params.ownerId,req.params.playlistId,{
    limit : 50,
    offset: 0
  },function(err,data){
    if(err){
      console.error("Something went wrong in downloading user playlist tracks now ",err);
    }else{
      var updatedJson = '{"result":[';
    var str = JSON.stringify(data.body);
     //console.log("Songs data is " +str+"\n\n");
      var obj = JSON.parse(str);
      var itemsList = obj.items;
      for(var i = 0;i<itemsList.length;i++){
        var trackStr = JSON.stringify(itemsList[i].track);
        var trackDetails = JSON.parse(trackStr);
          var albumName = trackDetails.album.name;
          var songName = trackDetails.name;
          var songId = trackDetails.id;
        //  console.log("albums "+ albumName+" :: "+songName+" :: "+songId);
          albumName = albumName.replace(/['"]+/g, '');
          songName = songName.replace(/['"]+/g, '');
          var songUri = trackDetails.uri;
          var artistName = trackDetails.album.artists[0].name;
          artistName = artistName.replace(/['"]+/g, '');
          //console.log("artist name is "+artistName);
          var imageArrayLength = trackDetails.album.images.length;
          var smallLength;
          var mediumLength;
        //  console.log("image arr "+ imageArrayLength);
         if(imageArrayLength >1){
            smallLength = imageArrayLength-1;
            mediumLength = imageArrayLength-2;
        } else if(imageArrayLength ==1){
           smallLength = imageArrayLength-1;
           mediumLength = imageArrayLength-1;
  }
      //    console.log("lengths "+ smallLength+ " :: "+mediumLength);
      var songImageStr = JSON.stringify(trackDetails.album.images);
    //  var song = JSON.stringify(songImageStr[0]);
    var song = JSON.parse(songImageStr);
    //var song1 = JSON.stringify(song[0]);
    //  console.log("song images "+song1);

        var songImage = JSON.stringify(song[mediumLength].url);
          var songImageSmall = JSON.stringify(song[smallLength].url);
        //  console.log("image urls "+songImage+" :: "+songImageSmall);
          var duration = trackDetails.duration_ms;
          //  console.log("song name is ",songImage);

          var itemLength = itemsList.length;
          itemLength = itemLength -1;
          if(i === itemLength){
              var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
              +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"}';
              updatedJson  = updatedJson +jsonObject;

          } else if(i === 0){
              var jsonObject ='{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
              +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
              updatedJson = updatedJson + jsonObject;
          }
          else{
              var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
              +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
              updatedJson = updatedJson + jsonObject;
          }
        //  console.log("json object " + jsonObject);
  }

        updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';


     // var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
//console.log("user playlist json " + updatedJson);
      res.send(updatedJson);

    }
    });

} else{
  //    console.log("query = "+req.params.id);

    spotifyApi.getPlaylistTracks(req.params.ownerId,req.params.playlistId,{
  limit : 50,
  offset: 0
},function(err,data){
  if(err){
     console.error('Something went wrong in downloading featured playlist tracks data!',err);
  } else{
    var updatedJson = '{"result":[';
   var str = JSON.stringify(data.body);
   // console.log("Songs data is " +str);
    var obj = JSON.parse(str);
    var itemsList = obj.items;
    for(var i = 0;i<itemsList.length;i++){
      var trackStr = JSON.stringify(itemsList[i].track);
      var trackDetails = JSON.parse(trackStr);
        var albumName = trackDetails.album.name;
        var songName = trackDetails.name;
        var songId = trackDetails.id;
      //  console.log("albums "+ albumName+" :: "+songName+" :: "+songId);
        albumName = albumName.replace(/"/g,"");
        songName = songName.replace(/"/g,"");
        var songUri = trackDetails.uri;
        var artistName = trackDetails.album.artists[0].name;
        var imageArrayLength = trackDetails.album.images.length;
        var smallLength;
        var mediumLength;
      //  console.log("image arr "+ imageArrayLength);
       if(imageArrayLength >1){
          smallLength = imageArrayLength-1;
          mediumLength = imageArrayLength-2;
      } else if(imageArrayLength ==1){
         smallLength = imageArrayLength-1;
         mediumLength = imageArrayLength-1;
   }
    //    console.log("lengths "+ smallLength+ " :: "+mediumLength);
    var songImageStr = JSON.stringify(trackDetails.album.images);
   //  var song = JSON.stringify(songImageStr[0]);
   var song = JSON.parse(songImageStr);
   //var song1 = JSON.stringify(song[0]);
   //  console.log("song images "+song1);

      var songImage = JSON.stringify(song[mediumLength].url);
        var songImageSmall = JSON.stringify(song[smallLength].url);
      //  console.log("image urls "+songImage+" :: "+songImageSmall);
        var duration = trackDetails.duration_ms;
        //  console.log("song name is ",songImage);
        var itemLength = itemsList.length;
          console.log("song details " +songName+" :: "+duration);
        itemLength = itemLength -1;
        if(i === itemLength){
            var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"}';
            updatedJson  = updatedJson +jsonObject;

        } else if(i === 0){
            var jsonObject ='{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
            updatedJson = updatedJson + jsonObject;
        }
        else{
            var jsonObject = '{"song_id":"'+songId+'","song_name":"'+songName+'","album_name":"'+albumName
            +'","artist_name":"'+artistName+'","image_url":'+songImage+',"image_url_small":'+songImageSmall+',"duration":"'+duration+'","song_uri":"'+songUri+'"},';
            updatedJson = updatedJson + jsonObject;
        }
      //  console.log("json object " + jsonObject);
   }

      updatedJson = updatedJson + '],"auth_code":"'+auth_code+'"}';



    // var obj = '{"result":'+str+',"auth_code" :"'+auth_code+'"}';
     res.send(updatedJson);

  }
});
//});
}
});

app.listen(port,url);
