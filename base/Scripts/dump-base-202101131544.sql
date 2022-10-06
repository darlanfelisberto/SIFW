

CREATE TABLE public.usuarios (
    usuario_id uuid NOT NULL default uuid_generate_v4(),
    senha varchar(255),
    username_keycloak varchar(11) not null,
    primary key (usuario_id)
);

COMMENT ON COLUMN public.usuarios.senha IS 'nao usado no keycloak';
alter  table public.usuarios add CONSTRAINT unique_uk  UNIQUE (username_keycloak);

create type type_credito as enum ('ENTRADA','SAIDA','TRANS');
create type type_refeicao as enum ('CAFE','ALMOCO','JANTA');
--ALTER TYPE type_credito ADD VALUE 'TRANS';

create table public.creditos(
	credito_id uuid not null default uuid_generate_v4(),
	usuario_id uuid not null,
	valor numeric(4,2) not null,
	tipo_credito type_credito not null default 'ENTRADA',
	tipo_refeicao tipo_refeicao not null default 'ALMOCO',
	dt_credito timestamp not null default current_timestamp,
	primary key (credito_id),
	CONSTRAINT u_c_fk FOREIGN KEY (usuario_id) REFERENCES public.usuarios(usuario_id)
);

--alter table creditos add column tipo_refeicao type_refeicao not null default 'ALMOCO';





truncate usuarios cascade  


select cpf_string,nome ,email, matricula from alunos a

select * from creditos credito0_ inner join public.usuarios usuario1_ on credito0_.usuario_id=usuario1_.usuario_id 
where usuario1_.usuario_id='f2c814d2-c866-450c-94cb-f34928cbc66e'


select * from usuarios u 


select sum(saldo) as saldo from (
	select sum(c.valor) as saldo from creditos c 
	inner join usuarios u2 on c.usuario_id = u2.usuario_id 
	where c.tipo_credito = 'ENTRADA' and u2.username_keycloak = 'teste' 
	union 
	select sum(c.valor) *(-1) as saldo from creditos c 
	inner join usuarios u2 on c.usuario_id = u2.usuario_id 
	where c.tipo_credito in ('SAIDA','TRANS') and u2.username_keycloak = 'teste'
)as c


select * from agendamentos a where agendamento_id = '652d2632-c2de-4df7-bd91-62a6f287ab6a'



