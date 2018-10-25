/*
 * @author Daniel Messiha
 * ID: 105519550
 * October 25 - 2018
 */

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Base64;

public class CodingServiceImpl extends UnicastRemoteObject implements CodingService {

    public static final String SERVICE_NAME = CodingServiceImpl.class.getSimpleName();

    CodingServiceImpl() throws RemoteException {

    }

    @Override
    public String base64Encode(String plainText) throws RemoteException {
        return new String(Base64.getEncoder().encode(plainText.getBytes()));
    }

    @Override
    public String base64Decode(String encodedText) throws RemoteException {
        return new String(Base64.getDecoder().decode(encodedText.getBytes()));
    }

    @Override
    public String md5Encode(String plainText) throws RemoteException, NoSuchAlgorithmException {
        return new String(MessageDigest.getInstance("MD5").digest(plainText.getBytes()));
    }

    @Override
    public String sha1Hash(String plainText) throws RemoteException, NoSuchAlgorithmException {
        return base64Encode(new String(MessageDigest.getInstance("SHA-1").digest(plainText.getBytes())));
    }

    @Override
    public String encrypt(String txt, String key) throws RemoteException {
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, skeyspec);
            byte[] encrypted = cipher.doFinal(txt.getBytes());
            return new String(Base64.getEncoder().encode(encrypted));
        } catch (Exception e) {
        }

        return "Error encrypting message!";
    }

    @Override
    public String decrypt(String txt, String key) {
        try {
            SecretKeySpec skeyspec = new SecretKeySpec(key.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, skeyspec);
            byte[] unencoded = Base64.getDecoder().decode(txt);
            return new String(cipher.doFinal(unencoded));
        } catch (Exception e) {
        }
        return "Decryption Failed!";
    }

    @Override
    public void storeEncryptedFile(String fileName, String fileContent, String key) throws IOException {
        final File file = new File(fileName);

        if (!file.getAbsoluteFile().getParentFile().exists()) {
            file.getAbsoluteFile().getParentFile().mkdirs();
        }

        final FileOutputStream fos = new FileOutputStream(file);

        fos.write(encrypt(fileContent, key).getBytes());
        fos.close();
    }

    @Override
    public String readEncryptedFile(String fileName, String key) throws IOException {
        final byte[] fileContent = Files.readAllBytes(Paths.get(fileName));

        return decrypt(new String(fileContent), key);
    }
}
