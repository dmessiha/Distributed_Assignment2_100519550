/*
 * @author Daniel Messiha
 * ID: 105519550
 * October 25 - 2018
 */

import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {

    public static void main(String[] args) throws Exception {
        // get RMI Registry
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        // get RMI interface
        final CodingService service = (CodingService) registry.lookup(CodingServiceImpl.SERVICE_NAME);
        // GUI creation
        final Display display = new Display(service);
        // GUI dimentions
        display.setSize(500, 500);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.setVisible(true);
    }
}