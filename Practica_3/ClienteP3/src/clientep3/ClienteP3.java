/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientep3;

import IcomunicacionCliente.IcomunicacionCliente;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author Jose-laptop
 */
public class ClienteP3 {

    private String host = "";
    private IcomunicacionCliente miServidor;
    private Registry mireg;

    public ClienteP3() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        // Crea el stub para el cliente especificando el nombre del servidor
        host = "server1";
        try {
            mireg = LocateRegistry.getRegistry("localhost", 1099);
            miServidor = (IcomunicacionCliente) mireg.lookup(host);
        } catch (NotBoundException | RemoteException ex) {
            System.err.println("Exception del sistema: " + ex);
        }

    }

    public String registrarse(String cliente) {
        try {
            System.out.println("Registrandome en el server");
            host = miServidor.registro(cliente);
            if (host == "error") {
                return "Usuario ya registrado";
            }
            System.out.println("Redirigido al server: " + host);
            miServidor = (IcomunicacionCliente) mireg.lookup(host);
        } catch (RemoteException ex) {
            System.err.println("Exception del sistema: " + ex);
        } catch (NotBoundException ex) {
            System.err.println("Exception del sistema: " + ex);
        }
        return "Usuario registrado correctamente en el servidor " + host;
    }

    public String donar(String cliente, double donacion, String concepto) {
        System.out.println("Donando...");
        int code;
        try {
            code = miServidor.setDonacion(cliente, donacion, concepto);
            if (code == 1) { // Error de registro
                return "Los usuarios deben registrarse para donar";
            } else if (code == 0) {
                return "Donación correcta";
            }
        } catch (RemoteException ex) {
            System.err.println("Exception del sistema: " + ex);
        }
        return "Error en la donación";
    }

    public String getTotal() {
        System.out.println("Donando...");
        double code=0;
        try {
            code = miServidor.getTotal();
           
        } catch (RemoteException ex) {
            System.err.println("Exception del sistema: " + ex);
        }
        return ""+code;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String host = "";
        String cliente = "";
        Scanner teclado = new Scanner(System.in);
        System.out.println("Escriba el nombre del servidor: ");
        host = teclado.nextLine();

        Scanner teclado2 = new Scanner(System.in);
        System.out.println("Escriba el nombre del cliente: ");
        cliente = teclado2.nextLine();
        // Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            // Crea el stub para el cliente especificando el nombre del servidor
            Registry mireg = LocateRegistry.getRegistry("localhost", 1099);
            IcomunicacionCliente miServidor = (IcomunicacionCliente) mireg.lookup(host);
            // Pone el contador al valor inicial 0
            //System.out.println("Registrandome en el server");
            //host = miServidor.registro(cliente);
            if (host == "error") {
                System.out.println("Usuarios ya registrado");
            }
            System.out.println("Redirigido al server: " + host);
            miServidor = (IcomunicacionCliente) mireg.lookup(host);

            long donacion = 50;

            System.out.println("Donando...");
            int code = miServidor.setDonacion(cliente, donacion, "Hola");
            if (code == 1) { // Error de registro
                System.out.println("Los usuarios deben registrarse para donar");
            } else if (code == 0) {
                System.out.println("Donación correcta");
            }

            System.out.println("Total donaciones" + miServidor.getTotal());
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
        System.exit(0);
    }

}
