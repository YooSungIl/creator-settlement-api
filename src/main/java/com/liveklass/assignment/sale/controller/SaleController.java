package com.liveklass.assignment.sale.controller;

import com.liveklass.assignment.sale.dto.SaleCancelRequest;
import com.liveklass.assignment.sale.dto.SalePayRequest;
import com.liveklass.assignment.sale.dto.SaleRecordResponse;
import com.liveklass.assignment.sale.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel(@Valid @RequestBody SaleCancelRequest request) {
        saleService.cancel(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SaleRecordResponse>> findSaleRecords(
            @RequestParam(required = false) String creatorId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        List<SaleRecordResponse> result = saleService.findSaleRecords(creatorId, fromDate, toDate);
        return ResponseEntity.ok(result);
    }
}