package web.controlevacinacao.repository.helper.vacina;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Vacina;

public interface VacinaQueries {
    
    Page<Vacina> filtrar(VacinaFilter filtro, Pageable pageable);

}
