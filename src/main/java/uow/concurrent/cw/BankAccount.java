package uow.concurrent.cw;

import uow.concurrent.cw.exceptions.InsufficientFundsException;

import java.util.List;

/**
 * Represents the Bank Account
 */
public class BankAccount {
    private String accountNumber;
    private List<Customer> accountHolders;
    private AccountType accountType;
    private double balance;
    private static final double MONTHLY_INTEREST_RATE = 0.005;

    BankAccount(String accountNumber, List<Customer> accountHolders, AccountType accountType, double balance) {
        this.accountNumber = accountNumber;
        this.accountHolders = accountHolders;
        this.accountType = accountType;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public List<Customer> getAccountHolders() {
        return accountHolders;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void withdraw(double amount) throws InsufficientFundsException {
        if (this.balance >= amount) {
            this.balance -= amount;
        } else {
            throw new InsufficientFundsException();
        }
    }

    public synchronized void deposit(double amount) {
        this.balance += amount;
    }

    public synchronized double addInterest() throws InsufficientFundsException {
        if (balance > 0) {
            double interest = balance * MONTHLY_INTEREST_RATE;
            balance = balance + interest;
            return interest;
        } else {
            throw new InsufficientFundsException();
        }
    }

    public boolean isCustomerHoldsAccount(Customer customer) {
        return this.accountHolders.stream().anyMatch(accountHolder -> accountHolder.getCustomerId() == customer.getCustomerId());
    }

    public void addCustomerToAccount(Customer customer) {
        this.accountHolders.add(customer);
    }

    public void removeCustomerFromAccount(Customer customer) {
        this.accountHolders.removeIf(accountHolder -> accountHolder.getCustomerId() == customer.getCustomerId());
    }
}
