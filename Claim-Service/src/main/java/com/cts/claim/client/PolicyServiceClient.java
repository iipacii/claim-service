package com.cts.claim.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.model.Policy;
import com.cts.claim.model.PolicyProvider;



@FeignClient(url="${policy.url}",name="policyapp")
public interface PolicyServiceClient {
	@GetMapping(value="/getEligibleClaimamount/{policyId}")
	public Integer getEligibleClaimAmount(@PathVariable("policyId") String policyId) throws PolicyNotFoundException;
	
	@GetMapping(value="/getAllProviders/{policyId}")
	public List<PolicyProvider> getAllPolicyProviders(@PathVariable("policyId")String policyId);

	@GetMapping(value="/getPolicy/{policyId}")
	Policy getPolicy(@PathVariable("policyId")String policyId);
}
