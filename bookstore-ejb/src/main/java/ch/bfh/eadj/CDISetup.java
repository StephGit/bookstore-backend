package ch.bfh.eadj;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class CDISetup {
    @Produces
    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    private EntityManager em;
}
