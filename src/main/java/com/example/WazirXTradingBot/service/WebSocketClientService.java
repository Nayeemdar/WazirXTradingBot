package com.example.WazirXTradingBot.service;

import com.example.WazirXTradingBot.model.OrderPayload;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WebSocketClientService {

    @Value("${wazirx.api.key}")
    private String apiKey;

    @Value("${wazirx.secret.key}")
    private String secretKey;

    private final OrderService orderService;

    public WebSocketClientService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void startWebSocketClient(double triggerPrice) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("wss://stream.wazirx.com/stream").build();
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                double price = parsePriceFromMessage(text);
                OrderPayload buyOrder = orderService.prepareBuyOrderPayload(price, triggerPrice);
                OrderPayload sellOrder = orderService.prepareBuyOrderPayload(price, triggerPrice);

                if (buyOrder != null) {
                    System.out.println("Prepared Buy Order Payload: " + buyOrder);
                }

                if (sellOrder != null) {
                    System.out.println("Prepared Sell Order Payload: " + sellOrder);
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                System.out.println("Receiving bytes: " + bytes.hex());
            }
        });

        // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
        client.dispatcher().executorService().shutdown();
    }

    private double parsePriceFromMessage(String message) {
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        return jsonObject.getAsJsonObject("data").get("price").getAsDouble();
    }
}