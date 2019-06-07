package com.finance.revolution.revMoneyApp.resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.finance.revolution.revMoneyApp.model.Account;

class TestAccountResource extends TestResource {


	@DisplayName("testGetAllAccounts")
	@Test
	public void testGetAllAccounts() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		Account[] accounts = mapper.readValue(jsonString, Account[].class);
		assertTrue(accounts.length > 0);
	}

	@DisplayName("testGetAccount")
	@Test
	public void testGetAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/1234").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		Account account = mapper.readValue(jsonString, Account.class);
		assertTrue(account != null);
	}

	@DisplayName("testGetAccountBalance")
	@Test
	public void testGetAccountBalance() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/1234/balance").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);

		String balanceStr = EntityUtils.toString(response.getEntity());
		BigDecimal balance = new BigDecimal(balanceStr).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal modAmount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		assertTrue(balance.equals(modAmount));
	}

	@DisplayName("testDeposit")
	@Test
	public void testDeposit() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/9012/deposit/100").build();
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String balanceStr = EntityUtils.toString(response.getEntity());
		BigDecimal balance = new BigDecimal(balanceStr).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal modAmount = new BigDecimal(600).setScale(4, RoundingMode.HALF_EVEN);
		assertTrue(balance.equals(modAmount));
	}

	@DisplayName("testWithdrawal")
	@Test
	public void testWithdrawal() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/5678/withdraw/50").build();
		HttpPut request = new HttpPut(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String balanceStr = EntityUtils.toString(response.getEntity());
		BigDecimal balance = new BigDecimal(balanceStr).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal modAmount = new BigDecimal(150).setScale(4, RoundingMode.HALF_EVEN);
		assertTrue(balance.equals(modAmount));

	}
	
	@DisplayName("testInsufficientWithdrawal")
	@Test
	public void testInsufficientWithdrawal() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/5678/withdraw/5000").build();
		HttpPut request = new HttpPut(uri);
		HttpResponse response = client.execute(request);
		String responseBody = EntityUtils.toString(response.getEntity());
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(responseBody.contains("Insufficient funds"));
		assertTrue(statusCode == 400);

	}

	@DisplayName("testDelete")
	@Test
	public void testDelete() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/1234").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	@DisplayName("testCreateAccount")
	@Test
	public void testCreateAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/create").build();
		BigDecimal balance = new BigDecimal(199).setScale(4, RoundingMode.HALF_EVEN);
		Account acc = new Account("12345", balance, "INR");
		String jsonInString = mapper.writeValueAsString(acc);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		String jsonString = EntityUtils.toString(response.getEntity());
		//fail(statusCode+""+jsonString);
		assertTrue(statusCode == 201);	
	}
	
	@DisplayName("testCreateExistinAccount")
	@Test
	public void testCreateExistinAccount() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/create").build();
		BigDecimal balance = new BigDecimal(179).setScale(4, RoundingMode.HALF_EVEN);
		Account acc = new Account("9789", balance, "INR");
		String jsonInString = mapper.writeValueAsString(acc);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 201);	
	}
	
	@DisplayName("testCreateAccountForNoUser")
	@Test
	public void testCreateAccountForNoUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/accounts/create").build();
		BigDecimal balance = new BigDecimal(179).setScale(4, RoundingMode.HALF_EVEN);
		Account acc = new Account("9789009", balance, "INR");
		String jsonInString = mapper.writeValueAsString(acc);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 406);	
	}

    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/567890").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
    }


    @Test
    public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/56789000").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 400);
    }
    
}
