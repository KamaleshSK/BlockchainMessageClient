package com.Hermes.Client.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
	
	@JsonProperty
	private String encryptedAesSymmetricKey;
	
	@JsonProperty
	private String encryptedMessage;
	
	@JsonProperty
	private byte[] signature;
	
	@JsonProperty
	private String senderPublicKey;
	
	@JsonProperty
	private String recipientPublicKey;
	
	public Message() {
		
	}

	public Message(String encryptedRandomSymmetricKey, String encryptedMessage, byte[] signature, String senderPublicKey, String recipientPublicKey) {
		this.encryptedAesSymmetricKey = encryptedRandomSymmetricKey;
		this.encryptedMessage = encryptedMessage;
		this.signature = signature;
		this.senderPublicKey = senderPublicKey;
		this.recipientPublicKey = recipientPublicKey;
	}
	
	public String getSenderPublicKey() {
		return senderPublicKey;
	}

	public void setSenderPublicKey(String senderPublicKey) {
		this.senderPublicKey = senderPublicKey;
	}

	public String getEncryptedAesSymmetricKey() {
		return encryptedAesSymmetricKey;
	}

	public void setEncryptedAesSymmetricKey(String encryptedAesSymmetricKey) {
		this.encryptedAesSymmetricKey = encryptedAesSymmetricKey;
	}

	public String getEncryptedMessage() {
		return encryptedMessage;
	}

	public void setEncryptedMessage(String encryptedMessage) {
		this.encryptedMessage = encryptedMessage;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public String getRecipientPublicKey() {
		return recipientPublicKey;
	}

	public void setRecipientPublicKey(String recipientPublicKey) {
		this.recipientPublicKey = recipientPublicKey;
	}

	
}
