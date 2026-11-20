package vn.edu.restman.model;

import java.time.LocalDateTime;

public class TableReservation4 {
    private int id;
    private LocalDateTime expectedArrivalTime;
    private int guestCount;
    private String status;
    private Customer4 customer;
    private SalesStaff4 confirmedBy;

    public TableReservation4() { }

    public TableReservation4(int id, LocalDateTime expectedArrivalTime, int guestCount,
                             String status, Customer4 customer, SalesStaff4 confirmedBy) {
        this.id = id;
        this.expectedArrivalTime = expectedArrivalTime;
        this.guestCount = guestCount;
        this.status = status;
        this.customer = customer;
        this.confirmedBy = confirmedBy;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getExpectedArrivalTime() { return expectedArrivalTime; }
    public void setExpectedArrivalTime(LocalDateTime expectedArrivalTime) { this.expectedArrivalTime = expectedArrivalTime; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Customer4 getCustomer() { return customer; }
    public void setCustomer(Customer4 customer) { this.customer = customer; }
    public SalesStaff4 getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(SalesStaff4 confirmedBy) { this.confirmedBy = confirmedBy; }
}
