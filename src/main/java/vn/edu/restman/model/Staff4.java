package vn.edu.restman.model;

public class Staff4 extends User4 {
    private String staffCode;

    public Staff4() { }

    public Staff4(int id, String fullName, String username, String passwordHash,
                  String phone, String email, String staffCode) {
        super(id, fullName, username, passwordHash, phone, email);
        this.staffCode = staffCode;
    }

    public String getStaffCode() { return staffCode; }
    public void setStaffCode(String staffCode) { this.staffCode = staffCode; }
}
