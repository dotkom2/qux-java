package com.qux;

import com.qux.model.User;
import com.qux.auth.QUXTokenService;
import org.junit.Assert;
import org.junit.Test;

import io.vertx.core.json.JsonObject;

public class TokenServiceTest {
	
	@Test
	public void test(){
		System.out.println("TokenServiceTest.test() > enter");
		QUXTokenService ts = new QUXTokenService();

		ts.setSecret("123");
		
		JsonObject json = new JsonObject().put("_id", "id1").put("email", "klaus@quant-ux.de");
		String token = ts.getToken(json);
		
		Assert.assertNotNull(token);

		
		User user = ts.getUser(token);
		Assert.assertNotNull(user);
		Assert.assertEquals("id1", user.getId());
		Assert.assertEquals("klaus@quant-ux.de", user.getEmail());
		Assert.assertEquals(User.GUEST, user.getRole());
		Assert.assertNull(user.getName());
	}
	
	@Test
	public void test_user(){
		System.out.println("TokenServiceTest.test_user() > enter");

		QUXTokenService ts = new QUXTokenService();
		ts.setSecret("123");
		
		JsonObject json = new JsonObject()
				.put("_id", "id1")
				.put("email", "klaus@quant-ux.de")
				.put("role", User.CLIENT);
		String token = ts.getToken(json);
		
		Assert.assertNotNull(token);

		User user = ts.getUser(token);
		Assert.assertNotNull(user);
		Assert.assertEquals("id1", user.getId());
		Assert.assertEquals("klaus@quant-ux.de", user.getEmail());
		Assert.assertEquals(User.CLIENT, user.getRole());
	}
	
	
	@Test
	public void test_wrong_secret(){
		System.out.println("TokenServiceTest.test_wrong_secret() > enter");

		QUXTokenService ts = new QUXTokenService();
		ts.setSecret("123");
		
		JsonObject json = new JsonObject()
				.put("_id", "id1")
				.put("email", "klaus@quant-ux.de")
				.put("role", User.CLIENT);
		String token = ts.getToken(json);
		
		Assert.assertNotNull(token);
		
		ts.setSecret("abc");
		
		User user = ts.getUser(token);
		
		Assert.assertNull(user);
	
	}
	
	@Test
	public void test_expired(){
		System.out.println("TokenServiceTest.test_wrong_secret() > enter");

		QUXTokenService ts = new QUXTokenService();
		ts.setSecret("123");
		
		JsonObject json = new JsonObject()
				.put("_id", "id1")
				.put("email", "klaus@quant-ux.de")
				.put("role", User.CLIENT);
		String token = ts.getToken(json, -2);
		
		Assert.assertNotNull(token);
		
		User user = ts.getUser(token);
		
		Assert.assertNull(user);
	}

	@Test
	public void test_getExpiredAt(){
		System.out.println("TokenServiceTest.test_getExpiredAt() > enter");

		QUXTokenService ts = new QUXTokenService();
		ts.setSecret("123");

		JsonObject json = new JsonObject()
				.put("_id", "id1")
				.put("email", "klaus@quant-ux.de")
				.put("role", User.CLIENT);
		String token = ts.getToken(json);

		Assert.assertNotNull(token);

		// ste other secret
		ts.setSecret("");

		String expiresAt = ts.getExpiresAt(token);
		Assert.assertNotNull(expiresAt);

		expiresAt = ts.getExpiresAt("");
		Assert.assertEquals("-", expiresAt);

		expiresAt = ts.getExpiresAt("No token");
		Assert.assertEquals("-", expiresAt);

		System.out.println("TokenServiceTest.test_getExpiredAt() > exit "+ expiresAt);

	}

}
