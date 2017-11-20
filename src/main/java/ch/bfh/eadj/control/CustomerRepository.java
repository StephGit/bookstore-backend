package ch.bfh.eadj.control;

import ch.bfh.eadj.dto.CustomerInfo;
import ch.bfh.eadj.entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CustomerRepository extends AbstractRepository<Customer> {

    @PersistenceContext
    EntityManager em;

    public CustomerRepository() {
        super(Customer.class);
    }

    public List<CustomerInfo> findCustomerByName(String name) {
        TypedQuery<CustomerInfo> query = em.createNamedQuery(Customer.FIND_BY_NAME_QUERY.QUERY_NAME, CustomerInfo.class);
        query.setParameter("name", name);
        List<CustomerInfo> resultList = query.getResultList();
        return resultList;
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
