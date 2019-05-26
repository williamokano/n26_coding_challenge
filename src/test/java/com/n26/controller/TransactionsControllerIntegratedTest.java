package com.n26.controller;

import com.n26.TestUtil;
import com.n26.api.request.CreateTransactionRequest;
import com.n26.model.Statistics;
import com.n26.repository.TransactionsRepository;
import com.n26.service.StatisticsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionsControllerIntegratedTest extends BaseControllerTest {
    private final String TRANSACTIONS_PATH = "/transactions";

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private StatisticsService statisticsService;

    @Before
    public void setup() {
        transactionsRepository.clearTransactions();
    }

    @Test
    public void should_create_transaction() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(new BigDecimal("12.34"))
                .timestamp(LocalDateTime.now())
                .build();

        mockMvc.perform(post(TRANSACTIONS_PATH)
                .content(TestUtil.objectToString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andDo(
                        document("{method-name}",
                                requestFields(
                                        fieldWithPath("amount").type("BigDecimal").description("Transaction amount; a string of arbitrary length that is parsable as a BigDecimal"),
                                        fieldWithPath("timestamp").type("String").description("Transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the UTC timezone")
                                ))
                );

        Statistics statistics = statisticsService.getStatistics();
        assertEquals(1L, (long) statistics.getCount());
    }

    @Test
    public void should_not_create_transaction_if_transaction_is_in_the_future() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(new BigDecimal("454.65"))
                .timestamp(LocalDateTime.now().plusSeconds(99L))
                .build();

        mockMvc.perform(post(TRANSACTIONS_PATH)
                .content(TestUtil.objectToString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity());

        Statistics statistics = statisticsService.getStatistics();
        assertEquals(0L, (long) statistics.getCount());
    }

    @Test
    public void should_return_unprocessable_entity_if_wrong_type() throws Exception {
        String content = "{\"amount\": \"10.01\", \"timestamp\":\"2018-07-17T09:59:51 PM\"}";
        mockMvc.perform(post(TRANSACTIONS_PATH)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void should_return_bad_request_if_not_parseable_body() throws Exception {
        String content = "I'm not parsable";
        mockMvc.perform(post(TRANSACTIONS_PATH)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_not_create_transaction_if_transaction_is_before_beginning_transactions_window() throws Exception {
        CreateTransactionRequest request = CreateTransactionRequest.builder()
                .amount(new BigDecimal("315.01"))
                .timestamp(LocalDateTime.now().minusSeconds(99L))
                .build();

        mockMvc.perform(post(TRANSACTIONS_PATH)
                .content(TestUtil.objectToString(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        Statistics statistics = statisticsService.getStatistics();
        assertEquals(0L, (long) statistics.getCount());
    }

    @Test
    public void should_delete_transactions() throws Exception {
        insertTransactions();
        Statistics statisticsBeforeDelete = statisticsService.getStatistics();
        mockMvc.perform(delete(TRANSACTIONS_PATH))
                .andExpect(status().isNoContent());

        Statistics statisticsAfterDeletion = statisticsService.getStatistics();

        assertNotEquals(statisticsBeforeDelete.getCount(), statisticsAfterDeletion.getCount());
        assertEquals(0L, (long) statisticsAfterDeletion.getCount());
    }

    private void insertTransactions() {
        Arrays.asList(
                TestUtil.transaction("45.04"),
                TestUtil.transaction("21.25"),
                TestUtil.transaction("98.72"),
                TestUtil.transaction("32.13"),
                TestUtil.transaction("12.35"),
                TestUtil.transaction("78.91"),
                TestUtil.transaction("12.69"),
                TestUtil.transaction("23.00"),
                TestUtil.transaction("12.78"),
                TestUtil.transaction("21.09")
        ).parallelStream().forEach(transactionsRepository::save);
    }
}
