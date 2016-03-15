'use strict';

// TODO: Fix this npm package
/*var shell = require('node-powershell');*/

var express = require('express');

var app = express();

app.get('/', (req, res) => {
	res.send('Hello World!');
});

app.get('/move/:left', (req, res) => {
	res.send(`You want to move ${req.params.left} to left`);
});

app.listen(8080, () => {
	console.log('Start listen on localhost:8080');
});



/*
var http = require('http');
var spawn = require("child_process").spawn,child;

http.createServer((request, response) => {

	// Return some response to the user
	response.writeHead(200, {'content-type': 'text/plain'});
	response.end('Hello World!');

	// Execute file
	child = spawn(
		"powershell.exe", 
		["-ExecutionPolicy", "ByPass", 
		"-File", "C:/Workspace/NodeEs6Test/commands/moveMouse.ps1", 
		"-Left", 100]
	);

	console.log("Hmm");

	// On output
	child.stdout.on("data", output => {
		console.log("Script output: " + output);
	});

	// On errors
	child.stderr.on("data", output => {
		console.log("Script error: " + output);
	});

	// On exit
	child.on("exit", () => {
		console.log("Script finished");
	});

	// Force Close
	// child.stdin.end();

}).listen(8080);

console.log("Server start running on localhost:8080...");


*/

class HelloWorld{
	constructor(){
		console.log("Hello World!");
	}
}

var example = new HelloWorld();