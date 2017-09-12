package com.midea.meicloud.common;

import org.apache.commons.codec.binary.Base64;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * .需要依赖 commons-codec 包, RSA加密的明文不可以超过117字节，长内容加密时，使用AES256加密，然后再用RSA加密密码
 */
public class RSACoder {
    public static final String KEY_ALGORITHM = "RSA";
    public static final String RSA_ALG = "RSA/ECB/PKCS1Padding";
    public static final String AES_ALG= "AES/CBC/PKCS5Padding";

    private static boolean inited = false;
    private static byte[] publicKey = null;
    private static byte[] privateKey = null;

    public static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static String encryptBASE64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

//    /**
//     * 用私钥对信息生成数字签名
//     *
//     * @param data       加密数据
//     * @return
//     * @throws Exception
//     */
//    public static String sign(byte[] data) throws Exception {
//        // 解密由base64编码的私钥
//        byte[] keyBytes = privateKey;
//        // 构造PKCS8EncodedKeySpec对象
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
//        // KEY_ALGORITHM 指定的加密算法
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 取私钥匙对象
//        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
//        // 用私钥对信息生成数字签名
//        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//        signature.initSign(priKey);
//        signature.update(data);
//        return encryptBASE64(signature.sign());
//    }
//
//    /**
//     * 校验数字签名
//     *
//     * @param data      加密数据
//     * @param sign      数字签名
//     * @return 校验成功返回true 失败返回false
//     * @throws Exception
//     */
//    public static boolean verify(byte[] data, String sign)
//            throws Exception {
//        // 解密由base64编码的公钥
//        byte[] keyBytes = publicKey;
//        // 构造X509EncodedKeySpec对象
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//        // KEY_ALGORITHM 指定的加密算法
//        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//        // 取公钥匙对象
//        PublicKey pubKey = keyFactory.generatePublic(keySpec);
//        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
//        signature.initVerify(pubKey);
//        signature.update(data);
//        // 验证签名是否正常
//        return signature.verify(decryptBASE64(sign));
//    }

