package com.finance.revolution.revMoneyApp.resource;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import com.finance.revolution.revMoneyApp.model.Transaction;

class TestTransactionResource extends TestResource {
	
	
    @Test
    public void testTransactionEnoughFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(19).setScale(4, RoundingMode.HALF_EVEN);
        Transaction transaction = new Transaction("USD", amount, "1111", "2222");

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        
    }

    @Test
    public void testTransactionNotEnoughFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(1900).setScale(4, RoundingMode.HALF_EVEN);
        Transaction transaction = new Transaction("USD", amount, "1111", "2222");

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        assertTrue(statusCode == 400);
        assertTrue(responseBody.contains("Failed due to insufficient balance"));
    }
    
    @Test
    public void testNegativeTransfer() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(-120).setScale(4, RoundingMode.HALF_EVEN);
        Transaction transaction = new Transaction("USD", amount, "1111", "2222");

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        assertTrue(statusCode == 400);
        assertTrue(responseBody.contains("Amount cannot be less than 0"));
    }
    
    @Test
    public void testSelfTransfer() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/transaction").build();
        BigDecimal amount = new BigDecimal(1900).setScale(4, RoundingMode.HALF_EVEN);
        Transaction transaction = new Transaction("USD", amount, "1111", "1111");

        String jsonInString = mapper.writeValueAsString(transaction);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        String responseBody = EntityUtils.toString(response.getEntity());
        assertTrue(statusCode == 400);
        assertTrue(responseBody.contains("From and To account cannot be same"));
    }
}
