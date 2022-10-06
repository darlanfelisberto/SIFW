update cursos set ativo = 'N' where id_curso <>46




select distinct ca.id_categoria,ca.descricao, replace(ca.descricao,' ','_')as descricao_se 
from cursos c2  
inner join categorias ca on ca.id_categoria =c2.id_categoria 
where c2.ativo ='S'
order by ca.descricao asc


alter table cursos add dt_aber_insc timestamp;
alter table cursos add dt_term_insc timestamp;
update cursos  set dt_aber_insc ='2021-02-18 00:00:00' where id_curso =46
update cursos  set dt_term_insc ='2021-02-18 23:59:00' where id_curso =46
select current_timestamp 

select * from cursos c where id_curso =46