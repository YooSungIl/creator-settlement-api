package com.liveklass.assignment.sale.service;

import com.liveklass.assignment.sale.dto.SaleCancelRequest;
import com.liveklass.assignment.sale.dto.SalePayRequest;
import com.liveklass.assignment.sale.dto.SaleRecordResponse;
import com.liveklass.assignment.sale.entity.SaleRecord;
import com.liveklass.assignment.sale.mapper.SaleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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

    @Transactional
    public void cancel(SaleCancelRequest request) {

        SaleRecord paidSale = saleMapper.findPaidSaleBySaleNum(request.getSaleNum());

        if (paidSale == null) {
            throw new IllegalArgumentException("결제 승인 내역이 존재하지 않습니다.");
        }

        if (saleMapper.existsCanceledSaleBySaleNum(request.getSaleNum()) > 0) {
            throw new IllegalArgumentException("이미 취소된 판매번호입니다.");
        }

        if (request.getAmount() > paidSale.getAmount()) {
            throw new IllegalArgumentException("환불 금액이 결제 금액보다 클 수 없습니다.");
        }

        SaleRecord canceledSale = new SaleRecord();

        canceledSale.setSaleNum(paidSale.getSaleNum());
        canceledSale.setCourseId(paidSale.getCourseId());
        canceledSale.setStudentId(paidSale.getStudentId());
        canceledSale.setAmount(request.getAmount());
        canceledSale.setSaleStatus("CANCELED");
        canceledSale.setOccurredAt(request.getOccurredAt());

        saleMapper.insertSaleRecord(canceledSale);
    }

    public List<SaleRecordResponse> findSaleRecords(
            String creatorId,
            LocalDate fromDate,
            LocalDate toDate
    ) {
        LocalDate toNextDate = null;

        if (toDate != null) {
            toNextDate = toDate.plusDays(1);
        }

        return saleMapper.findSaleRecords(creatorId, fromDate, toNextDate);
    }
}