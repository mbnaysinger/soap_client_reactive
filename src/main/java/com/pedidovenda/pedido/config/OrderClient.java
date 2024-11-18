package com.pedidovenda.pedido.config;

import com.pedidovenda.pedido.api.v1.dto.order.InstallmentDTO;
import com.pedidovenda.pedido.api.v1.dto.order.ItemDTO;
import com.pedidovenda.pedido.api.v1.dto.order.OrderDTO;
import com.pedidovenda.pedido.api.v1.dto.order.RateDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Dispatch;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class OrderClient {

    @Value("${soap.service.wsdl}")
    private String soapWsdl;

    @Value("${soap.service.namespace-url}")
    private String namespaceUrl;

    @Value("${soap.service.namespace-method-uri}")
    private String namespaceUrlMethod;

    @Value("${soap.service.port-name}")
    private String port;

    @Value("${soap.service.service-name}")
    private String service;

    @Value("${soap.service.uri-siga}")
    private String uriSiga;


    public Mono<Long> enviarPedido(OrderDTO orderDto, String token) throws Exception {
        try {
            URL wsdlURL = new URL(soapWsdl);
            QName serviceName = new QName(namespaceUrl, service);
            QName portName = new QName(namespaceUrl, port);
            javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlURL, serviceName);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, javax.xml.ws.Service.Mode.MESSAGE);

            // Configuração do tipo de conteúdo e SOAPAction
            dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
            dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, uriSiga);

            // Criação da mensagem SOAP
            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            // Criação do envelope SOAP
            SOAPEnvelope envelope = soapPart.getEnvelope();
            envelope.addNamespaceDeclaration("tns", "http://ws.iifcore.fiergs.org.br/");
            SOAPBody soapBody = envelope.getBody();

            // Adicionando cabeçalho de segurança
            SOAPHeader header = envelope.getHeader();
            SOAPElement security = header.addChildElement("Security", "wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
            SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
            SOAPElement username = usernameToken.addChildElement("Username", "wsse");
            username.addTextNode("daniel.chaves");
            SOAPElement password = usernameToken.addChildElement("Password", "wsse");
            password.addTextNode("dbtest01");

            // Criação do corpo do SOAP e mapeamento do OrderDTO e token
            SOAPElement soapBodyElem = soapBody.addChildElement("gerarPedido", "tns");
            mapOrderDTOToSoapBody(orderDto, soapBodyElem, token);

            // Salvando alterações e enviando a mensagem
            soapMessage.saveChanges();

            // Log da mensagem SOAP
            logSoapMessage(soapMessage);

            SOAPMessage soapResponse = dispatch.invoke(soapMessage);

            // Processa a resposta para extrair o número do pedido
            SOAPBody responseBody = soapResponse.getSOAPBody();

            logSoapMessage(soapResponse);

            return Mono.just(orderNumber(responseBody));

        } catch (Exception e) {
            return Mono.error(new Exception("Erro ao enviar pedido: " + e.getMessage(), e));
        }
    }

    private void mapOrderDTOToSoapBody(OrderDTO orderDto, SOAPElement rootElement, String token) throws SOAPException {

        rootElement.addChildElement("token").addTextNode(token);
        SOAPElement pedidoElement = rootElement.addChildElement("pedido");
        pedidoElement.addChildElement("filial").addTextNode(orderDto.getFilial() != null ? orderDto.getFilial() : "");
        pedidoElement.addChildElement("cliente").addTextNode(orderDto.getCliente() != null ? orderDto.getCliente() : "");
        pedidoElement.addChildElement("tipo").addTextNode(orderDto.getTipo() != null ? orderDto.getTipo() : "");
        pedidoElement.addChildElement("condicaoPagamento").addTextNode(orderDto.getCondicaoPagamento() != null ? orderDto.getCondicaoPagamento() : "");
        pedidoElement.addChildElement("dataEmissao").addTextNode(orderDto.getDataEmissao() != null ? orderDto.getDataEmissao() : "");
        pedidoElement.addChildElement("convenio").addTextNode(orderDto.getConvenio() != null ? orderDto.getConvenio() : "");
        pedidoElement.addChildElement("matricula").addTextNode(orderDto.getMatricula() != null ? orderDto.getMatricula() : "");
        pedidoElement.addChildElement("nome").addTextNode(orderDto.getNome() != null ? orderDto.getNome() : "");
        pedidoElement.addChildElement("serie").addTextNode(orderDto.getSerie() != null ? orderDto.getSerie() : "");
        pedidoElement.addChildElement("faturamentoAutomatico").addTextNode(orderDto.getFaturamentoAutomatico() != null ? orderDto.getFaturamentoAutomatico() : "");
        pedidoElement.addChildElement("natureza").addTextNode(orderDto.getNatureza() != null ? orderDto.getNatureza() : "");
        pedidoElement.addChildElement("tipoFrete").addTextNode(orderDto.getTipoFrete() != null ? orderDto.getTipoFrete() : "");
        pedidoElement.addChildElement("valorFrete").addTextNode(orderDto.getValorFrete() != null ? orderDto.getValorFrete() : "");
        pedidoElement.addChildElement("banco").addTextNode(orderDto.getBanco() != null ? orderDto.getBanco() : "");
        pedidoElement.addChildElement("nota").addTextNode(orderDto.getNota() != null ? orderDto.getNota() : "");
        pedidoElement.addChildElement("agedep").addTextNode(orderDto.getAgedep() != null ? orderDto.getAgedep() : "");
        pedidoElement.addChildElement("conta").addTextNode(orderDto.getConta() != null ? orderDto.getConta() : "");
        pedidoElement.addChildElement("mensagemDaNota").addTextNode(orderDto.getMensagemDaNota() != null ? orderDto.getMensagemDaNota() : "");
        pedidoElement.addChildElement("despachante").addTextNode(orderDto.getDespachante() != null ? orderDto.getDespachante() : "");

        if (orderDto.getParcelas() != null) {
            SOAPElement parcelasElement = pedidoElement.addChildElement("parcela");
            for (InstallmentDTO installment : orderDto.getParcelas()) {
                parcelasElement.addChildElement("numero").addTextNode(installment.getNumero() != null ? installment.getNumero() : "");
                parcelasElement.addChildElement("valor").addTextNode(installment.getValor() != null ? installment.getValor() : "");
                parcelasElement.addChildElement("vencimento").addTextNode(installment.getVencimento() != null ? installment.getVencimento() : "");
                parcelasElement.addChildElement("titulo").addTextNode(installment.getTitulo() != null ? installment.getTitulo() : "");
            }
        }

        if (orderDto.getItens() != null) {
            SOAPElement itemElement = pedidoElement.addChildElement("item");
            for (ItemDTO item : orderDto.getItens()) {
                itemElement.addChildElement("num").addTextNode(item.getNum() != null ? item.getNum() : "");
                itemElement.addChildElement("produto").addTextNode(item.getProduto() != null ? item.getProduto() : "");
                itemElement.addChildElement("quantidadeVenda").addTextNode(item.getQuantidadeVenda() != null ? item.getQuantidadeVenda() : "");
                itemElement.addChildElement("precoVenda").addTextNode(item.getPrecoVenda() != null ? item.getPrecoVenda() : "");
                itemElement.addChildElement("valor").addTextNode(item.getValor() != null ? item.getValor() : "");
                itemElement.addChildElement("quantidadeLiberada").addTextNode(item.getQuantidadeLiberada() != null ? item.getQuantidadeLiberada() : "");
                itemElement.addChildElement("contaContabil").addTextNode(item.getContaContabil() != null ? item.getContaContabil() : "");
                itemElement.addChildElement("centroDeCusto").addTextNode(item.getCentroDeCusto() != null ? item.getCentroDeCusto() : "");
                itemElement.addChildElement("itemContabil").addTextNode(item.getItemContabil() != null ? item.getItemContabil() : "");
                itemElement.addChildElement("classeValor").addTextNode(item.getClasseValor() != null ? item.getClasseValor() : "");
                itemElement.addChildElement("matricula").addTextNode(item.getMatricula() != null ? item.getMatricula() : "");
                itemElement.addChildElement("nome").addTextNode(item.getNome() != null ? item.getNome() : "");
                itemElement.addChildElement("ordemServico").addTextNode(item.getOrdemServico() != null ? item.getOrdemServico() : "");

                if (item.getRateios() != null) {
                    SOAPElement rateElement = itemElement.addChildElement("rateio");
                    for (RateDTO rate : item.getRateios()) {
                        rateElement.addChildElement("percentual").addTextNode(rate.getPercentual() != null ? rate.getPercentual() : "");
                        rateElement.addChildElement("centroDeCusto").addTextNode(rate.getCentroDeCusto() != null ? rate.getCentroDeCusto() : "");
                        rateElement.addChildElement("contaContabil").addTextNode(rate.getContaContabil() != null ? rate.getContaContabil() : "");
                        rateElement.addChildElement("itemContabil").addTextNode(rate.getItemContabil() != null ? rate.getItemContabil() : "");
                        rateElement.addChildElement("classeValor").addTextNode(rate.getClasseValor() != null ? rate.getClasseValor() : "");
                    }
                }
            }
        }
    }

    private Long orderNumber(SOAPBody respSoap) throws SOAPException {
        SOAPElement pedidoResponseElement = (SOAPElement) respSoap.getElementsByTagName("gerarPedidoResponse").item(0);
        if (pedidoResponseElement != null) {
            return Long.valueOf(pedidoResponseElement.getElementsByTagName("return").item(0).getTextContent());
        }
        throw new SOAPException("Resposta SOAP não contém o número do pedido esperado.");
    }

    private void logSoapMessage(SOAPMessage soapMessage) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            soapMessage.writeTo(stream);
            String message = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("Mensagem SOAP Enviada: \n" + message); // Ou logue usando uma biblioteca de logging
        } catch (Exception e) {
            e.printStackTrace(); // Log da exceção ao tentar logar a mensagem
        }
    }

    private void logSoapMessage(SOAPBody responseBody) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            responseBody.wait(stream.size());
            String message = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("Mensagem SOAP Recebida: \n" + message); // Ou logue usando uma biblioteca de logging
        } catch (Exception e) {
            e.printStackTrace(); // Log da exceção ao tentar logar a mensagem
        }
    }
}
