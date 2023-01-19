package br.edu.iffar.fw.classBag.util;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import br.auth.models.Permissao;
import br.edu.iffar.fw.classBag.db.Model;
import br.edu.iffar.fw.classBag.db.dao.ConverterModelDAO;
import br.edu.iffar.fw.classBag.db.model.Agendamento;
import br.edu.iffar.fw.classBag.db.model.AlteracoesAgendamentos;
import br.edu.iffar.fw.classBag.db.model.Cardapio;
import br.edu.iffar.fw.classBag.db.model.Cidade;
import br.edu.iffar.fw.classBag.db.model.Curso;
import br.edu.iffar.fw.classBag.db.model.Estado;
import br.edu.iffar.fw.classBag.db.model.GrupoRefeicoes;
import br.edu.iffar.fw.classBag.db.model.HabitanteUnidade;
import br.edu.iffar.fw.classBag.db.model.ItemUnidade;
import br.edu.iffar.fw.classBag.db.model.Matricula;
import br.edu.iffar.fw.classBag.db.model.Refeicao;
import br.edu.iffar.fw.classBag.db.model.Servidor;
import br.edu.iffar.fw.classBag.db.model.TipoCredito;
import br.edu.iffar.fw.classBag.db.model.TipoRefeicao;
import br.edu.iffar.fw.classBag.db.model.TipoUnidade;
import br.edu.iffar.fw.classBag.db.model.TipoVinculo;
import br.edu.iffar.fw.classBag.db.model.Turma;
import br.edu.iffar.fw.classBag.db.model.Unidade;
import br.edu.iffar.fw.classBag.db.model.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;
import jakarta.inject.Named;


@Named
//@FacesConverter(managed = true,value = "modelConverter")
@RequestScoped
@SuppressWarnings("rawtypes") 
public class ModelConverter implements Converter< Model> {
	
	private static HashMap<Integer, String> classes;
	static{
		classes = new HashMap<Integer, String>();
		
		classes.put(Agendamento.class.getName().hashCode(), Agendamento.class.getName());

		classes.put(AlteracoesAgendamentos.class.getName().hashCode(), AlteracoesAgendamentos.class.getName());
		
		classes.put(Cardapio.class.getName().hashCode(), Cardapio.class.getName());
		classes.put(Cidade.class.getName().hashCode(), Cidade.class.getName());
		classes.put(Curso.class.getName().hashCode(), Curso.class.getName());
		
		classes.put(Estado.class.getName().hashCode(), Estado.class.getName());
		
		classes.put(GrupoRefeicoes.class.getName().hashCode(), GrupoRefeicoes.class.getName());
		
		classes.put(HabitanteUnidade.class.getName().hashCode(), HabitanteUnidade.class.getName());

		classes.put(ItemUnidade.class.getName().hashCode(), ItemUnidade.class.getName());

		classes.put(Matricula.class.getName().hashCode(), Matricula.class.getName());

		classes.put(Refeicao.class.getName().hashCode(), Refeicao.class.getName());

		classes.put(Servidor.class.getName().hashCode(), Servidor.class.getName());

		classes.put(Permissao.class.getName().hashCode(), Permissao.class.getName());

		classes.put(TipoCredito.class.getName().hashCode(), TipoCredito.class.getName());
		classes.put(TipoUnidade.class.getName().hashCode(), TipoUnidade.class.getName());
		classes.put(TipoRefeicao.class.getName().hashCode(), TipoRefeicao.class.getName());
		classes.put(TipoVinculo.class.getName().hashCode(), TipoVinculo.class.getName());
		classes.put(Turma.class.getName().hashCode(), Turma.class.getName());
		
		classes.put(Unidade.class.getName().hashCode(), Unidade.class.getName());
		classes.put(Usuario.class.getName().hashCode(), Usuario.class.getName());
	}
	
	@Inject private ConverterModelDAO cmd;
	
	@Override 
	public Model getAsObject(FacesContext context, UIComponent component, String value) {
		
		
		if(value != null && !value.contains(Model.SEPARATIOR_KEY)) {
			return null;
		}
		
		try {
//			long antes = ZonedDateTime.now().toInstant().toEpochMilli();
			String[]name = value.split(Model.SEPARATIOR_KEY);

			Class<?> entity = Class.forName(classes.get(Integer.parseInt(name[0])));
			Class generic =  (Class) ((ParameterizedType)entity.getGenericSuperclass()).getActualTypeArguments()[0];
						
			Model m = (Model) cmd.findBd(entity,generic, name[1]);
			
//			long depois = ZonedDateTime.now().toInstant().toEpochMilli();
//			System.out.println("tempo de converção: " + (depois-antes) +" milisegundos");
			return m;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override 
	public String getAsString(FacesContext context, UIComponent component,Model value) {
		if(value == null) {
			return null;
		}
		return value.getKeyConverter();
	}

}
