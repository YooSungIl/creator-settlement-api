package com.liveklass.assignment.settlement.mapper;

import com.liveklass.assignment.settlement.dto.AdminCreatorSettlementResponse;
import com.liveklass.assignment.settlement.dto.CreatorSettlementResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SettlementMapper {

    CreatorSettlementResponse findCreatorMonthlySettlement(@Param("creatorId") String creatorId, @Param("yearMonth") String yearMonth);

    List<AdminCreatorSettlementResponse> findAdminSettlement(@Param("fromDate") LocalDate fromDate, @Param("toNextDate") LocalDate toNextDate);

    String findCreatorName(@Param("creatorId") String creatorId);
}