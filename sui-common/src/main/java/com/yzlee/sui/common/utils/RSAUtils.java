package com.yzlee.sui.common.utils;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 11:02
 */
public class RSAUtils {

    private static String charset="utf-8";

    // 生成密钥对
    public static KeyPair genKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        return keyPairGenerator.generateKeyPair();
    }

    // 公钥加密
    public static String encrypt(String content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content.getBytes(charset));
        return Base64.encode(bytes);
    }

    // 私钥解密
    public static String decrypt(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(Base64.decode(content.getBytes(charset)));
        return new String(bytes,charset);
    }

    public static void main(String[] args) throws Exception {
        KeyPair keyPair= genKeyPair();
        String s=encrypt("hello",keyPair.getPublic());
        System.out.println(s);
        String ss=decrypt(s, keyPair.getPrivate());
        System.out.println(ss);

    }

}
