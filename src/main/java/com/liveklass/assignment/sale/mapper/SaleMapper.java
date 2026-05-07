package com.liveklass.assignment.sale.mapper;

import com.liveklass.assignment.sale.entity.SaleRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaleMapper {

    int existsPaidSaleBySaleNum(String saleNum);

    int existsCourseByCourseId(String courseId);

    void insertSaleRecord(SaleRecord saleRecord);


}