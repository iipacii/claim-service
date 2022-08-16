package com.cts.claim.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.model.PolicyProvider;



@FeignClient(url="${policy.url}",name="policyapp")
public interface PolicyServiceClient {
	//Method to get Claim Amount
	@GetMapping(value="/getEligibleClaimAmount/{policyId}")
	public Integer getEligibleClaimAmount(@PathVariable("policyId") String policyId, @RequestHeader("Authorization") String token) throws PolicyNotFoundException;
	
	//Method to get List of Providers
	@GetMapping(value="/getChainOfProviders/{policyId}")
	public List<PolicyProvider> getAllPolicyProviders(@PathVariable("policyId")String policyId, @RequestHeader("Authorization") String token);

	//Method to get Benefits provided by a Policy
	@GetMapping(value="/getEligibleBenefits/{policyId}")
	String getPolicyBenefits(@PathVariable("policyId")String policyId, @RequestHeader("Authorization") String token);
}
