package com.cts.claim.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Class to get user input using post method and convert to Claim class

@Setter
@Getter
@ToString
public class ClaimInput {
	
	public String policyId;
	public String claimBenefit;
	public String hospitalId;
	public Integer benefitAvailed;
	public Integer amtClaimed;
	public String memberId;
	private String benefitId;

}
