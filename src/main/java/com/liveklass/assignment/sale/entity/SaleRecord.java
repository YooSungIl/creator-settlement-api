package com.liveklass.assignment.sale.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaleRecord {

    private Long saleSerlNum;
    private String saleNum;
    private String studentId;
    private Long amount;
    private String saleStatus;
    private LocalDateTime occurredAt;
    private LocalDateTime createdAt;
    private String courseId;

}