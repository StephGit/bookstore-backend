package control;

import dto.CustomerInfo;
import entity.Customer;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CustomerService extends AbstractService<Customer> {

    @PersistenceContext
    EntityManager em;

    public CustomerService() {
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
