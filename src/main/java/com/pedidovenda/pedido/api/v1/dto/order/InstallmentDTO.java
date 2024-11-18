package com.pedidovenda.pedido.api.v1.dto.order;

public class InstallmentDTO {

    private String numero;
    private String valor;
    private String vencimento;
    private String titulo;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "InstallmentDTO{" +
                "numero='" + numero + '\'' +
                ", valor='" + valor + '\'' +
                ", vencimento='" + vencimento + '\'' +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
