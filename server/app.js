'use strict';

// TODO: Fix this npm package
/*var shell = require('node-powershell');*/

var net = require('net');
var os = require('os');

var Mouse = require('./mouse.js');
var Vector = require('./vector.js')


function _getLocalIp() {
	var networkInterfaces = os.networkInterfaces();

	for (var k in networkInterfaces){
		for (var k2 in networkInterfaces[k]){
			var address = networkInterfaces[k][k2];
			if(address.family === 'IPv4' && !address.internal){
				// Take the first in the list
				return address.address;
			}
		}
	}
};


var mouse = new Mouse();
var listeningIp = _getLocalIp();

var server = net.createServer(socket => {
	socket.write('Echo server\r\n');

	socket.on('data', data => {
		var obj = JSON.parse(data);
		console.log(obj);

		if(obj.type === "joystick"){
			var angleVector = new Vector(obj.vector.x, obj.vector.y);
			mouse.move(angleVector.multiply(obj.magnitude));
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

server.listen(1337, listeningIp);

server.on('listening', () => {
	console.log(`TCP server listening on ${listeningIp}:1337`);
});

server.on('error', error => {
	console.log("Error: " + error);
});