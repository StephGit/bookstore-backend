package ch.bfh.eadj.control.customer;

import ch.bfh.eadj.control.AbstractRepository;
import ch.bfh.eadj.dto.CustomerInfo;
import ch.bfh.eadj.entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static ch.bfh.eadj.entity.Customer.FIND_BY_NAME_QUERY.PARAM_NAME;

@Stateless
public class CustomerRepository extends AbstractRepository<Customer> {

    @PersistenceContext
    EntityManager em;

    public CustomerRepository() {
        super(Customer.class);
    }

    public List<CustomerInfo> findCustomerByName(String name) {
        TypedQuery<CustomerInfo> query = em.createNamedQuery(Customer.FIND_BY_NAME_QUERY.QUERY_NAME, CustomerInfo.class);
        query.setParameter(PARAM_NAME, name);
        return query.getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
