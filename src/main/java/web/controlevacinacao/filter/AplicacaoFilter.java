package web.controlevacinacao.filter;

import java.time.LocalDate;

public class AplicacaoFilter {
    
    private Long codigo;
    private LocalDate dataInicial;
    private LocalDate dataFinal;
    private String cpfPessoa;
    private Long codigoLote;
    private String nomeVacina;
    
    public Long getCodigo() {
        return codigo;
    }
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }
    public LocalDate getDataInicial() {
        return dataInicial;
    }
    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }
    public LocalDate getDataFinal() {
        return dataFinal;
    }
    public void setDataFinal(LocalDate dataFinal) {
        this.dataFinal = dataFinal;
    }
    public String getCpfPessoa() {
        return cpfPessoa;
    }
    public void setCpfPessoa(String cpfPessoa) {
        this.cpfPessoa = cpfPessoa;
    }
    public Long getCodigoLote() {
        return codigoLote;
    }
    public void setCodigoLote(Long codigoLote) {
        this.codigoLote = codigoLote;
    }
    public String getNomeVacina() {
        return nomeVacina;
    }
    public void setNomeVacina(String nomeVacina) {
        this.nomeVacina = nomeVacina;
    }

    @Override
    public String toString() {
        return "AplicacaoFilter [codigo=" + codigo + ", dataInicial=" + dataInicial + ", dataFinal=" + dataFinal
                + ", cpfPessoa=" + cpfPessoa + ", codigoLote=" + codigoLote + ", nomeVacina=" + nomeVacina + "]";
    }
}
