//package br.edu.iffar.api;
//
//import jakarta.ws.rs.ext.ContextResolver;
//import jakarta.ws.rs.ext.Provider;
//
//import com.fasterxml.jackson.databind.MapperFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
//import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
//
//@Provider
//public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {  
//    private final ObjectMapper MAPPER;
//
//    public ObjectMapperContextResolver() {
//        MAPPER = new ObjectMapper();
//        
//        
//        Hibernate5Module hibernate5Module = new Hibernate5Module().configure(Feature.FORCE_LAZY_LOADING, false);
//        // Enable below line to switch lazy loaded json from null to a blank object!
//        //hibernate5Module.configure(Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
//        MAPPER.registerModule(hibernate5Module);
//        
//        
//        
//        
//        MAPPER.registerModule(new JaxbAnnotationModule());
//        MAPPER.registerModule(new JavaTimeModule());
//        MAPPER.configure(MapperFeature.USE_ANNOTATIONS, true);
//        MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//    }
//
//    @Override
//    public ObjectMapper getContext(Class<?> type) {
//        return MAPPER;
//    }  
//}