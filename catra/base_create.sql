--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4 (Debian 13.4-0+deb11u1)
-- Dumped by pg_dump version 13.4 (Debian 13.4-0+deb11u1)

-- Started on 2022-01-27 16:56:38 -03

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA public;


--
-- TOC entry 3085 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 200 (class 1259 OID 29399)
-- Name: agendamentos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.agendamentos (
    agendamento_id uuid NOT NULL,
    dt_agendamento date NOT NULL,
    credito_id uuid,
    refeicao_id uuid NOT NULL,
    usuario_id uuid
);


--
-- TOC entry 201 (class 1259 OID 29402)
-- Name: agendamentos_disponibilizados; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.agendamentos_disponibilizados (
    agendamento_disponibilizado_id uuid NOT NULL,
    dt_disponibilizado timestamp without time zone,
    sincronizado timestamp without time zone,
    usuario_pegou_id uuid,
    agendamento_id uuid NOT NULL,
    usuario_disponibilizou_id uuid
);


--
-- TOC entry 202 (class 1259 OID 29405)
-- Name: api_refeicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.api_refeicao (
    refeicao_id uuid NOT NULL,
    bloquear integer,
    descricao_grupo character varying(255),
    grupo_refeicoes_id uuid,
    hora_inicio_find time without time zone,
    hora_inicio_util time without time zone,
    valor real,
    tipo_refeicao_id uuid NOT NULL
);


--
-- TOC entry 203 (class 1259 OID 29408)
-- Name: api_saldo; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.api_saldo (
    username character varying(255) NOT NULL,
    saldo real,
    usuario_id uuid
);


--
-- TOC entry 204 (class 1259 OID 29411)
-- Name: api_usuario_refeicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.api_usuario_refeicao (
    refeicao_id uuid NOT NULL,
    usuario_id uuid NOT NULL,
    ativa boolean NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 29435)
-- Name: api_usuarios; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.api_usuarios (
    usuario_id uuid NOT NULL,
    cpf character varying(255),
    nome character varying(255),
    token_ru character varying(255),
    username character varying(255),
    ativo boolean DEFAULT true NOT NULL
);


--
-- TOC entry 3086 (class 0 OID 0)
-- Dependencies: 210
-- Name: COLUMN api_usuarios.ativo; Type: COMMENT; Schema: public; Owner: -
--

COMMENT ON COLUMN public.api_usuarios.ativo IS '''diz se o usuario esta ativo para a catraca''';


--
-- TOC entry 205 (class 1259 OID 29414)
-- Name: creditos; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.creditos (
    credito_id uuid NOT NULL,
    dt_credito timestamp without time zone,
    sincronizado timestamp without time zone,
    valor real,
    tipo_credito_id character varying(255),
    usuario_id uuid
);


--
-- TOC entry 206 (class 1259 OID 29417)
-- Name: imagen; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.imagen (
    imagen_id uuid NOT NULL,
    file_name character varying(255),
    usuario_id uuid
);


--
-- TOC entry 207 (class 1259 OID 29420)
-- Name: sincronizado; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.sincronizado (
    sincronizado_id uuid NOT NULL,
    dt_sincronizado timestamp without time zone,
    tipo_sincronizacao character varying(255)
);


--
-- TOC entry 208 (class 1259 OID 29423)
-- Name: tipo_credito; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_credito (
    tipo_credito_id character varying(255) NOT NULL,
    cor character varying(255),
    descricao character varying(255),
    fator integer NOT NULL,
    style_class character varying(255)
);


--
-- TOC entry 209 (class 1259 OID 29429)
-- Name: tipo_refeicao; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tipo_refeicao (
    tipo_refeicao_id uuid NOT NULL,
    background_color character varying(255),
    cor_fonte character varying(255),
    descricao character varying(255),
    hora_fim time without time zone,
    hora_inicio time without time zone,
    style_class character varying(255)
);


--
-- TOC entry 211 (class 1259 OID 29625)
-- Name: usuario_off_line; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.usuario_off_line (
    usuario_off_line_id uuid NOT NULL,
    username character varying NOT NULL,
    hash character varying NOT NULL,
    salt character varying NOT NULL
);


