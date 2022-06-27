package net.minecraft.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

public class CryptManager
{
    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Generate a new shared secret AES key from a secure random source
     */
    public static SecretKey createNewSharedKey()
    {
        try
        {
            KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
            keygenerator.init(128);
            return keygenerator.generateKey();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            throw new Error(nosuchalgorithmexception);
        }
    }

    /**
     * Generates RSA KeyPair
     */
    public static KeyPair generateKeyPair()
    {
        try
        {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
            keypairgenerator.initialize(1024);
            return keypairgenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            LOGGER.error("Key pair generation failed!");
            return null;
        }
    }

    /**
     * Compute a serverId hash for use by sendSessionRequest()
     */
    public static byte[] getServerIdHash(String serverId, PublicKey publicKey, SecretKey secretKey)
    {
        try
        {
            return digestOperation(serverId.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded());
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            unsupportedencodingexception.printStackTrace();
            return null;
        }
    }

    /**
     * Compute a message digest on arbitrary byte[] data
     */
    private static byte[] digestOperation(byte[]... data)
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");

            for (byte[] abyte : data)
            {
                messagedigest.update(abyte);
            }

            return messagedigest.digest();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new PublicKey from encoded X.509 data
     */
    public static PublicKey decodePublicKey(byte[] encodedKey)
    {
        try
        {
            EncodedKeySpec encodedkeyspec = new X509EncodedKeySpec(encodedKey);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            return keyfactory.generatePublic(encodedkeyspec);
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException ignored)
        {
        }

        LOGGER.error("Public key reconstitute failed!");
        return null;
    }

    /**
     * Decrypt shared secret AES key using RSA private key
     */
    public static SecretKey decryptSharedKey(PrivateKey key, byte[] secretKeyEncrypted)
    {
        return new SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES");
    }

    /**
     * Encrypt byte[] data with RSA public key
     */
    public static byte[] encryptData(Key key, byte[] data)
    {
        return cipherOperation(1, key, data);
    }

    /**
     * Decrypt byte[] data with RSA private key
     */
    public static byte[] decryptData(Key key, byte[] data)
    {
        return cipherOperation(2, key, data);
    }

    /**
     * Encrypt or decrypt byte[] data using the specified key
     */
    private static byte[] cipherOperation(int opMode, Key key, byte[] data)
    {
        try
        {
            return Objects.requireNonNull(createTheCipherInstance(opMode, key.getAlgorithm(), key)).doFinal(data);
        }
        catch (IllegalBlockSizeException | BadPaddingException illegalblocksizeexception)
        {
            illegalblocksizeexception.printStackTrace();
        }

        LOGGER.error("Cipher data failed!");
        return null;
    }

    /**
     * Creates the Cipher Instance.
     */
    private static Cipher createTheCipherInstance(int opMode, String transformation, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(transformation);
            cipher.init(opMode, key);
            return cipher;
        }
        catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException invalidkeyexception)
        {
            invalidkeyexception.printStackTrace();
        }

        LOGGER.error("Cipher creation failed!");
        return null;
    }

    /**
     * Creates a Cipher instance using the AES/CFB8/NoPadding algorithm. Used for protocol encryption.
     */
    public static Cipher createNetCipherInstance(int opMode, Key key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
            return cipher;
        }
        catch (GeneralSecurityException generalsecurityexception)
        {
            throw new RuntimeException(generalsecurityexception);
        }
    }
}
