'use strict';

class Vector {
	constructor(x, y){
		this.x = x;
		this.y = y;
	}

	multiply(scalar){
		this.x = this.x * scalar;
		this.y = this.y * scalar;
		return this;
	}
}

module.exports = Vector;