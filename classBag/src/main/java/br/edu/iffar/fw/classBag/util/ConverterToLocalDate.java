//package br.edu.iffar.fw.classBag.util;
//
//import java.time.LocalDate;
//
//import javax.faces.component.UIComponent;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.DateTimeConverter;
//import javax.faces.convert.FacesConverter;
//
//@FacesConverter(value = "ConverterToLocalDate")
//public class ConverterToLocalDate extends DateTimeConverter {
//    public static final String ID = "com.example.LocalDateConverter";
//
//    @Override
//    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
//        Object o = super.getAsObject(facesContext, uiComponent, value);
//
//        if (o == null) {
//            return null;
//        }
//
//        if (o instanceof LocalDate) {
//            Instant instant = Instant.ofEpochMilli(((Date) o).getTime());
//            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
//        } else {
//            throw new IllegalArgumentException(String.format("value=%s could not be converted to a LocalDate, result super.getAsObject=%s", value, o));
//        }
//    }
//
//    @Override
//    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
//        if (value == null) {
//            return super.getAsString(facesContext, uiComponent,value);
//        }
//
//        if (value instanceof LocalDate) {
//            LocalDate lDate = (LocalDate) value;
//            Instant instant = lDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
//            Date date = Date.from(instant);
//            return super.getAsString(facesContext, uiComponent, date);
//        } else {
//            throw new IllegalArgumentException(String.format("value=%s is not a instanceof LocalDate", value));
//        }
//    }
//}