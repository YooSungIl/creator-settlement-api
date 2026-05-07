package com.liveklass.assignment.sale.mapper;

import com.liveklass.assignment.sale.entity.SaleRecord;
import org.apache.ibatis.annotations.Mapper;

import com.liveklass.assignment.sale.dto.SaleRecordResponse;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SaleMapper {

    int existsPaidSaleBySaleNum(String saleNum);

    int existsCourseByCourseId(String courseId);

    int existsCanceledSaleBySaleNum(String saleNum);

    SaleRecord findPaidSaleBySaleNum(String saleNum);

    void insertSaleRecord(SaleRecord saleRecord);

    List<SaleRecordResponse> findSaleRecords(
            @Param("creatorId") String creatorId,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}