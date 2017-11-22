package ch.bfh.eadj;

import ch.bfh.eadj.entity.BookOrder;
import ch.bfh.eadj.entity.OrderItem;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.math.BigDecimal;
import java.util.logging.Logger;

public abstract class AbstractTest {

    protected final static Logger LOGGER = Logger.getLogger(AbstractTest.class.getName());

    protected static EntityManagerFactory emf;
    protected static EntityManager em;
    private EntityTransaction transaction;

    protected Long orderId;
    protected Long orderItemId;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("bookstoreTestPU");
        em = emf.createEntityManager();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }

}
