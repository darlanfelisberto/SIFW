package br.edu.iffar.fw.comendo.interceptor;

import java.io.Serializable;

import br.edu.iffar.fw.comendo.bean.fragment.VinculoSelecionadoBean;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@SelecionaVinculo
@Priority(Interceptor.Priority.PLATFORM_AFTER+1)
@Interceptor
public class SelecionaVinculoInterceptor  implements Serializable {

    @Inject
    VinculoSelecionadoBean vinculoSelecionadoBean;

    public SelecionaVinculoInterceptor() {
        System.out.println("Construror--------------------------------------------------------------------------------------------------------------------");
    }

    @AroundInvoke
    public Object manage(InvocationContext ctx) throws Exception {
        System.out.println("aqui--------------------------------------------------------------------------------------------------------------------");
        if(this.vinculoSelecionadoBean.isNecessarioSelecionarVinculo()){
            this.vinculoSelecionadoBean.redirect();
        }

        Object o = null;
        return o;
    }
    @AroundConstruct
    public Object construtor(InvocationContext ctx) throws Exception {
        if(this.vinculoSelecionadoBean.isNecessarioSelecionarVinculo()){
            this.vinculoSelecionadoBean.redirect();
        }
        System.out.println("aqui constritor-------------------------------------------------------------------------------------------------------------------");
        Object o = ctx.proceed();
        return "/app/selecionaVinculo.xhtml?faces-redirect=true&return=/sifw/app/agendamento.xhtml";
    }

}
