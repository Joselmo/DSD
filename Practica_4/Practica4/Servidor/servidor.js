/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = {"html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var termostato = 25;
var lumens = 2500;
var humedad = 20;

var TEMP_MAX = 40;
var LUZ_MIN = 1000;
var LUZ_MAX = 8000;
var TEMP_AC = 25;
var LUZ_INTERIOR = 4000;

// Funciones auxiliares
function getValor(equipo) {
    if (equipo == "temp")
        return termostato;
    else if (equipo == "luz")
        return lumens;
    else
        return "Error: Sensor no v&aacute;lidos";


}
;

function setValor(equipo, valor) {
    if (equipo === "temp")
        termostato = valor;
    else if (equipo === "luz")
        lumens = valor;
    else if(equipo === "humedad")
        humedad = valor;
    else
        return "Error: Sensor no v&aacute;lidos";
        console.log("Actualizado el valor del sensor:" + equipo + " con " + valor);
}


/**
 * Devuelve un código con el estado del agente.
 * 0 = Ok
 * 1 = Temperatura elevada
 * 2 = Luz baja
 * 3 = Luz muy fuerte.
 * @returns {Number} código de control del agente.
 */
function controlAgente() {
    var mensaje = 0;
    if (termostato > TEMP_MAX) {
        mensaje = 1;
    }
    if (lumens <= LUZ_MIN) {
        mensaje = 2;
    }
    if (lumens >= LUZ_MAX) {
        mensaje = 3;
    }

    return mensaje;
}




////////////////////////////////////////////
//          SERVER
////////////////////////////////////////////

var httpServer = http.createServer(
        function (request, response) {
            var uri = url.parse(request.url).pathname;
            if (uri == "/")
                uri = "Servidor/index.html";
            var fname = path.join(process.cwd(), uri);
            fs.exists(fname, function (exists) {
                if (exists) {   // Redirigimos a index.html
                    fs.readFile(fname, function (err, data) {
                        if (!err) {
                            var extension = path.extname(fname).split(".")[1];
                            var mimeType = mimeTypes[extension];
                            response.writeHead(200, mimeType);
                            response.write(data);
                            response.end();
                        } else {
                            response.writeHead(200, {"Content-Type": "text/plain"});
                            response.write('Error de lectura en el fichero: ' + uri);
                            response.end();
                        }
                    });
                } else {    // Una vez dentro de index.html

                    while (uri.indexOf('/') === 0) { // Obtenemos la url de la petición
                        uri = uri.slice(1);
                        console.log("uri:" + uri);
                    }

                    var params = uri.split("/"); // la pasamos a array

                    response.writeHead(200, {"Content-Type": "text/plain"});
                    var result = '404 Not Found\n';

                    if (params[0] === 'temp' || params[0] === 'luz' || params[0] === 'humedad') {
                        console.log("Peticion REST sensors: " + uri);
                        var valor = parseFloat(params[1]);
                        setValor(params[0], valor);

                        result = getValor(params[0]);

                    } else if (params[0] === 'ac') {
                        console.log("ac1: " + params);
                        result = params[1];
                        console.log("ac2: " + result.toString());
                        if (result === 'ON') {
                            console.log('Encendemos el Aire Acondicionado.');
                        } else if (result === 'OFF') {
                            console.log('Apagamos el Aire Acondicionado.');
                        }


                    } else if (params[0] === 'interruptor') {
                        console.log("interruptor: " + params);
                        result = params[1];
                       
                        if (result === 'ON') {
                            console.log('Encendemos las luces.');
                        } else if (result === 'OFF') {
                            console.log('Apagamos las luces.');
                        }


                    } else if (params[0] === 'persiana') {
                        result = params[1];
                        if (result === 'Subida') {
                            console.log('Subimos las persianas');
                        } else if (result === 'Bajada') {
                            console.log('Bajamos las persianas');
                        }


                    } else if (params.length >= 3) { //Respuestas de cliente
                        console.log("Peticion REST Client: " + uri);
                        var val1 = parseFloat(params[1]);
                        var val2 = parseFloat(params[2]);

                        result = 'Operaciones cliente';


                    }


                    response.write(result.toString());
                    response.end();
                }
            });
        }
);



////////////////////////////////////////////
//          MONGO DBx
////////////////////////////////////////////
var mongoClient = new MongoClient(new MongoServer('localhost', 27017));
mongoClient.connect("mongodb://localhost:27017/mibdp4", function (err, db) {
    httpServer.listen(8080);
    var io = socketio.listen(httpServer);

    db.createCollection("test", function (err, collection) {
        io.sockets.on('connection',
                function (client) {
                    client.emit('my-address', {host: client.request.connection.remoteAddress, port: client.request.connection.remotePort});
                    client.on('poner', function (data) {
                        collection.insert(data, {safe: true}, function (err, result) {});
                    });
                    client.on('obtener', function (data) {
                        collection.find(data).toArray(function (err, results) {
                            client.emit('obtener', results);
                        });
                    });

                });
    });

    db.createCollection("historial", function (err, collection) {
        io.sockets.on('connection',
                function (client) {
                    // Inserto el historial de la temp nueva
                    client.on('add_historial', function (data) {
                        collection.insert(data, {safe: true}, function (err, result) {});
                        var code = controlAgente();
                    if (code === 1) {
                        io.emit('warning_alert', 1);
                    } else if (code === 2) {
                        io.emit('warning_alert',2);
                    } else if (code === 3) {
                        io.emit('warning_alert',3);
                    }
                    });
                    // Control del Agente

                    

                });
    });

});


console.log("Servicio MongoDB iniciado");




