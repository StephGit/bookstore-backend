package ch.bfh.eadj;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.logging.Logger;

public abstract class AbstractTest {

    private static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeClass
    public static void setUpBeforeClass() {
        emf = Persistence.createEntityManagerFactory("bookstoreTestPU");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

}
