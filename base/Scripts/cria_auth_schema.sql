
CREATE SCHEMA auth;

CREATE TABLE auth.auth_login (
    auth_user_id uuid NOT NULL,
    state uuid NOT NULL,
    code uuid NOT NULL,
    cliente_id uuid,
    valido_ate timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    usado boolean DEFAULT true NOT NULL,
    auth_login_id uuid DEFAULT gen_random_uuid() NOT NULL,
    ativo boolean DEFAULT true NOT NULL
);

CREATE TABLE auth.auth_user (
    username character varying(30) NOT NULL,
    password character varying(500) NOT NULL,
    email character varying(100) NOT NULL,
    salt character varying(100) NOT NULL,
    auth_user_id uuid DEFAULT gen_random_uuid() NOT NULL,
    inativo boolean DEFAULT false NOT NULL
);

CREATE TABLE auth.auth_user_permissao (
    auth_user_id uuid NOT NULL,
    permissao_id uuid NOT NULL
);

CREATE TABLE auth.cliente (
    nome character varying(50) NOT NULL,
    redirecionamento character varying(100) NOT NULL,
    cliente_url character varying(100) NOT NULL,
    secret character varying(100) DEFAULT gen_random_uuid() NOT NULL,
    response_type character varying DEFAULT 'code'::character varying NOT NULL,
    cliente_id uuid DEFAULT gen_random_uuid() NOT NULL
);

CREATE TABLE auth.permissao (
    nome character varying(30) NOT NULL,
    permissao_id uuid DEFAULT gen_random_uuid() NOT NULL
);

INSERT INTO auth.auth_user VALUES ('02365495028', 'CHtWhreVIUC3R3wvyiqhJZ8iCb2fRZV/HQCBxIelr0tv7K0oar+Cn4PEA1hjq84fQ3mrtF2JKIDt9SvxE3IILA==', 'darlan.felisberto@iffarroupilha.edu.br', 'H57a/ti0h9f2ujVlrEcnbQ==', 'bf874c2e-85a7-4e92-93c6-f0b9b4370d15', false);

INSERT INTO auth.auth_user_permissao (auth_user_id,permissao_id) VALUES
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','1fc1d1f7-59f0-4bbb-a903-df39ec898891'),
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','463d740f-b0fe-48fe-a65a-4ca8673c7ca4'),
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','e945e9ee-53f6-418f-bc58-831a2dcb9574'),
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','ed6818c4-ee4a-4e31-96a2-b8bdea3efdd3'),
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','3e8f7ac5-4372-4691-80e1-c90a6bd91912'),
	 ('bf874c2e-85a7-4e92-93c6-f0b9b4370d15','b43c01af-875b-4c3a-83ab-cb669e9f940e');


INSERT INTO auth.cliente VALUES ('fw', 'http://localhost:8080/comendo', 'localhost:8080/comendo', '3fd70ff4-fe2b-47b6-a8a3-cd1cf281a937', 'CODE', '05d684a6-f47f-48eb-9089-f3dbddfc6ac4');
INSERT INTO auth.cliente VALUES ('angular', 'http://localhost:4200', 'http://localhost:4200', '3fd70ff4-fe2b-47b6-a8a3-cd1cf281a937', 'TOKEN', '1373b43d-76db-44ce-821c-f55d1e1dfa4f');
INSERT INTO auth.cliente VALUES ('sifw', 'http://localhost:8080/sifw', 'localhost:8080/sifw', '3fd70ff4-fe2b-47b6-a8a3-cd1cf281a937', 'CODE', '9009d6ad-bf60-4f1d-a429-a054cf775e8c');

INSERT INTO auth.permissao VALUES ('IFFAR_RU', '463d740f-b0fe-48fe-a65a-4ca8673c7ca4');
INSERT INTO auth.permissao VALUES ('IFFAR_ADMIN', '1fc1d1f7-59f0-4bbb-a903-df39ec898891');
INSERT INTO auth.permissao VALUES ('IFFAR_MORADIA', 'e945e9ee-53f6-418f-bc58-831a2dcb9574');
INSERT INTO auth.permissao VALUES ('IFFAR_RU_ADMIN', 'ed6818c4-ee4a-4e31-96a2-b8bdea3efdd3');
INSERT INTO auth.permissao VALUES ('IFFAR_RU_CREDITO', '3e8f7ac5-4372-4691-80e1-c90a6bd91912');
INSERT INTO auth.permissao VALUES ('IFFAR_RU_CATRACA', 'b43c01af-875b-4c3a-83ab-cb669e9f940e');


