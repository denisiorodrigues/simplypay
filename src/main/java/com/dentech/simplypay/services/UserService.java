package com.dentech.simplypay.services;

import com.dentech.simplypay.domain.user.User;
import com.dentech.simplypay.domain.user.UserType;
import com.dentech.simplypay.dtos.UserDto;
import com.dentech.simplypay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void validationTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Usuário do tipo Lojista não está autorizado a receber tansação");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw new Exception("Usuário tem saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public User createUser(UserDto data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }
}
