package ch.bfh.eadj.boundary;

import ch.bfh.eadj.persistence.dto.CustomerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;


@Provider
@Produces(APPLICATION_JSON)
public class CustomerInfoMessageWriter implements MessageBodyWriter<List<CustomerInfo>>{

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
        if (mediaType.getType().equals("application") && mediaType.getSubtype().equals("json")) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(entityStream, customers);
        } else {
            throw new UnsupportedOperationException("Not supported MediaType: " + mediaType);
        }
    }
}
