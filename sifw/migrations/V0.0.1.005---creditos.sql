ALTER TABLE public.altenacoes_creditos RENAME TO alteracoes_creditos;

ALTER TABLE public.alteracoes_creditos DROP COLUMN para_id;
ALTER TABLE public.alteracoes_creditos RENAME COLUMN realizado_por_id TO usuario_logado_id;
ALTER TABLE public.alteracoes_creditos RENAME COLUMN realizado_por_credito_id TO de_credito_id;
ALTER TABLE public.alteracoes_creditos RENAME COLUMN altenacoes_creditos_id TO alteracoes_creditos_id;

ALTER TABLE public.tipo_credito ALTER COLUMN cor TYPE varchar(12) USING cor::varchar(12);
UPDATE public.tipo_credito SET cor='221,0,0' WHERE cor='#dd0000';
UPDATE public.tipo_credito set cor='66,147,58' WHERE cor='#42933a';
UPDATE public.tipo_credito set cor='155,191,11' WHERE cor='#9bbf0b';
UPDATE public.tipo_credito set cor='242,178,0' WHERE cor='#f2b200';
UPDATE public.tipo_credito set cor='221,0,0' WHERE cor='#dd0000';