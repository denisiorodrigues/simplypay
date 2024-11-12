package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.transaction.Transaction;
import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.dtos.TransactionDto;
import com.dentech.simplypay.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDto transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.sendId());
        User receiver = this.userService.findUserById(transaction.receiverId());

        userService.validationTransaction(sender, transaction.value());
        boolean isAuthorize = this.authorizationService.authorizeTransaction(sender, transaction.value());
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

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }
}
