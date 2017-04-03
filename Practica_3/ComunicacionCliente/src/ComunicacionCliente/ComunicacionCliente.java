/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ComunicacionCliente;

import ComunicacionServer.ComunicacionServer;
import IcomunicacionCliente.IcomunicacionCliente;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import javafx.util.Pair;

/**
 *
 * @author Jose-laptop
 */
public class ComunicacionCliente implements IcomunicacionCliente{
    
    private double total;
    private ComunicacionServer conexionServer;
    private String server_name;
    private Map<String,Pair<Boolean,String>> usuarios = new HashMap<String,Pair<Boolean,String>>();
    
    
    
    public ComunicacionCliente(double total, ComunicacionServer conexion,
            String name,Map<String,Pair<Boolean,String>> usuarios ){
        this.total = total;
        conexionServer = conexion;
        server_name = name;
        this.usuarios = usuarios;
    }
    

    @Override
    public String registro(String user) throws RemoteException {
        if(conexionServer != null){
            usuarios.put(user, new Pair<Boolean,String>(false,"concepto"));
            System.out.println("Nuevo registro: "+user);
        }
        return server_name;
    }

    @Override
    public double getTotal() throws RemoteException {
      //  if(conexionServer != null)
          //  total += conexionServer.getSubTotal();
        
        return total;
    }

    @Override
    public void setDonacion(String user, double cantidad, String concepto) throws RemoteException {
        if(usuarios.containsKey(user) && cantidad > 0){
            total += cantidad;
            usuarios.put(user, new Pair<Boolean,String>(true,"concepto:"+concepto));
            System.out.println("Nueva donación: "+user+" Cantidad:"+cantidad+" Concepto:"+concepto);
        }else{
            System.out.println(user+" intento donar, pero no está registrado");
        }       
    }
    
}
