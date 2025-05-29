package com.example.carsharing.service.impl;


import com.example.carsharing.model.Payment;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WayForPayService {

    private final String merchantAccount = "example_com43";
    private final String merchantDomainName = "https://example.com";
    private final String secretKey = "a5714b026bf4344e16959533aeb058cebc77cf60";
    private final String createLinkUrl = "https://secure.wayforpay.com/pay?behavior=offline";
    private final String checkStatusUrl = "https://api.wayforpay.com/api";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String createLink(Payment payment) throws Exception {
        long orderDate = Instant.now().getEpochSecond();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("merchantAccount", merchantAccount);
        payload.put("merchantDomainName", merchantDomainName);
        payload.put("orderReference", payment.getId().toString());
        payload.put("amount", payment.getAmount());
        payload.put("orderDate", orderDate);
        payload.put("currency", "UAH");
        payload.put("orderTimeout", 900_000);
        payload.put("productName[]", List.of(payment.getRental().getCar().getModel()));
        payload.put("productPrice[]", List.of(payment.getAmount()));
        payload.put("productCount[]", List.of(1));
        payload.put("clientAccountId", payment.getUser().getId().toString());
        payload.put("defaultPaymentSystem", "card");
        payload.put("language", "ua");

        String signature = generatePaymentSignature(payload);
        payload.put("merchantSignature", signature);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(createLinkUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormDataFromMap(payload))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());

        if (json.has("url")) {
            return json.get("url").asText();
        } else {
            throw new RuntimeException("Failed to generate payment link: " + response.body());
        }
    }

    private String generatePaymentSignature(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        sb.append(data.get("merchantAccount")).append(";");
        sb.append(data.get("merchantDomainName")).append(";");
        sb.append(data.get("orderReference")).append(";");
        sb.append(data.get("orderDate")).append(";");
        sb.append(data.get("amount")).append(";");
        sb.append(data.get("currency")).append(";");

        List<String> productNames = getList(data, "productName[]");
        sb.append(String.join(";", productNames)).append(";");

        List<Object> productCounts = getList(data, "productCount[]");
        sb.append(String.join(";", productCounts.stream().map(Object::toString).collect(Collectors.toList()))).append(";");

        List<Object> productPrices = getList(data, "productPrice[]");
        sb.append(String.join(";", productPrices.stream().map(Object::toString).collect(Collectors.toList())));

        String signatureString = sb.toString();
        if (signatureString.endsWith(";")) {
            signatureString = signatureString.substring(0, signatureString.length() - 1);
        }
        System.out.println("Signature input string: " + secretKey + signatureString);
        return getSignature(signatureString);
    }

    private String getSignature(String signatureString) {
        String signature = null;
        try {
            Mac mac = Mac.getInstance("HmacMD5");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacMD5");
            mac.init(secretKeySpec);

            byte[] rawHmac = mac.doFinal(signatureString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : rawHmac) {
                hexString.append(String.format("%02x", b));
            }

            signature = hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return signature;
    }


    @SuppressWarnings("unchecked")
    private <T> List<T> getList(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof List) {
            return (List<T>) value;
        }
        return List.of();
    }

    private String generateHistorySignature(String orderReference) {
        String data = merchantAccount + ";" + orderReference;
        return getSignature(data);
    }

    public boolean checkStatus(String paymentId) throws Exception {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("transactionType", "CHECK_STATUS");
        payload.put("merchantAccount", merchantAccount);
        payload.put("orderReference", paymentId.toString());
        payload.put("apiVersion", 1);

        String signature = generateHistorySignature(paymentId.toString());
        payload.put("merchantSignature", signature);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(checkStatusUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonNode json = objectMapper.readTree(response.body());
        return json.path("reason").asText().equalsIgnoreCase("Ok");
    }


    private HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, Object> data) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue() instanceof List<?> list) {
                for (Object val : list) {
                    builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(String.valueOf(val), StandardCharsets.UTF_8))
                            .append("&");
                }
            } else {
                builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(String.valueOf(entry.getValue()), StandardCharsets.UTF_8))
                        .append("&");
            }
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}
