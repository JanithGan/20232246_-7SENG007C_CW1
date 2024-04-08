package uow.concurrent.cw;

import uow.concurrent.cw.exceptions.InsufficientFundsException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the Bank
 */
public class Bank {
    private Map<String, BankAccount> bankAccounts;

    public Bank() {
        bankAccounts = new HashMap<String, BankAccount>();
    }

    public BankAccount addBankAccount(String accountNumber, List<Customer> accountHolders, AccountType accountType,
                                      double startingBalance) {
        if (!bankAccounts.containsKey(accountNumber)) {
            bankAccounts.put(accountNumber, new BankAccount(accountNumber, accountHolders, accountType,
                    startingBalance));
            System.out.printf("[SUCCESS] New account created with number: %s\n", accountNumber);

        } else {
            System.out.printf("[ERRORED] An account already exists with number: %s\n", accountNumber);
        }

        return bankAccounts.get(accountNumber);
    }

    public void removeBankAccount(String accountNumber) {
        bankAccounts.remove(accountNumber);
    }

    public BankAccount getBankAccount(String accountNumber) {
        return bankAccounts.get(accountNumber);
    }

    public void summarizeBankAccounts() {
        System.out.println("\n[ACCOUNTS SUMMARY]\n------------------");
        System.out.printf("| Account No | Balance (LKR) |\n| ---------- | ------------- |\n");
        for (BankAccount account : bankAccounts.values()) {
            System.out.printf("| %-10s | %13.2f |\n", account.getAccountNumber(), account.getBalance());
        }
    }

    public void addInterestToBankAccounts() {
        for (BankAccount account : bankAccounts.values()) {
            try {
                double interest = account.addInterest();
                System.out.printf("[SUCCESS] Interest of LKR %.2f added to account: %s. New Account balance is LKR %" +
                        ".2f\n", interest, account.getAccountNumber(), account.getBalance());
            } catch (InsufficientFundsException e) {
                System.out.printf("[ERRORED] Account: %s do not have sufficient funds for adding interest\n",
                        account.getAccountNumber());
            }
        }
    }
}
