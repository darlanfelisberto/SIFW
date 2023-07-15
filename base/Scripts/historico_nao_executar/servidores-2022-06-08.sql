ALTER TABLE public.servidor ADD siape varchar NULL;
ALTER TABLE public.servidor ADD status varchar NULL;
COMMENT ON COLUMN public.servidor.status IS '"Ativo, Inativo"';
ALTER TABLE public.servidor ALTER COLUMN status SET NOT NULL;
ALTER TABLE public.servidor ALTER COLUMN siape SET NOT NULL;

ALTER TABLE public.servidor ALTER COLUMN siape TYPE int4 USING siape::int4;
ALTER TABLE public.servidor ADD situacao varchar(10) NOT NULL;

