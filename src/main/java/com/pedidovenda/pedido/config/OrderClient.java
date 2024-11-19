package com.pedidovenda.pedido.config;

import com.pedidovenda.pedido.api.v1.dto.order.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import reactor.core.publisher.Mono;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Dispatch;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
public class OrderClient {

    @Value("${soap.service.namespace-method-uri}")
    private String namespaceUrlMethod;

    @Value("${soap.service.wss-url}")
    private String wssUrl;

    @Value("${soap.service.username}")
    private String userName;

    @Value("${soap.service.userkey}")
    private String userKey;

    @Value("${soap.service.wsdl}")
    private String soapWsdl;

    @Value("${soap.service.namespace-url}")
    private String namespaceUrl;

    @Value("${soap.service.port-name}")
    private String port;

    @Value("${soap.service.service-name}")
    private String service;

    @Value("${soap.service.uri-siga}")
    private String uriSiga;


    public Mono<OrderResponseDTO> callSoapClient(OrderDTO orderDto, String token, String origin) throws Exception {
        OrderResponseDTO ord = new OrderResponseDTO();
        try {
            URL wsdlURL = new URL(soapWsdl);
            QName serviceName = new QName(namespaceUrl, service);
            QName portName = new QName(namespaceUrl, port);
            javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdlURL, serviceName);
            Dispatch<SOAPMessage> dispatch = service.createDispatch(portName, SOAPMessage.class, javax.xml.ws.Service.Mode.MESSAGE);

            dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, Boolean.TRUE);
            dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, uriSiga);

            MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPElement soapBodyElem = getSoapElement(soapMessage);
            mapOrderDTOToSoapBody(orderDto, soapBodyElem, token);

            soapMessage.saveChanges();
            logSoapMessage(soapMessage);

            SOAPMessage soapResponse = dispatch.invoke(soapMessage);
            SOAPBody responseBody = soapResponse.getSOAPBody();

            logSoapMessage(soapResponse);

            //seta o response
            String no = orderNumber(responseBody);
            ord.setApplication(origin);
            ord.setSalesOrder(no);
            ord.setCreatedAt(LocalDateTime.now());

            return Mono.just(ord);

        } catch (Exception e) {
            return Mono.error(new Exception("Erro ao enviar pedido: " + e.getMessage(), e));
        }
    }

    private SOAPElement getSoapElement(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("tns", namespaceUrlMethod);
        SOAPBody soapBody = envelope.getBody();

        SOAPHeader header = envelope.getHeader();
        SOAPElement security = header.addChildElement("Security", "wsse", wssUrl);
        SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
        SOAPElement username = usernameToken.addChildElement("Username", "wsse");
        username.addTextNode(userName);
        SOAPElement password = usernameToken.addChildElement("Password", "wsse");
        password.addTextNode(userKey);

        return soapBody.addChildElement("gerarPedido", "tns");
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
                parcelasElement.addChildElement("titulo").addTextNode(installment.getTitulo() != null ? installment.getTitulo() : "0");
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

    private String orderNumber(SOAPBody soapBody) throws SOAPException {

        NodeList gerarPedidoResponseList = soapBody.getElementsByTagNameNS(namespaceUrlMethod, "gerarPedidoResponse");
        if (gerarPedidoResponseList.getLength() == 0) {
            throw new SOAPException("Elemento 'ns0:gerarPedidoResponse' não encontrado");
        }
        Node gerarPedidoResponse = gerarPedidoResponseList.item(0);

        NodeList returnNodes = ((org.w3c.dom.Element) gerarPedidoResponse).getElementsByTagName("return");
        if (returnNodes.getLength() == 0) {
            throw new SOAPException("Elemento 'return' não encontrado");
        }

        String numeroPedido = returnNodes.item(0).getTextContent();

        if (numeroPedido == null || numeroPedido.trim().isEmpty()) {
            throw new SOAPException("Número do pedido está vazio");
        }

        try {
            return numeroPedido.trim();
        } catch (NumberFormatException e) {
            throw new SOAPException("Número do pedido inválido: " + numeroPedido, e);
        }
    }

    //melhorar usando logger
    private void logSoapMessage(SOAPMessage soapMessage) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            soapMessage.writeTo(stream);
            String message = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            System.out.println("Mensagem SOAP Enviada: \n" + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
