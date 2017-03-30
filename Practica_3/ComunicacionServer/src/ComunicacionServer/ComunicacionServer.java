package ComunicacionServer;

import IcomuniacionServer.IcomuniacionServer;
import java.rmi.RemoteException;

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

    @Override
    public double getSubTotal() throws RemoteException {
        
        
        
        return 0;
    }

    @Override
    public int getNumRegistros() throws RemoteException {
        
        
        return 0;
    }

    @Override
    public String getRMIreg() throws RemoteException {
        return null;
    }
    
}
