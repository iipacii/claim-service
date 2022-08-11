package com.cts.claim.model;
import lombok.Getter;
import lombok.Setter;

//Class to get user input using post method and convert to Claim class

@Setter
@Getter
public class ClaimInput {
	
	public String policyId;
	public String claimBenefit;
	public String hospitalId;
	public Integer benefitAvailed;
	public Integer amtClaimed;
	public String memberId;

}
