package com.cts.claim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cts.claim.client.AuthClient;
import com.cts.claim.client.PolicyServiceClient;
import com.cts.claim.entity.Claim;
import com.cts.claim.exception.ClaimNotFoundException;
import com.cts.claim.exception.PolicyNotFoundException;
import com.cts.claim.exception.TokenExpireException;
import com.cts.claim.model.ClaimInput;
import com.cts.claim.model.ClaimStatusOutput;
import com.cts.claim.model.Policy;
import com.cts.claim.repository.ClaimRepository;



class ClaimServiceTest {
	@Mock
	AuthClient authClient;
	@Mock
	PolicyServiceClient policyclient;
	@Mock
	ClaimRepository claimrepo;
	@InjectMocks
	ClaimService service;
	@SuppressWarnings("deprecation")
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	//Test for correct Claim 
	void testGetClaimStatus() throws ClaimNotFoundException, TokenExpireException {
		Claim claim1= new Claim("CMS_C001", "Pending Action", "NIL", 2700000, 333900, true, "CMS_P002", "Dengue", "CMS_H001","CMS_M001");
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		when(claimrepo.save(claim1)).thenReturn(claim1);
		when(claimrepo.findByClaimId("CMS_C001")).thenReturn((claim1));
		assertTrue(service.getClaimStatus("CMS_C001","CorrectToken").equals(ClaimStatusOutput.builder().claimStatus(claim1.getStatus()).remarks(claim1.getRemarks()).build()));
	}
	
	//Test for Incorrect Claim
	@Test
	void testGetClaimStatusWithInvalidId() throws ClaimNotFoundException {
		Claim claim1= new Claim("CMS_C001", "Pending Action", "NIL", 2700000, 333900, true, "CMS_P002", "BP, Diabeties, Cancer", "CMS_H001","CMS_M001");
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		when(claimrepo.save(claim1)).thenReturn(claim1);
		when(claimrepo.findByClaimId("CMS_C001")).thenReturn((claim1));
		assertThrows(ClaimNotFoundException.class, () -> {
			service.getClaimStatus("CMS_C002","CorrectToken"); 
		});
	}

	//Test for correct claim submission
	@Test
	void testSubmitClaim() throws PolicyNotFoundException, TokenExpireException {
		ClaimInput claim1= new ClaimInput();
		claim1.amtClaimed= 8000;
		claim1.benefitAvailed= 2700000;
		claim1.hospitalId="CMS_H001";
		claim1.claimBenefit="Dialysis";
		claim1.policyId="CMS_P002";
		when(claimrepo.save(claim1)).thenReturn(claim1);
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		Policy policy = new Policy("CMS_P002","ReAssure",12,9950,201,"Dialysis, Diabeties, Cancer");
		when(policyclient.getPolicy(claim1.getPolicyId())).thenReturn(policy);
		Claim claim= service.submitClaim(claim1,"CorrectToken");
		when(claimrepo.save(claim)).thenReturn(claim);
		when(service.submitClaim(claim1,"CorrectToken")).thenReturn(claim);
		assertThat(service.submitClaim(claim1,"CorrectToken")).isEqualTo(claim);
		}
}

