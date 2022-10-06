select  * from usuarios u 


select u.usuario_id,u.username_keycloak,u.token_ru,u.nome ,COALESCE(saldo.saldo,0) as saldo from usuarios u 
left join (

drop view saldo_usuario
create view saldo_usuario as select COALESCE(sum(c.valor*etc.mult),0) as saldo,c.usuario_id from creditos c 
inner join usuarios u2 on c.usuario_id = u2.usuario_id 
inner join enum_tipo_credito etc on c.tipo_credito = etc.tipo_credito_id 
group by c.usuario_id 


select * from saldo_usuario

select u.usuario_id,u.username_keycloak as username ,u.cpf, u.nome, u.token_ru ,su.saldo from usuarios u 
inner join saldo_usuario su on u.usuario_id = su.usuario_id 

CREATE OR REPLACE VIEW public.saldo_usuario AS 

	SELECT COALESCE(sum(c.valor * etc.mult::numeric), 0::numeric) AS saldo,
	    u2.usuario_id,''||u2.usuario_id||u2.cpf as saldo_usuario_id
	   FROM usuarios u2
	     left JOIN creditos c ON c.usuario_id = u2.usuario_id
	     left JOIN enum_tipo_credito etc ON c.tipo_credito::text = etc.tipo_credito_id::text
	  GROUP BY u2.usuario_id,u2.cpf

	  

CREATE TABLE usuarios (
	usuario_id uuid NOT NULL,
	cpf VARCHAR(255),
	dt_nasc DATE,
	email VARCHAR(255),
	nome VARCHAR(255),
	token_ru VARCHAR(255),
	username VARCHAR(255),
	PRIMARY KEY (usuario_id)
);


CREATE TABLE saldo (
	saldo_usuario_id VARCHAR(255) NOT NULL,
	saldo float,
	usuario_id uuid NOT NULL,
	PRIMARY KEY (saldo_usuario_id),
	FOREIGN KEY (usuario_id) references usuarios(usuario_id)
);
CREATE INDEX "FK569usc7ge4q6989vykyrn1qrm_INDEX_1" ON PUBLIC."saldo_usuario" ("usuario_id");
CREATE UNIQUE INDEX PRIMARY_KEY_1A ON PUBLIC."saldo_usuario" ("saldo_usuario_id");


CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE public.creditos (
	credito_id uuid NOT NULL DEFAULT uuid_generate_v4(),
	usuario_id uuid NOT NULL,
	valor numeric(4,2) NOT NULL,
	tipo_credito varchar(20) NULL ,
	dt_credito timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	tipo_refeicao varchar NULL,
	CONSTRAINT creditos_pkey PRIMARY KEY (credito_id),
	CONSTRAINT u_c_fk FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);




CREATE TABLE public.tipo_refeicao (
    tipo_refeicao_id character varying(20) NOT NULL,
    valor numeric(4,2)
);


ALTER TABLE public.tipo_refeicao OWNER TO postgres;

--
-- TOC entry 3066 (class 0 OID 28597)
-- Dependencies: 209
-- Data for Name: tipo_refeicao; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.tipo_refeicao VALUES ('ALMOCO', 2.50);
INSERT INTO public.tipo_refeicao VALUES ('CAFE_MANHA', 2.50);
INSERT INTO public.tipo_refeicao VALUES ('JANTA', 2.50);


--
-- TOC entry 2938 (class 2606 OID 28601)
-- Name: tipo_refeicao tipo_refeicao_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tipo_refeicao
    ADD CONSTRAINT tipo_refeicao_pk PRIMARY KEY (tipo_refeicao_id);

