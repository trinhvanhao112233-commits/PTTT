package vn.edu.restman.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TableVisit4 {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    private int id;
    private LocalDateTime arrivalTime;
    private int guestCount;
    private String status;
    private DiningTable4 table;
    private TableReservation4 reservation;

    public TableVisit4() { }

    public TableVisit4(int id, LocalDateTime arrivalTime, int guestCount, String status,
                       DiningTable4 table, TableReservation4 reservation) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.guestCount = guestCount;
        this.status = status;
        this.table = table;
        this.reservation = reservation;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public int getGuestCount() { return guestCount; }
    public void setGuestCount(int guestCount) { this.guestCount = guestCount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public DiningTable4 getTable() { return table; }
    public void setTable(DiningTable4 table) { this.table = table; }
    public TableReservation4 getReservation() { return reservation; }
    public void setReservation(TableReservation4 reservation) { this.reservation = reservation; }

    public String getArrivalTimeDisplay() {
        return arrivalTime == null ? "" : arrivalTime.format(TIME_FORMAT);
    }

    public String getCustomerName() {
        return reservation == null || reservation.getCustomer() == null ? "" : reservation.getCustomer().getFullName();
    }
}
