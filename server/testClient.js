var net = require('net');

var client = new net.Socket();
client.connect(1337, '127.0.0.1', () => {
	console.log('Connected!');
	client.write(JSON.stringify({ type: "vector", "x": 100, "y": 100 }));
	client.end();
});

client.on('data', res => {
	console.log('Recieved: ' + res);
	client.destroy();
});

client.on('close', () => {
	console.log('Connection closed');
});