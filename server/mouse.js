'use strict';

// Path to moving mouse script
const scriptPath = "C:/Workspace/MouseController/commands/moveMouse.ps1";

let _spawn = require("child_process").spawn;

class Mouse {
	constructor(){
		// Default attributes
		this.scriptAttr = ["-ExecutionPolicy", "ByPass", "-File", scriptPath, "-Left", 0, "-Top", 0];
	}

	_execute(){
		// Execute script
		this.child = _spawn("powershell.exe", this.scriptAttr);
		this.child.stdout.on("data", this._onResponse);
		this.child.stderr.on("data", this._onError);
		this.child.on("exit", this._onFinish);
	}

	_onResponse(res){
		console.log("Moving mouse " + res);
	}

	_onError(res){
		//console.log("Error: " + res);
	}

	_onFinish(){
	}

	move(vector){
		this.scriptAttr[this.scriptAttr.indexOf("-Left") + 1] = vector.x;
		this.scriptAttr[this.scriptAttr.indexOf("-Top") + 1] = vector.y;
		this._execute();
	}
}

module.exports = Mouse;