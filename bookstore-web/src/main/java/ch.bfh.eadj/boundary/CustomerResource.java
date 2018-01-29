package ch.bfh.eadj.boundary;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.application.service.CustomerService;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.*;

@Path("customers")
public class CustomerResource {

    public static final String NO_CUSTOMER_WITH_EMAIL = "no customer with the specified email address exists";
    public static final String INVALID_PASSWORD = "the password is invalid";
    public static final String NO_CUSTOMER_WITH_ID = "no customer with the specified number exists";
    public static final String NO_DATA_FOR_CUSTOMER = "the number of the customer data is not null";
    public static final String EMAIL_ALREADY_USED = "the email address is already used by another customer";
    public static final String CHANGE_SUCCESSFUL = "the change was successful";
    public static final String NUMBER_NOT_MATCHING_PARAM = "the number of the customer data does not match the path parameter";
    public static final String UPDATE_SUCCESSFUL = "the update was successful";
    public static final String EMAIL_TO_CHANGE_ALREADY_USED = "the email address to be changed is already used by another customer";

    @Inject
    CustomerService customerService;

    @GET
    @Path("login")
    @Produces(TEXT_HTML)
    public Response authenticateCustomer(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        try {
            Long id = customerService.authenticateCustomer(email, password);
            return Response.status(Response.Status.OK).entity(id).build();
        } catch (CustomerNotFoundException e) {
            String message = NO_CUSTOMER_WITH_EMAIL;
            Response response = Response.status(Response.Status.NOT_FOUND).entity(message).build();
            throw new WebApplicationException(message, response);
        } catch (InvalidPasswordException e) {
            String message = INVALID_PASSWORD;
            Response response = Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
            throw new WebApplicationException(message, response);
        }
    }

    @PUT
    @Path("login")
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    public Response changePassword(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        try {
            customerService.changePassword(email, password);
            String message = CHANGE_SUCCESSFUL;
            return Response.status(Response.Status.NO_CONTENT).entity(message).build();
        } catch (CustomerNotFoundException e) {
            String message = NO_CUSTOMER_WITH_EMAIL;
            Response response = Response.status(Response.Status.NOT_FOUND).entity(message).build();
            throw new WebApplicationException(message, response);
        }
    }

    @GET
    @Path("{nr}")
    @Produces(APPLICATION_JSON)
    public Response findCustomer(@PathParam("nr") Long nr) {
        try {
            Customer customer = customerService.findCustomer(nr);
            return Response.status(Response.Status.OK).entity(customer).build();
        } catch (CustomerNotFoundException e) {
            String message = NO_CUSTOMER_WITH_ID;
            Response response = Response.status(Response.Status.NOT_FOUND).entity(message).build();
            throw new WebApplicationException(message, response);
        }
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(TEXT_PLAIN)
    public Response registerCustomer(Customer customer, @HeaderParam("password") String password) {
        try {
            Long customerId = customerService.registerCustomer(customer, password);
            if (customerId != null) {
                return Response.status(Response.Status.CREATED).entity(customerId).build();
            } else {
                String message = NO_DATA_FOR_CUSTOMER;
                Response response = Response.status(Response.Status.BAD_REQUEST).entity(message).build();
                throw new WebApplicationException(message, response);
            }
        } catch (EmailAlreadyUsedException e) {
            String message = EMAIL_ALREADY_USED;
            Response response = Response.status(Response.Status.CONFLICT).entity(message).build();
            throw new WebApplicationException(message, response);
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
    public Response updateCustomer(@PathParam("nr") Long nr, Customer customer) {

        if ((customer.getNr() != null) && (!customer.getNr().equals(nr))) {
            String message = NUMBER_NOT_MATCHING_PARAM;
            Response response = Response.status(Response.Status.BAD_REQUEST).entity(message).build();
            throw new WebApplicationException(message, response);
        } else {
            try {
                customerService.updateCustomer(customer);
                String message = UPDATE_SUCCESSFUL;
                return Response.status(Response.Status.NO_CONTENT).entity(message).build();
            } catch (CustomerNotFoundException e) {
                String message = NO_CUSTOMER_WITH_ID;
                Response response = Response.status(Response.Status.NOT_FOUND).entity(message).build();
                throw new WebApplicationException(message, response);
            } catch (EmailAlreadyUsedException e) {
                String message = EMAIL_TO_CHANGE_ALREADY_USED;
                Response response = Response.status(Response.Status.CONFLICT).entity(message).build();
                throw new WebApplicationException(message, response);
            }
        }
    }

}

