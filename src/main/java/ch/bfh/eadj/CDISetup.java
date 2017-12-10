package ch.bfh.eadj;

import org.jboss.weld.transaction.spi.TransactionServices;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Synchronization;
import javax.transaction.UserTransaction;

@Singleton
public class CDISetup {
    @Produces
    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    static EntityManager em;
}
