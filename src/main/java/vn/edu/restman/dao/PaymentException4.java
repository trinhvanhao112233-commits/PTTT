package vn.edu.restman.dao;

public class PaymentException4 extends RuntimeException {
    public enum Reason {
        NOT_FOUND,
        NOT_OPEN,
        EMPTY_BILL,
        ALREADY_CONFIRMED,
        INVALID_STAFF
    }

    private final Reason reason;

    public PaymentException4(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }
}
