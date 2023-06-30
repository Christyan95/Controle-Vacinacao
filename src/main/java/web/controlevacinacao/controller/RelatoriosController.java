package web.controlevacinacao.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import web.controlevacinacao.service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

	private static final Logger logger = LoggerFactory.getLogger(RelatoriosController.class);
	
	@Autowired
	private RelatorioService relatorioService;
	
	@GetMapping("/relatoriosimples")
	public ResponseEntity<byte[]> gerarRelatorioSimplesTodasVacinas() {
		logger.trace("Entrou em gerarRelatorioSimplesTodasVacinas");
		logger.debug("Gerando relatório simples de todas as vacinas");
		
		byte[] relatorio = relatorioService.gerarRelatorioSimplesTodasVacinas();
		
		logger.debug("Relatório simples de todas as vacinas gerado");
		logger.debug("Retornando o relatório simples de todas as vacinas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=VacinasSimples.pdf")
				.body(relatorio);
	}

	@GetMapping("/relatoriocomplexo")
	public ResponseEntity<byte[]> gerarRelatorioComplexoTodasVacinas() {
		logger.trace("Entrou em gerarRelatorioComplexoTodasVacinas");
		logger.debug("Gerando relatório complexo de todas as vacinas");
		
		byte[] relatorio = relatorioService.gerarRelatorioComplexoTodasVacinas();
		
		logger.debug("Relatório complexo de todas as vacinas gerado");
		logger.debug("Retornando o relatório complexo de todas as vacinas");
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=VacinasComplexo.pdf")
				.body(relatorio);
	}
}