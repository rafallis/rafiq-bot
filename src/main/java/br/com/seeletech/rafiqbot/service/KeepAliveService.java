package br.com.seeletech.rafiqbot.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class KeepAliveService {

    @Scheduled(fixedRate = 60_000)
    public void reportCurrentTime() {
        System.out.println(System.currentTimeMillis());
    }
}
