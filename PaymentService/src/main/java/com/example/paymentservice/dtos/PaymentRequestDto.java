package com.example.paymentservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long amount;
    private String orderId;
    private String phoneNumber;
    private String name;
    private String email;
}
