package web.controlevacinacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.helper.vacina.VacinaQueries;

public interface VacinaRepository extends JpaRepository<Vacina, Long>, VacinaQueries {
    
    public List<Vacina> findByNomeContainingIgnoreCaseAndStatus(String nome, Status status);

    public Page<Vacina> findByStatus(Status status, Pageable pageable);
    
    public List<Vacina> findByStatus(Status status);

    public Optional<Vacina> findByCodigoAndStatus(Long codigo, Status status);

}
