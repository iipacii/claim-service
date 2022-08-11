package com.cts.claim.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Policy model to save policy details from Policy Microservice

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Policy {
	private String ploicyId;
	private String policyName;
	private int tenure;
	private long premium;
	private long lateCharges;
	private String policyBenefits;
}
