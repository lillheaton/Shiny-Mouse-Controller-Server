'use strict';

// Npm packages
var net = require('net');
var robot = require('robotjs');

// Own
var Mouse = require('./mouse');
var Vector = require('./vector');
var Utils = require('./utils');

var isInputTest = process.argv.indexOf('inputTest') > 0;
var listeningIp = Utils.getLocalIp();
var mouse = new Mouse();

var server = net.createServer(socket => {
	socket.write('Echo server\r\n');
	mouse.start(); // Start the mouse loop

	socket.on('data', data => {
		var data = data + "";
		if(data === undefined || data === ""){
			return;
		}

		data = data.replace(/(?:\r\n|\r|\n)/g, '');
		data = '[' + data + ']';

		var regex = new RegExp('} }', 'g');
		data = data.replace(regex, '} },');
		data = data.replace(',]', ']');

		try{
			let obj = JSON.parse(data);

			for (var i = 0; i < obj.length; i++) {

				switch(obj[i].type){
					case "joystick":
						if(isInputTest){
							console.log("Joystick input is sent");
						} else{
							mouse.target = new Vector(obj[i].vector.x, obj[i].vector.y);
							mouse.force = obj[i].magnitude * 0.1; // decrease the force to 10%	
						}
						
						break;

					case "click":
						if(isInputTest){
							console.log("Mouse click sent");
						} else {
							mouse.click(obj[i].button);
						}

						break;
				}

			};
		} catch(e){}
		
	});

	socket.on('end', () => {
		console.log(socket.remoteAddress + " left the room");
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