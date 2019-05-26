package com.n26.api;

import com.n26.api.request.CreateTransactionRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping(value = "/transactions")
public interface TransactionsResource {
    @PostMapping
    void createTransaction(@Valid @RequestBody CreateTransactionRequest createTransactionsRequest);

    @DeleteMapping
    void deleteTransactions();
}
