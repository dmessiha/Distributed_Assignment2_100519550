
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;

public class Display extends JFrame {

    private CodingService service;
    private JPanel panel1;
    private JTextArea outputTxt;
    private JTextArea inputTxt;
    private JComboBox operation;
    private JButton executeButton;
    private JPanel rhs;
    private JPanel lhs;
    private JButton storeEncryptedFileButton;
    private JButton loadEncryptedFileButton;


    public Display(final CodingService service) {
        this.service = service;

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleOperation(operation.getSelectedItem().toString(), inputTxt.getText());
                } catch (RemoteException e1) {
                    disconnectError();
                } catch (Throwable e2) {
                    JOptionPane.showMessageDialog(panel1, e2.getMessage());
                }
            }
        });

        setContentPane(panel1);
        storeEncryptedFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String localFilePath = JOptionPane.showInputDialog(panel1, "Enter file location");
                String remoteFilePath = JOptionPane.showInputDialog(panel1, "Enter the remote file name");
                String key = JOptionPane.showInputDialog(panel1, "Enter the key");

                try {
                    String fileContent = new String(Files.readAllBytes(Paths.get(localFilePath)));
                    service.storeEncryptedFile(remoteFilePath, fileContent, key);
                } catch (RemoteException e1) {
                    disconnectError();
                } catch (Throwable e2) {
                    JOptionPane.showMessageDialog(panel1, "Error: " + e2.getClass() + ": " + e2.getMessage());
                }
            }
        });

        loadEncryptedFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String remoteFilePath = JOptionPane.showInputDialog(panel1, "Enter the remote file name");
                String localFilePath = JOptionPane.showInputDialog(panel1, "Enter file location");
                String key = JOptionPane.showInputDialog(panel1, "Enter the key");

                try {
                    String fileContent = service.readEncryptedFile(remoteFilePath, key);
                    FileOutputStream fos = new FileOutputStream(localFilePath);

                    fos.write(fileContent.getBytes());
                    fos.close();

                } catch (RemoteException e1) {
                    disconnectError();
                } catch (Throwable e2) {
                    JOptionPane.showMessageDialog(panel1, "Error: " + e2.getClass() + ": " + e2.getMessage());
                }
            }
        });
    }

    private void handleOperation(final String operation, final String input) throws Throwable {
        switch (operation) {
            case "Base64 Encode":
                outputTxt.setText(service.base64Encode(input));
                break;
            case "Base64 Decode":
                outputTxt.setText(service.base64Decode(input));
                break;
            case "MD5 Hash":
                outputTxt.setText(service.md5Encode(input));
                break;
            case "SHA-1 Hash":
                outputTxt.setText(service.sha1Hash(input));
                break;
            case "Encrypt":
                String encKey = JOptionPane.showInputDialog(panel1, "Enter encryption key");
                outputTxt.setText(service.encrypt(input, encKey));
                break;
            case "Decrypt":
                String decKey = JOptionPane.showInputDialog(panel1, "Enter decryption key");
                outputTxt.setText(service.decrypt(input, decKey));
                break;
        }
    }

    private void disconnectError() {
        JOptionPane.showMessageDialog(panel1, "You have been disconnected!");
        System.exit(0);
    }
}
