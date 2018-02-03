package ch.bfh.eadj;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class AbstractTest {

    private static EntityManagerFactory emf;
    protected static EntityManager em;

    @BeforeAll
    public static void setUpBeforeClass() {
        emf = Persistence.createEntityManagerFactory("bookstoreTestPU");
        em = emf.createEntityManager();
    }

    @AfterAll
    public static void tearDownAfterClass() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

}
