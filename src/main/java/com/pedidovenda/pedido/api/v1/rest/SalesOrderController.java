package com.pedidovenda.pedido.api.v1.rest;

import br.org.fiergs.common.model.exception.AccessForbiddenException;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@RestController("SalesOrderControllerV1")
@RequestMapping("/v1")
public class SalesOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SalesOrderController.class);
    private static final String AUTHORIZATION_ERROR = "Acesso negado";

    private String apiKey;

    public SalesOrderController(@Value("${app.bank-slip-api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    @GetMapping("/orders")
    public Mono<List<Map<String, String>>> getMockBankSlips(@RequestParam Optional<Boolean> open) {
        List<Map<String, String>> mockList = Arrays.asList(
                createMockRecord("1", "8232681942", "SOF", "Boleto DNS", "28000640190124160", "000043127/NFS", "F58773", "A"),
                createMockRecord("2", "8232664683", "CDE", "Boleto CDE", "28000640190124150", "000043128/NFS", "F58770", "A"),
                createMockRecord("3", "8232660986", "SOF", "Boleto DNS", "28000640190124140", "000043129/NFS", "F58768", "A"),
                createMockRecord("4", "8232576912", "CDE", "Boleto CDE", "28000640190124130", "000043130/NFS", "F58766", "A"),
                createMockRecord("5", "8232561927", "SOF", "Boleto DNS", "28000640190124120", "000043131/NFS", "F58741", "C"),
                createMockRecord("6", "8232551593", "CDE", "Boleto CDE", "28000640190124110", "000043132/NFS", "F58720", "C")
        );

        List<Map<String, String>> filteredList = mockList;

        // Se a flag estiver presente e for verdadeira, filtrar para apenas status "A"
        if (open.isPresent() && open.get()) {
            filteredList = mockList.stream()
                    .filter(order -> "A".equals(order.get("status")))
                    .collect(Collectors.toList());
        }

        return Mono.just(filteredList);
    }

    private Map<String, String> createMockRecord(String id, String correlationId, String sourceSystem,
                                                 String integrationMessage, String securitiesNumber, String rps, String salesOrderNumber, String status) {
        Map<String, String> record = new HashMap<>();
        record.put("id", id);
        record.put("correlationId", correlationId);
        record.put("sourceSystem", sourceSystem);
        record.put("integrationMessage", integrationMessage);
        record.put("securitiesNumber", securitiesNumber);
        record.put("rps", rps);
        record.put("salesOrderNumber", salesOrderNumber);
        record.put("status", status);
        return record;
    }

    @PostMapping(value = "/upload-nf")
    public Mono<String> uploadFileBase64(
            @RequestParam("base64File") String base64File,
            @RequestParam("id") String id,
            @RequestHeader("X-API-Key") String apiKeyParam) {

        return Mono.justOrEmpty(apiKey.equals(apiKeyParam) ? apiKey : null)
                .switchIfEmpty(Mono.error(new AccessForbiddenException(AUTHORIZATION_ERROR)))
                .flatMap(validApiKey -> {
                    try {
                        //apenas para perfumaria, tudo mockado.
                        byte[] decodedBytes = Base64.decodeBase64(base64File);

                        LOGGER.debug("Arquivo decodificado com ID: {}", id);

                        return Mono.just("Arquivo recebido com sucesso");
                    } catch (Exception e) {
                        LOGGER.error("Erro ao decodificar arquivo Base64: {}", e.getMessage());
                        return Mono.error(new IllegalArgumentException("Arquivo Base64 inválido"));
                    }
                });
    }
}
