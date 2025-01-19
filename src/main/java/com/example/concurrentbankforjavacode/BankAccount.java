package com.example.concurrentbankforjavacode;

import java.math.BigDecimal;

class BankAccount {
    private final int accountNumber;

    public BankAccount(int accountNumber){
        this.accountNumber = accountNumber;

    }
    public int getAccountNumber(){
        return accountNumber;
    }

    private BigDecimal balance = BigDecimal.ZERO;

    public synchronized void deposit(BigDecimal count) {

        balance = balance.add(count);
    }

    public synchronized void withdraw(BigDecimal count) {
        if(balance.compareTo(count) < 0){
            throw new RuntimeException("impossible operation");
        }
        balance = balance.subtract(count);
    }

    public synchronized BigDecimal getBalance() {
        return balance;

    }







}
