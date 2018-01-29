package ch.bfh.eadj.boundary.resource;

import ch.bfh.eadj.application.exception.CustomerNotFoundException;
import ch.bfh.eadj.application.exception.EmailAlreadyUsedException;
import ch.bfh.eadj.application.exception.InvalidPasswordException;
import ch.bfh.eadj.application.service.CustomerService;
import ch.bfh.eadj.boundary.dto.CustomerDTO;
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
            throw new WebApplicationException(NO_CUSTOMER_WITH_EMAIL, Response.Status.NOT_FOUND);
        } catch (InvalidPasswordException e) {
            throw new WebApplicationException(INVALID_PASSWORD, Response.Status.UNAUTHORIZED);
        }
    }

    @PUT
    @Path("login")
    @Consumes(TEXT_PLAIN)
    @Produces(APPLICATION_JSON)
    public Response changePassword(@HeaderParam("email") String email, @HeaderParam("password") String password) {
        try {
            customerService.changePassword(email, password);
            return Response.status(Response.Status.NO_CONTENT).entity(CHANGE_SUCCESSFUL).build();
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(NO_CUSTOMER_WITH_EMAIL, Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("{nr}")
    @Produces(APPLICATION_JSON)
    public Response findCustomer(@PathParam("nr") Long nr) {
        try {
            Customer customer = customerService.findCustomer(nr);
            return Response.status(Response.Status.OK).entity(convertCustomer(customer)).build();
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(NO_CUSTOMER_WITH_ID, Response.Status.NOT_FOUND);
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
                throw new WebApplicationException(NO_DATA_FOR_CUSTOMER, Response.Status.BAD_REQUEST);
            }
        } catch (EmailAlreadyUsedException e) {
            throw new WebApplicationException(EMAIL_ALREADY_USED, Response.Status.CONFLICT);
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
    public Response updateCustomer(@PathParam("nr") Long nr, CustomerDTO customerDTO) {

        if ((customerDTO.getNr() == null) || (!customerDTO.getNr().equals(nr))) {
            throw new WebApplicationException(NUMBER_NOT_MATCHING_PARAM, Response.Status.BAD_REQUEST);
        } else {
            try {
                Customer customer = customerService.findCustomer(nr);
                applyUpdates(customer, customerDTO);
                customerService.updateCustomer(customer);
                return Response.status(Response.Status.NO_CONTENT).entity(UPDATE_SUCCESSFUL).build();
            } catch (CustomerNotFoundException e) {
                throw new WebApplicationException(NO_CUSTOMER_WITH_ID, Response.Status.NOT_FOUND);
            } catch (EmailAlreadyUsedException e) {
                throw new WebApplicationException(EMAIL_TO_CHANGE_ALREADY_USED, Response.Status.CONFLICT);
            }
        }
    }

    private void applyUpdates(Customer customer, CustomerDTO customerDTO) {
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setCreditCard(customerDTO.getCreditCard());
    }

    private CustomerDTO convertCustomer(Customer customer) {
        return new CustomerDTO(customer.getNr(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getCreditCard(), customer.getAddress());
    }

}

