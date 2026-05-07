package com.liveklass.assignment.sale.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaleCancelRequest {

    @NotBlank
    private String saleNum;

    @NotNull
    @Min(1)
    private Long amount;

    @NotNull
    private LocalDateTime occurredAt;

}