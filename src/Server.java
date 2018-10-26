/*
 * @author Daniel Messiha
 * ID: 105519550
 * October 25 - 2018
 */

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

    // RMI server
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, MalformedURLException {
        LocateRegistry.createRegistry(1099);

        Naming.bind(CodingServiceImpl.SERVICE_NAME, new CodingServiceImpl());
    }
}