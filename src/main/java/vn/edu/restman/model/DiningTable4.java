package vn.edu.restman.model;

public class DiningTable4 {
    private int id;
    private String tableCode;
    private String area;
    private int capacity;
    private String status;

    public DiningTable4() { }

    public DiningTable4(int id, String tableCode, String area, int capacity, String status) {
        this.id = id;
        this.tableCode = tableCode;
        this.area = area;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTableCode() { return tableCode; }
    public void setTableCode(String tableCode) { this.tableCode = tableCode; }
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
