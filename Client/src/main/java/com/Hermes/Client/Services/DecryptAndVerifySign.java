package com.Hermes.Client.Services;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Hermes.Client.Models.Message;
import com.Hermes.Client.Utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DecryptAndVerifySign {

	@Autowired
	CommunicationClient communicationClient;
	
	public String VerifySignAndDecryptMessage(Object payload) throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		Message rawMessage = mapper.readValue((String) payload, Message.class);
		
		if (!Utils.verifyECDSASig(Utils.getKeyFromString(rawMessage.getSenderPublicKey()), rawMessage.getSenderPublicKey() + rawMessage.getRecipientPublicKey() + rawMessage.getEncryptedMessage(), rawMessage.getSignature())) {
			return "Message Signature Invalid";
		}
		
		SecretKey decryptedAesSymmetricKey = communicationClient.decryptRSA(rawMessage.getEncryptedAesSymmetricKey());
		String message = communicationClient.decryptAES(decryptedAesSymmetricKey.getEncoded(), rawMessage.getEncryptedMessage());
		return message;
	}
	
}
