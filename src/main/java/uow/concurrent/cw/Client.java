package uow.concurrent.cw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Banking Client (System)
 */
public class Client {
    // Bank's Customer List
    static Customer customer1 = new Customer("CUS-0001", "Alex", "Johns");
    static Customer customer2 = new Customer("CUS-0002", "John", "Smith");
    static Customer customer3 = new Customer("CUS-0003", "Mike", "Miller");

    private static void testTwoThreadsOneAccount() {
        System.out.println("\nSTARTING TEST CASE 1\n--------------------");

        // Instantiate Bank
        Bank bank = new Bank();

        // Bank's Accounts
        BankAccount bankAccount = bank.addBankAccount("ACC-0001", Arrays.asList(customer1, customer2),
                AccountType.NORMAL, 100);

        Thread withdrawerThread = new Thread(new WithdrawerClient(bankAccount, 5000, customer1));
        Thread depositorThread = new Thread(new DepositorClient(bankAccount, 10000));

        withdrawerThread.start();
        depositorThread.start();

        // Wait till threads die
        try {
            depositorThread.join();
            withdrawerThread.join();
        } catch (InterruptedException e) {
            System.out.printf("[ERRORED] Error occurred during execution. Error: %s\n", e.getMessage());
        }

        bank.summarizeBankAccounts();
    }

    private static void testMultipleThreadsOneAccount() {
        System.out.println("\nSTARTING TEST CASE 2\n--------------------");

        // Instantiate Bank
        Bank bank = new Bank();

        // Bank's Accounts
        BankAccount bankAccount = bank.addBankAccount("ACC-0001", Arrays.asList(customer1), AccountType.NORMAL, 0);

        Thread depositorThread1 = new Thread(new DepositorClient(bankAccount, 1000));
        Thread depositorThread2 = new Thread(new DepositorClient(bankAccount, 500));
        Thread depositorThread3 = new Thread(new DepositorClient(bankAccount, 400));

        Thread withdrawerThread1 = new Thread(new WithdrawerClient(bankAccount, 500, customer1));
        Thread withdrawerThread2 = new Thread(new WithdrawerClient(bankAccount, 300, customer2)); // Illegal Access
        Thread withdrawerThread3 = new Thread(new WithdrawerClient(bankAccount, 1000, customer1));

        List<Thread> threads = new ArrayList<>();

        Collections.addAll(threads, depositorThread1, depositorThread2, depositorThread3, withdrawerThread1,
                withdrawerThread2, withdrawerThread3);

        threads.forEach(thread -> thread.start());

        // Wait till threads die
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("[ERRORED] Error occurred during execution. Error: %s\n", e.getMessage());
            }
        });

        bank.summarizeBankAccounts();
    }

    private static void testThreadGroupsWithPriorityOneAccount() {
        System.out.println("\nSTARTING TEST CASE 3\n--------------------");

        // Instantiate Bank
        Bank bank = new Bank();

        // Bank's Accounts
        BankAccount bankAccount = bank.addBankAccount("ACC-0001", Arrays.asList(customer1), AccountType.NORMAL, 1000);

        ThreadGroup priority = new ThreadGroup("Priority");
        priority.setMaxPriority(5);

        ThreadGroup normal = new ThreadGroup("Normal");
        normal.setMaxPriority(2);

        // Deposits are prioritized
        Thread depositorThread1 = new Thread(priority, new DepositorClient(bankAccount, 1000));
        Thread depositorThread2 = new Thread(priority, new DepositorClient(bankAccount, 300));

        Thread interestCreditorManagerThread = new Thread(new InterestCreditorManager(bank));
        interestCreditorManagerThread.setPriority(10); // Maximum priority

        Thread withdrawerThread1 = new Thread(normal, new WithdrawerClient(bankAccount, 800, customer1));
        Thread withdrawerThread2 = new Thread(normal, new WithdrawerClient(bankAccount, 500, customer1));
        Thread withdrawerThread3 = new Thread(normal, new WithdrawerClient(bankAccount, 100, customer1));

        List<Thread> threads = new ArrayList<>();

        Collections.addAll(threads, interestCreditorManagerThread, depositorThread1, depositorThread2,
                withdrawerThread1, withdrawerThread2, withdrawerThread3);

        threads.forEach(thread -> thread.start());

        // Wait till threads die
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("[ERRORED] Error occurred during execution. Error: %s\n", e.getMessage());
            }
        });

        bank.summarizeBankAccounts();
    }

    private static void testMultipleThreadsMultipleAccounts() {
        System.out.println("\nSTARTING TEST CASE 4\n--------------------");

        // Instantiate Bank
        Bank bank = new Bank();

        // Bank's Accounts
        BankAccount bankAccount1 = bank.addBankAccount("ACC-0001", Arrays.asList(customer1), AccountType.NORMAL, 100);
        BankAccount bankAccount2 = bank.addBankAccount("ACC-0002", Arrays.asList(customer2, customer3),
                AccountType.NORMAL, 0);
        BankAccount bankAccount3 = bank.addBankAccount("ACC-0003", Arrays.asList(customer3), AccountType.EXPRESS, 500);

        ThreadGroup priority = new ThreadGroup("Priority");
        priority.setMaxPriority(5);

        ThreadGroup normal = new ThreadGroup("Normal");
        normal.setMaxPriority(2);

        List<Thread> threads = new ArrayList<>();

        Thread interestCreditorManagerThread = new Thread(new InterestCreditorManager(bank));
        interestCreditorManagerThread.setPriority(10); // Maximum priority

        threads.add(interestCreditorManagerThread);

        threads.add(new Thread(normal, new DepositorClient(bankAccount1, 1000)));
        threads.add(new Thread(normal, new DepositorClient(bankAccount2, 300)));
        threads.add(new Thread(priority, new DepositorClient(bankAccount3, 200)));
        threads.add(new Thread(normal, new DepositorClient(bankAccount1, 500)));
        threads.add(new Thread(priority, new DepositorClient(bankAccount3, 400)));
        threads.add(new Thread(normal, new DepositorClient(bankAccount2, 2000)));

        threads.add(new Thread(normal, new WithdrawerClient(bankAccount1, 800, customer1)));
        threads.add(new Thread(normal, new WithdrawerClient(bankAccount2, 300, customer2)));
        threads.add(new Thread(priority, new WithdrawerClient(bankAccount3, 400, customer3)));
        threads.add(new Thread(priority, new WithdrawerClient(bankAccount3, 1200, customer2)));
        threads.add(new Thread(normal, new WithdrawerClient(bankAccount1, 200, customer1)));
        threads.add(new Thread(normal, new WithdrawerClient(bankAccount3, 1200, customer3)));
        threads.add(new Thread(normal, new WithdrawerClient(bankAccount2, 700, customer2)));

        threads.forEach(thread -> thread.start());

        // Wait till threads die
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.printf("[ERRORED] Error occurred during execution. Error: %s\n", e.getMessage());
            }
        });

        bank.summarizeBankAccounts();
    }

    public static void main(String[] args) {
        testTwoThreadsOneAccount();
        testMultipleThreadsOneAccount();
        testThreadGroupsWithPriorityOneAccount();
        testMultipleThreadsMultipleAccounts();
    }
}
