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

	add(vector) {
		this.x = this.x + vector.x;
		this.y = this.y + vector.y;
		return this;
	}
}

module.exports = Vector;