package com.cts.claim;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.cts.claim.entity.Claim;
import com.cts.claim.repository.ClaimRepository;

@SpringBootApplication
@EnableFeignClients
@CrossOrigin
@EnableWebMvc
public class ClaimMicroserviceApplication {
	
	@Autowired
	private ResourceLoader resourceLoader;

	public static void main(String[] args) {
		SpringApplication.run(ClaimMicroserviceApplication.class, args);
	}

	//CommandLine Runner for initializing data into the Claim Table from cms_claims.csv file in resources folder
		@Bean
		CommandLineRunner run(ClaimRepository claimRepository) {
			return args ->{
				try (BufferedReader br = new BufferedReader(
						new InputStreamReader(resourceLoader.getResource("classpath:/cms_claims.csv").getInputStream()))) {
					String line;
					//breaking into each line
					while ((line = br.readLine()) != null) {
						//breaking each line to multiple data
						String[] values = line.split(",");
						//using a builder to quickly create and save Claim using repository
						claimRepository.save(Claim.builder()
								.status(values[1])
								.remarks((values[2]))
								.benefitAvailed(Integer.parseInt(values[3]))
								.amtClaimed(Integer.parseInt(values[4]))
								.isSettled(Boolean.valueOf(values[5]))
								.policyId(values[6])
								.policyBenefits(values[7])
								.hospitalId(values[8])
								.memberId(values[8])
								.build());
					}
				}
			
			};
		}
}
