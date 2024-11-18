package com.pedidovenda.pedido.api.v1.converter;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import reactor.core.publisher.Mono;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class OrderSoapResponse {

    public Mono<Long> buildSoapResponse(String response) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));

            System.out.println("Chegou aqui no response...");

            NodeList returnNodes = document.getElementsByTagName("return");
            if (returnNodes.getLength() > 0) {
                String returnValue = returnNodes.item(0).getTextContent();
                return Mono.just(Long.parseLong(returnValue));
            } else {
                return Mono.error(new RuntimeException("No <return> element found in response"));
            }
        } catch (Exception e) {
            return Mono.error(new RuntimeException("Failed to parse SOAP response", e));
        }
    }
}
