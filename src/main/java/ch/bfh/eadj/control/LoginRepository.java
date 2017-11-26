package ch.bfh.eadj.control;

import ch.bfh.eadj.dto.LoginInfo;
import ch.bfh.eadj.entity.Login;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class LoginRepository extends AbstractRepository<Login>{

    @PersistenceContext
    EntityManager em;

    public LoginRepository() {
        super(Login.class);
    }

    public LoginInfo findLoginByUserName( String userName ) {
        TypedQuery<LoginInfo> query = em.createNamedQuery(Login.FIND_BY_USERNAME_QUERY.QUERY_NAME, LoginInfo.class);
        query.setParameter("userName", userName);
        return query.getSingleResult();
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