--
-- TOC entry 3068 (class 0 OID 29399)
-- Dependencies: 200
-- Data for Name: agendamentos; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3069 (class 0 OID 29402)
-- Dependencies: 201
-- Data for Name: agendamentos_disponibilizados; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3070 (class 0 OID 29405)
-- Dependencies: 202
-- Data for Name: api_refeicao; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3071 (class 0 OID 29408)
-- Dependencies: 203
-- Data for Name: api_saldo; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3072 (class 0 OID 29411)
-- Dependencies: 204
-- Data for Name: api_usuario_refeicao; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3078 (class 0 OID 29435)
-- Dependencies: 210
-- Data for Name: api_usuarios; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3073 (class 0 OID 29414)
-- Dependencies: 205
-- Data for Name: creditos; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3074 (class 0 OID 29417)
-- Dependencies: 206
-- Data for Name: imagen; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3075 (class 0 OID 29420)
-- Dependencies: 207
-- Data for Name: sincronizado; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 3076 (class 0 OID 29423)
-- Dependencies: 208
-- Data for Name: tipo_credito; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.tipo_credito VALUES ('SAIDA', NULL, 'Sáida', -1, NULL);


--
-- TOC entry 3077 (class 0 OID 29429)
-- Dependencies: 209
-- Data for Name: tipo_refeicao; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.tipo_refeicao VALUES ('35acf661-807c-4618-a7c4-b2e4257ab658', '#42933a', '#ffffff', 'Almoço', '13:30:00', '12:00:00', 'remover');
INSERT INTO public.tipo_refeicao VALUES ('0a049843-1327-46bc-b807-66012514c289', '#c12f29', '#ffffff', 'Dejejum', '07:30:00', '07:00:00', 'remover');
INSERT INTO public.tipo_refeicao VALUES ('d31cdca3-1c50-4991-adb9-dd359729fa6a', '#263b4c', '#ffffff', 'Janta', '19:00:00', '18:00:00', 'remover');
INSERT INTO public.tipo_refeicao VALUES ('e7a81561-e496-46ca-acfc-231f533c41fd', '#0d89ec', '#000000', 'Lanche tarde', '15:30:00', '15:00:00', 'remover');


--
-- TOC entry 3079 (class 0 OID 29625)
-- Dependencies: 211
-- Data for Name: usuario_off_line; Type: TABLE DATA; Schema: public; Owner: -
--



--
-- TOC entry 2903 (class 2606 OID 29442)
-- Name: agendamentos_disponibilizados agendamentos_disponibilizados_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT agendamentos_disponibilizados_pkey PRIMARY KEY (agendamento_disponibilizado_id);


--
-- TOC entry 2901 (class 2606 OID 29444)
-- Name: agendamentos agendamentos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT agendamentos_pkey PRIMARY KEY (agendamento_id);


--
-- TOC entry 2905 (class 2606 OID 29446)
-- Name: api_refeicao api_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_refeicao
    ADD CONSTRAINT api_refeicao_pkey PRIMARY KEY (refeicao_id);


--
-- TOC entry 2907 (class 2606 OID 29448)
-- Name: api_saldo api_saldo_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_saldo
    ADD CONSTRAINT api_saldo_pkey PRIMARY KEY (username);


--
-- TOC entry 2909 (class 2606 OID 29450)
-- Name: api_usuario_refeicao api_usuario_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_usuario_refeicao
    ADD CONSTRAINT api_usuario_refeicao_pkey PRIMARY KEY (refeicao_id, usuario_id);


--
-- TOC entry 2911 (class 2606 OID 29452)
-- Name: creditos creditos_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT creditos_pkey PRIMARY KEY (credito_id);


--
-- TOC entry 2913 (class 2606 OID 29454)
-- Name: imagen imagen_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagen
    ADD CONSTRAINT imagen_pkey PRIMARY KEY (imagen_id);


--
-- TOC entry 2915 (class 2606 OID 29456)
-- Name: sincronizado sincronizado_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.sincronizado
    ADD CONSTRAINT sincronizado_pkey PRIMARY KEY (sincronizado_id);


--
-- TOC entry 2917 (class 2606 OID 29458)
-- Name: tipo_credito tipo_credito_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_credito
    ADD CONSTRAINT tipo_credito_pkey PRIMARY KEY (tipo_credito_id);


--
-- TOC entry 2919 (class 2606 OID 29460)
-- Name: tipo_refeicao tipo_refeicao_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tipo_refeicao
    ADD CONSTRAINT tipo_refeicao_pkey PRIMARY KEY (tipo_refeicao_id);


--
-- TOC entry 2923 (class 2606 OID 29632)
-- Name: usuario_off_line usuario_off_line_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_off_line
    ADD CONSTRAINT usuario_off_line_pk PRIMARY KEY (usuario_off_line_id);


--
-- TOC entry 2925 (class 2606 OID 29634)
-- Name: usuario_off_line usuario_off_line_un; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.usuario_off_line
    ADD CONSTRAINT usuario_off_line_un UNIQUE (username);


