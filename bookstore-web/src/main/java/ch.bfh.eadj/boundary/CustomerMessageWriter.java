package ch.bfh.eadj.boundary;

import ch.bfh.eadj.persistence.dto.CustomerInfo;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;


@Provider
@Produces(MediaType.APPLICATION_JSON)
public class CustomerMessageWriter implements MessageBodyWriter<List<CustomerInfo>>{

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(List<CustomerInfo> customers, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(List<CustomerInfo> customers, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        PrintWriter writer = new PrintWriter(entityStream);
        for (CustomerInfo customerInfo : customers) {
            writer.println(customerInfo.getEmail() + "," + customerInfo.getFirstName() + "," + customerInfo.getLastName() + "," + customerInfo.getNr());
        }
        writer.flush();
    }
}
