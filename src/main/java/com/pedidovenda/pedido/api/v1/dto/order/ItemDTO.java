package com.pedidovenda.pedido.api.v1.dto.order;

import java.util.List;

public class ItemDTO {

    private String num;
    private String produto;
    private String quantidadeVenda;
    private String precoVenda;
    private String valor;
    private String quantidadeLiberada;
    private String contaContabil;
    private String centroDeCusto;
    private String itemContabil;
    private String classeValor;
    private String matricula;
    private String nome;
    private String ordemServico;
    private List<RateDTO> rateios;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public String getQuantidadeVenda() {
        return quantidadeVenda;
    }

    public void setQuantidadeVenda(String quantidadeVenda) {
        this.quantidadeVenda = quantidadeVenda;
    }

    public String getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(String precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getQuantidadeLiberada() {
        return quantidadeLiberada;
    }

    public void setQuantidadeLiberada(String quantidadeLiberada) {
        this.quantidadeLiberada = quantidadeLiberada;
    }

    public String getContaContabil() {
        return contaContabil;
    }

    public void setContaContabil(String contaContabil) {
        this.contaContabil = contaContabil;
    }

    public String getCentroDeCusto() {
        return centroDeCusto;
    }

    public void setCentroDeCusto(String centroDeCusto) {
        this.centroDeCusto = centroDeCusto;
    }

    public String getItemContabil() {
        return itemContabil;
    }

    public void setItemContabil(String itemContabil) {
        this.itemContabil = itemContabil;
    }

    public String getClasseValor() {
        return classeValor;
    }

    public void setClasseValor(String classeValor) {
        this.classeValor = classeValor;
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

    public String getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(String ordemServico) {
        this.ordemServico = ordemServico;
    }

    public List<RateDTO> getRateios() {
        return rateios;
    }

    public void setRateios(List<RateDTO> rateios) {
        this.rateios = rateios;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "num='" + num + '\'' +
                ", produto='" + produto + '\'' +
                ", quantidadeVenda='" + quantidadeVenda + '\'' +
                ", precoVenda='" + precoVenda + '\'' +
                ", valor='" + valor + '\'' +
                ", quantidadeLiberada='" + quantidadeLiberada + '\'' +
                ", contaContabil='" + contaContabil + '\'' +
                ", centroDeCusto='" + centroDeCusto + '\'' +
                ", itemContabil='" + itemContabil + '\'' +
                ", classeValor='" + classeValor + '\'' +
                ", matricula='" + matricula + '\'' +
                ", nome='" + nome + '\'' +
                ", ordemServico='" + ordemServico + '\'' +
                ", rateios=" + rateios +
                '}';
    }
}
