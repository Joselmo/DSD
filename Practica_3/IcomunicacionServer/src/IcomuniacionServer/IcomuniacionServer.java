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
     */
    public double getSubTotal() throws RemoteException;
    
    /**
     * Obtiene el numero de clientes registrados en una replica
     * @return el total de clientes registrados
     */
    public int getNumRegistros() throws RemoteException;
    
    /**
     * Devuelve la informacion de la replica.
     * @return
     * @throws RemoteException 
     */
    public String getRMIreg() throws RemoteException;
    
    
    
}
