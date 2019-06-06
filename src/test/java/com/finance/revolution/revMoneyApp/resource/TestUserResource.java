package com.finance.revolution.revMoneyApp.resource;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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

import com.finance.revolution.revMoneyApp.model.User;

class TestUserResource extends TestResource {

	@DisplayName("testGetAllUsers")
	@Test
	public void testGetAllUsers() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/all").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		User[] users = mapper.readValue(jsonString, User[].class);
		assertTrue(users.length > 0);
	}

	@DisplayName("testGetUser")
	@Test
	public void testGetUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/1234").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		// check the content
		String jsonString = EntityUtils.toString(response.getEntity());
		User user = mapper.readValue(jsonString, User.class);
		assertTrue(user != null);
	}

	@DisplayName("testDeleteUser")
	@Test
	public void testDeleteUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/12345").build();
		HttpDelete request = new HttpDelete(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}

	@DisplayName("testDeleteNonExistingUser")
	@Test
	public void testDeleteNonExistingUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/12345789").build();
		HttpDelete request = new HttpDelete(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		//fail(statusCode + "");
		assertTrue(statusCode == 404);
	}

	@DisplayName("testCreateUser")
	@Test
	public void testCreateUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/create").build();
		User user = new User("98765", "createdUser", "createduser@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 201);
	}

	@DisplayName("testCreateSameUser")
	@Test
	public void testCreateSameUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/create").build();
		User user = new User("98765", "createdUser", "createduser@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 406);
	}

	@DisplayName("testUpdateUser:")
	@Test
	public void testUpdateUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/9789").build();
		User user = new User("9789", "zoltarUser", "zoltaruser@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 202);
	}

	@DisplayName("testUpdateNoSuchUser")
	@Test
	public void testUpdateNoSuchUser() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/users/9879").build();
		User user = new User("9789", "zoltarUser", "zoltaruser@gmail.com");
		String jsonInString = mapper.writeValueAsString(user);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPut request = new HttpPut(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 404);
	}

}
