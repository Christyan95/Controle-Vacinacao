package web.controlevacinacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.repository.helper.pessoa.PessoaQueries;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaQueries {
    
}
