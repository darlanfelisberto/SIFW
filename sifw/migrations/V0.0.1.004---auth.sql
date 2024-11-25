
ALTER TABLE public.usuarios ADD pessoa_id uuid;

DROP VIEW public.api_saldo;

CREATE VIEW public.api_saldo AS
SELECT usuario_id,
       username,
       COALESCE(sum, (0)::numeric) AS saldo
FROM ( SELECT u.usuario_id,
              au.username,
              sum((c.valor * (tc.fator)::numeric)) AS sum
       FROM ((((public.usuarios u
           LEFT JOIN public.creditos c ON ((u.usuario_id = c.usuario_id)))
           LEFT JOIN public.tipo_credito tc ON (((c.tipo_credito_id)::text = (tc.tipo_credito_id)::text)))
           LEFT JOIN auth.pessoas p ON ((p.pessoa_id = u.pessoa_id)))
           LEFT JOIN auth.auth_user au ON ((au.auth_user_id = p.auth_user_id)))
       GROUP BY u.usuario_id, au.username) foo;

DROP VIEW public.api_usuarios;

CREATE VIEW public.api_usuarios AS
SELECT DISTINCT u.usuario_id,
                p.cpf,
                p.dt_nasc,
                p.nome,
                u.token_ru,
                au.username,
                true AS ativo
FROM (((((public.usuarios u
    LEFT JOIN auth.auth_user au ON ((au.auth_user_id = u.pessoa_id)))
    JOIN public.matricula m ON ((m.usuario_id = u.usuario_id)))
    JOIN auth.pessoas p ON ((p.pessoa_id = u.pessoa_id)))
    JOIN ( SELECT max((sm_1.momento)::timestamp with time zone) AS momento,
                  sm_1.matricula_id
           FROM public.situacao_matricula sm_1
           GROUP BY sm_1.matricula_id) foo ON ((foo.matricula_id = m.matricula_id)))
    JOIN public.situacao_matricula sm ON (((sm.matricula_id = m.matricula_id) AND ((sm.momento)::timestamp with time zone = foo.momento))))
  WHERE ((sm.situacao)::text = 'ATIVA'::text)
  ORDER BY p.nome;

update public.usuarios u set pessoa_id = p.pessoa_id
from public.usuarios uu
left join auth.auth_user au on au.username = uu.username
left join auth.pessoas p on p.auth_user_id = au.auth_user_id
where uu.usuario_id = u.usuario_id;

ALTER TABLE public.usuarios DROP COLUMN cpf;
ALTER TABLE public.usuarios DROP COLUMN dt_nasc;
ALTER TABLE public.usuarios DROP COLUMN email;
ALTER TABLE public.usuarios DROP COLUMN nome;

ALTER TABLE public.usuarios ALTER COLUMN pessoa_id SET NOT NULL;

ALTER TABLE ONLY public.usuarios
    ADD CONSTRAINT usuarios_pessoas_fk FOREIGN KEY (pessoa_id) REFERENCES auth.pessoas(pessoa_id);
