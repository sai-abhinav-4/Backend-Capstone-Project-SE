package com.example.paymentservice.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/stripe")
public class StripWebHookController {

    @PostMapping
    public void listenToStripeWebHooks(@RequestBody String event) {
        System.out.println("Received Stripe event: " + event);
    }

}

