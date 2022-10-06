select sum(saldo) as saldo from (
	select sum(c.valor) as saldo from creditos c where c.tipo_credito = type_credito('ENTRADA')
	union 
	select sum(c.valor) *(-1) as saldo from creditos c where c.tipo_credito in ('SAIDA','TRANS')
)as c

select u.usuario_id from usuarios u where u.username_keycloak = 

INSERT INTO public.log (tupla_id, table, operacao,  dt_log)VALUES('8c510083-ba59-4574-8206-588c37f52481', br.edu.iffar.fw.comendo.db.model.Usuario, 'onSave',  CURRENT_TIMESTAMP)


update cursos set dt_aber_insc = '2020-05-25 00:00:00' ,dt_term_incri  = '2020-06-04 22:44:59' where id_curso  <>46



SELECT EXTRACT(DOW FROM current_timestamp);


 tipo_refeicao_id,count(usuario_id)

 + (6 - (EXTRACT(DOW FROM current_timestamp))::int)


select a.*, p.*from agendamentos a
cross join (select current_date - (EXTRACT(DOW FROM current_timestamp))::int as first, current_date+ (6 - (EXTRACT(DOW FROM current_timestamp))::int)  as last) as p
where 
a.dt_fim >=p.first and a.dt_inicio <= p.last  
group by tipo_refeicao_id



with pe as (
	select current_date - (EXTRACT(DOW FROM current_timestamp))::int as first, current_date+ (6 - (EXTRACT(DOW FROM current_timestamp))::int)  as last
)

select dia,	coalesce(
		(select count(a.usuario_id) from agendamentos a  where a.dt_inicio >=foo.dia and  foo.dia>= a.dt_fim and a.tipo_refeicao_id = 'ALMOCO' group by a.usuario_id)
	,0) as total
from(
	select generate_series(current_date - (EXTRACT(DOW FROM current_timestamp))::int,current_date+ (6 - (EXTRACT(DOW FROM current_timestamp))::int) , '1 day') as dia
) as foo



select * from agendamentos a 
select generate_series('2021-04-04'::date ,'2021-04-10'::date  , '1 day')


select dia,	coalesce((select count(a.usuario_id) from agendamentos a  
where 
(a.dt_inicio <=foo.dia and a.dt_fim>=foo.dia)
group by a.usuario_id),0)
from(
	select generate_series('2021-04-04'::date ,'2021-04-10'::date  , '1 day') as dia
) as foo

s


inicio = '2021-04-07 00:00:00'::timestamp

fim    = '2021-04-09 23:59:00'::timestamp

select * from agendamentos a where 
--data de inicio 
(a.dt_inicio<=:inicio and :inicio <=a.dt_fim )
or
(:fim >=a.dt_inicio and :fim <=a.dt_fim)
or
(:inicio<=a.dt_inicio and :fim >= a.dt_fim)


--and a.dt_fim>='2021-04-07 23:59:00'::timestamp
update usuarios set token_ru = uuid_generate_v4() where email ='s'
a100f197-f35f-4590-bb95-0442325affaa

select u.*,coalesce(i.file_name,'semfoto') as file_name from usuarios u 
left join imagen i on i.usuario_id =u.usuario_id 

select * from agendamentos a 


select count(a.usuario_id)  from agendamentos a  
where 
--(a.dt_inicio <=foo.dia and a.dt_fim>=foo.dia) and 
a.tipo_refeicao_id = 'ALMOCO' group by a.tipo_refeicao_id 



select dia,	coalesce(
(select count(a.usuario_id)  from agendamentos a  where (a.dt_inicio <=foo.dia and a.dt_fim>=foo.dia) and a.tipo_refeicao_id = 'ALMOCO' group by a.tipo_refeicao_id)
,0) as total
from(
 	select generate_series(cast('2021-04-11' as date) , cast('2021-04-17' as date)  , '1 day') as dia
 ) as foo

 
 
 

 
 
 
 