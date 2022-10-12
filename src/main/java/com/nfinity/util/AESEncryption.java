package com.nfinity.util;

import lombok.extern.slf4j.Slf4j;
import org.cryptonode.jncryptor.AES256JNCryptor;
import org.cryptonode.jncryptor.CryptorException;
import org.cryptonode.jncryptor.JNCryptor;
import org.springframework.util.Base64Utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class AESEncryption {

    public static String generateKey(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return Base64Utils.encodeToString(b);
        }
        catch (NoSuchAlgorithmException e) {
            log.error("generate key failed.", e);
            return null;
        }
    }

    public static String encrypt(String src, String key) throws CryptorException {
        JNCryptor cryptor = new AES256JNCryptor();
        byte[] cipher = cryptor.encryptData(src.getBytes(StandardCharsets.UTF_8), key.toCharArray());
        return Base64Utils.encodeToString(cipher);
    }

    public static String decrypt(String src, String key) throws CryptorException {
        JNCryptor cryptor = new AES256JNCryptor();
        byte[] decode =  Base64Utils.decodeFromString(src);
        byte[] cipher = cryptor.decryptData(decode, key.toCharArray());
        return new String(cipher, StandardCharsets.UTF_8);
    }
}
