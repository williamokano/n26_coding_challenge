package com.n26.controller;

import com.n26.api.TransactionsResource;
import com.n26.api.request.CreateTransactionsRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionsController implements TransactionsResource {
    @Override
    public void createTransaction(@Valid @RequestBody CreateTransactionsRequest createTransactionsRequest) {

    }

    @Override
    public void deleteTransactions() {

    }
}
