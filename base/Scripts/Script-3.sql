CREATE TABLE usuarios (
	usuario_id uuid NOT NULL,
	senha varchar(255) NULL,
	username varchar(11) NOT NULL,
	cpf varchar(11) NOT NULL,
	nome varchar(150) NOT NULL,
	dt_nasc date NOT NULL,
	email varchar(100) NOT NULL,
	token_ru varchar NOT NULL ,
	CONSTRAINT unique_uk UNIQUE (username),
	CONSTRAINT usuarios_pkey PRIMARY KEY (usuario_id),
	CONSTRAINT usuarios_un UNIQUE (cpf)
)

drop table saldo_usuario
create table saldo_usuario(
	saldo float not null,
	usuario_id varchar(50) not null,
	primary key(usuario_id),
	FOREIGN KEY(usuario_id) REFERENCES usuarios(usuario_id)
)

CREATE TABLE usuario_e_saldo(
	saldo float not null,
	usuario_id varchar(50) not null,
	username varchar(50) not null,
	cpf varchar(50) not null,
	nome varchar(50) not null,
	token_ru varchar(50) not null,
	primary key(usuario_id)
);

select CURRENT_TIMESTAMP

create table entradas(
	entrada_id varchar(50) not null,
	usuario_id varchar(50) not null,
	tipoRefeicao varchar(50) not null,
	dt timestamp DEFAULT CURRENT_TIMESTAMP,
	primary key(entrada_id)
);

