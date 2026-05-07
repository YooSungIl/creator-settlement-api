package com.liveklass.assignment.sale.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaleRecordResponse {

    private String saleNum;
    private String creatorId;
    private String creatorNm;
    private String courseId;
    private String courseTitle;
    private String studentId;
    private Long amount;
    private String saleStatus;
    private LocalDateTime occurredAt;
}