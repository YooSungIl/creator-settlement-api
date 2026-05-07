package com.liveklass.assignment.settlement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminCreatorSettlementResponse {
    private String creatorId;
    private String creatorNm;
    private Long expectedSettlementAmount;
}
