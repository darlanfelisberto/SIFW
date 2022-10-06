
select * from agendamentos a 
where 
(a.dt_inicio >='2021-04-04'::date and a.dt_fim<='2021-04-04'::date)
or
(a.dt_inicio <='2021-04-04'::date and a.dt_fim>='2021-04-04'::date)


select generate_series('2021-04-04'::date ,'2021-04-10'::date  , '1 day')


select dia,	coalesce(
(
select *  from agendamentos a where 
(a.dt_inicio <='2021-04-10'::date 
and a.dt_fim>='2021-04-10'::date)

//group by a.usuario_id),0)
from(
	select generate_series('2021-04-04'::date ,'2021-04-10'::date  , '1 day') as dia
) as foo