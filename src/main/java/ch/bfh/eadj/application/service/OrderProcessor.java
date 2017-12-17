package ch.bfh.eadj.application.service;

import ch.bfh.eadj.application.exception.OrderProcessingException;
import ch.bfh.eadj.persistence.entity.Order;
import ch.bfh.eadj.persistence.enumeration.OrderStatus;
import ch.bfh.eadj.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Collection;
import java.util.Date;

@MessageDriven(
        activationConfig = {
                @ActivationConfigProperty(propertyName = "destinationLookup",
                        propertyValue = "jms/orderQueue"),
                @ActivationConfigProperty(
                        propertyName = "destinationType", propertyValue = "javax.jms.Queue")
        })
public class OrderProcessor implements MessageListener {

    @EJB
    private OrderRepository orderRepository;

    @Inject
    private MailService mailService;

    @Resource
    private TimerService timerService;

    @Resource(name = "timePeriod")
    Long timePeriod;

    @Override
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void onMessage(Message message) {
        try {
            MapMessage msg = (MapMessage) message;
            String status = msg.getJMSType();
            Long orderNr = msg.getLong("orderNr");
            if (status.equals(OrderStatus.ACCEPTED.name())) {
                processOrder(orderNr);
            } else {
                cancelOrder(orderNr);
            }
        } catch (Exception e) {
            //LogException
            e.printStackTrace();
        }
    }

    private void cancelOrder(Long orderNr) {
        Order order = orderRepository.find(orderNr);
        order.setStatus(OrderStatus.CANCELED);
        orderRepository.edit(order);

        Collection<Timer> timers = timerService.getTimers();
        for (Timer timer : timers) {
            Order timedOrder = (Order) timer.getInfo();
            if (timedOrder.getNr().equals(order.getNr())) {
                timer.cancel();
            }
        }
    }

    private void processOrder(Long orderNr) {
        Order order = orderRepository.find(orderNr);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.edit(order);
        mailService.sendProccessStartedMail(order);
        Date expiration = new Date();
        expiration.setTime(expiration.getTime() + timePeriod);
        timerService.createSingleActionTimer(expiration, new TimerConfig(order, true));
    }

    @Timeout
    private void shipOrder(Timer timer) throws OrderProcessingException {
        Order order = (Order) timer.getInfo();
        if (order.getStatus().equals(OrderStatus.PROCESSING)) {
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.edit(order);
            mailService.sendProccessStartedMail(order);
        } else {
            throw new OrderProcessingException(null);
        }
    }
}
