package com.example.WazirXTradingBot.cli;


import com.example.WazirXTradingBot.service.WebSocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Autowired
    private WebSocketClientService webSocketClientService;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the trigger price: ");
        double triggerPrice = scanner.nextDouble();

        webSocketClientService.startWebSocketClient(triggerPrice);
    }
}