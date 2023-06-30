package web.controlevacinacao.filter;

import java.time.LocalDate;

public class LoteFilter {

    private Long codigo;
    private LocalDate validadeInicial;
    private LocalDate validadeFinal;
    private Integer nroDosesDoLoteInicial;
    private Integer nroDosesDoLoteFinal;
    private Integer nroDosesAtualInicial;
    private Integer nroDosesAtualFinal;
    private Long codigoVacina;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getValidadeInicial() {
        return validadeInicial;
    }

    public void setValidadeInicial(LocalDate validadeInicial) {
        this.validadeInicial = validadeInicial;
    }

    public LocalDate getValidadeFinal() {
        return validadeFinal;
    }

    public void setValidadeFinal(LocalDate validadeFinal) {
        this.validadeFinal = validadeFinal;
    }

    public Integer getNroDosesDoLoteInicial() {
        return nroDosesDoLoteInicial;
    }

    public void setNroDosesDoLoteInicial(Integer nroDosesDoLoteInicial) {
        this.nroDosesDoLoteInicial = nroDosesDoLoteInicial;
    }

    public Integer getNroDosesDoLoteFinal() {
        return nroDosesDoLoteFinal;
    }

    public void setNroDosesDoLoteFinal(Integer nroDosesDoLoteFinal) {
        this.nroDosesDoLoteFinal = nroDosesDoLoteFinal;
    }

    public Integer getNroDosesAtualInicial() {
        return nroDosesAtualInicial;
    }

    public void setNroDosesAtualInicial(Integer nroDosesAtualInicial) {
        this.nroDosesAtualInicial = nroDosesAtualInicial;
    }

    public Integer getNroDosesAtualFinal() {
        return nroDosesAtualFinal;
    }

    public void setNroDosesAtualFinal(Integer nroDosesAtualFinal) {
        this.nroDosesAtualFinal = nroDosesAtualFinal;
    }

    public Long getCodigoVacina() {
        return codigoVacina;
    }

    public void setCodigoVacina(Long codigoVacina) {
        this.codigoVacina = codigoVacina;
    }

    @Override
    public String toString() {
        return "codigo: " + codigo + "\nvalidadeInicial: " + validadeInicial + "\nvalidadeFinal: " + validadeFinal
                + "\nnroDosesDoLoteInicial: " + nroDosesDoLoteInicial + "\nnroDosesDoLoteFinal: " + nroDosesDoLoteFinal
                + "\nnroDosesAtualInicial: " + nroDosesAtualInicial + "\nnroDosesAtualFinal: " + nroDosesAtualFinal
                + "\ncodigoVacina: " + codigoVacina;
    }

}
