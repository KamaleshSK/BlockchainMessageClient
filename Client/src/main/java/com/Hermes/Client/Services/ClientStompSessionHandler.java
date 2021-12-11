package com.Hermes.Client.Services;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ClientStompSessionHandler extends StompSessionHandlerAdapter {
	
	@Autowired
	DecryptAndVerifySign decryptAndVerifySign;

	private static final Logger logger = LoggerFactory.getLogger(ClientStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
        logger.info("Client connected: headers {}", headers);

        session.subscribe("/app/subscribe", this);
        session.subscribe("/queue/responses", this);
        session.subscribe("/queue/errors", this);
        session.subscribe("/topic/periodic", this);
        
        String message = "one-time message from client";
        logger.info("Client sends: {}", message);
        session.send("/app/request", message);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        logger.info("Client received: payload {}, headers {}", payload, headers);
        
        try {
			logger.info(decryptAndVerifySign.VerifySignAndDecryptMessage(payload));
			
			OkHttpClient client = new OkHttpClient().newBuilder()
					  .build();
			MediaType mediaType = MediaType.parse("application/json");
			RequestBody body = RequestBody.create(mediaType, (String) payload);
			Request request = new Request.Builder()
			  .url("http://localhost:9190/queue/push")
			  .method("POST", body)
			  .build();
			Response response = client.newCall(request).execute();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void handleException(StompSession session, StompCommand command,
                                StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Client error: exception {}, command {}, payload {}, headers {}",
                exception.getMessage(), command, payload, headers);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        logger.error("Client transport error: error {}", exception.getMessage());
    }
	
}
