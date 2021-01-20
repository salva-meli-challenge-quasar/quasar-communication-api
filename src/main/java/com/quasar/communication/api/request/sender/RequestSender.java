package com.quasar.communication.api.request.sender;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.quasar.communication.api.handler.QuasarResponseErrorHandler;

@Service
public class RequestSender {

	public String send(String url, MediaType contentType, String body) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(contentType);
		HttpEntity<String> request = new HttpEntity<String>(body, httpHeaders);
		return new RestTemplateBuilder().errorHandler(new QuasarResponseErrorHandler()).build().postForObject(url,
				request, String.class);
	}
}
