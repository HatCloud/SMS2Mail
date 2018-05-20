
package me.hatcloud.sms2mail.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Coder {

    public static final int AES_KEY_LENGTH    = 16;
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA_ALGORITHM = "RSA";

    private static final String RSA_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW3emgj+/AFd9JnHJeR2yz4cPCCE4Mhr/Yb2wlgcmY5/plPduXr7L39MF9uO5pW/R09Y48nvi38tBXpd7+5wT6XiZmNfOiZCnCs8YSza7kb1W0ZKc8nY8iIf3qKp1WMAJhmOwVF4c6hQnSXSdvumuaKDROaWDifaHHqbzduvNUZQIDAQAB";
    private static final String RSA_PRI_KEY = "";

    private final static String[] hexDigits = {
            "0", "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "a", "b", "c", "d", "e", "f"
    };

    public static final String encodeMD5(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digester.update(string.getBytes());
        byte[] digest = digester.digest();
        return byteArrayToString(digest);
    }

    public static final String encodeMD5(File file) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        try {
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return byteArrayToString(md5.digest());
    }

    private static String byteArrayToString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    public static byte[] hex2Byte(String hexStr) {
        if (hexStr == null) {
            return null;
        }
        int l = hexStr.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        try {
            for (int i = 0; i != l / 2; i++) {
                b[i] = (byte) Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return b;
    }

    public static final String encodeSHA(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digester.update(string.getBytes());
        byte[] digest = digester.digest();
        return byteArrayToString(digest);
    }

    public static final byte[] encodeSHABytes(String string) {
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        MessageDigest digester = null;
        try {
            digester = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        digester.update(string.getBytes());
        return digester.digest();
    }

    public static final String encodeBase64(String string) {
        return Base64.encodeToString(string.getBytes(), Base64.DEFAULT | Base64.NO_WRAP);
    }

    public static final String encodeBase64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT | Base64.NO_WRAP);
    }

    public static final byte[] encodeBase64Bytes(String string) {
        return Base64.encode(string.getBytes(), Base64.DEFAULT | Base64.NO_WRAP);
    }

    public static final String decodeBase64(String string) {
        return new String(Base64.decode(string, Base64.DEFAULT));
    }

    public static final byte[] decodeBase64Bytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    /**
     * 生成密钥 <br>
     *
     * @return byte[] 二进制密钥
     * @throws Exception
     */
    public static byte[] initKey() {
        try {
            String key = getRandomString(AES_KEY_LENGTH);
            LogUtil.d("Key :" + key);
            return key.getBytes();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMN";

    private static String getRandomString(int length){
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len-1)));
        }
        return sb.toString();
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    /**
     * 返回经过AES加密和base64编码后的数据
     */
    public static final String encodeAES(String data, String key) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {
            return null;
        }
        byte[] raw = decodeBase64Bytes(key);
        if (raw == null || raw.length != AES_KEY_LENGTH) {
            return null;
        }
        SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            return encodeBase64(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回经过base64解码和AES解密后的数据
     */
    public static final String decodeAES(String data, String key) {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {
            return null;
        }
        if (key.length() != AES_KEY_LENGTH) {
            return null;
        }
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] encryptedByte = decodeBase64Bytes(data);
            if (null == encryptedByte) {
                return null;
            }
            byte[] decryptedByte = cipher.doFinal(encryptedByte);
            return new String(decryptedByte);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (NoSuchPaddingException e) {
            return null;
        } catch (InvalidKeyException e) {
            return null;
        } catch (InvalidAlgorithmParameterException e) {
            return null;
        } catch (IllegalBlockSizeException e) {
            return null;
        } catch (BadPaddingException e) {
            return null;
        }
    }

    public static byte[] encryptByPublicKey(byte[] data) {
        try {
            // 对公钥解密
            byte[] keyBytes = decodeBase64Bytes(RSA_PUB_KEY);

            // 对数据加密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(keyBytes));

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用私钥解密 
     *
     * @param encryptedData
     *            经过encryptedData()加密返回的byte数据 
     * @return
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(Coder.decodeBase64Bytes(RSA_PRI_KEY)));
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * anroid私钥加密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(Coder.decodeBase64Bytes(RSA_PRI_KEY)));
        return cipher.doFinal(encryptedData);
    }

    /**
     * android公钥解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData) throws Exception {
        return decryptByPublicKey(RSA_PUB_KEY, encryptedData);
    }

    public static byte[] decryptByPublicKey(String rsaPubKey, byte[] encryptedData) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decodeBase64Bytes(rsaPubKey);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(keyBytes));
        return cipher.doFinal(encryptedData);
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法 
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法 
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private static PrivateKey getPrivateKey(byte[] keyBytes) throws NoSuchAlgorithmException,
            InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    public static String subAESKey(String AESResult, int length) {
        if (TextUtils.isEmpty(AESResult) || AESResult.length() < length)
            return AESResult;

        return AESResult.substring(AESResult.length() - length, AESResult.length());
    }

}
