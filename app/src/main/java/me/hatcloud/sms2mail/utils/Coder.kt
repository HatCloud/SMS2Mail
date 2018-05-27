package me.hatcloud.sms2mail.utils

import android.text.TextUtils
import android.util.Base64
import java.io.*
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object Coder {

    private const val AES_KEY_LENGTH = 16
    private const val AES_ALGORITHM = "AES/CBC/PKCS5Padding"
    private const val RSA_ALGORITHM = "RSA"

    private const val RSA_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCW3emgj+/A" +
            "Fd9JnHJeR2yz4cPCCE4Mhr/Yb2wlgcmY5/plPduXr7L39MF9uO5pW/R09Y48nvi38tBXpd7+5wT6XiZmNfO" +
            "iZCnCs8YSza7kb1W0ZKc8nY8iIf3qKp1WMAJhmOwVF4c6hQnSXSdvumuaKDROaWDifaHHqbzduvNUZQIDAQAB"
    private const val RSA_PRI_KEY = ""

    private val hexDigits = arrayOf("0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

    private const val string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIGKLMN"

    fun encodeMD5(string: String): String? {
        if (TextUtils.isEmpty(string)) {
            return null
        }
        val digester: MessageDigest
        try {
            digester = MessageDigest.getInstance("MD5")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

        digester.update(string.toByteArray())
        val digest = digester.digest()
        return byteArrayToString(digest)
    }

    fun encodeMD5(file: File): String? {
        val fis: InputStream
        val buffer = ByteArray(1024)
        var numRead = 0
        val md5: MessageDigest
        try {
            fis = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return null
        }

        try {
            md5 = MessageDigest.getInstance("MD5")
            while (fis.read(buffer).apply { numRead = this } > 0) {
                md5.update(buffer, 0, numRead)
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return byteArrayToString(md5.digest())
    }

    private fun byteArrayToString(b: ByteArray): String {
        val resultSb = StringBuffer()
        for (i in b.indices) {
            resultSb.append(byteToHexString(b[i]))
        }
        return resultSb.toString()
    }

    private fun byteToHexString(b: Byte): String {
        var n = b.toInt()
        if (n < 0) {
            n += 256
        }
        val d1 = n / 16
        val d2 = n % 16
        return hexDigits[d1] + hexDigits[d2]
    }

    fun hex2Byte(hexStr: String?): ByteArray? {
        if (hexStr == null) {
            return null
        }
        val l = hexStr.length
        if (l % 2 == 1) {
            return null
        }
        val b = ByteArray(l / 2)
        try {
            for (i in 0 until l / 2) {
                b[i] = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 2), 16).toByte()
            }
        } catch (e: NumberFormatException) {
            return null
        }

        return b
    }

    fun encodeSHA(string: String): String? {
        if (TextUtils.isEmpty(string)) {
            return null
        }
        val digester: MessageDigest
        try {
            digester = MessageDigest.getInstance("SHA")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

        digester.update(string.toByteArray())
        val digest = digester.digest()
        return byteArrayToString(digest)
    }

    fun encodeSHABytes(string: String): ByteArray? {
        if (TextUtils.isEmpty(string)) {
            return null
        }
        val digester: MessageDigest
        try {
            digester = MessageDigest.getInstance("SHA")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null
        }

        digester.update(string.toByteArray())
        return digester.digest()
    }

    fun encodeBase64(string: String): String {
        return Base64.encodeToString(string.toByteArray(), Base64.DEFAULT or Base64.NO_WRAP)
    }

    fun encodeBase64(bytes: ByteArray?): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT or Base64.NO_WRAP)
    }

    fun encodeBase64Bytes(string: String): ByteArray {
        return Base64.encode(string.toByteArray(), Base64.DEFAULT or Base64.NO_WRAP)
    }

    fun decodeBase64(string: String): String {
        return String(Base64.decode(string, Base64.DEFAULT))
    }

    fun decodeBase64Bytes(string: String): ByteArray? {
        return Base64.decode(string, Base64.DEFAULT)
    }

    /**
     * 生成密钥 <br></br>
     *
     * @return byte[] 二进制密钥
     * @throws Exception
     */
    fun initAESKey(): ByteArray? {
        try {
            val key = getRandomString(AES_KEY_LENGTH)
            LogUtil.d("Key :$key")
            return key.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun initAESKeyString(): String? {
        try {
            return getRandomString(AES_KEY_LENGTH)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getRandomString(length: Int): String {
        val sb = StringBuffer()
        val len = string.length
        for (i in 0 until length) {
            sb.append(string[getRandom(len - 1)])
        }
        return sb.toString()
    }

    private fun getRandom(count: Int): Int {
        return Math.round(Math.random() * count).toInt()
    }

    /**
     * 返回经过AES加密和base64编码后的数据
     */
    fun encodeAES(data: String, key: String): String? {
        if (TextUtils.isEmpty(data)) {
            return null
        }
        if (TextUtils.isEmpty(key)) {
            throw InvalidKeySpecException("Empty AES Key")
        }
        val raw = decodeBase64Bytes(key)
        if (raw == null || raw.size != AES_KEY_LENGTH) {
            throw InvalidKeySpecException("Error AES Key")
        }
        val keySpec = SecretKeySpec(raw, "AES")

        try {
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            val iv = IvParameterSpec("0102030405060708".toByteArray())
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            return encodeBase64(cipher.doFinal(data.toByteArray()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 返回经过base64解码和AES解密后的数据
     */
    fun decodeAES(data: String, key: String): String? {
        if (TextUtils.isEmpty(data) || TextUtils.isEmpty(key)) {
            return null
        }
        val raw = decodeBase64Bytes(key)
        if (raw?.size != AES_KEY_LENGTH) {
            throw InvalidKeySpecException("Error AES Key")
        }
        val keySpec = SecretKeySpec(raw, "AES")

        try {
            val cipher = Cipher.getInstance(AES_ALGORITHM)
            val iv = IvParameterSpec("0102030405060708".toByteArray())
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val encryptedByte = decodeBase64Bytes(data) ?: return null
            val decryptedByte = cipher.doFinal(encryptedByte)
            return String(decryptedByte)
        } catch (e: NoSuchAlgorithmException) {
            return null
        } catch (e: NoSuchPaddingException) {
            return null
        } catch (e: InvalidKeyException) {
            return null
        } catch (e: InvalidAlgorithmParameterException) {
            return null
        } catch (e: IllegalBlockSizeException) {
            return null
        } catch (e: BadPaddingException) {
            return null
        }

    }

    fun encryptByPublicKey(data: ByteArray): ByteArray? {
        try {
            // 对公钥解密
            val keyBytes = decodeBase64Bytes(RSA_PUB_KEY)

            // 对数据加密
            val cipher = Cipher.getInstance(RSA_ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(keyBytes))

            return cipher.doFinal(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 用私钥解密
     *
     * @param encryptedData
     * 经过encryptedData()加密返回的byte数据
     * @return
     */
    fun decryptByPrivateKey(encryptedData: ByteArray): ByteArray? {
        try {
            val cipher = Cipher.getInstance(RSA_ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(Coder.decodeBase64Bytes(RSA_PRI_KEY)))
            return cipher.doFinal(encryptedData)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * anroid私钥加密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun encryptByPrivateKey(encryptedData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(Coder.decodeBase64Bytes(RSA_PRI_KEY)))
        return cipher.doFinal(encryptedData)
    }

    /**
     * android公钥解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    fun decryptByPublicKey(encryptedData: ByteArray): ByteArray {
        return decryptByPublicKey(RSA_PUB_KEY, encryptedData)
    }

    @Throws(Exception::class)
    fun decryptByPublicKey(rsaPubKey: String, encryptedData: ByteArray): ByteArray {
        // 对公钥解密
        val keyBytes = decodeBase64Bytes(rsaPubKey)

        val cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, getPublicKey(keyBytes))
        return cipher.doFinal(encryptedData)
    }

    /**
     * 通过公钥byte[](publicKey.getEncoded())将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getPublicKey(keyBytes: ByteArray?): PublicKey {
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return keyFactory.generatePublic(keySpec)
    }

    /**
     * 通过私钥byte[]将公钥还原，适用于RSA算法
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getPrivateKey(keyBytes: ByteArray?): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return keyFactory.generatePrivate(keySpec)
    }

    fun subAESKey(AESResult: String, length: Int): String? {
        return if (TextUtils.isEmpty(AESResult) || AESResult.length < length) AESResult else AESResult.substring(AESResult.length - length, AESResult.length)

    }

}