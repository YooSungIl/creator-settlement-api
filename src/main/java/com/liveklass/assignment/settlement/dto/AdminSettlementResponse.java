package com.liveklass.assignment.settlement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminSettlementResponse {
    private List<AdminCreatorSettlementResponse> creators;
    private Long totalExpectedSettlementAmount;
}
