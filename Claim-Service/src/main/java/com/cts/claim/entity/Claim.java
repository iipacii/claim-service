package com.cts.claim.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.cts.claim.sequence.ClaimIdSequence;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "Claim")
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "claim_seq")
	@GenericGenerator(name = "claim_seq", strategy = "com.cts.claim.sequence.ClaimIdSequence", parameters = {
			@Parameter(name = ClaimIdSequence.INCREMENT_PARAM, value = "1"),
			@Parameter(name = ClaimIdSequence.VALUE_PREFIX_PARAMETER, value = "CMS_C"),
			@Parameter(name = ClaimIdSequence.NUMBER_FORMAT_PARAMETER, value = "%03d") })
	//@Column(columnDefinition = "varchar(255) default 'CMC_C000'")
	private String claimId;
	private String status;
	private String remarks;
	private Integer benefitAvailed;
	private Integer amtClaimed;
	private boolean isSettled;
	private String policyId;
	private String policyBenefits;
	private String hospitalId;

}
