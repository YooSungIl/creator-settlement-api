package com.liveklass.assignment.sale.controller;

import com.liveklass.assignment.sale.dto.SalePayRequest;
import com.liveklass.assignment.sale.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/pay")
    public ResponseEntity<Void> pay(@Valid @RequestBody SalePayRequest request) {
        saleService.pay(request);
        return ResponseEntity.ok().build();
    }
}