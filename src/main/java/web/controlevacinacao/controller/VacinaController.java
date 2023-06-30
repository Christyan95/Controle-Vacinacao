package web.controlevacinacao.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import web.controlevacinacao.ajax.NotificacaoAlertify;
import web.controlevacinacao.ajax.RespostaJSON;
import web.controlevacinacao.ajax.ThymeleafUtil;
import web.controlevacinacao.ajax.TipoNotificaoAlertify;
import web.controlevacinacao.ajax.TipoResposta;
import web.controlevacinacao.filter.VacinaFilter;
import web.controlevacinacao.model.Status;
import web.controlevacinacao.model.Vacina;
import web.controlevacinacao.pagination.PageWrapper;
import web.controlevacinacao.repository.VacinaRepository;
import web.controlevacinacao.service.VacinaService;

@Controller
@RequestMapping("/vacinas")
public class VacinaController {

    private static final Logger logger = LoggerFactory.getLogger(VacinaController.class);

    @Autowired
    private VacinaRepository vacinaRepository;

    @Autowired
    private VacinaService vacinaService;

    @Autowired
    private ThymeleafUtil thymeleafUtil;

    private void loggingErrosValidacao(String mensagem, BindingResult resultado) {
        logger.info(mensagem);
        logger.info("Erros encontrados:");
        for (FieldError erro : resultado.getFieldErrors()) {
            logger.info("{}", erro);
        }
    }

    // ==========
    @GetMapping("/cadastrar")
    public String abrirCadastrar(Vacina vacina) {
        return "vacinas/cadastrar";
    }

    @PostMapping(value = { "/cadastrar" }, produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseBody
    public RespostaJSON cadastrar(@RequestBody @Valid Vacina vacina, BindingResult resultado,
            Model model, HttpServletRequest request,
            HttpServletResponse response) {
        RespostaJSON respostaJSON;

        if (resultado.hasErrors()) {
            loggingErrosValidacao("A vacina recebida para cadastrar não é válida.", resultado);
            
            String html = thymeleafUtil.processThymeleafTemplate(request, response,
                    model.asMap(), "vacinas/cadastrar", "formulario");

            respostaJSON = new RespostaJSON(TipoResposta.FRAGMENTO);
            respostaJSON.setHtmlFragmento(html);
        } else {
            vacinaService.salvar(vacina);
            model.addAttribute("vacina", new Vacina());

            String html = thymeleafUtil.processThymeleafTemplate(request, response,
                    model.asMap(), "vacinas/cadastrar", "formulario");

            respostaJSON = new RespostaJSON(TipoResposta.FRAGMENTO_E_NOTIFICACAO);
            respostaJSON.setHtmlFragmento(html);

            NotificacaoAlertify notificacaoAlertify = new NotificacaoAlertify("Vacina cadastrada com sucesso!",
                    TipoNotificaoAlertify.SUCESSO);

            respostaJSON.setNotificacao(notificacaoAlertify);
        }
        return respostaJSON;
    }

    @GetMapping("/mostrarmensagemcadastrook")
    public String mostrarMensagemCadastroOK(Vacina vacina, Model model) {

        NotificacaoAlertify notificacao = new NotificacaoAlertify("Vacina cadastrada com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);

        return "vacinas/cadastrar";
    }
    // ==========

    @GetMapping("/abrirpesquisar")
    public String abrirPesquisar() {
        return "vacinas/pesquisar";
    }

    @GetMapping("/pesquisar")
    public String pesquisar(VacinaFilter filtro, Model model,
            @PageableDefault(size = 5) @SortDefault(sort = "codigo", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {
        Page<Vacina> pagina = vacinaRepository.filtrar(filtro, pageable);
        PageWrapper<Vacina> paginaWrapper = new PageWrapper<>(pagina, request);
        logger.info("Vacinas buscadas no BD: {}", paginaWrapper.getConteudo());
        model.addAttribute("pagina", paginaWrapper);
        return "vacinas/mostrartodas";
    }

    // ==========
    @GetMapping("/abriralterar")
    public String abrirAlterar(Long codigo, Model model) {
        Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
        if (optVacina.isPresent()) {
            model.addAttribute("vacina", optVacina.get());
            return "vacinas/alterar";
        } else {
            model.addAttribute("opcao", "vacinas");
            model.addAttribute("mensagem", "Não existe vacina com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/abriralterar")
    public String abrirAlterarPost(Long codigo, Model model) {
        Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
        if (optVacina.isPresent()) {
            model.addAttribute("vacina", optVacina.get());
            return "vacinas/alterar";
        } else {
            model.addAttribute("opcao", "vacinas");
            model.addAttribute("mensagem", "Não existe vacina com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/alterar")
    public String alterar(@Valid Vacina vacina, BindingResult resultado) {

        if (resultado.hasErrors()) {
            loggingErrosValidacao("A vacina recebida para alterar não é válida.", resultado);

            return "vacinas/alterar";
        } else {
            vacinaService.salvar(vacina);
            return "redirect:/vacinas/mostrarmensagemalterarok";
        }
    }

    @GetMapping("/mostrarmensagemalterarok")
    public String mostrarMensagemAlterarOK(Model model) {

        NotificacaoAlertify notificacao = new NotificacaoAlertify("Vacina cadastrada com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);

        return "vacinas/pesquisar";
    }
    // ==========

    // ==========
    @PostMapping("/abrirremover")
    public String abrirConfirmar(Long codigo, Model model) {
        Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
        if (optVacina.isPresent()) {
            model.addAttribute("vacina", optVacina.get());
            return "vacinas/confirmarremocao";
        } else {
            model.addAttribute("opcao", "vacinas");
            model.addAttribute("mensagem", "Não existe vacina com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @PostMapping("/remover")
    public String remover(Long codigo, Model model) {
        Optional<Vacina> optVacina = vacinaRepository.findById(codigo);
        if (optVacina.isPresent()) {
            Vacina vacina = optVacina.get();
            vacina.setStatus(Status.INATIVO);
            vacinaService.alterar(vacina);
            return "redirect:/vacinas/mostrarmensagemremocaook";
        } else {
            model.addAttribute("opcao", "vacinas");
            model.addAttribute("mensagem", "Impossível remover vacina com código: " + codigo);
            return "mostrarmensagem";
        }
    }

    @GetMapping("/mostrarmensagemremocaook")
    public String mostrarMensagemRemoverOK(Model model) {

        NotificacaoAlertify notificacao = new NotificacaoAlertify("Vacina removida com sucesso!",
                TipoNotificaoAlertify.SUCESSO);
        model.addAttribute("notificacao", notificacao);

        return "vacinas/pesquisar";
    }
    // ==========
}
