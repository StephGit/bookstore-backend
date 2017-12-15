package ch.bfh.eadj.application.exception;

import javax.jms.JMSException;

public class OrderProcessingException extends BookstoreException {
    public OrderProcessingException(Exception e) {
        super();
    }
}
