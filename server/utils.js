
var os = require('os');

var getLocalIp = function() {
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

module.exports.getLocalIp = getLocalIp;