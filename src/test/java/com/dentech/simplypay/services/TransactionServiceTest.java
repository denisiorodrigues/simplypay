package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.domain.user.UserType;
import com.dentech.simplypay.dtos.TransactionDto;
import com.dentech.simplypay.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @Autowired
    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void beforeEach(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when everything is Ok")
    void createTransactionWhenSuccessIsOk() throws Exception {
        User sender = new User(1L,"Maria", "Sousa", "00000000099", "maria@test.com", "123456", new BigDecimal(1000), UserType.COMMON);
        User receiver = new User(2L, "Jose", "Sousa", "00000000099", "jose@test.com", "123456", new BigDecimal(1000), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);
        when(authorizationService.authorizeTransaction(sender, new BigDecimal(1000))).thenReturn(true);

        TransactionDto request = new TransactionDto(new BigDecimal(1000), 1L, 2L);
        this.transactionService.createTransaction(request);

        verify(transactionRepository, times(1)).save(any());

        sender.setBalance(new BigDecimal(0));
        verify(userService, times(2)).saveUser(sender);

        receiver.setBalance(new BigDecimal(2000));
        verify(userService, times(2)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso");
        verify(notificationService, times(1)).sendNotification(receiver, "Transação recebida com sucesso");
    }

    @Test
    @DisplayName("Should throw exception when transaction is not allowed")
    void throwsAnExceptionWhenNotAllowed() {

    }
}