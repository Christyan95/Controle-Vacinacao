package web.controlevacinacao.controller;

import java.util.List;

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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.filter.LoteFilter;
import web.controlevacinacao.model.Lote;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.LoteRepository;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.LoteService;

@Controller
@RequestMapping("/lotes")
public class LoteController {

    private static final Logger logger = LoggerFactory.getLogger(LoteController.class);

    @Autowired
    private LoteService loteService;

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private LoteRepository loteRepository;

    private void loggingErrosValidacao(String mensagem, BindingResult resultado) {
        logger.info(mensagem);
        logger.info("Erros encontrados:");
        for (FieldError erro : resultado.getFieldErrors()) {
            logger.info("{}", erro);
        }
    }

    private void colocarVacinasNoModel(Model model) {
        List<Vacina> vacinas = vacinaRepository.findByStatus(Status.ATIVO);
        logger.info("Vacinas buscadas no BD: {}", vacinas);
        model.addAttribute("vacinas", vacinas);
    }

    //==========
    @GetMapping("/cadastrar")
    public String abrirCadastrar(Lote lote, Model model) {
        colocarVacinasNoModel(model);
        return "lotes/cadastrar";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@Valid Lote lote, BindingResult resultado, Model model) {

        if (resultado.hasErrors()) {
            colocarVacinasNoModel(model);
            loggingErrosValidacao("O lote recebido para cadastrar não é válido.", resultado);

            return "lotes/cadastrar";
        } else {
            loteService.salvar(lote);
            return "redirect:/lotes/mostrarmensagemcadastrook";
        }
    }

    @GetMapping("/mostrarmensagemcadastrook")
    public String mostrarMensagemCadastroOK(Lote lote, Model model) {
        
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Lote cadastrado com sucesso!", TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        colocarVacinasNoModel(model);
        
        return "lotes/cadastrar";
    }
    //==========

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar(Model model) {
        model.addAttribute("url", "/lotes/pesquisar");
        model.addAttribute("uso", "lotes");
        colocarVacinasNoModel(model);
        return "lotes/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(LoteFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Lote> pagina = loteRepository.filtrar(filtro, pageable);
        PageWrapper<Lote> paginaWrapper = new PageWrapper<>(pagina, request);
        logger.info("Lotes buscados no BD: {}", paginaWrapper.getConteudo());
        model.addAttribute("pagina", paginaWrapper);
        model.addAttribute("uso", "lotes");
        return "lotes/mostrartodos";
    }

    //==========
    @PostMapping("/abriralterar")
    public String abrirAlterar(Long codigo, Model model) {
        Lote lote = loteRepository.buscarComVacina(codigo);
        if (lote != null) {
            model.addAttribute("lote", lote);
            colocarVacinasNoModel(model);
            return "lotes/alterar";
        } else {
            model.addAttribute("opcao", "lotes");
            model.addAttribute("mensagem", "Não existe lote com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/alterar")
    public String alterar(@Valid Lote lote, BindingResult resultado, Model model) {

        if (resultado.hasErrors()) {
            colocarVacinasNoModel(model);
            loggingErrosValidacao("O lote recebido para alterar não é válido.", resultado);

            return "lotes/cadastrar";   
        } else {

            loteService.salvar(lote);
            return "redirect:/lotes/mostrarmensagemalterarok";
        }
    }

    @GetMapping("/mostrarmensagemalterarok")
    public String mostrarMensagemAlterarOK(Model model) {

        NotificacaoAlertify notificacao = new NotificacaoAlertify("Lote alterado com sucesso!", TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        colocarVacinasNoModel(model);
        model.addAttribute("url", "/lotes/pesquisar");
        model.addAttribute("uso", "lotes");

        return "lotes/pesquisar";
    }
    //==========

    //==========
    @PostMapping("/abrirremover")
    public String abrirConfirmar(Long codigo, Model model) {
        Lote lote = loteRepository.buscarComVacina(codigo);
        if (lote != null) {
            model.addAttribute("lote", lote);
            return "lotes/confirmarremocao";
        } else {
            model.addAttribute("opcao", "lotes");
            model.addAttribute("mensagem", "Não existe lote com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Lote lote = loteRepository.buscarComVacina(codigo);
        if (lote != null) {
            lote.setStatus(Status.INATIVO);
            loteService.alterar(lote);
            return "redirect:/lotes/mostrarmensagemremocaook";
        } else {
            model.addAttribute("opcao", "lotes");
            model.addAttribute("mensagem", "Impossível remover lote com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @GetMapping("/mostrarmensagemremocaook")
    public String mostrarMensagemRemoverOK(Model model) {
        NotificacaoAlertify notificacao = new NotificacaoAlertify("Lote removido com sucesso!", TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);
        colocarVacinasNoModel(model);
        model.addAttribute("url", "/lotes/pesquisar");
        model.addAttribute("uso", "lotes");

        return "lotes/pesquisar";
    }
    //==========
}
