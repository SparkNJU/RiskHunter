package com.RiskHunter;

import com.RiskHunter.util.TokenUtil;
import com.RiskHunter.po.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RiskHunterApplicationTests {

	@Autowired
    TokenUtil tokenUtil;

	@Test
	void contextLoads() {
		User user=new User();
		user.setId(1);
		user.setPassword("123456");
		System.out.println(tokenUtil.getToken(user));
	}

}
