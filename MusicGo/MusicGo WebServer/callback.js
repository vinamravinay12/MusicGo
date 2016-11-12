http.createServer(function(request, response) {
  response.writeHead(200, {"Content-Type": "text/html"});
  response.write("<h1>Welcome to MusicGo</h1>");
  response.end();
}).listen(1337);