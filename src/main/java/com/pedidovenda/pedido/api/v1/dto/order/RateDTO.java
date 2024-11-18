package com.pedidovenda.pedido.api.v1.dto.order;

public class RateDTO {

    private String percentual;
    private String centroDeCusto;
    private String contaContabil;
    private String itemContabil;
    private String classeValor;

    public String getClasseValor() {
        return classeValor;
    }

    public void setClasseValor(String classeValor) {
        this.classeValor = classeValor;
    }

    public String getItemContabil() {
        return itemContabil;
    }

    public void setItemContabil(String itemContabil) {
        this.itemContabil = itemContabil;
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

    public String getPercentual() {
        return percentual;
    }

    public void setPercentual(String percentual) {
        this.percentual = percentual;
    }

    @Override
    public String toString() {
        return "RateDTO{" +
                "percentual='" + percentual + '\'' +
                ", centroDeCusto='" + centroDeCusto + '\'' +
                ", contaContabil='" + contaContabil + '\'' +
                ", itemContabil='" + itemContabil + '\'' +
                ", classeValor='" + classeValor + '\'' +
                '}';
    }
}
