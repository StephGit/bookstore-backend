package ch.bfh.eadj.presentation;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.application.service.CustomerService;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;

import javax.inject.Inject;
import javax.resource.spi.IllegalStateException;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.*;

@Path("customers")
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @GET
    @Path("login")
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

    @PUT
    @Path("login")
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    public Response changePassword(String email, String password) {
        try {
            customerService.changePassword(email, password);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("{nr}")
    @Produces(APPLICATION_JSON)
    public Customer findCustomer(@PathParam("nr") Long nr) {
        try {
            return customerService.findCustomer(nr);
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
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
    @Produces(APPLICATION_JSON)
    public List<CustomerInfo> searchCustomers(@QueryParam("name") String name) {
            return customerService.searchCustomers(name);
    }

    @PUT
    @Path("{nr}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response updateCustomer(@PathParam("nr") Long nr) {
        try {
            Customer customer = customerService.findCustomer(nr);
            customerService.updateCustomer(customer);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        } catch (IllegalStateException e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        } catch (EmailAlreadyUsedException e) {
            throw new WebApplicationException(Response.Status.CONFLICT);
        }
    }

}

