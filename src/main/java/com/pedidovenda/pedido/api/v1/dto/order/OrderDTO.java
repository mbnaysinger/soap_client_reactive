package com.pedidovenda.pedido.api.v1.dto.order;

import java.util.List;

public class OrderDTO {

    private String filial;
    private String cliente;
    private String tipo;
    private String condicaoPagamento;
    private String dataEmissao;
    private String convenio;
    private String matricula;
    private String nome;
    private String serie;
    private String faturamentoAutomatico;
    private String natureza;
    private String tipoFrete;
    private String valorFrete;
    private String banco;
    private String nota;
    private String agedep;
    private String conta;
    private String mensagemDaNota;
    private String despachante;
    private List<InstallmentDTO> parcelas;
    private List<ItemDTO> itens;

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(String condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getConvenio() {
        return convenio;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getFaturamentoAutomatico() {
        return faturamentoAutomatico;
    }

    public void setFaturamentoAutomatico(String faturamentoAutomatico) {
        this.faturamentoAutomatico = faturamentoAutomatico;
    }

    public String getNatureza() {
        return natureza;
    }

    public void setNatureza(String natureza) {
        this.natureza = natureza;
    }

    public String getTipoFrete() {
        return tipoFrete;
    }

    public void setTipoFrete(String tipoFrete) {
        this.tipoFrete = tipoFrete;
    }

    public String getValorFrete() {
        return valorFrete;
    }

    public void setValorFrete(String valorFrete) {
        this.valorFrete = valorFrete;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getAgedep() {
        return agedep;
    }

    public void setAgedep(String agedep) {
        this.agedep = agedep;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getMensagemDaNota() {
        return mensagemDaNota;
    }

    public void setMensagemDaNota(String mensagemDaNota) {
        this.mensagemDaNota = mensagemDaNota;
    }

    public String getDespachante() {
        return despachante;
    }

    public void setDespachante(String despachante) {
        this.despachante = despachante;
    }

    public List<InstallmentDTO> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<InstallmentDTO> parcelas) {
        this.parcelas = parcelas;
    }

    public List<ItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemDTO> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "filial='" + filial + '\'' +
                ", cliente='" + cliente + '\'' +
                ", tipo='" + tipo + '\'' +
                ", condicaoPagamento='" + condicaoPagamento + '\'' +
                ", dataEmissao='" + dataEmissao + '\'' +
                ", convenio='" + convenio + '\'' +
                ", matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", serie='" + serie + '\'' +
                ", faturamentoAutomatico='" + faturamentoAutomatico + '\'' +
                ", natureza='" + natureza + '\'' +
                ", tipoFrete='" + tipoFrete + '\'' +
                ", valorFrete='" + valorFrete + '\'' +
                ", banco='" + banco + '\'' +
                ", nota='" + nota + '\'' +
                ", agedep='" + agedep + '\'' +
                ", conta='" + conta + '\'' +
                ", mensagemDaNota='" + mensagemDaNota + '\'' +
                ", despachante='" + despachante + '\'' +
                ", parcelas=" + parcelas +
                ", itens=" + itens +
                '}';
    }
}
