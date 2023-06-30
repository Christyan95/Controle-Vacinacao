package web.controlevacinacao.repository.helper.lote;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.model.Lote;

public interface LoteQueries {

    Page<Lote> filtrar (LoteFilter filtro, Pageable pageable);

    Lote buscarComVacina (Long codigoLote);

}
