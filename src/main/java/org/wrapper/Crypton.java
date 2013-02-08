/**
 * 
 */
package org.wrapper;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author uli
 *
 */
public class Crypton {
    //                             1234567890123456
    static final String MY_SECRET="uliWarDa-padPadP"; // FIXME: Keep this secret"
    static final String MY_IV    ="2937235gddgff222"; // FIXME: Keep this secret"
    private Cipher cipher;
    private byte[] keyBytes;
    private byte[] ivBytes;
    private final SecretKeySpec keySpec;
    private final IvParameterSpec ivSpec;
    
    static private class CryptonHolder {
        static Crypton INSTANCE=new Crypton();
    }
    
    static public Crypton getInstance() {
        return CryptonHolder.INSTANCE;
    }
    
    private Crypton() {
        this.keyBytes = MY_SECRET.getBytes();
        this.ivBytes  = MY_IV.getBytes();
        this.keySpec = new SecretKeySpec(keyBytes, "AES");
        this.ivSpec = new IvParameterSpec(ivBytes);
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public String encrypt(String src) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        return toBase64(encrypt(src.getBytes()));
    }

    public String decrypt(String base64) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        byte[] e = fromBase64(base64);
        return new String(decrypt(e));
    }

    public synchronized byte[] encrypt(byte[] decrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] salt = new salt().getValue();
        byte[] decryptedWithSalt = new byte[decrypted.length + salt.length];
        System.arraycopy(decrypted, 0, decryptedWithSalt, 0, decrypted.length);
        System.arraycopy(salt, 0, decryptedWithSalt, decrypted.length, salt.length);
        byte[] encrypted = new byte[cipher.getOutputSize(decryptedWithSalt.length)];
        int enc_len = cipher.update(decryptedWithSalt, 0, decryptedWithSalt.length, encrypted, 0);
        /*enc_len +=*/ cipher.doFinal(encrypted, enc_len);
        return encrypted;
    }

    public synchronized byte[] decrypt(byte[] encrypted) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decrypted = new byte[cipher.getOutputSize(encrypted.length)];
        int dec_len = cipher.update(encrypted, 0, encrypted.length, decrypted, 0);
        dec_len += cipher.doFinal(decrypted, dec_len);
        byte[] truncated = new byte[dec_len - salt.length()];
        System.arraycopy(decrypted, 0, truncated, 0, dec_len - salt.length());
        return truncated;
    }

    static private class salt {

        static private final Random r = new Random();
        static private final int saltLength = 8;
        private byte[] saltValue = new byte[saltLength];

        public salt() {
            r.nextBytes(saltValue);
        }

        public byte[] getValue() {
            return this.saltValue;
        }

        public static int length() {
            return saltLength;
        }
    }

    static private String toBase64(byte[] src) {
        return new String(Base64.encodeBase64(src));
    }

    static private byte[] fromBase64(String src) {
        return Base64.decodeBase64(src.getBytes());
    }

    static public void main(String[] args) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        Crypton crypton = Crypton.getInstance();
        for (String arg : args) {
             System.out.println(arg + " -> " + crypton.encrypt(arg));
        }
    }

}
