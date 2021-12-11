package com.Hermes.Client.Controllers;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Hermes.Client.Services.CommunicationClient;
import com.Hermes.Client.Utils.JavaObjectToJson;
import com.Hermes.Client.Utils.Utils;

@RestController
public class PublicKeyController {
	
	@Autowired
	CommunicationClient communicationClient;

	@GetMapping("/get-public-key")
	public String getPublicKey() {
		PublicKey publicKey = communicationClient.getEncPublicKey();
		return Utils.getStringFromKey(publicKey);
	}
	
}
