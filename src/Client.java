import javax.swing.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    public static void main(String[] args) throws Exception {
        final Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        final CodingService service = (CodingService) registry.lookup(CodingServiceImpl.SERVICE_NAME);

        final Display display = new Display(service);

        display.setSize(500, 500);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.setVisible(true);
    }
}