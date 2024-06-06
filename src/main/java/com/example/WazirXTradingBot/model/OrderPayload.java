package com.example.WazirXTradingBot.model;


public class OrderPayload {
    private String side;
    private double price;
    private int quantity;

    public OrderPayload(String side, double price, int quantity) {
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrderPayload{" +
                "side='" + side + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}