import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface CodingService extends Remote {

    String base64Encode(String plainText) throws RemoteException;
    String base64Decode(String encodedText) throws RemoteException;
    String md5Encode(String plainText) throws RemoteException, NoSuchAlgorithmException;
    String sha1Hash(String plainText) throws RemoteException, NoSuchAlgorithmException;

    String encrypt(String text, String key) throws RemoteException;
    String decrypt(String encrypted, String key) throws RemoteException;

    void storeEncryptedFile(String fileName, String fileContent, String key) throws IOException;
    String readEncryptedFile(String fileName, String key) throws IOException;
}
