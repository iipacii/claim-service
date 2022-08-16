package com.cts.claim.model;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Class to get user input using post method and convert to Claim class

@Setter
@Getter
@ToString
public class ClaimInput {
	
	@NotBlank(message = "Policy Id is mandatory")
	private String policyId;
	@NotBlank(message = "Benefits missing")
	private String claimBenefit;
	@NotBlank(message = "Hospital Id is mandatory")
	private String hospitalId;
	@NotBlank(message = "Benefit Availed is missing")
	private Integer benefitAvailed;
	@NotBlank(message = "Amount Claimed is missing")
	private Integer amtClaimed;
	@NotBlank(message = "Member Id is mandatory")
	private String memberId;
	@NotBlank(message = "Benefit Id is mandatory")
	private String benefitId;

}