ALTER TABLE ONLY auth.auth_login
    ADD CONSTRAINT auth_login_pk PRIMARY KEY (auth_login_id);

ALTER TABLE ONLY auth.auth_user_permissao
    ADD CONSTRAINT auth_user_permissao_un UNIQUE (permissao_id, auth_user_id);

ALTER TABLE ONLY auth.auth_user
    ADD CONSTRAINT auth_user_pk PRIMARY KEY (auth_user_id);

ALTER TABLE ONLY auth.cliente
    ADD CONSTRAINT cliente_pk PRIMARY KEY (cliente_id);

ALTER TABLE ONLY auth.cliente
    ADD CONSTRAINT clientes_un UNIQUE (nome);

ALTER TABLE ONLY auth.permissao
    ADD CONSTRAINT permissao_pk PRIMARY KEY (permissao_id);

ALTER TABLE ONLY auth.permissao
    ADD CONSTRAINT permissao_un UNIQUE (nome);

ALTER TABLE ONLY auth.auth_user
    ADD CONSTRAINT usuario_un UNIQUE (username);

CREATE INDEX auth_login_usado_code_idx ON auth.auth_login USING btree (usado, code);

ALTER TABLE ONLY auth.auth_login
    ADD CONSTRAINT auth_login_aut_user_fk FOREIGN KEY (auth_user_id) REFERENCES auth.auth_user(auth_user_id);

ALTER TABLE ONLY auth.auth_login
    ADD CONSTRAINT auth_login_fk FOREIGN KEY (cliente_id) REFERENCES auth.cliente(cliente_id);

ALTER TABLE ONLY auth.auth_user_permissao
    ADD CONSTRAINT auth_user_permissao_1_fk FOREIGN KEY (auth_user_id) REFERENCES auth.auth_user(auth_user_id);

ALTER TABLE ONLY auth.auth_user_permissao
    ADD CONSTRAINT auth_user_permissao_fk FOREIGN KEY (permissao_id) REFERENCES auth.permissao(permissao_id);
   
ALTER TABLE public.usuarios ADD auth_user_id uuid NULL;
update public.usuarios set auth_user_id = 'bf874c2e-85a7-4e92-93c6-f0b9b4370d15' where cpf = '02365495028';
ALTER TABLE public.usuarios ADD CONSTRAINT usuarios_auth_user_fk FOREIGN KEY (auth_user_id) REFERENCES auth.auth_user(auth_user_id);


drop VIEW public.api_saldo;
drop VIEW public.api_usuarios;

CREATE OR REPLACE VIEW public.api_saldo
AS SELECT foo.usuario_id,
    foo.username,
    COALESCE(foo.sum, 0::numeric) AS saldo
   FROM ( SELECT u.usuario_id,
            au.username,
            sum(c.valor * tc.fator::numeric) AS sum
           FROM usuarios u
             LEFT JOIN creditos c ON u.usuario_id = c.usuario_id
             LEFT JOIN tipo_credito tc ON c.tipo_credito_id = tc.tipo_credito_id
             left join auth.auth_user au on au.auth_user_id = u.auth_user_id
          GROUP BY u.usuario_id, au.username) foo;
          

CREATE OR REPLACE VIEW public.api_usuarios
AS SELECT DISTINCT u.usuario_id,
    u.cpf,
    u.dt_nasc,
    u.nome,
    u.token_ru,
	au.username,
    true AS ativo
   FROM usuarios u
   	left join auth.auth_user au on au.auth_user_id = u.auth_user_id 
     JOIN matricula m ON m.usuario_id = u.usuario_id
     JOIN ( SELECT max(sm_1.momento::timestamp with time zone) AS momento,
            sm_1.matricula_id
           FROM situacao_matricula sm_1
          GROUP BY sm_1.matricula_id) foo ON foo.matricula_id = m.matricula_id
     JOIN situacao_matricula sm ON sm.matricula_id = m.matricula_id AND sm.momento::timestamp with time zone = foo.momento
  WHERE sm.situacao::text = 'ATIVA'::text
  ORDER BY u.nome;

 
 ALTER TABLE public.usuarios DROP COLUMN username;
 ALTER TABLE public.usuarios DROP COLUMN email;
 
