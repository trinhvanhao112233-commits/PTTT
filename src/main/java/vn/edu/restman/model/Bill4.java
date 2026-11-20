package vn.edu.restman.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bill4 {
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private String status;
    private LocalDateTime confirmedAt;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private TableVisit4 visit;
    private SalesStaff4 confirmedBy;
    private List<BillItem4> items = new ArrayList<>();

    public Bill4() { }

    public Bill4(int id, String status, LocalDateTime confirmedAt, BigDecimal totalAmount,
                 TableVisit4 visit, SalesStaff4 confirmedBy, List<BillItem4> items) {
        this.id = id;
        this.status = status;
        this.confirmedAt = confirmedAt;
        this.totalAmount = totalAmount == null ? BigDecimal.ZERO : totalAmount;
        this.visit = visit;
        this.confirmedBy = confirmedBy;
        this.items = items == null ? new ArrayList<>() : new ArrayList<>(items);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(LocalDateTime confirmedAt) { this.confirmedAt = confirmedAt; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public TableVisit4 getVisit() { return visit; }
    public void setVisit(TableVisit4 visit) { this.visit = visit; }
    public SalesStaff4 getConfirmedBy() { return confirmedBy; }
    public void setConfirmedBy(SalesStaff4 confirmedBy) { this.confirmedBy = confirmedBy; }
    public List<BillItem4> getItems() { return items; }
    public void setItems(List<BillItem4> items) { this.items = items == null ? new ArrayList<>() : new ArrayList<>(items); }

    public String getCustomerName() { return visit == null ? "" : visit.getCustomerName(); }
    public String getTableCode() {
        return visit == null || visit.getTable() == null ? "" : visit.getTable().getTableCode();
    }
    public String getConfirmedAtDisplay() {
        return confirmedAt == null ? "" : confirmedAt.format(DATE_TIME_FORMAT);
    }
    public boolean isPaid() { return confirmedAt != null || "Đã thanh toán".equals(status); }
}
