--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: log; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA log;


--
-- Name: moradia_estudantil; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA moradia_estudantil;


--
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;


--
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';


--
-- Name: uuid-ossp; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;


--
-- Name: EXTENSION "uuid-ossp"; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';


--
-- Name: move_foto_turma(integer, integer, integer, integer); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION public.move_foto_turma(ano_origem integer, ano_destino integer, turma_origem integer, turma_destino integer) RETURNS TABLE(ano_origem_ret integer, ano_destino_ret integer, turma_origem_ret integer, turma_destino_ret integer, matricula_id_ret uuid, turma_id_destino uuid)
    LANGUAGE plpgsql
    AS $$
DECLARE
query TEXT;
  row RECORD;
  turma_id_origem uuid;
BEGIN
select turma_id into turma_id_origem from turma where ano=ano_origem and numero = turma_origem;
select turma_id into turma_id_destino from turma where ano=ano_destino and numero = turma_destino;

query := 'select matricula_id from matricula_turma where turma_id = ''' || turma_id_origem || ''';';
FOR row IN EXECUTE query LOOP
  	ano_origem_ret = ano_origem;
	ano_destino_ret = ano_destino;
    turma_origem_ret = turma_origem;
	turma_destino_ret = turma_destino;
    matricula_id_ret = row.matricula_id;

insert into matricula_turma(matricula_id,turma_id) values (matricula_id_ret, turma_id_destino);

return next;
END LOOP;
END
$$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: leituras; Type: TABLE; Schema: log; Owner: -
--

CREATE TABLE log.leituras (
                              leitura_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                              qrcode character varying NOT NULL,
                              usuario_lido_id uuid,
                              usuario_operador_id uuid NOT NULL,
                              dt_leitura timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: habitante_unidade; Type: TABLE; Schema: moradia_estudantil; Owner: -
--

CREATE TABLE moradia_estudantil.habitante_unidade (
                                                      habitante_unidade_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                                      unidade_id uuid NOT NULL,
                                                      matricula_id uuid NOT NULL,
                                                      dt_entrada date NOT NULL,
                                                      dt_saida date
);


--
-- Name: item_unidade; Type: TABLE; Schema: moradia_estudantil; Owner: -
--

CREATE TABLE moradia_estudantil.item_unidade (
                                                 item_unidade_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                                 descricao character varying(100) NOT NULL,
                                                 unidade_id uuid NOT NULL,
                                                 patrimonio integer
);


--
-- Name: presenca; Type: TABLE; Schema: moradia_estudantil; Owner: -
--

CREATE TABLE moradia_estudantil.presenca (
                                             presenca_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                             habitante_unidade_id uuid NOT NULL,
                                             realizada_por_id uuid NOT NULL,
                                             dt_referencia date NOT NULL,
                                             dt_realizada timestamp without time zone NOT NULL,
                                             justificativa character varying(255),
                                             presente boolean NOT NULL
);


--
-- Name: tipo_unidade; Type: TABLE; Schema: moradia_estudantil; Owner: -
--

CREATE TABLE moradia_estudantil.tipo_unidade (
                                                 tipo_unidade_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                                 descricao character varying(100) NOT NULL,
                                                 tipo character varying(20)
);


--
-- Name: unidade; Type: TABLE; Schema: moradia_estudantil; Owner: -
--

CREATE TABLE moradia_estudantil.unidade (
                                            unidade_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                            descricao character varying(100) NOT NULL,
                                            seq integer NOT NULL,
                                            parent_id uuid,
                                            tipo_unidade_id uuid
);


--
-- Name: unidade_seq_seq; Type: SEQUENCE; Schema: moradia_estudantil; Owner: -
--

CREATE SEQUENCE moradia_estudantil.unidade_seq_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: unidade_seq_seq; Type: SEQUENCE OWNED BY; Schema: moradia_estudantil; Owner: -
--

ALTER SEQUENCE moradia_estudantil.unidade_seq_seq OWNED BY moradia_estudantil.unidade.seq;


--
-- Name: agendamentos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.agendamentos (
                                     agendamento_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                     editavel boolean,
                                     refeicao_id uuid NOT NULL,
                                     agendado boolean DEFAULT true,
                                     credito_id uuid,
                                     dt_agendamento date NOT NULL,
                                     observacao character varying(50),
                                     matricula_id uuid,
                                     servidor_id uuid,
                                     CONSTRAINT agendamentos_check CHECK ((((matricula_id IS NULL) AND (servidor_id IS NOT NULL)) OR ((matricula_id IS NOT NULL) AND (servidor_id IS NULL))))
);


--
-- Name: agendamentos_disponibilizados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.agendamentos_disponibilizados (
                                                      agendamento_disponibilizado_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                                      dt_disponibilizado timestamp without time zone NOT NULL,
                                                      usuario_disponibilizou_id uuid NOT NULL,
                                                      agendamento_id uuid,
                                                      usuario_pegou_id uuid,
                                                      sincronizado timestamp(0) without time zone
);


--
-- Name: altenacoes_creditos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.altenacoes_creditos (
                                            altenacoes_creditos_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                            para_id uuid NOT NULL,
                                            para_credito_id uuid NOT NULL,
                                            realizado_por_id uuid NOT NULL,
                                            realizado_por_credito_id uuid
);


--
-- Name: alteracoes_agendamentos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.alteracoes_agendamentos (
                                                alteracoes_agendamentos_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                                usuario_origem_id uuid NOT NULL,
                                                usuario_destino_id uuid NOT NULL,
                                                agendamento_id uuid NOT NULL,
                                                aceito boolean,
                                                dt_transferencia timestamp without time zone NOT NULL,
                                                dt_aceite timestamp without time zone
);


--
-- Name: grupo_refeicoes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.grupo_refeicoes (
                                        grupo_refeicoes_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                        descricao character varying(255) NOT NULL,
                                        tipo_vinculo_id uuid,
                                        sigla character varying DEFAULT 'sigla'::character varying NOT NULL
);


--
-- Name: refeicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.refeicao (
                                 refeicao_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                 bloquear integer NOT NULL,
                                 hora_inicio_find time(0) without time zone NOT NULL,
                                 hora_inicio_util time(0) without time zone NOT NULL,
                                 valor real NOT NULL,
                                 grupo_refeicoes_id uuid NOT NULL,
                                 tipo_refeicao_id uuid NOT NULL,
                                 termino integer
);


--
-- Name: COLUMN refeicao.bloquear; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.refeicao.bloquear IS 'tem em horas, para bloquear o cadastro e remoção de agendamentos';


--
-- Name: COLUMN refeicao.termino; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.refeicao.termino IS 'tempo em minitos, para o termino do servimento das refeições';


--
-- Name: tipo_refeicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_refeicao (
                                      tipo_refeicao_id uuid NOT NULL,
                                      descricao character varying(255),
                                      style_class character varying(255),
                                      background_color character varying(9) DEFAULT '#0d89ec'::character varying,
                                      cor_fonte character varying,
                                      hora_inicio time(0) without time zone,
                                      hora_fim time(0) without time zone
);


--
-- Name: COLUMN tipo_refeicao.background_color; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.tipo_refeicao.background_color IS 'em hexa';


--
-- Name: tipo_vinculo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_vinculo (
                                     tipo_vinculo_id uuid NOT NULL,
                                     descricao character varying(255),
                                     id_tipo_vinculo integer,
                                     tipo_matricula boolean,
                                     tipo_usuario boolean,
                                     nivel character(1) DEFAULT 8 NOT NULL
);


--
-- Name: api_refeicao; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.api_refeicao AS
SELECT r.refeicao_id,
       r.bloquear,
       r.hora_inicio_find,
       r.hora_inicio_util,
       r.valor,
       r.grupo_refeicoes_id,
       r.tipo_refeicao_id,
       g.descricao AS descricao_grupo,
       g.sigla
FROM (((public.refeicao r
    JOIN public.tipo_refeicao tr ON ((r.tipo_refeicao_id = tr.tipo_refeicao_id)))
    JOIN public.grupo_refeicoes g ON ((r.grupo_refeicoes_id = g.grupo_refeicoes_id)))
    LEFT JOIN public.tipo_vinculo tv ON ((g.tipo_vinculo_id = tv.tipo_vinculo_id)))
ORDER BY r.refeicao_id;


--
-- Name: creditos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.creditos (
                                 credito_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                 dt_credito timestamp without time zone NOT NULL,
                                 tipo_refeicao character varying(255),
                                 usuario_id uuid NOT NULL,
                                 tipo_credito_id character varying(20) NOT NULL,
                                 sincronizado timestamp without time zone,
                                 parent_id uuid,
                                 valor numeric(6,2) NOT NULL
);


--
-- Name: COLUMN creditos.parent_id; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.creditos.parent_id IS '''para referenciar transferencias''';


--
-- Name: tipo_credito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_credito (
                                     style_class character varying(255),
                                     tipo_credito_id character varying(255) NOT NULL,
                                     fator smallint NOT NULL,
                                     descricao character varying(30) NOT NULL,
                                     cor character varying(7) DEFAULT '#000'::character varying NOT NULL
);


--
-- Name: usuarios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuarios (
                                 usuario_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                 cpf character varying(255) NOT NULL,
                                 dt_nasc date NOT NULL,
                                 email character varying(255) NOT NULL,
                                 nome character varying(255) NOT NULL,
                                 token_ru character varying(255) DEFAULT public.uuid_generate_v4(),
                                 username character varying(255) NOT NULL
);


--
-- Name: api_saldo; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.api_saldo AS
SELECT usuario_id,
       username,
       COALESCE(sum, (0)::numeric) AS saldo
FROM ( SELECT u.usuario_id,
              u.username,
              sum((c.valor * (tc.fator)::numeric)) AS sum
       FROM ((public.usuarios u
           LEFT JOIN public.creditos c ON ((u.usuario_id = c.usuario_id)))
           LEFT JOIN public.tipo_credito tc ON (((c.tipo_credito_id)::text = (tc.tipo_credito_id)::text)))
       GROUP BY u.usuario_id, u.username) foo;


--
-- Name: matricula; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.matricula (
                                  matricula_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                  matricula integer NOT NULL,
                                  tipo_vinculo_id uuid NOT NULL,
                                  usuario_id uuid NOT NULL,
                                  curso_id uuid NOT NULL
);


--
-- Name: matricula_grupo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.matricula_grupo (
                                        grupo_refeicoes_id uuid NOT NULL,
                                        matricula_id uuid NOT NULL
);


--
-- Name: servidor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.servidor (
                                 servidor_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                 tipo_vinculo_id uuid NOT NULL,
                                 usuario_id uuid NOT NULL,
                                 siape integer,
                                 situacao character varying(10) NOT NULL
);


--
-- Name: COLUMN servidor.situacao; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.servidor.situacao IS '"Ativo, Inativo"';


--
-- Name: api_usuario_refeicao; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.api_usuario_refeicao AS
SELECT COALESCE(COALESCE(m2.usuario_id, m.usuario_id), s.usuario_id) AS usuario_id,
       r.refeicao_id,
       COALESCE(m.matricula_id, m2.matricula_id) AS matricula_id,
       s.servidor_id,
       true AS ativa
FROM (((((((public.refeicao r
    JOIN public.tipo_refeicao tr ON ((r.tipo_refeicao_id = tr.tipo_refeicao_id)))
    JOIN public.grupo_refeicoes g ON ((r.grupo_refeicoes_id = g.grupo_refeicoes_id)))
    LEFT JOIN public.tipo_vinculo tv ON ((g.tipo_vinculo_id = tv.tipo_vinculo_id)))
    LEFT JOIN public.matricula_grupo mg ON ((g.grupo_refeicoes_id = mg.grupo_refeicoes_id)))
    LEFT JOIN public.matricula m ON ((mg.matricula_id = m.matricula_id)))
    LEFT JOIN public.servidor s ON ((s.tipo_vinculo_id = tv.tipo_vinculo_id)))
    LEFT JOIN public.matricula m2 ON ((m2.tipo_vinculo_id = tv.tipo_vinculo_id)))
ORDER BY COALESCE(COALESCE(m2.usuario_id, m.usuario_id), s.usuario_id), r.valor;


--
-- Name: situacao_matricula; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.situacao_matricula (
                                           situacao_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                           matricula_id uuid NOT NULL,
                                           situacao character varying(7) DEFAULT 'ATIVA'::character varying NOT NULL,
                                           momento information_schema.time_stamp DEFAULT CURRENT_TIMESTAMP NOT NULL
);


--
-- Name: api_usuarios; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.api_usuarios AS
SELECT DISTINCT u.usuario_id,
                u.cpf,
                u.dt_nasc,
                u.email,
                u.nome,
                u.token_ru,
                u.username,
                true AS ativo
FROM (((public.usuarios u
    JOIN public.matricula m ON ((m.usuario_id = u.usuario_id)))
    JOIN ( SELECT max((sm_1.momento)::timestamp with time zone) AS momento,
                  sm_1.matricula_id
           FROM public.situacao_matricula sm_1
           GROUP BY sm_1.matricula_id) foo ON ((foo.matricula_id = m.matricula_id)))
    JOIN public.situacao_matricula sm ON (((sm.matricula_id = m.matricula_id) AND ((sm.momento)::timestamp with time zone = foo.momento))))
  WHERE ((sm.situacao)::text = 'ATIVA'::text)
  ORDER BY u.nome;


--
-- Name: cardapio; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cardapio (
                                 cardapio_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                                 dt_inicio date NOT NULL,
                                 dt_fim date NOT NULL,
                                 segunda text,
                                 terca text,
                                 quarta text,
                                 quinta text,
                                 sexta text,
                                 sabado text,
                                 domingo text
);


--
-- Name: cidades; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cidades (
                                id_cidade integer NOT NULL,
                                descricao character varying NOT NULL,
                                id_estado integer NOT NULL
);


--
-- Name: cursos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cursos (
                               curso_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                               id_curso integer,
                               id_unidade integer,
                               codigo character varying,
                               nome character varying NOT NULL,
                               nivel character(1) NOT NULL,
                               id_modalidade_educacao integer,
                               id_cidade integer NOT NULL,
                               id_tipo_oferta_curso integer,
                               id_area_curso integer,
                               id_grau_academico integer,
                               id_eixo_conhecimento integer,
                               ativo integer,
                               tipo_vinculo_id uuid
);


--
-- Name: estados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.estados (
                                id_estado integer NOT NULL,
                                descricao character varying NOT NULL,
                                sigla character varying NOT NULL
);


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: imagen; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.imagen (
                               imagen_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                               file_name character varying(255),
                               usuario_id uuid
);


--
-- Name: log; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.log (
                            dt_log timestamp without time zone,
                            tupla_id uuid,
                            operacao character varying(255),
                            entidade character varying(255),
                            log_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                            usuario_id character varying(255)
);


--
-- Name: matricula_turma; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.matricula_turma (
                                        turma_id uuid NOT NULL,
                                        matricula_id uuid NOT NULL
);


--
-- Name: parametros; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.parametros (
                                   parametro_id character varying(150) NOT NULL,
                                   valor character varying(150) NOT NULL
);


--
-- Name: sincronizado; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sincronizado (
                                     sincronizado_id uuid NOT NULL,
                                     dt_sincronizado timestamp without time zone,
                                     tipo_sincronizacao character varying(255)
);


--
-- Name: turma; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.turma (
                              turma_id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
                              ano integer NOT NULL,
                              descricao character varying(150),
                              numero integer NOT NULL,
                              curso_id uuid NOT NULL
);


--
-- Name: unidade seq; Type: DEFAULT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.unidade ALTER COLUMN seq SET DEFAULT nextval('moradia_estudantil.unidade_seq_seq'::regclass);


--
-- Name: leituras leituras_pk; Type: CONSTRAINT; Schema: log; Owner: -
--

ALTER TABLE ONLY log.leituras
    ADD CONSTRAINT leituras_pk PRIMARY KEY (leitura_id);


--
-- Name: habitante_unidade habitante_unidade_pk; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.habitante_unidade
    ADD CONSTRAINT habitante_unidade_pk PRIMARY KEY (habitante_unidade_id);


--
-- Name: item_unidade item_unidade_pk; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.item_unidade
    ADD CONSTRAINT item_unidade_pk PRIMARY KEY (item_unidade_id);


--
-- Name: presenca presenca_pk; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.presenca
    ADD CONSTRAINT presenca_pk PRIMARY KEY (presenca_id);


--
-- Name: presenca presenca_un; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.presenca
    ADD CONSTRAINT presenca_un UNIQUE (dt_referencia, habitante_unidade_id);


--
-- Name: tipo_unidade tipo_unidade_pk; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.tipo_unidade
    ADD CONSTRAINT tipo_unidade_pk PRIMARY KEY (tipo_unidade_id);


--
-- Name: unidade unidade_pk; Type: CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.unidade
    ADD CONSTRAINT unidade_pk PRIMARY KEY (unidade_id);


--
-- Name: agendamentos_disponibilizados agendamentos_disponibilizados_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT agendamentos_disponibilizados_pkey PRIMARY KEY (agendamento_disponibilizado_id);


--
-- Name: agendamentos_disponibilizados agendamentos_disponibilizados_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT agendamentos_disponibilizados_un UNIQUE (agendamento_id, usuario_disponibilizou_id);


--
-- Name: agendamentos agendamentos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT agendamentos_pkey PRIMARY KEY (agendamento_id);


--
-- Name: altenacoes_creditos altenacoes_creditos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.altenacoes_creditos
    ADD CONSTRAINT altenacoes_creditos_pkey PRIMARY KEY (altenacoes_creditos_id);


--
-- Name: alteracoes_agendamentos alteracoes_agendamentos_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alteracoes_agendamentos
    ADD CONSTRAINT alteracoes_agendamentos_pk PRIMARY KEY (alteracoes_agendamentos_id);


--
-- Name: cardapio cardapio_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cardapio
    ADD CONSTRAINT cardapio_pk PRIMARY KEY (cardapio_id);


--
-- Name: cidades cidades_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cidades
    ADD CONSTRAINT cidades_pk PRIMARY KEY (id_cidade);


--
-- Name: creditos creditos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT creditos_pkey PRIMARY KEY (credito_id);


--
-- Name: cursos cursos_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cursos
    ADD CONSTRAINT cursos_pk PRIMARY KEY (curso_id);


--
-- Name: cursos cursos_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cursos
    ADD CONSTRAINT cursos_un UNIQUE (id_curso);


--
-- Name: estados estados_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.estados
    ADD CONSTRAINT estados_pk PRIMARY KEY (id_estado);


--
-- Name: estados estados_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.estados
    ADD CONSTRAINT estados_un UNIQUE (sigla);


--
-- Name: grupo_refeicoes grupo_refeicoes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grupo_refeicoes
    ADD CONSTRAINT grupo_refeicoes_pkey PRIMARY KEY (grupo_refeicoes_id);


--
-- Name: imagen imagen_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagen
    ADD CONSTRAINT imagen_pkey PRIMARY KEY (imagen_id);


--
-- Name: imagen imagen_usuario_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagen
    ADD CONSTRAINT imagen_usuario_un UNIQUE (usuario_id);


--
-- Name: matricula_grupo matricula_grupo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_grupo
    ADD CONSTRAINT matricula_grupo_pkey PRIMARY KEY (grupo_refeicoes_id, matricula_id);


--
-- Name: matricula matricula_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula
    ADD CONSTRAINT matricula_pkey PRIMARY KEY (matricula_id);


--
-- Name: matricula_turma matricula_turma_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_turma
    ADD CONSTRAINT matricula_turma_pkey PRIMARY KEY (turma_id, matricula_id);


--
-- Name: parametros parametros_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.parametros
    ADD CONSTRAINT parametros_pkey PRIMARY KEY (parametro_id);


--
-- Name: refeicao refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refeicao
    ADD CONSTRAINT refeicao_pkey PRIMARY KEY (refeicao_id);


--
-- Name: servidor servidor_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.servidor
    ADD CONSTRAINT servidor_pkey PRIMARY KEY (servidor_id);


--
-- Name: sincronizado sincronizado_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sincronizado
    ADD CONSTRAINT sincronizado_pkey PRIMARY KEY (sincronizado_id);


--
-- Name: situacao_matricula situacao_id_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.situacao_matricula
    ADD CONSTRAINT situacao_id_pk PRIMARY KEY (situacao_id);


--
-- Name: situacao_matricula situacao_matricula_data_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.situacao_matricula
    ADD CONSTRAINT situacao_matricula_data_un UNIQUE (matricula_id, situacao, momento);


--
-- Name: tipo_credito tipo_credito_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_credito
    ADD CONSTRAINT tipo_credito_pk PRIMARY KEY (tipo_credito_id);


--
-- Name: tipo_refeicao tipo_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_refeicao
    ADD CONSTRAINT tipo_refeicao_pkey PRIMARY KEY (tipo_refeicao_id);


--
-- Name: tipo_vinculo tipo_vinculo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_vinculo
    ADD CONSTRAINT tipo_vinculo_pkey PRIMARY KEY (tipo_vinculo_id);


--
-- Name: tipo_vinculo tipo_vinculo_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_vinculo
    ADD CONSTRAINT tipo_vinculo_un UNIQUE (nivel);


--
-- Name: turma turma_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.turma
    ADD CONSTRAINT turma_pkey PRIMARY KEY (turma_id);


--
-- Name: turma turma_unick; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.turma
    ADD CONSTRAINT turma_unick UNIQUE (ano, numero);


--
-- Name: matricula uk_ld78d3uyhw39fbhmh44f9xs8k; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula
    ADD CONSTRAINT uk_ld78d3uyhw39fbhmh44f9xs8k UNIQUE (matricula);


--
-- Name: usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (usuario_id);


--
-- Name: habitante_unidade habitante_unidade_matricula_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.habitante_unidade
    ADD CONSTRAINT habitante_unidade_matricula_fk FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);


--
-- Name: habitante_unidade habitante_unidade_unidade_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.habitante_unidade
    ADD CONSTRAINT habitante_unidade_unidade_fk FOREIGN KEY (unidade_id) REFERENCES moradia_estudantil.unidade(unidade_id) ON DELETE CASCADE;


--
-- Name: item_unidade item_unidade_unidade_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.item_unidade
    ADD CONSTRAINT item_unidade_unidade_fk FOREIGN KEY (unidade_id) REFERENCES moradia_estudantil.unidade(unidade_id) ON DELETE CASCADE;


--
-- Name: presenca presenca_habitante_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.presenca
    ADD CONSTRAINT presenca_habitante_fk FOREIGN KEY (habitante_unidade_id) REFERENCES moradia_estudantil.habitante_unidade(habitante_unidade_id);


--
-- Name: presenca presenca_realizado_por_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.presenca
    ADD CONSTRAINT presenca_realizado_por_fk FOREIGN KEY (realizada_por_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: unidade unidade_parent_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.unidade
    ADD CONSTRAINT unidade_parent_fk FOREIGN KEY (parent_id) REFERENCES moradia_estudantil.unidade(unidade_id) ON DELETE CASCADE;


--
-- Name: unidade unidade_tipo_unidade_fk; Type: FK CONSTRAINT; Schema: moradia_estudantil; Owner: -
--

ALTER TABLE ONLY moradia_estudantil.unidade
    ADD CONSTRAINT unidade_tipo_unidade_fk FOREIGN KEY (tipo_unidade_id) REFERENCES moradia_estudantil.tipo_unidade(tipo_unidade_id);


--
-- Name: agendamentos_disponibilizados agedisp_user_pegou_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT agedisp_user_pegou_fk FOREIGN KEY (usuario_pegou_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: agendamentos agendamentos_credito_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT agendamentos_credito_fk FOREIGN KEY (credito_id) REFERENCES public.creditos(credito_id);


--
-- Name: agendamentos_disponibilizados agendamentos_disponibilizados_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT agendamentos_disponibilizados_fk FOREIGN KEY (usuario_disponibilizou_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: agendamentos agendamentos_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT agendamentos_fk FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);


--
-- Name: agendamentos agendamentos_fk_1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT agendamentos_fk_1 FOREIGN KEY (servidor_id) REFERENCES public.servidor(servidor_id);


--
-- Name: alteracoes_agendamentos alteracoes_agendamento_usuario_origem_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alteracoes_agendamentos
    ADD CONSTRAINT alteracoes_agendamento_usuario_origem_fk FOREIGN KEY (usuario_origem_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: alteracoes_agendamentos alteracoes_agendamentos_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alteracoes_agendamentos
    ADD CONSTRAINT alteracoes_agendamentos_fk FOREIGN KEY (agendamento_id) REFERENCES public.agendamentos(agendamento_id);


--
-- Name: alteracoes_agendamentos alteracoes_agendamentos_usuario_destino_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.alteracoes_agendamentos
    ADD CONSTRAINT alteracoes_agendamentos_usuario_destino_fk FOREIGN KEY (usuario_destino_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: cidades cidades_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cidades
    ADD CONSTRAINT cidades_fk FOREIGN KEY (id_estado) REFERENCES public.estados(id_estado);


--
-- Name: creditos creditos_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT creditos_fk FOREIGN KEY (tipo_credito_id) REFERENCES public.tipo_credito(tipo_credito_id);


--
-- Name: cursos cursos_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cursos
    ADD CONSTRAINT cursos_fk FOREIGN KEY (id_cidade) REFERENCES public.cidades(id_cidade);


--
-- Name: imagen fk2e4g9s3xbbfap2kymrh23n1n1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagen
    ADD CONSTRAINT fk2e4g9s3xbbfap2kymrh23n1n1 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: matricula fk3ma4ou1hiy0gmytfcp404qfgb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula
    ADD CONSTRAINT fk3ma4ou1hiy0gmytfcp404qfgb FOREIGN KEY (usuario_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: matricula_turma fk56bxxds1w3gxf1aq0hvabho15; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_turma
    ADD CONSTRAINT fk56bxxds1w3gxf1aq0hvabho15 FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);


--
-- Name: agendamentos fk59mqt9nglsei52j474tusrot9; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT fk59mqt9nglsei52j474tusrot9 FOREIGN KEY (refeicao_id) REFERENCES public.refeicao(refeicao_id);


--
-- Name: altenacoes_creditos fk6qnxcok619uq3anm3vnfbcpeg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.altenacoes_creditos
    ADD CONSTRAINT fk6qnxcok619uq3anm3vnfbcpeg FOREIGN KEY (para_credito_id) REFERENCES public.creditos(credito_id);


--
-- Name: agendamentos_disponibilizados fk97ynwof7k2ggkkgh714010gyr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT fk97ynwof7k2ggkkgh714010gyr FOREIGN KEY (agendamento_id) REFERENCES public.agendamentos(agendamento_id);


--
-- Name: matricula_turma fkfws5orscke7pe2v0w9lwuqv11; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_turma
    ADD CONSTRAINT fkfws5orscke7pe2v0w9lwuqv11 FOREIGN KEY (turma_id) REFERENCES public.turma(turma_id);


--
-- Name: altenacoes_creditos fkgsatehh5nqd2v99s29xl78psp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.altenacoes_creditos
    ADD CONSTRAINT fkgsatehh5nqd2v99s29xl78psp FOREIGN KEY (realizado_por_credito_id) REFERENCES public.creditos(credito_id);


--
-- Name: altenacoes_creditos fkjd5ylsydjjvcrrevv9wqwwkpr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.altenacoes_creditos
    ADD CONSTRAINT fkjd5ylsydjjvcrrevv9wqwwkpr FOREIGN KEY (realizado_por_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: creditos fklbvq9ymm4kdtq8p0oshgj820l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT fklbvq9ymm4kdtq8p0oshgj820l FOREIGN KEY (usuario_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: matricula_grupo fkn1d2hcmovgic1g20gtxidalm2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_grupo
    ADD CONSTRAINT fkn1d2hcmovgic1g20gtxidalm2 FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);


--
-- Name: altenacoes_creditos fknmbby0ohh25s3iwk8brala279; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.altenacoes_creditos
    ADD CONSTRAINT fknmbby0ohh25s3iwk8brala279 FOREIGN KEY (para_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: grupo_refeicoes fknqdleh10x9nyvfy605qrpdlco; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.grupo_refeicoes
    ADD CONSTRAINT fknqdleh10x9nyvfy605qrpdlco FOREIGN KEY (tipo_vinculo_id) REFERENCES public.tipo_vinculo(tipo_vinculo_id);


--
-- Name: refeicao fkoahp42635x26hkhponmthv5oi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refeicao
    ADD CONSTRAINT fkoahp42635x26hkhponmthv5oi FOREIGN KEY (tipo_refeicao_id) REFERENCES public.tipo_refeicao(tipo_refeicao_id);


--
-- Name: matricula_grupo fkoom4yu45os06fvrveyq38j1u8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula_grupo
    ADD CONSTRAINT fkoom4yu45os06fvrveyq38j1u8 FOREIGN KEY (grupo_refeicoes_id) REFERENCES public.grupo_refeicoes(grupo_refeicoes_id);


--
-- Name: servidor fkoorownrgssawfewb3p6u2iyf5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.servidor
    ADD CONSTRAINT fkoorownrgssawfewb3p6u2iyf5 FOREIGN KEY (usuario_id) REFERENCES public.usuarios(usuario_id);


--
-- Name: matricula fks3128p03s2dui4tw46cmvkxcb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula
    ADD CONSTRAINT fks3128p03s2dui4tw46cmvkxcb FOREIGN KEY (tipo_vinculo_id) REFERENCES public.tipo_vinculo(tipo_vinculo_id);


--
-- Name: refeicao fkso9fcd6yku3scfk27b7hdsdiq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.refeicao
    ADD CONSTRAINT fkso9fcd6yku3scfk27b7hdsdiq FOREIGN KEY (grupo_refeicoes_id) REFERENCES public.grupo_refeicoes(grupo_refeicoes_id);


--
-- Name: servidor fkt2vlrqvg9qo4epvle4jq6s15t; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.servidor
    ADD CONSTRAINT fkt2vlrqvg9qo4epvle4jq6s15t FOREIGN KEY (tipo_vinculo_id) REFERENCES public.tipo_vinculo(tipo_vinculo_id);


--
-- Name: matricula matricula_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matricula
    ADD CONSTRAINT matricula_fk FOREIGN KEY (curso_id) REFERENCES public.cursos(curso_id);


--
-- Name: creditos parent_id_credito_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT parent_id_credito_id_fk FOREIGN KEY (parent_id) REFERENCES public.creditos(credito_id);


--
-- Name: situacao_matricula situacao_matricula_matricula_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.situacao_matricula
    ADD CONSTRAINT situacao_matricula_matricula_fk FOREIGN KEY (matricula_id) REFERENCES public.matricula(matricula_id);


--
-- Name: cursos tipo_vinculo_cursos_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cursos
    ADD CONSTRAINT tipo_vinculo_cursos_fk FOREIGN KEY (tipo_vinculo_id) REFERENCES public.tipo_vinculo(tipo_vinculo_id);


--
-- Name: turma turma_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.turma
    ADD CONSTRAINT turma_fk FOREIGN KEY (curso_id) REFERENCES public.cursos(curso_id);


--
-- PostgreSQL database dump complete
--
