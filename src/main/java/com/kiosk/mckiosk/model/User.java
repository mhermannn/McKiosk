package com.kiosk.mckiosk.model;

public class User {
    private int id;
    private String login;
    private String password;
    private AccountType accountType;
    public User(int id, String login, String password, AccountType accountType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.accountType = accountType;
    }
    public User(String login, String password, AccountType accountType) {
        this.login = login;
        this.password = password;
        this.accountType = accountType;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public AccountType getAccountType() {return accountType;}
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
}
