package com.example.carsharing.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(config = MapperConfig.class, uses = {PaymentMapper.class})
public interface PaymentMapper {
    @Mapping(target = "status", constant = "UNPAID")
    Payment toModel(PaymentCreationRequestDto paymentRequestDto);

    @Mapping(target = "userId", expression = "java(payment.getUser().getId())")
    @Mapping(target = "rentalId", expression = "java(payment.getRental().getId())")
    PaymentResponseDto toDto(Payment payment);
}
