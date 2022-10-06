select * from cursos c order by id_curso 

alter table cursos add dt_aber_insc timestamp;
alter table cursos add dt_term_incri timestamp;

update cursos set dt_aber_insc = '2020-05-25 00:00:00' ,dt_term_incri  = '2020-06-04 22:44:59' where id_curso  <>46

update cursos set ativo ='N' where id_curso  <>46



INSERT INTO public.cursos
(id_curso, titulo, ementa, duracao, carga_horaria, publico_alvo, requisitos, ativo, vagas, vagas_ociosas, id_categoria, data_inicio, dt_aber_insc, dt_term_insc)
VALUES(46, 'Atualização em Medicina de Felinos', 'O curso tem o objetivo de oferecer formação continuada sobre Medicina de Felinos aos Médicos Veterinários que atuam na Clínica de Pequenos Animais. Com o crescente aumento da população de gatos intradomicialiados, há uma procura cada vez maior, por parte dos tutores, pelo atendimento especializado desta espécie. Por esse motivo, já existem várias clínicas veterinárias no Brasil com a certificação Cat Friendly da American Association of Feline Practitioners (AAFP). 
O curso pretende abordar o manejo, o diagnóstico e o tratamento das doenças mais frequentes pelos felinos: 
Manejo Cat Friendly; 
Pecualiaridades terapêuticas; 
Pediatria; Geriatria; 
Nutrição e nutrição clínica; 
Patologia Clínica; 
Doenças hepatobiliares; 
Cardiologia; 
Urologia; 
Nefrologia; 
Doenças respiratórias; 
Parasitoses; 
Doenças virais (FIV/FeLV); 
Dermatologia e Oncologia.', 16, 40, 'Médicos Veterinários e estudantes de Medicina Veterinária', 'Apresentar identificação do Conselho de Medicina Veterinária, certificado de conclusão ou comprovante de matrícula na graduação de Medicina Veterinária. ',
'S', 30, 'N', 4, '2021-07-01', '2021-02-18 00:00:00.000', '2021-02-22 23:59:00.000');


INSERT INTO public.instrutor (id_instrutor,nome,link)
	VALUES (54,'Silvana Bellini Vidor','http://lattes.cnpq.br/2989313384686077');
INSERT INTO public.instrutor (id_instrutor,nome,link)
	VALUES (55,'Paulo Henrique Braz','http://lattes.cnpq.br/7696428399690860');
	
INSERT INTO public.curso_instrutor
(id_curso, id_instrutor)
VALUES(46, 54);
INSERT INTO public.curso_instrutor
(id_curso, id_instrutor)
VALUES(46, 55);

select * from inscritos i where cpf =02365495028 
