package br.edu.iffar.fw.classBag.db.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Map;

import org.apache.commons.csv.CSVPrinter;

import br.edu.iffar.fw.classBag.db.DAO;
import br.edu.iffar.fw.classBag.db.Model;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class RelatoriosDAO extends DAO<Model<?>>{

	
	private static final long serialVersionUID = 22021991L;

	@Inject Connection connection;

	public void listAllAgendamentoFrustrads(CSVPrinter printer, LocalDate dtInicio, LocalDate dtFim,Map<String, Object> where) {
		try {
//			CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Nome","Tipo Refeição","Grupo", "Curso"));
			String and = where.get("where_adicional").toString() + where.get("where_tipo_refeicao");
			PreparedStatement stm = connection.prepareStatement("""
					select a.dt_agendamento,u.nome ,tr.descricao, gr.sigla,coalesce(c2.nome,tvs.descricao) as cur_vin from agendamentos a
					join refeicao r ON r.refeicao_id =a.refeicao_id
					join tipo_refeicao tr on tr.tipo_refeicao_id =r.tipo_refeicao_id
					join grupo_refeicoes gr on gr.grupo_refeicoes_id = r.grupo_refeicoes_id 
					left join matricula m on m.matricula_id = a.matricula_id 
					left join cursos c2 on c2.curso_id =m.curso_id
					left join servidor s ON s.servidor_id = a.servidor_id 
					left join tipo_vinculo tvS on tvS.tipo_vinculo_id = s.tipo_vinculo_id
					left join tipo_vinculo tvM on tvM.tipo_vinculo_id = m.tipo_vinculo_id 
					left join usuarios u ON u.usuario_id = m.usuario_id or u.usuario_id = s.usuario_id 
					where a.credito_id isnull
					and a.dt_agendamento between ? and ?
					"""
					+and+
					"order by a.dt_agendamento,tr.descricao,4,u.nome");
			stm.setDate(1, Date.valueOf(dtInicio));
			stm.setDate(2, Date.valueOf(dtFim));

			ResultSet rs = stm.executeQuery();

//			while (rs.next()) {
			printer.printRecords(rs);

//			}

			stm.close();
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
