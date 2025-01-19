package com.example.concurrentbankforjavacode;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConcurrentBankForJavaCodeApplicationTests {


    @Test
    void createAccount(){
        ConcurrentBank bank = new ConcurrentBank();
        BankAccount account1 = bank.createAccount(1);
        assertNotNull(account1);
        assertEquals(account1.getAccountNumber(), 1);

        assertThrows(RuntimeException.class, () -> {
            bank.createAccount(1);
        });

        Thread thread1 = new Thread(() -> account1.deposit(new BigDecimal(100)));
        Thread thread2 = new Thread(() -> account1.deposit(new BigDecimal(200)));
        Thread thread3 = new Thread(() -> account1.deposit(new BigDecimal(300)));
        thread1.start();
        thread2.start();
        thread3.start();
        waitOtherTreads();
        assertEquals(new BigDecimal(600),account1.getBalance());

        Thread thread4 = new Thread(() -> account1.withdraw(new BigDecimal(100)));
        Thread thread5 = new Thread(() -> account1.withdraw(new BigDecimal(200)));
        Thread thread6 = new Thread(() -> account1.withdraw(new BigDecimal(300)));
        thread4.start();
        thread5.start();
        thread6.start();
        waitOtherTreads();
        assertEquals(new BigDecimal(0),account1.getBalance());

        assertThrows(RuntimeException.class, () -> {
            account1.withdraw(new BigDecimal(100));
        });

        BankAccount account2 = bank.createAccount(2);
        Thread thread7 = new Thread(() -> account1.deposit(new BigDecimal(100)));
        thread7.start();
        waitOtherTreads();

        Thread thread8 = new Thread(() -> bank.transfer(account1, account2, new BigDecimal(100)));
        thread8.start();
        waitOtherTreads();
        assertEquals(account1.getBalance(), new BigDecimal(0));
        assertEquals(account2.getBalance(), new BigDecimal(100));

        assertThrows(RuntimeException.class, () -> {
            bank.transfer(account1, account2, new BigDecimal(100));
        });

        Thread thread9 = new Thread(() -> bank.transfer(account2, account1, new BigDecimal(1)));
        Thread thread10 = new Thread(() -> bank.transfer(account2, account1, new BigDecimal(2)));
        Thread thread11 = new Thread(() -> bank.transfer(account2, account1, new BigDecimal(3)));
        thread9.start();
        thread10.start();
        thread11.start();
        waitOtherTreads();
        assertEquals(account2.getBalance(), new BigDecimal(94));
        assertEquals(account1.getBalance(), new BigDecimal(6));
    }

     private void waitOtherTreads() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void contextLoads() {

    }


}
