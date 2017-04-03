/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IcomuniacionServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jose-laptop
 */
public interface IcomuniacionServer extends Remote {
    
    /**
     * Obtine el subtotal de una replica
     * @return el total donado en una replica
     * @throws java.rmi.RemoteException
     */
    public double getSubTotal() throws RemoteException;
    
    /**
     * Obtiene el numero de clientes registrados en una replica
     * @return el total de clientes registrados
     * @throws java.rmi.RemoteException
     */
    public int getNumRegistros() throws RemoteException;
    
    /**
     * Devuelve la informacion de la replica.
     * @return
     * @throws RemoteException 
     */
    public String getRMIreg() throws RemoteException;
    
    /**
     * Indica si el usuario "user" está registrado en el servidor  
     * @param user nombre del usuario que queremos comprobar
     * @return true si el usuario está registrado y false en caso contrario
     * @throws java.rmi.RemoteException
     */
    public boolean isRegister(String user) throws RemoteException;
    
    
    public boolean isDonate(String user) throws RemoteException;
}
