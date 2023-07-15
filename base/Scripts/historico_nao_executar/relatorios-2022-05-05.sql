ALTER TABLE public.tipo_vinculo DROP CONSTRAINT tipo_vinculo_un;

ALTER TABLE public.tipo_vinculo ALTER COLUMN tipo_vinculo_id SET DEFAULT uuid_generate_v4();

INSERT INTO public.tipo_vinculo (tipo_vinculo_id, descricao, id_tipo_vinculo, tipo_matricula, tipo_usuario, nivel) 
VALUES('15e2419d-38fe-4733-b436-f1c67b20c978'::uuid, 'Moradia(Integrado)', 11, true, false, 'N');

--altera o vinculo da matricula dos alunos
update matricula mu set tipo_vinculo_id = (select tv.tipo_vinculo_id  from tipo_vinculo tv where id_tipo_vinculo = 11) 
from moradia_estudantil.habitante_unidade hu
left join matricula m  on m.matricula_id = hu.matricula_id 
where mu.matricula_id = m.matricula_id;

--remore os aluno do vinculo manual com o grupo
delete from matricula_grupo where grupo_refeicoes_id  = (
select grupo_refeicoes_id  from grupo_refeicoes gr where sigla ='MOR'
);


--seta tipo vinculo automatico para o grupo moradia
update grupo_refeicoes set tipo_vinculo_id = (select tv.tipo_vinculo_id  from tipo_vinculo tv where id_tipo_vinculo = 11)   
where grupo_refeicoes_id  = (
select grupo_refeicoes_id  from grupo_refeicoes gr where sigla ='MOR'
)

--insere refeicao almoço no grupo
INSERT INTO public.refeicao (refeicao_id, bloquear, hora_inicio_find, hora_inicio_util, valor, grupo_refeicoes_id, tipo_refeicao_id, termino) VALUES
('d1f28984-10db-492f-b53a-b119bc43f342'::uuid, 18, '11:00:00', '11:00:00', 0.0, '83449540-76b6-4cb6-81ff-66b5712f8c35'::uuid, '35acf661-807c-4618-a7c4-b2e4257ab658'::uuid, 120);


--atualiza refeições de agendamentos anteriores dos alunos do novo vinculo
update agendamentos au set refeicao_id = nr.refeicao_id 
from agendamentos a 
left join refeicao r on r.refeicao_id = a.refeicao_id 
left join tipo_refeicao tr on tr.tipo_refeicao_id = r.tipo_refeicao_id 
left join usuarios u on u.usuario_id = a.usuario_id 
left join matricula m on m.usuario_id = u.usuario_id 
left join tipo_vinculo tv on tv.tipo_vinculo_id  = m.tipo_vinculo_id 
left join (
	select r.refeicao_id ,tr.tipo_refeicao_id,tr.descricao,tv.descricao  , gr.tipo_vinculo_id  from grupo_refeicoes gr 
	left join refeicao r ON r.grupo_refeicoes_id = gr.grupo_refeicoes_id 
	left join tipo_refeicao tr on tr.tipo_refeicao_id = r.tipo_refeicao_id 
	inner join tipo_vinculo tv on tv.tipo_vinculo_id  = gr.tipo_vinculo_id 
) as  nr on nr.tipo_refeicao_id = tr.tipo_refeicao_id and nr.tipo_vinculo_id = tv.tipo_vinculo_id 
where tv.id_tipo_vinculo = 11 and tr.descricao ='Almoço'
and a.agendamento_id = au.agendamento_id 
order by a.dt_agendamento desc,a.agendamento_id



