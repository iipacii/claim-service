package com.cts.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.cts.claim.entity.Claim;
import com.cts.claim.exception.ClaimNotFoundException;
import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.exception.TokenExpireException;
import com.cts.claim.model.ClaimInput;
import com.cts.claim.service.ClaimService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ClaimController {
	@Autowired
	ClaimService service;
	@GetMapping(value="/getClaimStatus/{claimId}")
	public Claim getClaimStatus(@PathVariable("claimId") String claimId, @RequestHeader("Authorization") String token) throws ClaimNotFoundException, TokenExpireException, MissingRequestHeaderException {
		return service.getClaimStatus(claimId,token);
	}
	
	@PostMapping(value="/submitClaim")
	public Claim submitClaim(@RequestBody ClaimInput claim, @RequestHeader("Authorization") String token) throws PolicyNotFoundException, TokenExpireException {
		return service.submitClaim(claim,token);
	}

}
