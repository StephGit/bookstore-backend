package ch.bfh.eadj;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class CDISetup {
    @Produces
    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    static EntityManager em;

}
