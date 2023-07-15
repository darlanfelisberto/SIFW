--cria colunas de matricula e servidor
ALTER TABLE public.agendamentos ADD matricula_id uuid NULL;
ALTER TABLE public.agendamentos ADD servidor_id uuid NULL;
ALTER TABLE public.agendamentos ADD CONSTRAINT agendamentos_fk FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);
ALTER TABLE public.agendamentos ADD CONSTRAINT agendamentos_fk_1 FOREIGN KEY (servidor_id) REFERENCES public.servidor(servidor_id);


--preenche os dados de matricula e servidor
update agendamentos au set matricula_id = m.matricula_id  from agendamentos a
left join matricula m on m.usuario_id =a.usuario_id 
where a.agendamento_id  = au.agendamento_id 

update agendamentos au set servidor_id  = s.servidor_id  from agendamentos a
left join servidor s on s.usuario_id = a.usuario_id 
where a.agendamento_id  = au.agendamento_id


--remoção da coluna usuario
ALTER TABLE public.agendamentos DROP CONSTRAINT fklvnidyf53gkjeqkwwv6sxynpf;
ALTER TABLE public.agendamentos DROP CONSTRAINT agendamentos_un;
ALTER TABLE public.agendamentos DROP COLUMN usuario_id;

ALTER TABLE public.agendamentos ADD CONSTRAINT agendamentos_check CHECK ((matricula_id is null and servidor_id is not null) or (matricula_id is not null and servidor_id is null));

CREATE SCHEMA log;


CREATE TABLE log.leituras (
	leitura_id uuid NOT NULL DEFAULT uuid_generate_v4(),
	qrcode varchar NOT NULL,
	usuario_lido_id uuid NULL,
	usuario_operador_id uuid NOT NULL,
	dt_leitura timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE log.leituras ADD CONSTRAINT leituras_pk PRIMARY KEY (leitura_id);

delete from tipo_refeicao where tipo_refeicao_id  = 'e7a81561-e496-46ca-acfc-231f533c41fd'



