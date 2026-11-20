package vn.edu.restman.model;

public class Customer4 extends User4 {
    private String customerCode;

    public Customer4() { }

    public Customer4(int id, String fullName, String username, String passwordHash,
                     String phone, String email, String customerCode) {
        super(id, fullName, username, passwordHash, phone, email);
        this.customerCode = customerCode;
    }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }
}
