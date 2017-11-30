package ch.bfh.eadj.control;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.dto.LoginInfo;
import ch.bfh.eadj.entity.UserGroup;
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
    public void shouldFindLoginByUserName()  {
        //given
        String userName = "crasch3";

        //when
        Set<LoginInfo> loginInfoSet = loginRepository.findLoginByUserName(userName);

        //then
        assertFalse(loginInfoSet.isEmpty());
        assertTrue(loginInfoSet.size()==1);
        for (Iterator<LoginInfo> it = loginInfoSet.iterator(); it.hasNext(); ) {
            LoginInfo loginInfo = it.next();

            assertThat(loginInfo.getNr(), is(5L));
            assertThat(loginInfo.getUserGroup(), is(UserGroup.EMPLOYEE));
        }


    }

    @Test
    public void shouldNotFindLoginByUserName() {
        //given
        String userName = "Admin123";

        //when
        Set<LoginInfo> loginInfo = loginRepository.findLoginByUserName(userName);

        //then
        assertThat(loginInfo.isEmpty(), is(true));
    }

}