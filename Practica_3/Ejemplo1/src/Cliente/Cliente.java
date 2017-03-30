package Cliente;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import Interfaces.icontador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jose-laptop
 */
public class Cliente {

    public static void main(String[] args) {
        String host = "";
        Scanner teclado = new Scanner(System.in);
        System.out.println("Escriba el nombre o IP del servidor: ");
        host = teclado.nextLine();
// Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try {
// Crea el stub para el cliente especificando el nombre del servidor
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            icontador micontador = (icontador) mireg.lookup("mmicontador");
// Pone el contador al valor inicial 0
            System.out.println("Poniendo contador a 0");
            micontador.sumar(0);
// Obtiene hora de comienzo
            long horacomienzo = System.currentTimeMillis();
// Incrementa 1000 veces
            System.out.println("Incrementando...");
            for (int i = 0; i < 1000; i++) {
                micontador.incrementar();
            }
// Obtiene hora final, realiza e imprime calculos
            long horafin = System.currentTimeMillis();
            System.out.println("Media de las RMI realizadas = " + ((horafin - horacomienzo) / 1000f)
                    + " msegs");
            System.out.println("RMI realizadas = " + micontador.sumar());
        } catch (NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
        System.exit(0);
    }

}
