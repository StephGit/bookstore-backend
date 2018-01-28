package ch.bfh.eadj.presentation;
import ch.bfh.eadj.application.service.CustomerService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Produces;

public class EJBProvider {

    public EJBProvider() {
    }

    @Produces
    public CustomerService getCustomerService() {
        return jndiLookup("java:global/bookstore-ejb-1.0-SNAPSHOT/CustomerService!ch.bfh.eadj.application.service.CustomerServiceRemote",
                CustomerService.class);
    }

    private <T> T jndiLookup(String name, Class<T> type) {
        try {
            InitialContext ctx = new InitialContext();
            return type.cast(ctx.lookup(name));
        } catch (NamingException e) {
            String errorMessage = "Error during JNDI lookup for " + name;
            throw new RuntimeException(errorMessage, e);
        }
    }

}