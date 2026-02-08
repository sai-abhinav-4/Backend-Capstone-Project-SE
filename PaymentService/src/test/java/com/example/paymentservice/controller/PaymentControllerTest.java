// java
package com.example.paymentservice.controller;

import com.example.paymentservice.controllers.PaymentController;
import com.example.paymentservice.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    @Test
    void generatePaymentLink_returnsLink_and_callsService() throws Exception {
        String expectedLink = "https://pay.example/link/123";
        when(paymentService.getPaymentLink(any(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(expectedLink);

        Map<String, Object> payload = new HashMap<>();
        payload.put("amount", 100);
        payload.put("orderId", "order123");
        payload.put("phoneNumber", "1234567890");
        payload.put("name", "John");
        payload.put("email", "john@example.com");

        mockMvc.perform(post("/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedLink));

        verify(paymentService, times(1))
                .getPaymentLink(any(), eq("order123"), eq("1234567890"), eq("John"), eq("john@example.com"));
        verifyNoMoreInteractions(paymentService);
    }
}

