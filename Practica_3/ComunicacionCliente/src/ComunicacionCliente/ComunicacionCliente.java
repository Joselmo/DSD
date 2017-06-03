/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComunicacionCliente;

import ComunicacionServer.ComunicacionServer;
import IcomunicacionCliente.IcomunicacionCliente;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Jose-laptop
 */
public class ComunicacionCliente extends UnicastRemoteObject implements IcomunicacionCliente {

    private int server_num;

    private ComunicacionServer server_stub;

    public ComunicacionCliente() throws RemoteException {
        server_num = 1;
    }

    public ComunicacionCliente(int name, ComunicacionServer conexion) throws RemoteException {
        server_num = name;
        this.server_stub = conexion;

    }

    @Override
    public String registro(String user) throws RemoteException {
        if(server_stub != null)
            return server_stub.addRegistro(user);
        
        return "";
    }

    @Override
    public double getTotal() throws RemoteException {
        double cantidad = 0;

        System.out.println("Conectando con el Stub de sserver" + server_num + " ...");
        if (server_stub != null) {
            cantidad = server_stub.getTotal();
        }

        System.out.println("Recibida respuesta ");

        return cantidad;
    }

    @Override
    public int setDonacion(String user, double cantidad, String concepto) throws RemoteException {
        if (cantidad <= 0) {
            System.out.println(user + " intento donar " + cantidad);
            return 2;
        }

        return server_stub.setDonacion(user, cantidad, concepto);

    }

}
