package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Lote;
import web.controlevacinacao.repository.helper.lote.LoteQueries;

public interface LoteRepository extends JpaRepository<Lote, Long>, LoteQueries {
    
}
