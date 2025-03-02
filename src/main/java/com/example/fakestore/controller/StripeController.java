package com.example.fakestore.controller;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stripe")
@CrossOrigin(origins = "http://localhost:4200") // Allow frontend
public class StripeController {

    @Value("${stripe.secretKey}") // Use application.properties for security
    private String secretKey;


    @PostMapping("/checkout")
    public Map<String, String> checkout(@RequestBody Map<String, Object> data) throws StripeException {
        Stripe.apiKey = secretKey;
    
        // Retrieve 'items' safely
        Object rawItems = data.get("items");
        if (!(rawItems instanceof List<?> rawList)) {
            throw new IllegalArgumentException("Invalid format for 'items' field");
        }
    
        // Safe conversion to List<Map<String, Object>> using generics and explicit checks
        List<Map<String, Object>> items = rawList.stream()
                .filter(item -> item instanceof Map<?, ?>)
                .map(item -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> safeItem = (Map<String, Object>) item;
                    return safeItem;
                })
                .toList();
    
        // Build Stripe session parameters
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success")
                .setCancelUrl("http://localhost:4200/cancel");
    
        for (Map<String, Object> item : items) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(((Number) item.get("quantity")).longValue()) // Safe conversion
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("eur")
                                            .setUnitAmount(((Number) item.get("price")).longValue() * 100) // Convert to cents
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName((String) item.get("name"))
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }
    
        // Create Stripe session
        Session session = Session.create(paramsBuilder.build());
    
        // Return session ID to frontend
        Map<String, String> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }
    
    
}
