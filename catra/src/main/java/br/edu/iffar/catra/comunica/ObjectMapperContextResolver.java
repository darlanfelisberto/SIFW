package br.edu.iffar.catra.comunica;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {  
    private final ObjectMapper MAPPER;

    public ObjectMapperContextResolver() {
        MAPPER = new ObjectMapper();
        // Now you should use JavaTimeModule instead
        MAPPER.registerModule(new JaxbAnnotationModule());
        MAPPER.registerModule(new JavaTimeModule());
        MAPPER.configure(MapperFeature.USE_ANNOTATIONS, true);
        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return MAPPER;
    }  
}