package ch.bfh.eadj;

import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

@Singleton
public class TestCDISetup {

    @Produces
    @BookstorePersistenceUnit
    @PersistenceContext
    public EntityManager getEm() {
        return Persistence.createEntityManagerFactory("bookstoreTestPU").createEntityManager();
    }


}
