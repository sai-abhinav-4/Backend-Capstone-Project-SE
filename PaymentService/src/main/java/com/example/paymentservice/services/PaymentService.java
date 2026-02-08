package com.example.paymentservice.services;

import com.example.paymentservice.paymentgateway.IPaymentGateway;
import com.example.paymentservice.paymentgateway.PaymentGatewayChooserStartegy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentGatewayChooserStartegy paymentGatewayChooserStartegy;

    public String getPaymentLink(Long amount,String orderId,String phoneNumber,String name, String email){
        IPaymentGateway paymentGateway = paymentGatewayChooserStartegy.getBestPaymentGateway();
        return paymentGateway.createPaymentLink(amount,orderId,phoneNumber,name,email);
    }
}
