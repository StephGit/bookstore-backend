package ch.bfh.eadj.application.service;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;
@MessageDriven(activationConfig={
        @ActivationConfigProperty(propertyName="destinationLookup",
                propertyValue="jms/orderQueue"),
        @ActivationConfigProperty(
                propertyName="destinationType", propertyValue="javax.jms.Queue")
})
public class OrderProcessor implements MessageListener {

    @EJB
    private OrderService orderService;

    public OrderProcessor() {}

    @Resource
    TimerService timerService;

    @EJB
    MailService mailService;

    @Override
    public void onMessage(Message message) {
        //change  orders to processing
        // set timer
        //mail statechange to customer
    }

    @Timeout
    private void setOrderStatusShipped(Timer timer) {
        //change orderstate to shipped
        //mail statechange to customer
    }
}
