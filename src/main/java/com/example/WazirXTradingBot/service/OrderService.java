package com.example.WazirXTradingBot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
import okhttp3.MediaType;
import okhttp3.Response;

import com.example.WazirXTradingBot.model.OrderPayload;
import com.google.gson.Gson;

@Service
public class OrderService {




    

    @Value("${wazirx.api.key}")
    private String apiKey;

    @Value("${wazirx.secret.key}")
    private String secretKey;

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String API_BASE_URL = "https://api.wazirx.com/sapi/v1/order";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public String createOrderPayload(String symbol, String side, String type, double quantity, double price, long recvWindow, long timestamp) {
        Map<String, String> payload = new HashMap<>();
        payload.put("symbol", symbol);
        payload.put("side", side);
        payload.put("type", type);
        payload.put("quantity", String.valueOf(quantity));
        payload.put("price", String.valueOf(price));
        payload.put("recvWindow", String.valueOf(recvWindow));
        payload.put("timestamp", String.valueOf(timestamp));

        String queryString = getQueryString(payload);
        String signature = generateSignature(queryString);
        payload.put("signature", signature);

        return gson.toJson(payload);
    }

    public String simulateOrder(String payload) {
        RequestBody body = RequestBody.create(payload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_BASE_URL)
                .addHeader("X-MBX-APIKEY", apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private String getQueryString(Map<String, String> params) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            queryString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return queryString.toString();
    }

    private String generateSignature(String data) {
        try {
            Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA256);
            sha256Hmac.init(secretKeySpec);
            return bytesToHex(sha256Hmac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Unable to generate HMAC signature", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public OrderPayload prepareBuyOrderPayload(double price, double triggerPrice) {
        return null;
    }
}