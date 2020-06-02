package com.example.demo.Model;

public class Item {

    private int id;
    private String name;
    private int quantity = 0;
    private double price;

    public Item() {}

    public Item(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Item(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }



    public String getName() {
        return name;
    }

    public double getPrice() {
        return price*quantity;
    }

    public double getSinglePrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public String toString(){
        return id+','+name+','+quantity+','+price;
    }

}
