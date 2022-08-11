package com.cts.claim.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.claim.client.AuthClient;
import com.cts.claim.model.ClaimInput;
import com.cts.claim.service.ClaimService;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc	
class ClaimControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	ClaimService service;
	@Mock
	AuthClient authClient;
	
	@Test
	void notNull() {
		assertThat(service).isNotNull();
	}
	@Test
	void notNull1() {
		assertThat(mockMvc).isNotNull();
	}


	

	@Test
	void testGetClaimStatus() throws Exception {
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		this.mockMvc.perform(get("/getClaimStatus/{claimId}", 10).header("Authorization", "CorrectToken")).andExpect(status().isOk());

	}

	@Test
	void testSubmitClaim() throws Exception {
		ClaimInput claim1= new ClaimInput();
		claim1.amtClaimed= 8000;
		claim1.benefitAvailed= 2700000;
		claim1.hospitalId="CMS_H001";
		claim1.claimBenefit="Dialysis";
		claim1.policyId="CMS_P002";
		when(authClient.authorizeTheRequest("CorrectToken")).thenReturn(true);
		String jsonString = mapper.writeValueAsString(claim1);
		System.out.println(jsonString);
		this.mockMvc.perform(post("/submitClaim").contentType(MediaType.APPLICATION_JSON).header("Authorization", "CorrectToken")
				.content(jsonString)).andExpect(status().isOk());
	}

}