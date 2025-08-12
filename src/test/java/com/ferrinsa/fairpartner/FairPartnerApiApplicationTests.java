package com.ferrinsa.fairpartner;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=update",
		"spring.jpa.database-platform=org.hibernate.dialect.H2Dialect"
})
class FairPartnerApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
