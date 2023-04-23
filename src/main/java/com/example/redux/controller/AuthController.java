package com.example.redux.controller;

import com.example.redux.dto.RegisterRequest;
import com.example.redux.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){
         authService.signUp(registerRequest);
         return new ResponseEntity<>("User Registration Successful" , HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        return new ResponseEntity<>(authService.verifyAccount(token), HttpStatus.OK);
    }
    @GetMapping("/login")
    public String login(){
        authService.login();
        return "login";
    }
}
