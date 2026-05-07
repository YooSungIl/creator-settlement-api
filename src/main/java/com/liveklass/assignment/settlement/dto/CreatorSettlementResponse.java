package com.liveklass.assignment.settlement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatorSettlementResponse {

    private String creatorId;
    private String creatorNm;

    private Long totalPaidAmount;
    private Long totalCanceledAmount;
    private Long netSaleAmount;

    private Long platformCommissionAmount;
    private Long expectedSettlementAmount;

    private Long paidCount;
    private Long canceledCount;

}