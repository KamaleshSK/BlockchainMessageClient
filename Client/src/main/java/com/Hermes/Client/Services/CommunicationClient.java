package com.Hermes.Client.Services;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

@Service
public class CommunicationClient {

	private static PrivateKey privateKey;
	private static PublicKey publicKey;
	private static PublicKey encPublicKey;
	private static PrivateKey encPrivateKey;
	
	static {
	    Security.addProvider(new BouncyCastleProvider());
	}
	
	CommunicationClient() {
		generateKeyPair();
		generateEncKeyPair();
	}
	
	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}
	
	public PrivateKey getEncPrivateKey() {
		return encPrivateKey;
	}

	public PublicKey getEncPublicKey() {
		return encPublicKey;
	}
	
	public void generateEncKeyPair() {
		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(2048);
			KeyPair kp = keyGen.generateKeyPair();
			encPublicKey = kp.getPublic();
			encPrivateKey = kp.getPrivate();
		} catch (NoSuchAlgorithmException e) {	
			e.printStackTrace();
		}
		
	}
	
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			keyGen.initialize(ecSpec, random); //256 
	        KeyPair keyPair = keyGen.generateKeyPair();
	        // Set the public and private keys from the keyPair
	        privateKey = keyPair.getPrivate();
	        publicKey = keyPair.getPublic();
	        
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public SecretKey decryptRSA(String encryptedAesSymmetricKey) throws Exception {
        byte[] encryptedBytes = decode(encryptedAesSymmetricKey);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, encPrivateKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        SecretKey originalKey = new SecretKeySpec(decryptedMessage, 0, decryptedMessage.length, "AES"); 
        return originalKey;
    }
	
	public String decryptAES(byte[] aesPublicKey, String message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKey originalKey = new SecretKeySpec(aesPublicKey, 0, aesPublicKey.length, "AES");
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(decode(message));
        return new String(bytePlainText);
	}
	
	private static byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
	
}
