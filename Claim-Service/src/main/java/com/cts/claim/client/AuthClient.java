package com.cts.claim.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;


//Method to validate token using Auth Service
@FeignClient(url="${auth.url}",name="authapp")
public interface AuthClient{
	@PostMapping(value = "/authorize")
	public boolean authorizeTheRequest(
			@RequestHeader(value = "Authorization", required = true) String requestTokenHeader); 
}