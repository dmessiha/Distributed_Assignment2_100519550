/*
 * @author Daniel Messiha
 * ID: 105519550
 * October 25 - 2018
 */

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;

public interface CodingService extends Remote {

    String base64Encode(String plainText) throws RemoteException;                                   // No key Base64 Encryption
    String base64Decode(String encodedText) throws RemoteException;                                 // No key Base64 Decryption
    String md5Encode(String plainText) throws RemoteException, NoSuchAlgorithmException;            // MD5 Hashing function
    String sha1Hash(String plainText) throws RemoteException, NoSuchAlgorithmException;             // Sha1 hashing function

    String encrypt(String text, String key) throws RemoteException;                                 // Base64 Encryption with key
    String decrypt(String encrypted, String key) throws RemoteException;                            // Base64 decryption with key

    void storeEncryptedFile(String fileName, String fileContent, String key) throws IOException;    // Base64 file Encryption using key
    String readEncryptedFile(String fileName, String key) throws IOException;                       // Base64 file Decryption using key
}
