package ch.bfh.eadj;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class TestCDISetup {

    @Produces
    @BookstorePersistenceUnit
    @PersistenceContext
    public EntityManager getEm() {
        return Persistence.createEntityManagerFactory("bookstoreTestPU").createEntityManager();
    }


}
