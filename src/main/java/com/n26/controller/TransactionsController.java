package com.n26.controller;

import com.n26.api.TransactionsResource;
import com.n26.api.request.CreateTransactionsRequest;
import com.n26.model.Transaction;
import com.n26.service.TransactionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TransactionsController implements TransactionsResource {

    private final TransactionsService transactionsService;
    private final ModelMapper mapper;

    @Autowired
    TransactionsController(TransactionsService transactionsService, ModelMapper mapper) {
        this.transactionsService = transactionsService;
        this.mapper = mapper;
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@Valid @RequestBody CreateTransactionsRequest createTransactionsRequest) {
        transactionsService.addTransaction(mapper.map(createTransactionsRequest, Transaction.class));
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransactions() {
        transactionsService.clearTransactions();
    }
}
