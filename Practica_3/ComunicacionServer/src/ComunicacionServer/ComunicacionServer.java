package ComunicacionServer;

import IcomuniacionServer.IcomuniacionServer;
import java.rmi.RemoteException;
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
public class ComunicacionServer implements IcomuniacionServer{
    
    private double subtotal = 0;
    private String server_name="server0";
    // Clave valor que indica en Clave = Nombre de usuario , Valor = Ha pagado
    private Map<String,Pair<Boolean,String>> usuarios = new HashMap<String,Pair<Boolean,String>>();

    public ComunicacionServer(String name, Map<String,Pair<Boolean,String>> usuarios, double subtotal){
        server_name= name;
        this.usuarios = usuarios;
        this.subtotal = subtotal;
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
    public boolean isRegister(String user) throws RemoteException{
        return usuarios.containsKey(user);
    }
    
    @Override
    public boolean isDonate(String user) throws RemoteException{
        // si el usuario está registrado
        if(usuarios.containsKey(user))
            // Si ha pagado
            return usuarios.get(user).getKey();
        
        return false;
    }
    
}
