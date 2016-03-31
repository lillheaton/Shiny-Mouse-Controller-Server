'use strict';

class Vector {
	constructor(x, y){
		this.set(x, y);
	}

	get length() {
		return Math.sqrt((this.x * this.x) + (this.y * this.y));
	}

	set(x, y){
		if(typeof x != "number" || typeof y != "number"){
			throw new Error("Invalid input");
		}

		this.x = x;
		this.y = y;
	}

	add(vector) {
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}

	subtract(vector){
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}

	multiply(scalar){
		if(typeof scalar != "number"){
			throw new Error("Not a number");
		}

		this.x *= scalar;
		this.y *= scalar;
		return this;
	}
	
	clone() {
		return new Vector(this.x, this.y);
	}

	// Does not effect the object. Returns a copy of itself normalized
	normalized(){
		let length = this.length;
		return new Vector(
			this.x / length,
			this.y / length
		);
	}

	toString(){
		return `(${this.x}, ${this.y})`;
	}
}

module.exports = Vector;