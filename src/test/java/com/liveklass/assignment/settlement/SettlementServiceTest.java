package com.liveklass.assignment.settlement;

import com.liveklass.assignment.sale.dto.SaleRecordResponse;
import com.liveklass.assignment.sale.service.SaleService;
import com.liveklass.assignment.settlement.dto.AdminCreatorSettlementResponse;
import com.liveklass.assignment.settlement.dto.AdminSettlementResponse;
import com.liveklass.assignment.settlement.dto.CreatorSettlementResponse;
import com.liveklass.assignment.settlement.service.SettlementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SettlementServiceTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SettlementService settlementService;

    @Test
    void creator1_2025년3월_정산금액_자바계산값과_비교() {

        // given
        String creatorId = "creator-1";
        String yearMonth = "2025-03";
        LocalDate fromDate = LocalDate.of(2025, 3, 1);
        LocalDate toDate = LocalDate.of(2025, 3, 31);

        // when
        CreatorSettlementResponse settlement = settlementService.findCreatorMonthlySettlement(creatorId, yearMonth);
        List<SaleRecordResponse> saleRecords = saleService.findSaleRecords(creatorId, fromDate, toDate);

        // then - Java로 직접 계산
        long totalPaidAmount = saleRecords.stream()
                .filter(record -> "PAID".equals(record.getSaleStatus()))
                .mapToLong(SaleRecordResponse::getAmount)
                .sum();

        long totalCanceledAmount = saleRecords.stream()
                .filter(record -> "CANCELED".equals(record.getSaleStatus()))
                .mapToLong(SaleRecordResponse::getAmount)
                .sum();

        long netSaleAmount = totalPaidAmount - totalCanceledAmount;

        long platformCommissionAmount = netSaleAmount * 20 / 100;

        long expectedSettlementAmount = netSaleAmount - platformCommissionAmount;

        long paidCount = saleRecords.stream()
                .filter(record -> "PAID".equals(record.getSaleStatus()))
                .count();

        long canceledCount = saleRecords.stream()
                .filter(record -> "CANCELED".equals(record.getSaleStatus()))
                .count();

        // then - DB 집계 결과와 Java 계산 결과 비교
        assertThat(settlement.getTotalPaidAmount()).isEqualTo(totalPaidAmount);
        assertThat(settlement.getTotalCanceledAmount()).isEqualTo(totalCanceledAmount);
        assertThat(settlement.getNetSaleAmount()).isEqualTo(netSaleAmount);
        assertThat(settlement.getPlatformCommissionAmount()).isEqualTo(platformCommissionAmount);
        assertThat(settlement.getExpectedSettlementAmount()).isEqualTo(expectedSettlementAmount);
        assertThat(settlement.getPaidCount()).isEqualTo(paidCount);
        assertThat(settlement.getCanceledCount()).isEqualTo(canceledCount);
    }

    @Test
    void 부분환불처리_환불금액만차감_정산() {

        // given
        String creatorId = "creator-1";
        LocalDate fromDate = LocalDate.of(2025, 3, 1);
        LocalDate toDate = LocalDate.of(2025, 3, 31);

        // when
        List<SaleRecordResponse> saleRecords = saleService.findSaleRecords(creatorId, fromDate, toDate);

        // then
        long sale4PaidAmount = saleRecords.stream()
                .filter(record -> "sale-4".equals(record.getSaleNum()) && "PAID".equals(record.getSaleStatus()))
                .mapToLong(SaleRecordResponse::getAmount)
                .sum();

        long sale4CanceledAmount = saleRecords.stream()
                .filter(record -> "sale-4".equals(record.getSaleNum()) && "CANCELED".equals(record.getSaleStatus()))
                .mapToLong(SaleRecordResponse::getAmount)
                .sum();

        long netAmount = sale4PaidAmount - sale4CanceledAmount;

        assertThat(sale4PaidAmount).isEqualTo(80000L);
        assertThat(sale4CanceledAmount).isEqualTo(30000L);
        assertThat(netAmount).isEqualTo(50000L);
    }

    @Test
    void 월경계_취소_판매월과취소월_각각반영() {

        // given
        String creatorId = "creator-2";

        // when
        CreatorSettlementResponse januaryResponse = settlementService.findCreatorMonthlySettlement(creatorId, "2025-01");
        CreatorSettlementResponse februaryResponse = settlementService.findCreatorMonthlySettlement(creatorId, "2025-02");

        // then
        assertThat(januaryResponse.getTotalPaidAmount()).isEqualTo(60000L);
        assertThat(januaryResponse.getTotalCanceledAmount()).isEqualTo(0L);
        assertThat(januaryResponse.getNetSaleAmount()).isEqualTo(60000L);
        assertThat(januaryResponse.getPlatformCommissionAmount()).isEqualTo(12000L);
        assertThat(januaryResponse.getExpectedSettlementAmount()).isEqualTo(48000L);
        assertThat(januaryResponse.getPaidCount()).isEqualTo(1L);
        assertThat(januaryResponse.getCanceledCount()).isEqualTo(0L);

        assertThat(februaryResponse.getTotalPaidAmount()).isEqualTo(0L);
        assertThat(februaryResponse.getTotalCanceledAmount()).isEqualTo(60000L);
        assertThat(februaryResponse.getNetSaleAmount()).isEqualTo(-60000L);
        assertThat(februaryResponse.getPlatformCommissionAmount()).isEqualTo(-12000L);
        assertThat(februaryResponse.getExpectedSettlementAmount()).isEqualTo(-48000L);
        assertThat(februaryResponse.getPaidCount()).isEqualTo(0L);
        assertThat(februaryResponse.getCanceledCount()).isEqualTo(1L);
    }

    @Test
    void 판매내역_없는월은_0원으로_응답() {
        // given
        String creatorId = "creator-3";
        String yearMonth = "2025-03";

        // when
        CreatorSettlementResponse response = settlementService.findCreatorMonthlySettlement(creatorId, yearMonth);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getCreatorId()).isEqualTo("creator-3");
        assertThat(response.getCreatorNm()).isEqualTo("박강사");
        assertThat(response.getTotalPaidAmount()).isEqualTo(0L);
        assertThat(response.getTotalCanceledAmount()).isEqualTo(0L);
        assertThat(response.getNetSaleAmount()).isEqualTo(0L);
        assertThat(response.getPlatformCommissionAmount()).isEqualTo(0L);
        assertThat(response.getExpectedSettlementAmount()).isEqualTo(0L);
        assertThat(response.getPaidCount()).isEqualTo(0L);
        assertThat(response.getCanceledCount()).isEqualTo(0L);
    }

    @Test
    void 운영자_정산현황은_판매내역을_기준으로_계산된다() {

        // given
        LocalDate fromDate = LocalDate.of(2025, 3, 1);
        LocalDate toDate = LocalDate.of(2025, 3, 31);

        // when
        AdminSettlementResponse response = settlementService.findAdminSettlement(fromDate, toDate);
        List<SaleRecordResponse> saleRecords = saleService.findSaleRecords(null, fromDate, toDate);

        // then
        //크리에이터별 정산값 자바 계산
        Map<String, Long> expectedSettlementMap = saleRecords.stream()
                        .collect(Collectors.groupingBy(SaleRecordResponse::getCreatorId,
                                 Collectors.summingLong(record -> {
                                    long amount = "PAID".equals(record.getSaleStatus()) ? record.getAmount() : -record.getAmount();
                                    return amount * 80 / 100; // 수수료 20%
                                })
                        ));

        //운영자 정산 결과 검증
        for (AdminCreatorSettlementResponse creator : response.getCreators()) {
            Long expectedAmount = expectedSettlementMap.get(creator.getCreatorId());
            assertThat(expectedAmount).isNotNull();
            assertThat(creator.getExpectedSettlementAmount()).isEqualTo(expectedAmount);
        }

        //전체 정산 총합 자바 계산
        long calculatedTotalAmount = expectedSettlementMap.values().stream()
                        .mapToLong(Long::longValue)
                        .sum();

        //전체 정산 총합 결과 검증
        assertThat(response.getTotalExpectedSettlementAmount()).isEqualTo(calculatedTotalAmount);
    }
}
