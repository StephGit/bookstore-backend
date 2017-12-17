package ch.bfh.eadj.application.service;


import ch.bfh.eadj.persistence.entity.Customer;
import ch.bfh.eadj.persistence.entity.Order;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.Future;

@Stateless
public class MailService {

    @Resource(name = "mail/bookstore")
    private Session mailSession;

    @Asynchronous
    public Future<String> sendProccessStartedMail(Order order) {
        Customer c = order.getCustomer();
        return sendMail(order, "Hello " + c.getFirstName() + ". We just started processing your order. We'll notify you as soon as we shipped the order",
                "Order processing started",
                "adrian_krebs@outlook.com");
    }


    @Asynchronous
    public Future<String> sendOrderShippedMail(Order order) {
        Customer c = order.getCustomer();
        return sendMail(order, "Hello " + c.getFirstName() + "Hello " + c.getFirstName() + ". Your book order " + order.getNr() + " has been shipped",
                "Order shipped",
                c.getEmail());
    }


    private Future<String> sendMail(Order order, String text, String subject, String mailAddress) {
        String status;
        try {
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setFrom("me@example.com");
            msg.setRecipients(Message.RecipientType.TO,
                    "adrian_krebs@outlook.com");
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(text);
            status = "Sent";
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
            status = "Encountered an error";
        }
        return new AsyncResult<String>(status);
    }

}
