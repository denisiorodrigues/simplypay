package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.transaction.Transaction;
import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.dtos.TransactionDto;
import com.dentech.simplypay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDto transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.sendId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validationTransaction(sender, transaction.value());
        boolean isAuthorize = this.authorizeTransaction(sender, transaction.value());
        if(!isAuthorize){
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setRecivre(receiver);
        newTransaction.setCreateAt(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);
    }

    public boolean authorizeTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizeResponse = this.restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);
        if(authorizeResponse.getStatusCode() == HttpStatus.OK){
            String message = (String) authorizeResponse.getBody().get("message");
            return "Autorizado".equalsIgnoreCase(message);
        }

        return false;
    }
}
