package ch.bfh.eadj.boundary;

import ch.bfh.eadj.boundary.dto.CustomerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Provider
@Consumes(APPLICATION_JSON)
public class CustomerDTOMessageReader implements MessageBodyReader<CustomerDTO> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public CustomerDTO readFrom(Class<CustomerDTO> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        if (mediaType.getType().equals("application") && mediaType.getSubtype().equals("json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(entityStream, CustomerDTO.class);
        }
        throw new UnsupportedOperationException("Not supported MediaType: " + mediaType);
    }
}
