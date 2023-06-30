package web.controlevacinacao.repository.helper.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Pessoa;

public interface PessoaQueries {
    
    Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable);

}
