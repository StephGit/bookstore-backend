package ch.bfh.eadj.control.exception;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class PaymentException extends Exception {
}
