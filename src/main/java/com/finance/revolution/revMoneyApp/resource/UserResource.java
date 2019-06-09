package com.finance.revolution.revMoneyApp.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.model.User;
import com.finance.revolution.revMoneyApp.service.UserService;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

	private static final Logger LOGGER = Logger.getLogger(UserResource.class);
	UserService userService = new UserService();
	Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{phoneNumber}")
	public Response getUserByName(@PathParam("phoneNumber") String phoneNumber) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User user;
		try {
			user = userService.getUserByNo(phoneNumber);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		if (user == null)
			return Response.status(Response.Status.NO_CONTENT).entity("No such user").build();
		return Response.status(Response.Status.OK).entity(user).build();
	}

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllUsers() {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		List<User> users;
		try {
			users = userService.getAllUsers();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		if (users.size() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("No users present").build();
		else if (users.size() > 0)
			return Response.status(Response.Status.OK).entity(users).build();
		else
			return response;
	}

	@POST
	@Path("/create")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUser(User user) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		long id;
		try {
			synchronized (user) {
				id = userService.createUser(user);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
		if (id != 0)
			return Response.status(Response.Status.CREATED).entity("User created").build();
		else
			return response;
	}

	@Path("/{phoneNumber}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateUser(@PathParam("phoneNumber") String phoneNumber, User user) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		User userChanged;
		try {
			synchronized (user) {
				userChanged = userService.updateUser(phoneNumber, user);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		if (userChanged != null)
			return Response.status(Response.Status.ACCEPTED).entity(userChanged).build();
		else
			return response;
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{phoneNumber}")
	public Response deleteUser(@PathParam("phoneNumber") String phoneNumber) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean deleted = false;
		try {
			deleted = userService.deleteUser(phoneNumber);
		} catch (Exception e) {
			return Response.status(Status.NOT_FOUND).entity(e.getMessage()).build();
		}
		if (deleted)
			return Response.status(Response.Status.OK).entity("User deleted").build();
		else if (!deleted)
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("User could not be deleted").build();
		else
			return response;
	}

}
