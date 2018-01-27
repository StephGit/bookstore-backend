package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class CustomerRepositoryTest extends AbstractTest {
    private CustomerRepository customerRepo;


    @Before
    public void setUpRepo() {
        customerRepo = new CustomerRepository();
        customerRepo.em = em;
    }

    @Test
    public void shouldFindCustomerByLastName() {

        //given
        String lastName = "McKenny";
        String firstName = "Cléa";

        //when
        List<CustomerInfo> customersByName = customerRepo.findByName(lastName);

        //then
        assertThat(customersByName.size(),is(1));
        assertEquals(lastName,customersByName.get(0).getLastName());
        assertEquals(firstName,customersByName.get(0).getFirstName());

    }

    @Test
    public void shouldFindCustomerByFirstName() {

        //given
        String lastName = "McKenny";
        String firstName = "Cléa";

        //when
        List<CustomerInfo> customersByName = customerRepo.findByName(firstName);

        //then
        assertThat(customersByName.size(),is(1));
        assertEquals(lastName,customersByName.get(0).getLastName());
        assertEquals(firstName,customersByName.get(0).getFirstName());

    }

}