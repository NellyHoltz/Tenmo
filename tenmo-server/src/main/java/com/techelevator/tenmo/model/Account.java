package com.techelevator.tenmo.model;

public class Account {

    private int id;
    private User user;
    private double balance;

    public Account() {
    }

    public Account(int id, User user, double balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", user=" + user.getUsername() +
                ", balance=" + balance +
                '}';
    }
}
