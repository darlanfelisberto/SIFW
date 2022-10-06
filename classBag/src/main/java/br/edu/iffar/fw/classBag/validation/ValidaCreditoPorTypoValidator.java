package br.edu.iffar.fw.classBag.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.edu.iffar.fw.classBag.db.model.AltenacoesCreditos;
import br.edu.iffar.fw.classBag.db.model.Credito;
import br.edu.iffar.fw.classBag.enun.TypeCredito;

public class ValidaCreditoPorTypoValidator implements ConstraintValidator<ValidaCreditoPorTypo, Object> {

    private String typeCreditoPara;
    private String realizadoPorCredito;

    public void initialize(ValidaCreditoPorTypo constraintAnnotation) {
        this.typeCreditoPara = constraintAnnotation.typeCreditoPara();
        this.realizadoPorCredito = constraintAnnotation.realizadoPorCredito();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
    	
    	try {
    		Method getType = AltenacoesCreditos.class.getMethod(typeCreditoPara,null);
    		Method getCredito = AltenacoesCreditos.class.getMethod(realizadoPorCredito,null);
			TypeCredito tc = (TypeCredito) getType.invoke(value);
			Credito c = (Credito) getCredito.invoke(value);
			
			if(tc.equals(TypeCredito.TRANS_SAIDA) && c == null) {
				return false;
			}
			return true;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return false;
    }
}
