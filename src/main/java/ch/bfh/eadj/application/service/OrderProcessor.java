package ch.bfh.eadj.application.service;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven
public class OrderProcessor implements MessageListener {

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
