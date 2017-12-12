package ch.bfh.eadj.persistence.repository;

import static ch.bfh.eadj.persistence.entity.Customer.FIND_BY_NAME_QUERY.PARAM_NAME;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import ch.bfh.eadj.persistence.entity.Customer;

@Stateless
public class CustomerRepository extends AbstractRepository<Customer> {

    @Inject
    @BookstorePersistenceUnit
    EntityManager em;

    public CustomerRepository() {
        super(Customer.class);
    }



    public List<CustomerInfo> findByName(String name) {
        TypedQuery<CustomerInfo> query = em.createNamedQuery(Customer.FIND_BY_NAME_QUERY.QUERY_NAME, CustomerInfo.class);
        query.setParameter(PARAM_NAME, name);
        return query.getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }


}
