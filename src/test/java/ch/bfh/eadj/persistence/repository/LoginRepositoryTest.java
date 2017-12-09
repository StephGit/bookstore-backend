package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.entity.Login;
import ch.bfh.eadj.persistence.enumeration.UserGroup;
import ch.bfh.eadj.persistence.repository.LoginRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LoginRepositoryTest extends AbstractTest {

    private LoginRepository loginRepository;


    @Before
    public void setUpRepo()  {
        loginRepository = new LoginRepository();
        loginRepository.em = em;
    }

    @Test
    public void shouldFindLoginByUsername()  {
        //given
        String username = "crasch3";

        //when
        Set<Login> loginSet = loginRepository.findByUsername(username);

        //then
        assertFalse(loginSet.isEmpty());
        assertTrue(loginSet.size()==1);
        for (Login login : loginSet) {
            assertThat(login.getNr(), is(5L));
            assertThat(login.getGroup(), is(UserGroup.EMPLOYEE));
        }
    }

    @Test
    public void shouldNotFindLoginByUsername() {
        //given
        String username = "Admin123";

        //when
        Set<Login> login = loginRepository.findByUsername(username);

        //then
        assertThat(login.isEmpty(), is(true));
    }

}