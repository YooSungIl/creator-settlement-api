package com.liveklass.assignment.sale.mapper;

import com.liveklass.assignment.sale.entity.SaleRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleMapper {

    int existsPaidSaleBySaleNum(String saleNum);

    int existsCourseByCourseId(String courseId);

    int existsCanceledSaleBySaleNum(String saleNum);

    SaleRecord findPaidSaleBySaleNum(String saleNum);

    void insertSaleRecord(SaleRecord saleRecord);
}