package uow.concurrent.cw;

/**
 * Thread for depositing to account
 */
public class DepositorClient implements Runnable {
    private BankAccount account;
    private double amount;

    public DepositorClient(BankAccount account, double amount) {
        super();
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            account.deposit(amount);
            System.out.printf("[SUCCESS] LKR %.2f deposited to account: %s. New Account balance is LKR %.2f\n",
                    amount, account.getAccountNumber(), account.getBalance());
        } catch (Exception e) {
            System.out.printf("[ERRORED] Error occurred during depositing to account: %s. Error: %s\n",
                    account.getAccountNumber(), e.getMessage());
        }
    }
}
