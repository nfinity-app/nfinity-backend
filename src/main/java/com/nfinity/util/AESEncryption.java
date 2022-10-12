package com.nfinity.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class AESEncryption {

    public static String generateKey(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return Base64.encodeBase64String(b);
        }
        catch (NoSuchAlgorithmException e) {
            log.error("generate key failed.", e);
            return null;
        }
    }

    public static String encrypt(String sSrc, String sKey) {
        try {
            if (sKey == null) {
                log.error("key is null");
                return null;
            }
            if (sKey.length() != 24) {
                System.out.print("The Key length is not 24 bits");
                return null;
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"Algorithm/mode/complement mode"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encrypted);
        }catch (Exception e){
            log.error("encrypt failed", e);
            return null;
        }
    }

    // 解密
    public static String decrypt(String sSrc, String sKey) {
        try {
            if (sKey == null) {
                log.error("key is null");
                return null;
            }
            if (sKey.length() != 24) {
                System.out.print("The Key length is not 24 bits");
                return null;
            }
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);

            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, StandardCharsets.UTF_8);

        } catch (Exception ex) {
            log.error("decrypt failed", ex);
            return null;
        }
    }
}
