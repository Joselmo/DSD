/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import ComunicacionCliente.ComunicacionCliente;
import ComunicacionServer.ComunicacionServer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javafx.util.Pair;

/**
 *
 * @author Jose-laptop
 */
public class Server {

    private static double subtotal = 0;
    private static String server_name = "server0";
    // Clave valor que indica en Clave = Nombre de usuario , Valor = Ha pagado
    private static Map<String, Pair<Boolean,String>> usuarios;

    public static void main(String[] args) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            usuarios = new HashMap<String, Pair<Boolean,String>>();
            // Crea una instancia de contador
            String name = "";
            System.out.printf("Inserta numero del server: ");
            Scanner teclado = new Scanner(System.in);
            name = teclado.nextLine();
            server_name = name;
            Registry reg = LocateRegistry.createRegistry(1099);
            ComunicacionServer miComunicadorServer = new ComunicacionServer(server_name,usuarios,subtotal);
            ComunicacionCliente miComunicadorCliente = 
                    new ComunicacionCliente(subtotal, miComunicadorServer,server_name,usuarios);
            
            
            // Le damos nombre al servidor 
            //Naming.rebind(server_name, miComunicadorServer);
            Naming.rebind(server_name, miComunicadorCliente);
            System.out.println("Servidor "+server_name+" RemoteException | MalformedURLExceptiondor preparado");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

}
