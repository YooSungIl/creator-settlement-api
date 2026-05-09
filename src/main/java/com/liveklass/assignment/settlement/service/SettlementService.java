package com.liveklass.assignment.settlement.service;

import com.liveklass.assignment.settlement.dto.AdminCreatorSettlementResponse;
import com.liveklass.assignment.settlement.dto.AdminSettlementResponse;
import com.liveklass.assignment.settlement.dto.CreatorSettlementResponse;
import com.liveklass.assignment.settlement.mapper.SettlementMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SettlementService {

    private final SettlementMapper settlementMapper;

    public SettlementService(SettlementMapper settlementMapper) {
        this.settlementMapper = settlementMapper;
    }

    public CreatorSettlementResponse findCreatorMonthlySettlement(String creatorId, String yearMonth) {
        CreatorSettlementResponse response = settlementMapper.findCreatorMonthlySettlement(creatorId, yearMonth);

        if(response == null) {
            response = new CreatorSettlementResponse();
            response.setCreatorId(creatorId);
            response.setCreatorNm(settlementMapper.findCreatorName(creatorId));
            response.setTotalPaidAmount(0L);
            response.setTotalCanceledAmount(0L);
            response.setNetSaleAmount(0L);
            response.setPlatformCommissionAmount(0L);
            response.setExpectedSettlementAmount(0L);
            response.setPaidCount(0L);
            response.setCanceledCount(0L);
        }

        return response;
    }

    public AdminSettlementResponse findAdminSettlement(LocalDate fromDate, LocalDate toDate) {
        LocalDate toNextDate = toDate.plusDays(1);

        List<AdminCreatorSettlementResponse> creators = settlementMapper.findAdminSettlement(fromDate, toNextDate);

        if (creators == null) {
            creators = List.of();
        }

        long totalExpectedSettlementAmount = creators.stream()
                .mapToLong(AdminCreatorSettlementResponse::getExpectedSettlementAmount)
                .sum();

        AdminSettlementResponse response = new AdminSettlementResponse();
        response.setCreators(creators);
        response.setTotalExpectedSettlementAmount(totalExpectedSettlementAmount);

        return response;
    }
}
