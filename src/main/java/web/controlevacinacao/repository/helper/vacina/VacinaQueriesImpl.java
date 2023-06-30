package web.controlevacinacao.repository.helper.vacina;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.repository.pagination.PaginacaoUtil;

public class VacinaQueriesImpl implements VacinaQueries {

    private static final Logger logger = LoggerFactory.getLogger(VacinaQueriesImpl.class);

    @PersistenceContext
    private EntityManager manager;

    // @Override
    // public Page<Vacina> filtrar(VacinaFilter filtro, Pageable pageable) {
    //     CriteriaBuilder builder = manager.getCriteriaBuilder();
    //     CriteriaQuery<Vacina> criteriaQuery = builder.createQuery(Vacina.class);
    //     CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
    //     Root<Vacina> v = criteriaQuery.from(Vacina.class);
    //     v.alias("vacinaAlias");

    //     Root<Vacina> vCount = countQuery.from(Vacina.class);
    //     vCount.alias("vacinaAlias");

    //     TypedQuery<Vacina> typedQuery;
    //     TypedQuery<Long> typedQueryTotal;
    //     List<Predicate> predicateList = new ArrayList<>();

    //     if (filtro.getCodigo() != null) {
    //         predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
    //     }

    //     if (StringUtils.hasText(filtro.getNome())) {
    //         predicateList.add(builder.like(
    //                 builder.lower(v.<String>get("nome")),
    //                 "%" + filtro.getNome().toLowerCase() + "%"));
    //     }

    //     if (StringUtils.hasText(filtro.getDescricao())) {
    //         predicateList.add(builder.like(
    //                 builder.lower(v.<String>get("descricao")),
    //                 "%" + filtro.getDescricao().toLowerCase() + "%"));
    //     }

    //     predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

    //     Predicate[] predArray = new Predicate[predicateList.size()];
    //     predicateList.toArray(predArray);

    //     criteriaQuery.select(v).where(predArray);

    //     PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);

    //     typedQuery = manager.createQuery(criteriaQuery);

    //     PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

    //     List<Vacina> vacinas = typedQuery.getResultList();

    //     logger.info("Vacinas buscadas no BD: {}", vacinas);
        
    //     countQuery.select(builder.count(vCount)).where(criteriaQuery.getRestriction());
    //     typedQueryTotal = manager.createQuery(countQuery);

    //     long totalVacinas = typedQueryTotal.getSingleResult();

    //     return new PageImpl<>(vacinas, pageable, totalVacinas); 
    // }

    @Override
    public Page<Vacina> filtrar(VacinaFilter filtro, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Vacina> criteriaQuery = builder.createQuery(Vacina.class);
        Root<Vacina> v = criteriaQuery.from(Vacina.class);
        TypedQuery<Vacina> typedQuery;
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
        }

        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getDescricao())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("descricao")),
                    "%" + filtro.getDescricao().toLowerCase() + "%"));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);
        Predicate[] predArray2 = new Predicate[predicateList.size()];
        predicateList.toArray(predArray2);

        criteriaQuery.select(v).where(predArray);

        PaginacaoUtil.prepararOrdem(v, criteriaQuery, builder, pageable);

        typedQuery = manager.createQuery(criteriaQuery);

        PaginacaoUtil.prepararIntervalo(typedQuery, pageable);

        List<Vacina> vacinas = typedQuery.getResultList();
        
        long totalVacinas = getTotalVacinas(filtro);
        return new PageImpl<>(vacinas, pageable, totalVacinas); 
    }

    private Long getTotalVacinas(VacinaFilter filtro) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Vacina> v = criteriaQuery.from(Vacina.class);
        List<Predicate> predicateList = new ArrayList<>();

        if (filtro.getCodigo() != null) {
            predicateList.add(builder.equal(v.<Long>get("codigo"), filtro.getCodigo()));
        }

        if (StringUtils.hasText(filtro.getNome())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("nome")),
                    "%" + filtro.getNome().toLowerCase() + "%"));
        }

        if (StringUtils.hasText(filtro.getDescricao())) {
            predicateList.add(builder.like(
                    builder.lower(v.<String>get("descricao")),
                    "%" + filtro.getDescricao().toLowerCase() + "%"));
        }

        predicateList.add(builder.equal(v.<Status>get("status"), Status.ATIVO));

        Predicate[] predArray = new Predicate[predicateList.size()];
        predicateList.toArray(predArray);

        criteriaQuery.select(builder.count(v)).where(predArray);

        return manager.createQuery(criteriaQuery).getSingleResult();

    }

}