    public static byte[] decryptByPrivateKey(byte[] encryptedData) throws Exception {
        // 对密钥解密
        byte[] keyBytes = privateKey;
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param encryptedData
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] encryptedData)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = publicKey;
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据解密
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(encryptedData);
    }

    /**
     * 加密<br>
     * 用公钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data)
            throws Exception {
        // 对公钥解密
        byte[] keyBytes = publicKey;
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return  cipher.doFinal(data);
    }

    /**
     * 加密<br>
     * 用私钥加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = privateKey;
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(RSA_ALG);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return  cipher.doFinal(data);
    }
    /**
     * 取得公钥
     *
     * @return
     * @throws Exception
     */
    public static String getPublicKey(){
        return encryptBASE64(publicKey);
    }

    /**
     * 初始化密钥
     *
     * @return
     * @throws Exception
     */
    public static void initKey() throws Exception {
        synchronized (RSACoder.class){
            if(inited)
                return;
            KeyPairGenerator keyPairGen = KeyPairGenerator
                    .getInstance("RSA");
            keyPairGen.initialize(1024);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            privateKey = keyPair.getPrivate().getEncoded();
            publicKey = keyPair.getPublic().getEncoded();
            inited = true;
        }
    }

    public static void setKey(String pubKey, String priKey){
        publicKey = decryptBASE64(pubKey) ;
        privateKey = decryptBASE64(priKey);
        inited = true;
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

    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        if(decryptKey.length() > 16){
            decryptKey = decryptKey.substring(0, 16);
        }
        byte [] bIv = new byte[16];
        System.arraycopy(encryptBytes, 0, bIv, 0, 16);
        byte[] data = new byte[encryptBytes.length - 16];
        System.arraycopy(encryptBytes, 16, data, 0, encryptBytes.length -16);
        IvParameterSpec iv = new IvParameterSpec(bIv);
        SecretKeySpec skeySpec = new SecretKeySpec(decryptKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance(AES_ALG);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] decryptBytes = cipher.doFinal(data);
        return new String(decryptBytes);
    }

    public static EncryptedData encryptLongContentByPublicKey(String content) throws Exception{
        String aesKey = UUID.randomUUID().toString();
        String dataB64 = encryptBASE64(aesEncryptToBytes(content,aesKey));
        String encryptedKey = encryptBASE64(encryptByPublicKey(aesKey.getBytes()));
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setData(dataB64);
        encryptedData.setKey(encryptedKey);
        return encryptedData;
    }

    public static EncryptedData encryptLongContentByPrivateKey(String content) throws Exception{
        String aesKey = UUID.randomUUID().toString();
        String dataB64 = encryptBASE64(aesEncryptToBytes(content,aesKey));
        String encryptedKey = encryptBASE64(encryptByPrivateKey(aesKey.getBytes()));
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setData(dataB64);
        encryptedData.setKey(encryptedKey);
        return encryptedData;
    }

    public static String decryptLongContentByPublicKey(EncryptedData data)throws Exception{
        byte[] aesKeyRsa = decryptBASE64(data.getKey());
        String aesKey = new String(decryptByPublicKey(aesKeyRsa));
        String res = aesDecryptByBytes(decryptBASE64(data.getData()), aesKey);
        return  res;
    }
    public static String decryptLongContentByPrivateKey(EncryptedData data )throws Exception{
        byte[] aesKeyRsa = decryptBASE64(data.getKey());
        String aesKey = new String(decryptByPrivateKey(aesKeyRsa));
        String res = aesDecryptByBytes(decryptBASE64(data.getData()), aesKey);
        return  res;
    }

    public static void main(String[] args) throws Exception {
       // initKey();
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCekcTcnK2SblaJWBoWhat7nBFY4x3TGYkdnyWtE53Fdvx6r0nEbaGIhF2uW+IszN2IMcUj6DakV4GhF60Q4ZA3+E68fiR6PU0Q/SAm6qNb6qfxDkao5fOIVtaZCH6QTPCmb/kD5ePhe7Bnh7P16ZSmHtPv4j1QL2uGNF6IAyJ1CQIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ6RxNycrZJuVolYGhaFq3ucEVjjHdMZiR2fJa0TncV2/HqvScRtoYiEXa5b4izM3YgxxSPoNqRXgaEXrRDhkDf4Trx+JHo9TRD9ICbqo1vqp/EORqjl84hW1pkIfpBM8KZv+QPl4+F7sGeHs/XplKYe0+/iPVAva4Y0XogDInUJAgMBAAECgYAK56VtMo0xQ8tJuLhakii/4HTE8yGK8fefBOHXbEDuYodsfH+LNarlM40pv4HnaNNXWWzgUkjntmjgxjsNrSV4yCGS1Y3zVxjvgG5qaZXqlB288yeX2p2hhRIpCOxZXxuugVsiYDdPMkdETUvG3THRjho6s2fX6XL8wA6ZFvxcnQJBANdzic0l9WMNaTm5ElVueKaAFFUbxjJ96kmqQBdOHFCkxWF3K8FuP6wJDa+zZdQSZr3HnEgdkU9DM198D1IPwk8CQQC8aafdFWpheZ1ZE6Y4Jg2xw0PD/E8B/K5tTENo9bRfEctMx/OOozgvgbdOMxswrW4hYxJ4+ndnZ8IoeaqHhrUnAkBj5Kv1tC3MGUG6g7aeabQC7st+knwYmQzxzsAcjhjOwzbI8+oTqzxWVXRFDJaf91Avmcc6IItpBq1hDjJESA49AkEAuvSEEsZbRtmsHmV2/CQWVpuRNHm51Bjs45tXEGEuV1+KwWdu78xZxhoKz9e6VTTiINLz04OE0+CLMip34f7y5QJAJzB8J+n4Sn7H9385rYSTQeNegqsc30ozACg3ZlUZB5HSNuCoxnueFTUSA9YSeEl9uwNztebZZZfkhJLprEr3gg==";
        setKey(publicKey, privateKey);

        assert ("aGVsbG8zMTLpmYjkvbPmlIBoZWxsbzMxMumZiOS9s+aUgGhlbGxvMzEy6ZmI5L2z5pSAaGVsbG8zMTLpmYjkvbPmlIBoZWxsbzMxMumZiOS9s+aUgA==".equals(encryptBASE64("hello312陈佳攀hello312陈佳攀hello312陈佳攀hello312陈佳攀hello312陈佳攀".getBytes())));

        assert (decryptBASE64("aGVsbG8zMTLpmYjkvbPmlIBoZWxsbzMxMumZiOS9s+aUgGhlbGxvMzEy6ZmI5L2z5pSAaGVsbG8zMTLpmYjkvbPmlIBoZWxsbzMxMumZiOS9s+aUgA==").equals("hello312陈佳攀hello312陈佳攀hello312陈佳攀hello312陈佳攀hello312陈佳攀"));

        System.out.println(getPublicKey());
        System.out.println(encryptBASE64(encryptByPrivateKey("hello".getBytes())));

        String src = "helsdfaslo";//不超过117字节
        byte[]res =encryptByPrivateKey(src.getBytes());
        String src2 = new String(decryptByPublicKey(res));
        assert (src.equals(src2));//RSA

        String longContext = "voifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdjvoifojvertj09aejerqoufgvbaqjeorfijaw9irfdj";
        EncryptedData priEncLong = encryptLongContentByPrivateKey(longContext);
        String longContent2 = decryptLongContentByPublicKey(priEncLong);
        assert (longContent2.equals(longContext));

        priEncLong = encryptLongContentByPublicKey(longContext);
        longContent2 = decryptLongContentByPrivateKey(priEncLong);
        assert (longContent2.equals(longContext));

        String dt = "XkAG2xzEjf3iZ9jC8RLXOb5P+5ldUN16JRj8EonuZgn+7X02IGbVZAbXggrx9k9HqOGIPoljTLTWED6Fux2b88eBh/EjaSoiiUHA4PnxQe06SCGTDvUoHJV5d8h67du1MBxyYDrqikosmZNsnTDDppEk3zxDmiVFy8S6Xb6IVzk=";
        String dt2 = new String (decryptByPrivateKey(decryptBASE64(dt)));
        assert(dt2.equals("hello"));
    }
}