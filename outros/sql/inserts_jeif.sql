--gera agendamentos para os alunos ativos
with re as (
	select r.refeicao_id,tr.descricao as descricao_tr ,gr.sigla,tv.descricao as descricao_tv  from refeicao r
	left join grupo_refeicoes gr on gr.grupo_refeicoes_id = r.grupo_refeicoes_id
	left join tipo_vinculo tv on tv.tipo_vinculo_id = gr.tipo_vinculo_id 
	left join tipo_refeicao tr on tr.tipo_refeicao_id = r.tipo_refeicao_id 
	where gr.sigla = 'JEIF'
), mat as(
	select m.matricula_id,u.username from matricula m 
	left join usuarios u on u.usuario_id = m.usuario_id 
	left join cursos c on c.curso_id  = m.curso_id 
	left join tipo_vinculo tv on tv.tipo_vinculo_id  = c.tipo_vinculo_id 
	inner join(
		select max(sm2.momento) as momento,sm2.matricula_id  from situacao_matricula sm2 
		group by sm2.matricula_id 
	) as foo on foo.matricula_id = m.matricula_id 
	inner join situacao_matricula sm on sm.matricula_id = m.matricula_id and sm.momento  = foo.momento
	where tv.nivel ='J' and sm.situacao = 'ATIVA'
)
INSERT INTO public.agendamentos(agendamento_id,editavel, refeicao_id, agendado, credito_id, dt_agendamento,observacao, matricula_id, servidor_id)
select uuid_generate_v4(), false, re.refeicao_id, true, NULL, dia::date, 'JEIF SCRPIT', mat.matricula_id, null from pg_catalog.generate_series('2023-06-14'::date, '2023-06-17'::date,'1 day') as dia
cross join re  
cross join mat
where (dia::date = '2023-06-14'::date and re.descricao_tr <> 'Café da manhã') 
	or (dia::date = '2023-06-15'::date) 
	or (dia::date = '2023-06-16'::date) 
	or (dia::date = '2023-06-17'::date and re.descricao_tr <> 'Janta')
order by 6,3,1


--deleta  agendamentos fora das datas e de inativos
DELETE FROM public.agendamentos AS adelete
USING agendamentos as a
left join refeicao r on r.refeicao_id = a.refeicao_id 
left join tipo_refeicao tr on tr.tipo_refeicao_id = r.tipo_refeicao_id 
left join matricula m on m.matricula_id = a.matricula_id 
left join usuarios u on u.usuario_id = m.usuario_id 
left join cursos c on c.curso_id  = m.curso_id 
left join tipo_vinculo tv on tv.tipo_vinculo_id  = c.tipo_vinculo_id 
inner join(
	select max(sm2.momento) as momento,sm2.matricula_id  from situacao_matricula sm2 
	group by sm2.matricula_id 
) as foo on foo.matricula_id = m.matricula_id 
inner join situacao_matricula sm on sm.matricula_id = m.matricula_id and sm.momento  = foo.momento
where tv.nivel ='J' and adelete.agendamento_id = a.agendamento_id
and(
	(
		(a.dt_agendamento::date = '2023-06-14'::date and tr.descricao = 'Café da manhã') or 
		(a.dt_agendamento::date = '2023-06-17'::date and tr.descricao = 'Janta') or 
		(a.dt_agendamento::date < '2023-06-14') or 
		(a.dt_agendamento::date > '2023-06-17')
	)
	or
	(
		sm.situacao = 'INATIVA'
	)
)


--inativa matriculas
INSERT INTO public.situacao_matricula(situacao_id, matricula_id, situacao, momento)
select uuid_generate_v4(), m.matricula_id,'INATIVA',CURRENT_TIMESTAMP  from matricula m
left join usuarios u on u.usuario_id = m.usuario_id 
inner join cursos c on c.curso_id  = m.curso_id 
inner join tipo_vinculo tv on tv.tipo_vinculo_id  = c.tipo_vinculo_id and tv.nivel ='J'
inner join(
	select max(sm2.momento) as momento,sm2.matricula_id  from situacao_matricula sm2 
	group by sm2.matricula_id 
) as foo on foo.matricula_id = m.matricula_id 
inner join situacao_matricula sm on sm.matricula_id = m.matricula_id and sm.momento  = foo.momento and sm.situacao = 'ATIVA'



