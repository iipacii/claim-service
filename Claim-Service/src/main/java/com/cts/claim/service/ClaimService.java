package com.cts.claim.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cts.claim.client.AuthClient;
import com.cts.claim.client.PolicyServiceClient;
import com.cts.claim.entity.Claim;
import com.cts.claim.exception.ClaimNotFoundException;
import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.exception.TokenExpireException;
import com.cts.claim.model.ClaimInput;
import com.cts.claim.model.ClaimStatusOutput;
import com.cts.claim.model.PolicyProvider;
import com.cts.claim.repository.ClaimRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClaimService {
	@Autowired
	ClaimRepository claimrepo;
	@Autowired
	AuthClient authClient;
	@Autowired
	PolicyServiceClient policyclient;
	
	@Value("${Benefit.tenure}")
	private int beneditTenure; 
	
	//Gets Claim Status from the Claim Repo
	public ClaimStatusOutput getClaimStatus(String claimId, String token) throws ClaimNotFoundException, TokenExpireException {
		if (authClient.authorizeTheRequest(token)) {
			log.info("Token Authorized Inside ClaimStatusOutput");
			Claim claim = claimrepo.findByClaimId(claimId);
			if (claim == null)
				throw new ClaimNotFoundException("Claim not found");
			else
				return ClaimStatusOutput.builder().claimStatus(claim.getStatus()).remarks(claim.getRemarks()).build();
		} else {
			throw new TokenExpireException("Token is expired");
		}
	}

	//Submit new Claim; Check conditions and assign a status to claim before submitting
	public Claim submitClaim(ClaimInput claimInput, String token) throws PolicyNotFoundException, TokenExpireException {
		if (authClient.authorizeTheRequest(token)) {
			log.info("Token Authorized Inside submitClaim");
			return claimrepo.save(configureClaim(claimInput,token));
		} else {
			throw new TokenExpireException("Token is expired");
		}
	}
	
	public Claim configureClaim(ClaimInput claimInput,String token) throws PolicyNotFoundException {
		log.info("Inside configureClaim method");
		//getting claim amount from policy
		int claimAmount = policyclient.getEligibleClaimAmount(claimInput.getBenefitId(),token);
		List<PolicyProvider> list = policyclient.getAllPolicyProviders(claimInput.getPolicyId(),token);
		log.info("List of Hospitals: "+list.toString());
		boolean hospitalFlag = false;
		boolean policyBenefitFlag=false;
		//checking if hospital is a permissible health care provider
		for (PolicyProvider p : list) {
			if (p.getHospitalId().equalsIgnoreCase(claimInput.getHospitalId()) && p.isHealthCareProvider()) {
				hospitalFlag = true;
				break;}
		}
		String policyBenefits= policyclient.getPolicyBenefits(claimInput.getPolicyId(),token);
		log.info("Policy Benefits: "+policyBenefits);
		//checking if the policy covers the benefits required
		if(policyBenefits.contains(claimInput.getClaimBenefit()))
			policyBenefitFlag=true;
		Claim claim = Claim.builder().amtClaimed(claimInput.getAmtClaimed()).benefitAvailed(claimInput.getBenefitAvailed()).hospitalId(claimInput.getHospitalId()).policyBenefits(claimInput.getClaimBenefit()).policyId(claimInput.getPolicyId()).memberId(claimInput.getMemberId()).build();
		if(!policyBenefitFlag) {
			claim.setStatus("Under Dispute");
			claim.setRemarks("Your policy benefits do not match.");
		}
		else if (hospitalFlag && claimAmount <= claimInput.getBenefitAvailed()) {
			claim.setStatus("Pending Action");
			claim.setRemarks("Please contact the branch office.");
		} else {
			claim.setStatus("Claim Rejected");
			claim.setRemarks("Please check your eligibility criteria");
		}
		log.info("configureClaim END");
		return claim;
	}

}
