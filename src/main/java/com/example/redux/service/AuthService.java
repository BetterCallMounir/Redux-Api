package com.example.redux.service;

import com.example.redux.dto.RegisterRequest;
import com.example.redux.model.NotificationEmail;
import com.example.redux.model.User;
import com.example.redux.model.VerificationToken;
import com.example.redux.repository.TokenRepository;
import com.example.redux.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    @Transactional
    public void signUp(final RegisterRequest registerRequest) {
        // Validate user input
        if (registerRequest.getUsername() == null || registerRequest.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (registerRequest.getPassword() == null || registerRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (registerRequest.getEmail() == null || registerRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }

        // Create new user
        final User user = new User();
        user.setUsername(registerRequest.getUsername());
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        String salt = hashedPassword.substring(0,29);
        user.setPassword(hashedPassword);
        user.setSalt(salt);
        user.setEmail(registerRequest.getEmail());
        user.setCreated(Instant.now());
        user.setVerified(false);
        userRepository.save(user);
        String token = generateVerificationToken(user);
        NotificationEmail verificationEmail = new NotificationEmail("PLease Activate your Account" , user.getEmail() , "http://localhost:8080/api/auth/accountVerification/"+token);
        mailService.sendMail(verificationEmail);

    }
    private String generateVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(60000));
        tokenRepository.save(verificationToken);
        return token;
    }

    @Transactional
    public String verifyAccount(String token) {
        VerificationToken  verificationToken = tokenRepository.findByToken(token);
        if(verificationToken != null){
            if(verificationToken.getExpiryDate().isBefore(Instant.now())){
                return "Token Expired";
            }
            User user = verificationToken.getUser();
            if(user == null){
                throw new IllegalStateException("User not found");
            }
            user.setVerified(true);
            userRepository.save(user);
            tokenRepository.delete(verificationToken);
            return "Account Verified";
        }
        return "Invalid Token";
    }

    public void login() {
        // TODO: Implement login

    }
}

