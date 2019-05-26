package com.n26.controller;

import com.n26.Application;
import com.n26.TestUtil;
import com.n26.api.request.CreateTransactionRequest;
import com.n26.model.Transaction;
import com.n26.repository.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsControllerIntegratedTest {
    private final String TRANSACTIONS_PATH = "/transactions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionsRepository transactionsRepository;

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
                .andExpect(status().isCreated());

        List<Transaction> transactions = transactionsRepository.findAll();
        assertEquals(1, transactions.size());
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

        List<Transaction> transactions = transactionsRepository.findAll();
        assertEquals(0, transactions.size());
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

        List<Transaction> transactions = transactionsRepository.findAll();
        assertEquals(0, transactions.size());
    }

    @Test
    public void should_delete_transactions() throws Exception {
        insertTransactions();
        List<Transaction> transactionsBeforeDelete = transactionsRepository.findAll();
        mockMvc.perform(delete(TRANSACTIONS_PATH))
                .andExpect(status().isNoContent());

        List<Transaction> transactionsAfterDelete = transactionsRepository.findAll();

        assertEquals(10, transactionsBeforeDelete.size());
        assertEquals(0, transactionsAfterDelete.size());
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
