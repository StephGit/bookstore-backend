package ch.bfh.eadj.persistence.repository;

import ch.bfh.eadj.AbstractTest;
import ch.bfh.eadj.persistence.dto.CustomerInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerRepositoryTest extends AbstractTest {
    private CustomerRepository customerRepo;


    @BeforeEach
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

    }

    @Test
    public void shouldFindCustomerByFirstName() {

        //given
        String firstName = "Cléa";

        //when
        List<CustomerInfo> customersByName = customerRepo.findByName(firstName);

        //then
        assertThat(customersByName.size(),is(1));
        assertEquals(firstName,customersByName.get(0).getFirstName());

    }

    @Test
    public void shouldFindCustomerByMultipartName() {

        //given
        String firstName = "Hans Ulrich";
        String lastName = "von Känel";

        //when
        List<CustomerInfo> customersByName = customerRepo.findByName(firstName);

        //then
        assertThat(customersByName.size(),is(1));
        assertEquals(firstName,customersByName.get(0).getFirstName());

    }
}