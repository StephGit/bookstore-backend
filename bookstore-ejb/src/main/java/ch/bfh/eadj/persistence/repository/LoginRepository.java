package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.persistence.entity.Login;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static ch.bfh.eadj.persistence.entity.Login.FIND_BY_USERNAME_QUERY.PARAM_USERNAME;

@Stateless
public class LoginRepository extends AbstractRepository<Login>{

    @Inject
    @BookstorePersistenceUnit
    public EntityManager em;

    public LoginRepository() {
        super(Login.class);
    }

    public List<Login> findByUsername(String username ) {
        TypedQuery<Login> query = em.createNamedQuery(Login.FIND_BY_USERNAME_QUERY.QUERY_NAME, Login.class);
        query.setParameter(PARAM_USERNAME, username);
        return query.getResultList();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