--
-- TOC entry 2921 (class 2606 OID 29462)
-- Name: api_usuarios usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_usuarios
    ADD CONSTRAINT usuarios_pkey PRIMARY KEY (usuario_id);


--
-- TOC entry 2926 (class 2606 OID 29463)
-- Name: agendamentos fk1ms2dwg23nv1kpiljjil8vewv; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT fk1ms2dwg23nv1kpiljjil8vewv FOREIGN KEY (credito_id) REFERENCES public.creditos(credito_id);


--
-- TOC entry 2935 (class 2606 OID 29468)
-- Name: creditos fk1u5v1xp873guxt0yu97sia07c; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT fk1u5v1xp873guxt0yu97sia07c FOREIGN KEY (tipo_credito_id) REFERENCES public.tipo_credito(tipo_credito_id);


--
-- TOC entry 2937 (class 2606 OID 29473)
-- Name: imagen fk2e4g9s3xbbfap2kymrh23n1n1; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.imagen
    ADD CONSTRAINT fk2e4g9s3xbbfap2kymrh23n1n1 FOREIGN KEY (usuario_id) REFERENCES public.api_usuarios(usuario_id);


--
-- TOC entry 2933 (class 2606 OID 29478)
-- Name: api_usuario_refeicao fk2qa5u9ldji2i3i9045wqgykb3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_usuario_refeicao
    ADD CONSTRAINT fk2qa5u9ldji2i3i9045wqgykb3 FOREIGN KEY (refeicao_id) REFERENCES public.api_refeicao(refeicao_id);


--
-- TOC entry 2934 (class 2606 OID 29483)
-- Name: api_usuario_refeicao fk787gy2vcfhtp5dc08p8rwbfsy; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_usuario_refeicao
    ADD CONSTRAINT fk787gy2vcfhtp5dc08p8rwbfsy FOREIGN KEY (usuario_id) REFERENCES public.api_usuarios(usuario_id);


--
-- TOC entry 2932 (class 2606 OID 29488)
-- Name: api_saldo fk8vlkn2tbnisf3fma2oj9ki0q0; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_saldo
    ADD CONSTRAINT fk8vlkn2tbnisf3fma2oj9ki0q0 FOREIGN KEY (usuario_id) REFERENCES public.api_usuarios(usuario_id);


--
-- TOC entry 2929 (class 2606 OID 29493)
-- Name: agendamentos_disponibilizados fk97ynwof7k2ggkkgh714010gyr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT fk97ynwof7k2ggkkgh714010gyr FOREIGN KEY (agendamento_id) REFERENCES public.agendamentos(agendamento_id);


--
-- TOC entry 2931 (class 2606 OID 29498)
-- Name: api_refeicao fkcap8esbf3dwk6ibpfuv1sym6k; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.api_refeicao
    ADD CONSTRAINT fkcap8esbf3dwk6ibpfuv1sym6k FOREIGN KEY (tipo_refeicao_id) REFERENCES public.tipo_refeicao(tipo_refeicao_id);


--
-- TOC entry 2930 (class 2606 OID 29503)
-- Name: agendamentos_disponibilizados fkgc6n8kcaiwk6kffoocc87dn8v; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos_disponibilizados
    ADD CONSTRAINT fkgc6n8kcaiwk6kffoocc87dn8v FOREIGN KEY (usuario_disponibilizou_id) REFERENCES public.api_usuarios(usuario_id);


--
-- TOC entry 2927 (class 2606 OID 29508)
-- Name: agendamentos fkl974vecn507ntjdhangnfd1pi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT fkl974vecn507ntjdhangnfd1pi FOREIGN KEY (refeicao_id) REFERENCES public.api_refeicao(refeicao_id);


--
-- TOC entry 2936 (class 2606 OID 29513)
-- Name: creditos fklbvq9ymm4kdtq8p0oshgj820l; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.creditos
    ADD CONSTRAINT fklbvq9ymm4kdtq8p0oshgj820l FOREIGN KEY (usuario_id) REFERENCES public.api_usuarios(usuario_id);


--
-- TOC entry 2928 (class 2606 OID 29518)
-- Name: agendamentos fklvnidyf53gkjeqkwwv6sxynpf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.agendamentos
    ADD CONSTRAINT fklvnidyf53gkjeqkwwv6sxynpf FOREIGN KEY (usuario_id) REFERENCES public.api_usuarios(usuario_id);


-- Completed on 2022-01-27 16:56:38 -03

--
-- PostgreSQL database dump complete
--
