'use strict';

var robot = require('robotjs');
var Vector = require('./vector');

class Mouse {
	constructor(){
		this._target = new Vector(0, 0);
		this._force = 0;

		robot.setMouseDelay(2);
	}

	set force(value){
		this._force = value;
	}

	set target(t){
		if(!(t instanceof Vector)){
			throw new Error("Target is not instance of Vector");
		}

		this._target = t;
	}

	start(){
		console.log("Start mouse...");

		var intval = setInterval(() => {
			// Move towards target
			let pos = new Vector(robot.getMousePos().x, robot.getMousePos().y);
			pos.add(this._target.clone().multiply(this._force));

			robot.moveMouse(pos.x, pos.y);
		}, 30); // 30ms
	}
}

module.exports = Mouse;