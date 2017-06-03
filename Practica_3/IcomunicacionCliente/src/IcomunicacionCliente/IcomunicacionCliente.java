/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IcomunicacionCliente;


import java.rmi.Remote;
import java.rmi.RemoteException;


/**
 *
 * @author Jose-laptop
 */
public interface IcomunicacionCliente extends Remote{
    
    /**
     * Regista a un usuario nuevo en el sistema.
     * El usuario solicita el registo en el sistema y se le concede el mejor
     * servidor posible para que realice el registo.
     * @param user nombre del usuario
     * @return servidor que le atendera
     * @throws RemoteException 
     */
    public String registro(String user) throws RemoteException;
    
    public double getTotal() throws RemoteException;
    
    public int setDonacion(String user, double cantidad, String concepto) throws RemoteException; 
    
    
}
