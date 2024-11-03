package com.dentech.simplypay.controlles;

import com.dentech.simplypay.domain.transaction.Transaction;
import com.dentech.simplypay.dtos.TransactionDto;
import com.dentech.simplypay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDto data) throws Exception{
        Transaction newTransaction = this.transactionService.createTransaction(data);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);
    }
}
