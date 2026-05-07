package com.liveklass.assignment.sale.service;

import com.liveklass.assignment.sale.dto.SalePayRequest;
import com.liveklass.assignment.sale.entity.SaleRecord;
import com.liveklass.assignment.sale.mapper.SaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleService {

    private final SaleMapper saleMapper;

    public SaleService(SaleMapper saleMapper) {
        this.saleMapper = saleMapper;
    }

    @Transactional
    public void pay(SalePayRequest request) {

        if (saleMapper.existsCourseByCourseId(request.getCourseId()) == 0) {
            throw new IllegalArgumentException("존재하지 않는 강의입니다.");
        }

        if (saleMapper.existsPaidSaleBySaleNum(request.getSaleNum()) > 0) {
            throw new IllegalArgumentException("이미 결제 승인된 판매번호입니다.");
        }

        SaleRecord saleRecord = new SaleRecord();

        saleRecord.setSaleNum(request.getSaleNum());
        saleRecord.setCourseId(request.getCourseId());
        saleRecord.setStudentId(request.getStudentId());
        saleRecord.setAmount(request.getAmount());
        saleRecord.setSaleStatus("PAID");
        saleRecord.setOccurredAt(request.getOccurredAt());

        saleMapper.insertSaleRecord(saleRecord);
    }
}