package com.liveklass.assignment.sale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 존재하지_않는_강의로_결제승인하면_400을_반환한다() throws Exception {

        mockMvc.perform(post("/api/sales/pay")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "saleNum": "sale-999",
                                "courseId": "not-exist-course",
                                "studentId": "student-999",
                                "amount": 50000,
                                "occurredAt": "2025-03-01T10:00:00"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("존재하지 않는 강의입니다."));
    }

    @Test
    void 이미_결제승인된_판매번호로_결제승인하면_400을_반환한다() throws Exception {

        mockMvc.perform(post("/api/sales/pay")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "saleNum": "sale-1",
                                "courseId": "course-1",
                                "studentId": "student-1",
                                "amount": 50000,
                                "occurredAt": "2025-03-01T10:00:00"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이미 결제 승인된 판매번호입니다."));
    }

    @Test
    void 존재하지_않는_판매번호로_취소하면_400을_반환() throws Exception {

        mockMvc.perform(post("/api/sales/cancel")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "saleNum": "not-exist-sale",
                                "amount": 10000,
                                "occurredAt": "2025-03-01T10:00:00"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("결제 승인 내역이 존재하지 않습니다."));
    }

    @Test
    void 이미_취소된_판매번호로_다시_취소하면_400을_반환() throws Exception {

        mockMvc.perform(post("/api/sales/cancel")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "saleNum": "sale-3",
                                "amount": 10000,
                                "occurredAt": "2025-03-25T10:00:00"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("이미 취소된 판매번호입니다."));
    }

    @Test
    void 환불금액이_결제금액보다_크면_400을_반환() throws Exception {

        mockMvc.perform(post("/api/sales/cancel")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                            {
                                "saleNum": "sale-1",
                                "amount": 999999,
                                "occurredAt": "2025-03-25T10:00:00"
                            }
                            """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("환불 금액이 결제 금액보다 클 수 없습니다."));
    }
}