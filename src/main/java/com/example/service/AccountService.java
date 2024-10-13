package com.example.service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.ConflictException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Method to register a new account
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank() ||
                account.getPassword() == null || account.getPassword().length() < 4) {
            throw new BadRequestException("Invalid username or password");
        }

        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new ConflictException("Username already exists");
        }

        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Optional<Account> account = accountRepository.findByUsername(username);
        if (account.isPresent() && account.get().getPassword().equals(password)) {
            return account.get();
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }
}