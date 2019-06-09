package com.finance.revolution.revMoneyApp.resource;

import java.math.BigDecimal;
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

import com.finance.revolution.revMoneyApp.model.Account;
import com.finance.revolution.revMoneyApp.service.AccountService;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

	private static final Logger LOGGER = Logger.getLogger(AccountResource.class);
	AccountService accountService = new AccountService();
	Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

	@GET
	@Path("/all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllAccounts() {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		List<Account> accounts;
		try {
			accounts = accountService.getAllAccounts();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		if (accounts.size() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("No accounts present").build();
		else if (accounts.size() > 0)
			return Response.status(Response.Status.OK).entity(accounts).build();
		else
			return response;
	}

	@GET
	@Path("/{PhoneNumber}")
	public Response getAccount(@PathParam("PhoneNumber") String PhoneNumber) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account;
		try {
			account = accountService.getAccountByNo(PhoneNumber);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
		if (account == null)
			return Response.status(Response.Status.NO_CONTENT).entity("No such account").build();
		return Response.status(Response.Status.OK).entity(account).build();
	}

	@GET
	@Path("/{PhoneNumber}/balance")
	public Response getBalance(@PathParam("PhoneNumber") String PhoneNumber) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account;
		try {
			account = accountService.getAccountByNo(PhoneNumber);
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}

		if (account == null)
			return Response.status(Response.Status.NO_CONTENT).entity("No such account").build();
		BigDecimal balance = account.getBalance();
		if (balance != null)
			return Response.status(Response.Status.OK).entity(balance).build();
		else
			return response;
	}

	@POST
	@Path("/create")
	public Response createAccount(Account account) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		long id;
		try {
			synchronized (account) {
				id = accountService.createAccount(account);
			}
		} catch (Exception e) {
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
		}
		if (id != 0)
			return Response.status(Response.Status.CREATED).entity("Account created").build();
		else
			return response;
	}

	@PUT
	@Path("/{PhoneNumber}/deposit/{amount}")
	public Response deposit(@PathParam("PhoneNumber") String PhoneNumber, @PathParam("amount") BigDecimal amount) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account;
		try {

			account = accountService.deposit(PhoneNumber, amount);

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		if (account != null)
			return Response.status(Response.Status.OK).entity(account.getBalance()).build();
		else
			return response;
	}

	@PUT
	@Path("/{PhoneNumber}/withdraw/{amount}")
	public Response withdraw(@PathParam("PhoneNumber") String PhoneNumber, @PathParam("amount") BigDecimal amount) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		Account account;
		try {
			account = accountService.withdraw(PhoneNumber, amount);
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		if (account != null)
			return Response.status(Response.Status.OK).entity(account.getBalance()).build();
		else
			return response;
	}

	@DELETE
	@Path("/{PhoneNumber}")
	public Response deleteAccount(@PathParam("PhoneNumber") String PhoneNumber) {
		LOGGER.debug("Entering " + Thread.currentThread().getStackTrace()[1].getMethodName());
		boolean deleted = false;
		try {
			deleted = accountService.delete(PhoneNumber);
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		if (deleted)
			return Response.status(Response.Status.OK).entity("Account deleted").build();
		else if (!deleted)
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Account could not be deleted").build();
		else
			return response;
	}

}
