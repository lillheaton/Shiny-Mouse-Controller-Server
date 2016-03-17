'use strict';

// TODO: Fix this npm package
/*var shell = require('node-powershell');*/

/*var express = require('express');

var Mouse = require('./mouse.js');
var Vector = require('./vector.js')

var app = express();
var mouse = new Mouse();

app.get('/', (req, res) => {
	res.send('Hello World!');
});

app.get('/move/left/:left/top/:top', (req, res) => {
	res.send(`You want to move ${req.params.left} to left and ${req.params.top} top`);
	mouse.move(new Vector(req.params.left, req.params.top));
});

app.listen(8080, () => {
	console.log('Start listen on localhost:8080...');
});
*/

var net = require('net');
var Mouse = require('./mouse.js');
var Vector = require('./vector.js')

var mouse = new Mouse();

var server = net.createServer(socket => {
	socket.write('Echo server\r\n');

	socket.on('data', data => {
		var obj = JSON.parse(data);

		if(obj.type === "vector"){
			mouse.move(new Vector(obj.x, obj.y));
			
		}
	});

	socket.on('end', () => {
		console.log(socket.remoteAddress + "left the room");
	});

	// Need to listen on this one, otherwise error will be thrown
	socket.on('error', () => {
		console.log('Something happend');
	});
});

server.listen(1337, '127.0.0.1');

server.on('listening', () => {
	console.log("TCP server listening on 127.0.0.1:1337");
});

server.on('error', error => {
	console.log("Error: " + error);
});