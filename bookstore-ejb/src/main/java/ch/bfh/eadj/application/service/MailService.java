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
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.Future;

@SuppressWarnings("unused")
@Stateless
public class MailService {

    @Resource(name = "mail/bookstore")
    private Session mailSession;

    @Asynchronous
    public Future<String> sendProccessStartedMail(Order order) {
        Customer c = order.getCustomer();
        return sendMail(order, "Hello " + c.getFirstName() + ". We just started processing your order. We'll notify you as soon as we shipped the order",
                "Order processing started");
    }


    @Asynchronous
    public Future<String> sendOrderShippedMail(Order order) {
        Customer c = order.getCustomer();
        return sendMail(order, "Hello " + c.getFirstName() + "Hello " + c.getFirstName() + ". Your book order " + order.getNr() + " has been shipped",
                "Order shipped");
    }


    // 573 5.1.1 Swisscom Antispam: Authentifizierte Verbindung nicht moeglich. Bitte benutzen Sie den Port 587 oder 465 (SSL/TLS) anstelle von Port 25. Weitere Informationen: www.swisscom.ch/p25. Connexion authentifiee pas possible. Veuillez utiliser le port 587 ou 465 (SSL/TLS) a la place du port 25. Ulterieurs informations: www.swisscom.ch/p25. Collegamento autenticato non e possibile. Si prega di utilizzare la porta 587 o 465 (SSL/TLS) invece di porta 25. Altra informazione: www.swisscom.ch/p25. Authenticated connection is not possible. Please use port 587 or 465 (SSL/TLS) instead of port 25. More information: www.swisscom.ch/p25.
    // 550 5.7.60 SMTP; Client does not have permissions to send as this sender
    private Future<String> sendMail(Order order, String text, String subject) {
        String status;
        try {
            MimeMessage msg = new MimeMessage(mailSession);
            msg.setRecipients(Message.RecipientType.TO,
                    order.getCustomer().getEmail());
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(text);
            Transport.send(msg);
            status = "Sent";
        } catch (MessagingException mex) {
            System.out.println("send failed, exception: " + mex);
            status = "Encountered an error";
        }
        return new AsyncResult<String>(status);
    }

}
