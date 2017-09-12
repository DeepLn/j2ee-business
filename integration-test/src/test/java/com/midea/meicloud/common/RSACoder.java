package com.midea.meicloud.common;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

/**
 * .需要依赖 commons-codec 包, RSA加密的明文不可以超过117字节，长内容加密时，使用AES256加密，然后再用RSA加密密码
 */
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String RSA_ALG = "RSA/ECB/PKCS1Padding";
    public static final String AES_ALG= "AES/CBC/PKCS5Padding";

    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }



    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, byte[] keyBytes)
            throws Exception {
        // 对公钥解密
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return  cipher.doFinal(data);
    }


    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        if(encryptKey.length() > 16){
            encryptKey = encryptKey.substring(0, 16);
        }
        SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(AES_ALG);
        byte[] ivby = "1020304050617281".getBytes();
        IvParameterSpec iv = new IvParameterSpec(ivby);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] enc = cipher.doFinal(content.getBytes("utf-8"));
        byte[] res = new byte[16+enc.length];
        System.arraycopy(ivby, 0, res, 0, 16);
        System.arraycopy(enc, 0, res, 16, res.length-16);
        return res;
    }

    public static EncryptedData encryptLongContentByPublicKey(String content, String publicKey) throws Exception{
        String aesKey = UUID.randomUUID().toString();
        String dataB64 = encryptBASE64(aesEncryptToBytes(content,aesKey));
        String encryptedKey = encryptBASE64(encryptByPublicKey(aesKey.getBytes(), decryptBASE64(publicKey)));
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setData(dataB64);
        encryptedData.setKey(encryptedKey);
        return encryptedData;
    }


}