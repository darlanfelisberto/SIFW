select c.titulo,string_agg(i.nome,', ') from cursos c 
left join curso_instrutor ci on ci.id_curso = c.id_curso 
left join instrutor i on ci.id_instrutor =i.id_instrutor 
group by c.titulo 