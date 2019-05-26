package com.n26.controller;

import com.n26.Application;
import com.n26.TestUtil;
import com.n26.model.Transaction;
import com.n26.service.TransactionsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticsControllerIntegratedTest {
    private final String STATISTICS_PATH = "/statistics";

    @Autowired
    private MockMvc mockMvc;

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
