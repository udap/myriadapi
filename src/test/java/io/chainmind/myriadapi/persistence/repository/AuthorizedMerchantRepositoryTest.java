package io.chainmind.myriadapi.persistence.repository;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.chainmind.myriadapi.domain.entity.Organization;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorizedMerchantRepositoryTest {
	@Autowired
	private OrganizationRepository orgRepo;
	
    @Test
    public void findTopByUpCode(){
    	Organization merchant = orgRepo.findByUpCode("777290058135881");
    	assertTrue(merchant != null);
    	log.debug("Current merchant is {}", merchant.getStatus());
    }

}
