package com.dentech.simplypay.repositories;

import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.domain.user.UserType;
import com.dentech.simplypay.dtos.UserDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get User successfully from DB")
    void findUserByDocumentSuccess() {
        String document = "99999999901";
        UserDto data = new UserDto("Denisio", "Test", document, new BigDecimal(10), "test@teste.com", "4444", UserType.COMMON);
        this.createUser(data);

        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);

        assertThat(foundedUser.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Should not get User from DB when user not exists")
    void findUserByDocumentWasNotFound() {
        String document = "99999999901";
        Optional<User> foundedUser = this.userRepository.findUserByDocument(document);
        assertThat(foundedUser.isEmpty()).isTrue();
    }

    private User createUser(UserDto data) {
        User newUser = new User(data);
        this.entityManager.persist(newUser);
        return newUser;
    }
}