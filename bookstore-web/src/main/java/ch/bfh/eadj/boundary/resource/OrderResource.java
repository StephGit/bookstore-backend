package ch.bfh.eadj.boundary.resource;

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

    /**
     * Places an order on the bookstore and returns the data of the placed order.
     * @param body the data of the order to be placed
     * @responseMessage 201 the order was successful
     * @responseMessage 402 a payment error occurs
     * @responseMessage 404 the order references a non-existent customer or book
     */
    @POST
    public Response placeOrder(OrderDTO body) {


        Customer c;
        try {
            c = customerService.findCustomer(body.getCustomerNr());
        } catch (CustomerNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

        List<OrderItem> orderItems = extractOrderItems(body);

        try {
            Order order = orderService.placeOrder(c, orderItems);
            return Response.status(Status.CREATED).entity(order).build();

        } catch (PaymentFailedException e) {
            throw new WebApplicationException(Status.PAYMENT_REQUIRED);
        } catch (OrderProcessingException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        } catch (BookNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }

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

    /**
     * Finds an order by number and returns the data of the found order.
     * @param nr the number of the order
     * @responseMessage 200 the retrieval was successful
     * @responseMessage 404 no order with the specified number exists
     */
    @GET
    @Path("{nr}")
    public Response findOrder(@PathParam("nr") long nr) {
        try {
            Order order = orderService.findOrder(nr);
            return Response.status(Status.OK).entity(order).build();
        } catch (OrderNotFoundException e) {
            throw new WebApplicationException(Status.NOT_FOUND);        }
    }

    /**
     * Searches for orders by customer and year and returns a list of matching orders.
     * @param customerNr the number of the customer
     * @param year the year of the orders
     * @responseMessage 200 the search was successful
     * @responseMessage 404 no customer with the specified email address exists
     *
     */
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

    /**
     * Cancels an order.
     * @param nr the number of the order
     * @responseMessage 204 the cancellation was successful
     * @responseMessage 404 no order with the specified number exists
     * @responseMessage 409 the order has already been shipped
     */
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
