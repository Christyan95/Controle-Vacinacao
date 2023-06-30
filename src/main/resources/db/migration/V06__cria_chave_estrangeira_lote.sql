ALTER TABLE public.lote
    ADD FOREIGN KEY (codigo_vacina)
    REFERENCES public.vacina (codigo)
    NOT VALID;