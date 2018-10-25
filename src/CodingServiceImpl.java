import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
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

    private SecretKey asKey(String password) {
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte saltBytes[] = new byte[20];
            secureRandom.nextBytes(saltBytes);

            KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 128);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            SecretKey secretKey = factory.generateSecret(spec);

            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw new IllegalStateException("Key cant be generated!");
        }
    }
    /*
    byte[] mUniqueKey = new byte[16];
    byte[] mUniqueIV = new byte[16];
    // password
    // pass

    final Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    aesCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(mUniqueKey, "AES"), new IvParameterSpec(mUniqueIV));

    return aesCipher.doFinal(data);
    */
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
        final FileOutputStream fos = new FileOutputStream(fileName);

        fos.write(encrypt(fileContent, key).getBytes());
        fos.close();
    }

    @Override
    public String readEncryptedFile(String fileName, String key) throws IOException {
        final byte[] fileContent = Files.readAllBytes(Paths.get(fileName));

        return decrypt(new String(fileContent), key);
    }
}
