package com.pedidovenda.pedido.api.v1.converter;

import com.pedidovenda.pedido.api.v1.dto.order.InstallmentDTO;
import com.pedidovenda.pedido.api.v1.dto.order.ItemDTO;
import com.pedidovenda.pedido.api.v1.dto.order.OrderDTO;
import com.pedidovenda.pedido.api.v1.dto.order.RateDTO;

public class OrderSoapRequest {

    public String buildSoapRequest(OrderDTO pedidoDTO, String token, String username, String password) {
        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" ")
                .append("xmlns:ws=\"http://ws.iifcore.fiergs.org.br/\">")
                .append("<soapenv:Header>")
                .append("<wsse:Security soapenv:mustUnderstand=\"1\" xmlns:wsse=\"http://schemas.xmlsoap.org/ws/2002/12/secext\">")
                .append("<wsse:UsernameToken>")
                .append("<wsse:Username>").append(username).append("</wsse:Username>")
                .append("<wsse:Password>").append(password).append("</wsse:Password>")
                .append("</wsse:UsernameToken>")
                .append("</wsse:Security>")
                .append("</soapenv:Header>")
                .append("<soapenv:Body>")
                .append("<ws:gerarPedido>")
                .append("<token>").append(token).append("</token>")
                .append("<pedido>")
                .append("<filial>").append(pedidoDTO.getFilial()).append("</filial>")
                .append("<cliente>").append(pedidoDTO.getCliente()).append("</cliente>")
                .append("<tipo>").append(pedidoDTO.getTipo()).append("</tipo>")
                .append("<condicaoPagamento>").append(pedidoDTO.getCondicaoPagamento()).append("</condicaoPagamento>")
                .append("<dataEmissao>").append(pedidoDTO.getDataEmissao()).append("</dataEmissao>")
                .append("<convenio>").append(pedidoDTO.getConvenio()).append("</convenio>")
                .append("<matricula>").append(pedidoDTO.getMatricula()).append("</matricula>")
                .append("<nome>").append(pedidoDTO.getNome()).append("</nome>")
                .append("<serie>").append(pedidoDTO.getSerie()).append("</serie>")
                .append("<faturamentoAutomatico>").append(pedidoDTO.getFaturamentoAutomatico()).append("</faturamentoAutomatico>")
                .append("<natureza>").append(pedidoDTO.getNatureza()).append("</natureza>")
                .append("<tipoFrete>").append(pedidoDTO.getTipoFrete()).append("</tipoFrete>")
                .append("<valorFrete>").append(pedidoDTO.getValorFrete()).append("</valorFrete>")
                .append("<banco>").append(pedidoDTO.getBanco()).append("</banco>")
                .append("<nota>").append(pedidoDTO.getNota()).append("</nota>")
                .append("<agedep>").append(pedidoDTO.getAgedep()).append("</agedep>")
                .append("<conta>").append(pedidoDTO.getConta()).append("</conta>")
                .append("<mensagemDaNota>").append(pedidoDTO.getMensagemDaNota()).append("</mensagemDaNota>")
                .append("<despachante>").append(pedidoDTO.getDespachante()).append("</despachante>");

        for (InstallmentDTO parcela : pedidoDTO.getParcelas()) {
            xmlBuilder.append("<parcela>")
                    .append("<numero>").append(parcela.getNumero()).append("</numero>")
                    .append("<valor>").append(parcela.getValor()).append("</valor>")
                    .append("<vencimento>").append(parcela.getVencimento()).append("</vencimento>")
                    .append("<titulo>").append(parcela.getTitulo()).append("</titulo>")
                    .append("</parcela>");
        }

        for (ItemDTO item : pedidoDTO.getItens()) {
            xmlBuilder.append("<item>")
                    .append("<num>").append(item.getNum()).append("</num>")
                    .append("<produto>").append(item.getProduto()).append("</produto>")
                    .append("<quantidadeVenda>").append(item.getQuantidadeVenda()).append("</quantidadeVenda>")
                    .append("<precoVenda>").append(item.getPrecoVenda()).append("</precoVenda>")
                    .append("<valor>").append(item.getValor()).append("</valor>")
                    .append("<quantidadeLiberada>").append(item.getQuantidadeLiberada()).append("</quantidadeLiberada>")
                    .append("<contaContabil>").append(item.getContaContabil()).append("</contaContabil>")
                    .append("<centroDeCusto>").append(item.getCentroDeCusto()).append("</centroDeCusto>")
                    .append("<itemContabil>").append(item.getItemContabil()).append("</itemContabil>")
                    .append("<classeValor>").append(item.getClasseValor()).append("</classeValor>")
                    .append("<matricula>").append(item.getMatricula()).append("</matricula>")
                    .append("<nome>").append(item.getNome()).append("</nome>")
                    .append("<ordemServico>").append(item.getOrdemServico()).append("</ordemServico>");

            for (RateDTO rateio : item.getRateios()) {
                xmlBuilder.append("<rateio>")
                        .append("<percentual>").append(rateio.getPercentual()).append("</percentual>")
                        .append("<centroDeCusto>").append(rateio.getCentroDeCusto()).append("</centroDeCusto>")
                        .append("<contaContabil>").append(rateio.getContaContabil()).append("</contaContabil>")
                        .append("<itemContabil>").append(rateio.getItemContabil()).append("</itemContabil>")
                        .append("<classeValor>").append(rateio.getClasseValor()).append("</classeValor>")
                        .append("</rateio>");
            }

            xmlBuilder.append("</item>");
        }

        xmlBuilder.append("</pedido>")
                .append("</ws:gerarPedido>")
                .append("</soapenv:Body>")
                .append("</soapenv:Envelope>");

        System.out.println(xmlBuilder.toString());
        return xmlBuilder.toString();
    }
}
