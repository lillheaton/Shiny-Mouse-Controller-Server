'use strict';

// TODO: Fix this npm package
/*var shell = require('node-powershell');*/

var express = require('express');

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