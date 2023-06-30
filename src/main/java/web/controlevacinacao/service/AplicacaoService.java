package web.controlevacinacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.repository.AplicacaoRepository;
import web.controlevacinacao.repository.LoteRepository;

@Service
public class AplicacaoService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Autowired
    private LoteRepository loteRepository;

    @Transactional
    public void salvar(Aplicacao aplicacao) {
        aplicacaoRepository.save(aplicacao);
        Lote lote = aplicacao.getLote();
        lote.setNroDosesAtual(lote.getNroDosesAtual() - 1);
        loteRepository.save(lote);
    }
}
