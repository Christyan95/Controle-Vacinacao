package web.controlevacinacao.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.filter.PessoaFilter;
import web.controlevacinacao.model.Aplicacao;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Pessoa;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.PessoaRepository;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.AplicacaoService;

@Controller
@RequestMapping("/aplicacoes")
public class AplicacaoController {

    private static final Logger logger = LoggerFactory.getLogger(PessoaController.class);

    @Autowired
    private AplicacaoService aplicacaoService;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private LoteRepository loteRepository;

    private void colocarVacinasNoModel(Model model) {
        List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
        logger.info("Vacinas buscadas no BD: {}", vacinas);
        model.addAttribute("vacinas", vacinas);
    }

    //
    // =========================================================================================
    //
    
    @GetMapping("/cadastrar")
    public String abrirCadastroAplicacao(HttpSession sessao) {

        sessao.setAttribute("aplicacao", new Aplicacao());

        return "aplicacoes/cadastrar";
    }

    @GetMapping("/escolherpessoa")
    public String abrirEscolhaPessoa(Model model) {
        model.addAttribute("url", "/aplicacoes/pesquisarpessoa");
        model.addAttribute("uso", "aplicacoes");

        return "pessoas/pesquisar";
    }

    @GetMapping("/pesquisarpessoa")
    public String pesquisar(PessoaFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Pessoa> pagina = pessoaRepository.filtrar(filtro, pageable);
        PageWrapper<Pessoa> paginaWrapper = new PageWrapper<>(pagina, request);
        logger.info("Pessoas buscadas no BD: {}", paginaWrapper.getConteudo());
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("uso", "aplicacoes");
        return "pessoas/mostrartodas";
    }

    @PostMapping("/escolherpessoa")
    public String definirPessoa(Long codigo, Model model, HttpSession sessao) {
        Optional<Pessoa> optPessoa = pessoaRepository.findById(codigo);
        if (optPessoa.isPresent()) {
            Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");
            aplicacao.setPessoa(optPessoa.get());

            return "aplicacoes/cadastrar";
        } else {
            model.addAttribute("opcao", "pessoas");
            model.addAttribute("mensagem", "Não existe pessoa com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    //
    // =========================================================================================
    //

    @GetMapping("/escolherlote")
    public String abrirEscolhaLote(Model model) {
        colocarVacinasNoModel(model);
        model.addAttribute("url", "/aplicacoes/pesquisarlote");
        model.addAttribute("uso", "aplicacoes");

        return "lotes/pesquisar";
    }

    @GetMapping("/pesquisarlote")
    public String pesquisarLote(LoteFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Lote> pagina = loteRepository.filtrar(filtro, pageable);
        PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
        logger.info("Lotes buscados no BD: {}", paginaWrapper.getConteudo());
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("uso", "aplicacoes");
        return "lotes/mostrartodos";
    }

    @PostMapping("/escolherlote")
    public String definirLote(Long codigo, Model model, HttpSession sessao) {
        Lote lote = loteRepository.buscarComVacina(codigo);
        if (lote != null) {
            Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");
            aplicacao.setLote(lote);

            return "aplicacoes/cadastrar";
        } else {
            model.addAttribute("opcao", "lotes");
            model.addAttribute("mensagem", "Não existe lote com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    //
    // =========================================================================================
    //

    @PostMapping("/cadastrar")
    public String cadastrar(Model model, HttpSession sessao) {
        Aplicacao aplicacao = (Aplicacao) sessao.getAttribute("aplicacao");

        if (aplicacao.getPessoa() != null && aplicacao.getLote() != null
                && aplicacao.getLote().getNroDosesAtual() > 0) {
            aplicacaoService.salvar(aplicacao);
            sessao.removeAttribute("aplicacao");

            return "redirect:/aplicacoes/mostrarmensagemcadastrook";
        } else {
            model.addAttribute("opcao", "lotes");
            model.addAttribute("mensagem", "Aplicação inválida para cadastrar");
            return "mostrarmensagem";
        }
    }

    @GetMapping("/mostrarmensagemcadastrook")
    public String mostrarMensagemCadastroOK(Model model, HttpSession sessao) {

        NotificacaoAlertify notificacao = new NotificacaoAlertify("Aplicação cadastrado com sucesso!", TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        sessao.setAttribute("aplicacao", new Aplicacao());

        return "aplicacoes/cadastrar";
    }
    
    //
    // =========================================================================================
    //

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar(Model model) {
        colocarVacinasNoModel(model);
        return "aplicacoes/pesquisar";
    }

    // @GetMapping("/pesquisar")
    // public String pesquisar(AplicacaoFilter filtro, Model model,
    //         @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
    //         HttpServletRequest request) {
    //     Page<Aplicacao> pagina = aplicacaoRepository.filtrar(filtro, pageable);
    //     PageWrapper<Aplicacao> paginaWrapper = new PageWrapper<>(pagina, request);
    //     logger.info("Aplicações buscados no BD: {}", paginaWrapper.getConteudo());
    //     model.addAttribute("pagina", paginaWrapper);
    //     model.addAttribute("uso", "aplicacoes");
    //     return "aplicacoes/mostrartodos";
    // }
}
