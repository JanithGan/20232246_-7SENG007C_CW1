package uow.concurrent.cw;

import uow.concurrent.cw.exceptions.IllegalAccessException;
import uow.concurrent.cw.exceptions.InsufficientFundsException;

/**
 * Thread for withdrawing from account
 */
public class WithdrawerClient implements Runnable {
    private BankAccount account;
    private double amount;
    private Customer customer;

    public WithdrawerClient(BankAccount account, double amount, Customer customer) {
        super();
        this.account = account;
        this.amount = amount;
        this.customer = customer;
    }

    @Override
    public void run() {
        try {
            if (account.isCustomerHoldsAccount(customer)) {
                account.withdraw(amount);
                System.out.printf("[SUCCESS] Customer: %s (ID: %s) withdrew LKR %.2f from account: %s. New Account " +
                        "balance is LKR %.2f\n", customer.getFullName(), customer.getCustomerId(), amount,
                        account.getAccountNumber(), account.getBalance());
            } else {
                throw new IllegalAccessException();
            }
        } catch (IllegalAccessException e) {
            System.out.printf("[ERRORED] Customer: %s (ID: %s) do not have access to account: %s\n",
                    customer.getFullName(), customer.getCustomerId(), account.getAccountNumber());
        } catch (InsufficientFundsException e) {
            System.out.printf("[ERRORED] Account: %s do not have sufficient funds for transaction\n",
                    account.getAccountNumber());
        } catch (Exception e) {
            System.out.printf("[ERRORED] Error occurred during withdrawing from account: %s. Error: %s\n",
                    account.getAccountNumber(), e.getMessage());
        }
    }

}
