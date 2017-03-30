/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package icontador;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jose-laptop
 */
public interface icontador extends Remote {

    int sumar() throws RemoteException;

    void sumar(int valor) throws RemoteException;

    public int incrementar() throws RemoteException;
}


