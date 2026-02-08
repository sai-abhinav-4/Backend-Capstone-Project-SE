package com.example.paymentservice.paymentgateway;

public interface IPaymentGateway {
    String createPaymentLink(Long amount,String orderId,String phoneNumber,String name, String email);
}
