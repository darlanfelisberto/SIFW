//package br.edu.iffar.fw.comendo.db.model.enun;
//
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//import java.util.stream.Stream;
//
//@Converter(autoApply = true)
//public class TypeCreditoConverter implements AttributeConverter<TypeCredito, String> {
// 
//    @Override
//    public String convertToDatabaseColumn(TypeCredito category) {
//        if (category == null) {
//            return null;
//        }
//        return category.name();
//    }
//
//    @Override
//    public TypeCredito convertToEntityAttribute(String code) {
//        if (code == null) {
//            return null;
//        }
//
//        return Stream.of(TypeCredito.values())
//          .filter(c -> c.name().equals(code))
//          .findFirst()
//          .orElseThrow(IllegalArgumentException::new);
//    }
//}