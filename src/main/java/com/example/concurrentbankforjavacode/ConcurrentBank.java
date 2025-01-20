package com.example.concurrentbankforjavacode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConcurrentBank {
    List<BankAccount> clientsBank = new ArrayList<>();

    public BankAccount createAccount(Integer accountNumber) {
        for(BankAccount account : clientsBank){
            if(account.getAccountNumber() == accountNumber){
                throw new RuntimeException("an account with this number already exists");
            }
        }
        BankAccount bankAccount = new BankAccount(accountNumber);
        clientsBank.add(bankAccount);
        return bankAccount;
    }

    public void transfer(BankAccount accountSender, BankAccount accountRecipient, BigDecimal amountMoney) {
        BankAccount firstLock = accountSender;
        BankAccount secondLock = accountRecipient;

        if (accountSender.getAccountNumber() > accountRecipient.getAccountNumber()) {
            firstLock = accountRecipient;
            secondLock = accountSender;
        }

        synchronized (firstLock) {
            synchronized (secondLock) {
                accountSender.withdraw(amountMoney);
                try {
                    accountRecipient.deposit(amountMoney);
                } catch (Exception e) {
                    accountSender.deposit(amountMoney);
                    throw new RuntimeException("operation error");
                }
            }
        }
    }

    public BigDecimal getTotalBalance(){
        BigDecimal totalBalance = BigDecimal.ZERO;
        for(BankAccount bankAccount : clientsBank){
            totalBalance = totalBalance.add(bankAccount.getBalance());
        }
        return totalBalance;
    }
}
