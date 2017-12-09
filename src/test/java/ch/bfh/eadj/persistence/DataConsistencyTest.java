package ch.bfh.eadj.persistence;

import ch.bfh.eadj.AbstractTest;
import org.junit.Test;

import javax.persistence.Query;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DataConsistencyTest extends AbstractTest {

    @Test
    public void shouldNotFindOrderWithoutItems() {
        //given
        String queryString = "select o.nr from Order o left join o.items oi where oi.nr is null";

        //when
        Query query = em.createQuery(queryString);
        List<Object> result = query.getResultList();

        //then
        assertThat(result.size(), is(0));
    }
}
