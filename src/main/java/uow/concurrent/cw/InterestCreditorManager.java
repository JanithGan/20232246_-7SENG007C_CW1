package uow.concurrent.cw;

public class InterestCreditorManager implements Runnable {
    private Bank bank;

    public InterestCreditorManager(Bank bank) {
        super();
        this.bank = bank;
    }

    @Override
    public void run() {
        try {
            bank.addInterestToBankAccounts();
            System.out.println("[SUCCESS] Interest added to all bank accounts.");
        } catch (Exception e) {
            System.out.printf("[ERRORED] Adding interest to bank accounts failed. Error: %s\n", e.getMessage());
        }
    }
}
