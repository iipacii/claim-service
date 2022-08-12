package com.cts.claim.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Model to get Hospital data from Policy microservice
@Setter
@Getter
@ToString
public class PolicyProvider {

	@JsonProperty("id")
	 private String hospitalId;
	@JsonProperty("hospital_name")
	 private String hospitalName;
	@JsonProperty("location") 
	private String address;
	@JsonProperty("phno") 
	private String phoneNo;
	@JsonProperty("is_health_care_provider") 
	private boolean isHealthCareProvider;

}
