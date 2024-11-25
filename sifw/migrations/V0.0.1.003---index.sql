CREATE INDEX agendamentos_refeicao_id_idx ON public.agendamentos USING btree (refeicao_id, dt_agendamento);
CREATE INDEX creditos_usuario_id_idx ON public.creditos USING btree (usuario_id);

