package com.cts.claim.exception;

//Exception when the token is not valid or expired from auth microservice
public class TokenExpireException extends Exception {

	public TokenExpireException(String msg) {
		super(msg);
	}

}
