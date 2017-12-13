package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.BookstorePersistenceUnit;
import ch.bfh.eadj.persistence.entity.Login;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

import static ch.bfh.eadj.persistence.entity.Login.FIND_BY_USERNAME_QUERY.PARAM_USERNAME;

@Stateless
public class LoginRepository extends AbstractRepository<Login>{

    @BookstorePersistenceUnit
    @PersistenceContext(unitName = "bookstorePU")
    EntityManager em;

    public LoginRepository() {
        super(Login.class);
    }

    public Set<Login> findByUsername(String username ) {
        TypedQuery<Login> query = em.createNamedQuery(Login.FIND_BY_USERNAME_QUERY.QUERY_NAME, Login.class);
        query.setParameter(PARAM_USERNAME, username);
        return new HashSet<>(query.getResultList());
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
