package com.dentech.simplypay.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name="users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor // Criar um construtor para receber todos os atributos da classe
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String firstName;
    private String lsatName;
    @Column(unique = true)
    private String document;
    @Column(unique = true)
    private String email;
    private String passString;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private UserType userType;
}
