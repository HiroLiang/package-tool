package com.example.demo.util;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EncryptUtil {
	
	private static String keyString;
	
	@Value("${data.encrypt.key}")
	public void setKeyString(String keyString) {
		EncryptUtil.keyString = keyString;
	}
	
	public static String encrypt(String data, String keyBase64) {
		String result = null;
		try {
			byte[] key = Base64.getDecoder().decode(keyBase64);
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, keySpec);
			byte[] encrypted = cipher.doFinal(data.getBytes());
			
			result = Base64.getEncoder().encodeToString(encrypted); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String decrypt(String data, String keyBase64) {
		String result = null;
		try {
			byte[] key = Base64.getDecoder().decode(keyBase64);
			SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
			
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(data));
			
			result = new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getNewKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		return Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded());
	}
	
	public static String getSystemKey() {
		SecretKeySpec keySpec = new SecretKeySpec(keyString.getBytes(), "AES");
		return Base64.getEncoder().encodeToString(keySpec.getEncoded());
	}
	
	
	public static void main(String[] args) {
		try {
			String key = getSystemKey();
			System.out.println("1_" + key);
			
			String str = "1";
			
			String encrypt = encrypt(str, key);
			System.out.println("2_" + encrypt);
			
			String decrypt = decrypt(encrypt, key);
			System.out.println("3_" + decrypt);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
