package com.liveklass.assignment.settlement.controller;

import com.liveklass.assignment.settlement.dto.AdminSettlementResponse;
import com.liveklass.assignment.settlement.dto.CreatorSettlementResponse;
import com.liveklass.assignment.settlement.service.SettlementService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @GetMapping("/creators/{creatorId}")
    public ResponseEntity<CreatorSettlementResponse> findCreatorMonthlySettlement(
            @PathVariable String creatorId,
            @RequestParam String yearMonth
    ) {
        CreatorSettlementResponse response = settlementService.findCreatorMonthlySettlement(creatorId, yearMonth);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    public ResponseEntity<AdminSettlementResponse> findAdminSettlement(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {
        AdminSettlementResponse response = settlementService.findAdminSettlement(fromDate, toDate);
        return ResponseEntity.ok(response);
    }
}