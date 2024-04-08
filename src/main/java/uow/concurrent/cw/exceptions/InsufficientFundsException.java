package uow.concurrent.cw.exceptions;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Insufficient Funds Available");
    }
}
