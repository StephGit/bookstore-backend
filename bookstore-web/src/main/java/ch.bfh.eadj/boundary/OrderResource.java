package ch.bfh.eadj.boundary;

import ch.bfh.eadj.application.exception.*;
import ch.bfh.eadj.application.service.CustomerService;
import ch.bfh.eadj.application.service.OrderService;
import ch.bfh.eadj.boundary.dto.OrderDTO;
import ch.bfh.eadj.boundary.dto.OrderItemDTO;
import ch.bfh.eadj.persistence.dto.OrderInfo;
import ch.bfh.eadj.persistence.entity.Book;
import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.entity.OrderItem;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("orders")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
public class OrderResource {

    @Inject
    private OrderService orderService;

    @Inject
    private CustomerService customerService;

    @POST
    public Response placeOrder(OrderDTO orderDTO) {


        Customer c;
        try {
            c = customerService.findCustomer(orderDTO.getCustomerNr());
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        List<OrderItem> orderItems = extractOrderItems(orderDTO);

        try {
            orderService.placeOrder(c, orderItems);
        } catch (PaymentFailedException e) {
            throw new WebApplicationException(Status.PAYMENT_REQUIRED);
        } catch (OrderProcessingException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } catch (BookNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
        return Response.status(Status.CREATED).build();

    }

    private List<OrderItem> extractOrderItems(OrderDTO orderDTO) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO dto : orderDTO.getItems()) {
            OrderItem ord = new OrderItem();
            Book b = new Book();
            b.setIsbn(dto.getIsbn());
            ord.setQuantity(dto.getQuantity());
            ord.setBook(b);
            orderItems.add(ord);

        }
        return orderItems;
    }

    @GET
    @Path("{nr}")
    public Order findOrder(@PathParam("nr") long nr) {
        try {
            return orderService.findOrder(nr);
        } catch (OrderNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);        }
    }

    @GET
    public List<OrderInfo> searchOrders(@QueryParam("customerNr") long customerNr, @QueryParam("year") int year) {
        Customer customer;
        try {
            customer = customerService.findCustomer(customerNr);
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        return orderService.searchOrders(customer, year);
    }

    @DELETE
    @Path("{nr}")
    public Response updateBook(@PathParam("nr") long nr) {
        try {
            orderService.cancelOrder(nr);
            return Response.status(Status.NO_CONTENT).build();
        } catch (OrderNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        } catch (OrderAlreadyShippedException | OrderAlreadyCanceledException e) {
            throw new WebApplicationException(Status.CONFLICT);
        } catch (OrderProcessingException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }




}
