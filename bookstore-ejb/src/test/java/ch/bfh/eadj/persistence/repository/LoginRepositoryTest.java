package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.entity.Login;
import ch.bfh.eadj.persistence.enumeration.UserGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginRepositoryTest extends AbstractTest {

    private LoginRepository loginRepository;


    @BeforeEach
    public void setUpRepo()  {
        loginRepository = new LoginRepository();
        loginRepository.em = em;
    }

    @Test
    void shouldFindLoginByUsername()  {
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
    void shouldNotFindLoginByUsername() {
        //given
        String username = "Admin123";

        //when
        Set<Login> login = loginRepository.findByUsername(username);

        //then
        assertThat(login.isEmpty(), is(true));
    }

}