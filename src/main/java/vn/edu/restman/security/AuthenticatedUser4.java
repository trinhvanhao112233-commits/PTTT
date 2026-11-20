package vn.edu.restman.security;

public record AuthenticatedUser4(int userId, Integer staffId, String fullName, String username, String role) {
    public boolean isCustomer() { return "CUSTOMER".equals(role); }
    public boolean isSalesStaff() { return "SALES_STAFF".equals(role); }
}
