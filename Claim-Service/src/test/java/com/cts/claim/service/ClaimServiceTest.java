package com.cts.claim.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import com.cts.claim.model.PolicyProvider;
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
		assertEquals(service.getClaimStatus("CMS_C001","CorrectToken"),(ClaimStatusOutput.builder().claimStatus(claim1.getStatus()).remarks(claim1.getRemarks()).build()));
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
	
	//Test for InValid Token for getClaimStatus
	@Test
	void testGetClaimStatusWithInvalidToken() throws TokenExpireException{
		Claim claim1= new Claim("CMS_C001", "Pending Action", "NIL", 2700000, 333900, true, "CMS_P002", "BP, Diabeties, Cancer", "CMS_H001","CMS_M001");
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		when(claimrepo.save(claim1)).thenReturn(claim1);
		when(claimrepo.findByClaimId("CMS_C001")).thenReturn((claim1));
		assertThrows(TokenExpireException.class, () -> {
			service.getClaimStatus("CMS_C001","IncorrectToken"); 
		});
	}
	
	//Test for InValid Token for submitClaim
		@Test
		void testSubmitClaimWithInvalidToken() throws TokenExpireException, PolicyNotFoundException{
			ClaimInput claim1= new ClaimInput();
			claim1.setAmtClaimed(8000);
			claim1.setBenefitAvailed(2700000);
			claim1.setHospitalId("CMS_H001");
			claim1.setClaimBenefit("Dialysis");
			claim1.setPolicyId("CMS_P002");
			when(claimrepo.save(claim1)).thenReturn(claim1);
			when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
			String policyBenefit = "Dialysis, Diabeties, Cancer";
			when(policyclient.getPolicyBenefits(claim1.getPolicyId(),"CorrectToken")).thenReturn(policyBenefit);
			Claim claim= service.submitClaim(claim1,"CorrectToken");
			when(claimrepo.save(claim)).thenReturn(claim);
			when(service.submitClaim(claim1,"CorrectToken")).thenReturn(claim);
			assertThrows(TokenExpireException.class, () -> {
				service.submitClaim(claim1,"IncorrectToken"); 
			});
		}

	//Test for correct claim submission
	@Test
	void testSubmitClaim() throws PolicyNotFoundException, TokenExpireException {
		ClaimInput claim1= new ClaimInput();
		claim1.setAmtClaimed(8000);
		claim1.setBenefitAvailed(2700000);
		claim1.setHospitalId("CMS_H001");
		claim1.setClaimBenefit("Dialysis");
		claim1.setPolicyId("CMS_P002");
		when(claimrepo.save(claim1)).thenReturn(claim1);
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		String policyBenefit = "Dialysis, Diabeties, Cancer";
		when(policyclient.getPolicyBenefits(claim1.getPolicyId(),"CorrectToken")).thenReturn(policyBenefit);
		Claim claim= service.submitClaim(claim1,"CorrectToken");
		when(claimrepo.save(claim)).thenReturn(claim);
		when(service.submitClaim(claim1,"CorrectToken")).thenReturn(claim);
		assertThat(service.submitClaim(claim1,"CorrectToken")).isEqualTo(claim);
		}
	
	//Test claimAmount<=Benefit Availed
	@Test
	void testClaimAmountCorrect() throws PolicyNotFoundException, TokenExpireException {
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		ClaimInput claim1= new ClaimInput();
		claim1.setAmtClaimed(8000);
		claim1.setBenefitAvailed(2700000);
		claim1.setHospitalId("CMS_H001");
		claim1.setClaimBenefit("Dialysis");
		claim1.setPolicyId("CMS_P002");
		PolicyProvider provider=PolicyProvider.builder().hospitalId("CMS_H001").address("address").hospitalName("H").isHealthCareProvider(true).build();
		List<PolicyProvider> list = new ArrayList<>();
		list.add(provider);
		when(policyclient.getPolicyBenefits(claim1.getPolicyId(), "CorrectToken")).thenReturn("Dialysis,Cancer");
		when(policyclient.getEligibleClaimAmount(claim1.getBenefitId(), "CorrectToken")).thenReturn(1000);
		when(policyclient.getAllPolicyProviders(claim1.getPolicyId(), "CorrectToken")).thenReturn(list);
		Claim servicedClaim=service.configureClaim(claim1, "CorrectToken");
		assertEquals( "Pending Action",servicedClaim.getStatus());
		assertEquals("Please contact the branch office.",servicedClaim.getRemarks() );
		
	}
	
	//Test claimAmount>Benefit Availed
		@Test
		void testClaimAmountInCorrect() throws PolicyNotFoundException, TokenExpireException {
			when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
			ClaimInput claim1= new ClaimInput();
			claim1.setAmtClaimed(8000);
			claim1.setBenefitAvailed(2700);
			claim1.setHospitalId("CMS_H001");
			claim1.setClaimBenefit("Dialysis");
			claim1.setPolicyId("CMS_P002");
			PolicyProvider provider=PolicyProvider.builder().hospitalId("CMS_H001").address("address").hospitalName("H").isHealthCareProvider(true).build();
			List<PolicyProvider> list = new ArrayList<>();
			list.add(provider);
			when(policyclient.getPolicyBenefits(claim1.getPolicyId(), "CorrectToken")).thenReturn("Dialysis,Cancer");
			when(policyclient.getEligibleClaimAmount(claim1.getBenefitId(), "CorrectToken")).thenReturn(10000);
			when(policyclient.getAllPolicyProviders(claim1.getPolicyId(), "CorrectToken")).thenReturn(list);
			Claim servicedClaim=service.configureClaim(claim1, "CorrectToken");
			assertEquals( "Claim Rejected",servicedClaim.getStatus());
			assertEquals("Please check your eligibility criteria" ,servicedClaim.getRemarks());
			
		}
		
		//Test Hospital NOT a health care provider
				@Test
				void testNotaHealthCareProvider() throws PolicyNotFoundException, TokenExpireException {
					when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
					ClaimInput claim1= new ClaimInput();
					claim1.setAmtClaimed(8000);
					claim1.setBenefitAvailed(270000);
					claim1.setHospitalId("CMS_H001");
					claim1.setClaimBenefit("Dialysis");
					claim1.setPolicyId("CMS_P002");
					PolicyProvider provider=PolicyProvider.builder().hospitalId("CMS_H001").address("address").hospitalName("H").isHealthCareProvider(false).build();
					List<PolicyProvider> list = new ArrayList<>();
					list.add(provider);
					when(policyclient.getPolicyBenefits(claim1.getPolicyId(), "CorrectToken")).thenReturn("Dialysis,Cancer");
					when(policyclient.getEligibleClaimAmount(claim1.getBenefitId(), "CorrectToken")).thenReturn(1000);
					when(policyclient.getAllPolicyProviders(claim1.getPolicyId(), "CorrectToken")).thenReturn(list);
					Claim servicedClaim=service.configureClaim(claim1, "CorrectToken");
					assertEquals( "Claim Rejected",servicedClaim.getStatus());
					assertEquals("Please check your eligibility criteria" ,servicedClaim.getRemarks());
					
				}
				
				//Test Policy Benefit Incorrect
				@Test
				void testIncorrectPolicyBenefit() throws PolicyNotFoundException, TokenExpireException {
					when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
					ClaimInput claim1= new ClaimInput();
					claim1.setAmtClaimed(8000);
					claim1.setBenefitAvailed(270000);
					claim1.setHospitalId("CMS_H001");
					claim1.setClaimBenefit("Dialysis");
					claim1.setPolicyId("CMS_P002");
					PolicyProvider provider=PolicyProvider.builder().hospitalId("CMS_H001").address("address").hospitalName("H").isHealthCareProvider(true).build();
					List<PolicyProvider> list = new ArrayList<>();
					list.add(provider);
					when(policyclient.getPolicyBenefits(claim1.getPolicyId(), "CorrectToken")).thenReturn("Cancer");
					when(policyclient.getEligibleClaimAmount(claim1.getBenefitId(), "CorrectToken")).thenReturn(1000);
					when(policyclient.getAllPolicyProviders(claim1.getPolicyId(), "CorrectToken")).thenReturn(list);
					Claim servicedClaim=service.configureClaim(claim1, "CorrectToken");
					assertEquals( "Under Dispute",servicedClaim.getStatus());
					assertEquals("Your policy benefits do not match." ,servicedClaim.getRemarks());
					
				}
}

