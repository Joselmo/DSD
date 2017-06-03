package ComunicacionServer;

import IcomuniacionServer.IcomuniacionServer;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jose-laptop
 */
public class ComunicacionServer extends UnicastRemoteObject implements IcomuniacionServer {

    private double subtotal = 0;
    private String server_name = "server1";
    private String server_target;
    // Clave valor que indica en Clave = Nombre de usuario , Valor = Ha pagado
    private Map<String, Pair<Boolean, String>> usuarios = new HashMap<String, Pair<Boolean, String>>();
    private IcomuniacionServer stubServer;

    private void connectStubServer() {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
            // Crea el stub para el cliente especificando el nombre del servidor

            Registry mireg = LocateRegistry.getRegistry("localhost", 1099);
            System.out.println("registrado " + " " + server_target);
            // Utilizo la comunicación en anillo
            stubServer = (IcomuniacionServer) mireg.lookup(server_target);

        } catch (NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema al iniciar el stubclient: " + e);
        }
    }

    public void registro(String user) {
        usuarios.put(user, new Pair<Boolean, String>(false, "concepto"));
        System.out.println("Nuevo registro: " + user);
    }

    public ComunicacionServer(String name, double total) throws RemoteException {
        server_name = name;
        subtotal = total;
        if ("server1".equals(server_name)) {
            server_target = "sserver2";
        } else if ("server2".equals(server_name)) {
            server_target = "sserver1";
            // EXAMEN
            subtotal = 0; // Indicamos que es el esclavo
        }

    }

    @Override
    public double getSubTotal() throws RemoteException {
        return subtotal;
    }

    @Override
    public int getNumRegistros() throws RemoteException {
        return usuarios.size();
    }

    @Override
    public String getRMIreg() throws RemoteException {
        return server_name;
    }

    @Override
    public boolean isRegister(String user) throws RemoteException {
        return usuarios.containsKey(user);
    }

    @Override
    public boolean isDonate(String user) throws RemoteException {
        // si el usuario está registrado
        if (usuarios.containsKey(user)) // Si ha pagado
        {
            return usuarios.get(user).getKey();
        }

        return false;
    }

    public double getTotal() {
        double cantidad = 0;
        // EXAMEN
        if (server_name.equals("server2")) { // Si soy el esclavo
            // Le pido a server1 (maestro) el total
            try {
                System.out.println("Conectando con el Stub de sserver" + server_target + " ...");
                if (stubServer == null) {
                    connectStubServer();
                }

                cantidad = stubServer.getSubTotal();
                System.out.println("Recibida respuesta ");

            } catch (RemoteException e) {
                System.err.println("Exception del sistema: " + e);
            }
        }// Si soy maestro ya tengo el total.
        ///////////////////////////
        return subtotal + cantidad;
    }

    public int setDonacion(String user, double cantidad, String concepto) throws RemoteException {
        if (stubServer == null) {
            connectStubServer();
        }
        if (usuarios.containsKey(user)) {
            // EXAMEN
            if (server_name.equals("server1")) { // Si soy el maestro lo añado
                subtotal += cantidad;
            } else { // Si soy el esclavo lo mando al maestro
                stubServer.addCantidad(cantidad);
            }
            /////////////////
            usuarios.put(user, new Pair<Boolean, String>(true, "concepto:" + concepto));
            System.out.println("Nueva donación: " + user + " Cantidad:" + cantidad + " Concepto:" + concepto);
            return 0;
        } else {
            try {
                if (stubServer.isRegister(user)) {
                    return stubServer.setDonacion(user, cantidad, concepto);
                } else {
                    System.out.println(user + " intento donar, pero no está registrado");
                    return 1;
                }
            } catch (RemoteException ex) {
                System.err.println("Exception del sistema: " + ex);
            }
        }

        return 1;
    }

    public String addRegistro(String user) throws RemoteException {
        if (stubServer == null) {
            connectStubServer();
        }

        // Comprobamos que no esté registrado el usuario
        if (isRegister(user) || stubServer.isRegister(user)) {
            return "error";
        }

        // Miramos que server tiene menos usuarios registrados
        if (stubServer.getNumRegistros() >= usuarios.size()) {
            registro(user);
            return server_name;
        } else {
            stubServer.registro(user);
            return server_target.substring(1);
        }
    }

    /** EXAMEN
     * Método creado para el examen añade una cantidad al total del servidor princpial
     * @param cantidad 
     */
    @Override
    public void addCantidad(double cantidad) {
        if(server_name.equals("server1")){ // Si soy el maestro
            subtotal += cantidad;
            System.out.println("Añadida doonación al total+= " + cantidad);
        }
    }

}
