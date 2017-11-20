import entity.Order;
import entity.OrderItem;
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

    @Before
    public void setUp() {
        transaction = em.getTransaction();
        try {
            transaction.begin();

            entity.Order ord = new Order();
            ord.setAmount(BigDecimal.TEN);
            em.persist(ord);

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(10);
            ord.addOrderItem(orderItem);
            em.persist(orderItem);
            transaction.commit();

            orderItemId = orderItem.getId();
            orderId = ord.getId();

            em.clear();
            emf.getCache().evictAll();

        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @After
    public void tearDown() {
        transaction = em.getTransaction();
        try {
            transaction.begin();

            entity.OrderItem orderItem = em.find(OrderItem.class, orderItemId);
            em.remove(orderItem);

            Order order = em.find(Order.class, orderId);
            if (order != null) {
                em.remove(order);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }
}
