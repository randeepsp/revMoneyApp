package com.finance.revolution.revMoneyApp.resource;
 
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.finance.revolution.revMoneyApp.model.Transaction;
import com.finance.revolution.revMoneyApp.service.TransactionService;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

	private static final Logger LOGGER = Logger.getLogger(AccountResource.class);
	TransactionService transactionService = new TransactionService();

	@POST
	public Response transferFund(Transaction transaction) {
		LOGGER.debug(Thread.currentThread().getStackTrace()[1].getMethodName()+" transfer");
		boolean transactionResponse;
		try {
			transactionResponse = transactionService.transfer(transaction);
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
		if(transactionResponse)
			return Response.status(Response.Status.OK).build();
		else
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
	}
}
