package com.cts.claim.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.model.Policy;
import com.cts.claim.model.PolicyProvider;



@FeignClient(url="${policy.url}",name="policyapp")
public interface PolicyServiceClient {
	@GetMapping(value="/getEligibleClaimAmount/{policyId}")
	public Integer getEligibleClaimAmount(@PathVariable("policyId") String policyId, @RequestHeader("Authorization") String token) throws PolicyNotFoundException;
	
	@GetMapping(value="/getChainOfProviders/{policyId}")
	public List<PolicyProvider> getAllPolicyProviders(@PathVariable("policyId")String policyId, @RequestHeader("Authorization") String token);

	@GetMapping(value="/getEligibleBenefits/{policyId}")
	String getPolicyBenefits(@PathVariable("policyId")String policyId, @RequestHeader("Authorization") String token);
}
