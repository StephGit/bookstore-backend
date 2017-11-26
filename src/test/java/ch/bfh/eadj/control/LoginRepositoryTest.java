package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.LoginInfo;
import ch.bfh.eadj.entity.UserGroup;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.NoResultException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class LoginRepositoryTest extends AbstractTest {

    private LoginRepository loginRepository;


    @Before
    public void setUpRepo() throws Exception {
        loginRepository = new LoginRepository();
        loginRepository.em = em;
    }

    @Test
    public void shouldFindLoginByUserName() throws Exception {
        //given
        String userName = "crasch3";

        //when
        LoginInfo loginInfo = loginRepository.findLoginByUserName(userName);

        //then
        assertThat(loginInfo.getNr(), is(5L));
        assertThat(loginInfo.getUserGroup(), is(UserGroup.EMPLOYEE));

    }

    @Test(expected = NoResultException.class)
    public void shouldNotFindLoginByUserName() throws Exception {
        //given
        String userName = "Admin123";

        //when
        LoginInfo loginInfo = loginRepository.findLoginByUserName(userName);

        //then
        assertNull(loginInfo.getNr());
    }

}