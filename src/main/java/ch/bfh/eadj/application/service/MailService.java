package ch.bfh.eadj.application.service;


import ch.bfh.eadj.persistence.entity.Order;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Session;

@Stateless
public class MailService {

    @Resource(name="mail/bookstore")
    private Session mailSession;


    public void sendMail(Order order) {

    }
}
