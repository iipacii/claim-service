package com.cts.claim.model;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Model to get Hospital data from Policy microservice
@Setter
@Getter
@ToString
public class PolicyProvider {

	 private String hospitalId;
	 private String hospitalName;
	 private String address;
	 private String phoneNo;
	 private boolean isHealthCareProvider;

}
