package ch.bfh.eadj.presentation;

import ch.bfh.eadj.application.exception.*;
import ch.bfh.eadj.application.service.CustomerService;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.entity.Customer;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_HTML;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("customers")
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @GET
    @Path("login")
    @Consumes(TEXT_PLAIN)
    @Produces(TEXT_HTML)
    public Long authenticateCustomer(String email, String password) {
        try {
            return customerService.authenticateCustomer(email, password);
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }

    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public Response registerCustomer(Customer customer, String password) {
        //TODO check if incoming data is vaild
        try {
            Long customerId = customerService.registerCustomer(customer, password);
            if (customerId != null) {
                return Response.status(Response.Status.CREATED).entity(customerId).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (EmailAlreadyUsedException e) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
    }

    @GET
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public List<CustomerInfo> searchCustomers(@QueryParam("name") String name) {
            return customerService.searchCustomers(name);
    }

}

