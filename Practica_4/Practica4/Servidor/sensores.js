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

//var MongoClient = require('mongodb').MongoClient;
//var MongoServer = require('mongodb').Server;
var mimeTypes = {"html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

var termostato = 0;
var lumens = 0;


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
    if (equipo == "temp")
        termostato = valor;
    else if (equipo == "luz")
        lumens = valor;
    else
        return "Error: Sensor no v&aacute;lidos";

    console.log("Actualizado el valor del sensor:" + equipo + " con " + valor);
}

var httpServer = http.createServer(
        function (request, response) {
            var uri = url.parse(request.url).pathname;
            console.log("### " + uri + "##");
            if (uri == "/")
                uri = "/Servidor/sensores.html";
            var fname = path.join(process.cwd(), uri);

            fs.exists(fname, function (exists) {
                if (exists) { // Si existe la uri la envio tal cual
                    fs.readFile(fname, function (err, data) {
                        if (!err) {
                            var extension = path.extname(fname).split(".")[1];
                            var mimeType = mimeTypes[extension];
                            //response.writeHead(200, {"Content-Type": "text/plain", "access-control-allow-origin": "*"});
                            response.writeHead(200, mimeType);
                            response.write(data);
                            response.end();
                        } else {
                            response.writeHead(200, {"Content-Type": "text/plain"});
                            response.write('Error de lectura en el fichero: ' + uri);
                            response.end();
                        }
                    });
                } else { // En caso de que no exista la tratamos
                    while (uri.indexOf('/') == 0)
                        uri = uri.slice(1);
                    var params = uri.split("/");
                    if (params.length >= 1) { //REST Request

                        console.log("Peticion REST: " + uri);
                        var valor = parseFloat(params[1]);
                        setValor(params[0], valor);
                        var result = getValor(params[0]);

                        response.writeHead(200, {"Content-Type": "text/html"});
                        response.write(result.toString());
                        response.end();
                    } else {
                        console.log("Peticion invalida: " + uri);
                        response.writeHead(200, {"Content-Type": "text/plain"});
                        response.write('404 Not Found\n');
                        response.end();
                    }
                }
            });
        }
);
/*
 var mongoClient = new MongoClient(new MongoServer('localhost', 27017));
 mongoClient.connect("mongodb://localhost:27017/mibdp4", function (err, db) {
 httpServer.listen(8081);
 var io = socketio.listen(httpServer);
 
 db.createCollection("historial", function (err, collection) {
 io.sockets.on('connection',
 function (client) {
 // Inserto el historial de la temp nueva
 client.on('add_historial', function (data) {
 collection.insert(data, {safe: true}, function (err, result) {});
 });
 /*client.on('obtener', function (data) {
 collection.find(data).toArray(function (err, results) {
 client.emit('obtener', results);
 });
 });*/
/* });
 });
 });*/


httpServer.listen(8081);
console.log("Servicio HTTP iniciado");

