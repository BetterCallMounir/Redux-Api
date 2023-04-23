package com.example.redux.tasks;

import com.example.redux.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
public class removeTokensTask {
    private final TokenRepository tokenRepository;

    @Autowired
    public removeTokensTask(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void removeTokens() {
        Instant now = Instant.now();
        try{
            tokenRepository.deleteExpiredTokens(now);
        }catch (Exception e){
            log.error("Error removing expired tokens", e);
            throw e;
        }
        log.info("Removed expired tokens");
    }
}
