package com.cts.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cts.claim.entity.Claim;
import com.cts.claim.model.ClaimInput;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer>{

	void save(Integer claimId);

	Object save(ClaimInput claim1);
	Claim findByClaimId(String claimId);

}
