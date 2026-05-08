package com.liveklass.assignment.sale;

import com.liveklass.assignment.sale.dto.SaleCancelRequest;
import com.liveklass.assignment.sale.dto.SalePayRequest;
import com.liveklass.assignment.sale.dto.SaleRecordResponse;
import com.liveklass.assignment.sale.entity.SaleRecord;
import com.liveklass.assignment.sale.mapper.SaleMapper;
import com.liveklass.assignment.sale.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;

    @Autowired
    private SaleMapper saleMapper;

    @Test
    void 판매내역_등록() {

        // given
        SalePayRequest request = new SalePayRequest();
        request.setSaleNum("sale-999");
        request.setCourseId("course-1");
        request.setStudentId("student-999");
        request.setAmount(999999L);
        request.setOccurredAt(LocalDateTime.of(2026, 3, 1, 11, 0));

        // when
        saleService.pay(request);

        // then
        SaleRecord saleRecord = saleMapper.findPaidSaleBySaleNum("sale-999");
        assertThat(saleRecord).isNotNull();
        assertThat(saleRecord.getSaleNum()).isEqualTo("sale-999");
        assertThat(saleRecord.getCourseId()).isEqualTo("course-1");
        assertThat(saleRecord.getStudentId()).isEqualTo("student-999");
        assertThat(saleRecord.getAmount()).isEqualTo(999999L);
        assertThat(saleRecord.getOccurredAt()).isEqualTo(LocalDateTime.of(2026, 3, 1, 11, 0));
        assertThat(saleRecord.getSaleStatus()).isEqualTo("PAID");
    }

    @Test
    void 취소내역_등록() {

        // given
        SaleCancelRequest request = new SaleCancelRequest();
        request.setSaleNum("sale-1");
        request.setAmount(10000L);
        request.setOccurredAt(LocalDateTime.of(2026, 4, 1, 0, 0));

        // when
        saleService.cancel(request);

        // then
        SaleRecord saleRecord = saleMapper.findCanceledSaleBySaleNum("sale-1");
        assertThat(saleRecord).isNotNull();
        assertThat(saleRecord.getSaleNum()).isEqualTo("sale-1");
        assertThat(saleRecord.getCourseId()).isEqualTo("course-1");
        assertThat(saleRecord.getStudentId()).isEqualTo("student-1");
        assertThat(saleRecord.getAmount()).isEqualTo(10000L);
        assertThat(saleRecord.getOccurredAt()).isEqualTo(LocalDateTime.of(2026, 4, 1, 0, 0));
        assertThat(saleRecord.getSaleStatus()).isEqualTo("CANCELED");
    }

    @Test
    void 판매내역_조회() {

        // given
        String creatorId = "creator-1";
        LocalDate fromDate = LocalDate.of(2025, 3, 1);
        LocalDate toDate = LocalDate.of(2025, 3, 21);

        // when
        List<SaleRecordResponse> allResult = saleService.findSaleRecords(creatorId, fromDate, toDate);
        List<SaleRecordResponse> creatorResult = saleService.findSaleRecords(creatorId, null, null);
        List<SaleRecordResponse> fromDateResult = saleService.findSaleRecords(null, fromDate, null);
        List<SaleRecordResponse> toDateResult = saleService.findSaleRecords(null, null, toDate);
        List<SaleRecordResponse> dateResult = saleService.findSaleRecords(null, fromDate, toDate);

        // then
        assertThat(allResult).isNotNull();
        assertThat(creatorResult).isNotNull();
        assertThat(allResult).hasSize(3);
        assertThat(creatorResult).hasSize(6);
        assertThat(fromDateResult).hasSize(7);
        assertThat(toDateResult).hasSize(7);
        assertThat(dateResult).hasSize(4);
    }
}
