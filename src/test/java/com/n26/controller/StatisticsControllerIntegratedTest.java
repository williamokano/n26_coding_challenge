package com.n26.controller;

import com.n26.TestUtil;
import com.n26.model.Transaction;
import com.n26.service.TransactionsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticsControllerIntegratedTest extends BaseControllerTest {
    private final String STATISTICS_PATH = "/statistics";

    @Autowired
    private TransactionsService transactionsService;

    @Before
    public void setup() {
        transactionsService.clearTransactions();
    }

    @Test
    public void should_obtain_empty_statistics_when_no_transaction() throws Throwable {
        mockMvc.perform(get(STATISTICS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is("0.00")))
                .andExpect(jsonPath("$.avg", is("0.00")))
                .andExpect(jsonPath("$.min", is("0.00")))
                .andExpect(jsonPath("$.max", is("0.00")))
                .andExpect(jsonPath("$.count", is(0)));
    }

    @Test
    public void should_obtain_calculated_statistics_when_has_transaction() throws Throwable {
        insertTransactions();
        mockMvc.perform(get(STATISTICS_PATH))
                .andExpect(status().isOk())
                .andDo(document("{method-name}", responseFields(
                        fieldWithPath("sum").type("BigDecimal").description("a BigDecimal specifying the total sum of transaction value in the last 60 seconds"),
                        fieldWithPath("avg").type("BigDecimal").description("a BigDecimal specifying the average amount of transaction value in the last 60 seconds"),
                        fieldWithPath("min").type("BigDecimal").description("a BigDecimal specifying single lowest transaction value in the last 60 seconds"),
                        fieldWithPath("max").type("BigDecimal").description("a BigDecimal specifying single highest transaction value in the last 60 seconds"),
                        fieldWithPath("count").type("long").description("a long specifying the total number of transactions that happened in the last 60 seconds")
                )))
                .andExpect(jsonPath("$.sum", is("779.91")))
                .andExpect(jsonPath("$.avg", is("48.74")))
                .andExpect(jsonPath("$.min", is("10.11")))
                .andExpect(jsonPath("$.max", is("98.79")))
                .andExpect(jsonPath("$.count", is(16)));
    }

    @Test
    public void should_obtain_same_calculated_statistics_if_two_requests() throws Throwable {
        insertTransactions();
        Transaction trx = TestUtil.transaction("45.22");
        trx.setTimestamp(LocalDateTime.now().minusSeconds(58));
        transactionsService.addTransaction(trx);

        mockMvc.perform(get(STATISTICS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is("825.13")))
                .andExpect(jsonPath("$.avg", is("48.54")))
                .andExpect(jsonPath("$.min", is("10.11")))
                .andExpect(jsonPath("$.max", is("98.79")))
                .andExpect(jsonPath("$.count", is(17)));

        Thread.sleep(3000);
        mockMvc.perform(get(STATISTICS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is("779.91")))
                .andExpect(jsonPath("$.avg", is("48.74")))
                .andExpect(jsonPath("$.min", is("10.11")))
                .andExpect(jsonPath("$.max", is("98.79")))
                .andExpect(jsonPath("$.count", is(16)));
    }

    @Test
    public void should_obtain_empty_statistics_when_after_cleanup() throws Throwable {
        insertTransactions();
        transactionsService.clearTransactions();
        mockMvc.perform(get(STATISTICS_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sum", is("0.00")))
                .andExpect(jsonPath("$.avg", is("0.00")))
                .andExpect(jsonPath("$.min", is("0.00")))
                .andExpect(jsonPath("$.max", is("0.00")))
                .andExpect(jsonPath("$.count", is(0)));
    }

    private void insertTransactions() {
        transactionsService.clearTransactions();
        createTransactions().parallelStream()
                .forEach(transactionsService::addTransaction);
    }

    private List<Transaction> createTransactions() {
        return Arrays.asList(
                TestUtil.transaction("45.22"),
                TestUtil.transaction("65.42"),
                TestUtil.transaction("12.34"),
                TestUtil.transaction("78.23"),
                TestUtil.transaction("10.84"),
                TestUtil.transaction("87.03"),
                TestUtil.transaction("98.70"),
                TestUtil.transaction("98.79"),
                TestUtil.transaction("65.76"),
                TestUtil.transaction("32.31"),
                TestUtil.transaction("32.22"),
                TestUtil.transaction("11.11"),
                TestUtil.transaction("10.11"),
                TestUtil.transaction("65.14"),
                TestUtil.transaction("56.10"),
                TestUtil.transaction("10.59")
        );
    }

}
