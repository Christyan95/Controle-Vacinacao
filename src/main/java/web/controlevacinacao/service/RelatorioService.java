package web.controlevacinacao.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatorioService {

	private static final Logger logger = LoggerFactory.getLogger(RelatorioService.class);

	@Autowired
	private DataSource dataSource;
	
	public byte[] gerarRelatorioSimplesTodasVacinas() {
		logger.trace("Entrou em gerarRelatorioSimplesTodasVacinas");
		InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/RelatorioVacinasSQLDiretoSimples.jasper");
		try (Connection conexao = dataSource.getConnection()){
			try {
				JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, null, conexao);
				return JasperExportManager.exportReportToPdf(jasperPrint);
			} catch (JRException e) {
				logger.error("Problemas na geracao do PDF do relatório: " + e);
			}
		} catch (SQLException e) {
			logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e);
		}

		return null;
	}

    public byte[] gerarRelatorioComplexoTodasVacinas() {
		logger.trace("Entrou em gerarRelatorioComplexoTodasVacinas");
		InputStream arquivoJasper = getClass().getResourceAsStream("/relatorios/RelatorioVacinasELotesSQLDiretoComplexo.jasper");
		try (Connection conexao = dataSource.getConnection()){
			try {
                Map<String, Object> parametros = new HashMap<>();
                parametros.put("TITULO", "Relatório de Vacinas e Lotes");
				JasperPrint jasperPrint = JasperFillManager.fillReport(arquivoJasper, parametros, conexao);
				return JasperExportManager.exportReportToPdf(jasperPrint);
			} catch (JRException e) {
				logger.error("Problemas na geracao do PDF do relatório: " + e);
			}
		} catch (SQLException e) {
			logger.error("Problemas na obtenção de uma conexão com o BD na geração de relatório: " + e);
		}

		return null;
	}
}