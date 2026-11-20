package vn.edu.restman.model;

import java.math.BigDecimal;

public class BillItem4 {
    private int id;
    private Dish4 dish;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineAmount;

    public BillItem4() { }

    public BillItem4(int id, Dish4 dish, int quantity, BigDecimal unitPrice) {
        this(id, dish, quantity, unitPrice,
                unitPrice == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity)));
    }

    public BillItem4(int id, Dish4 dish, int quantity, BigDecimal unitPrice, BigDecimal lineAmount) {
        this.id = id;
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.lineAmount = lineAmount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Dish4 getDish() { return dish; }
    public void setDish(Dish4 dish) { this.dish = dish; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getLineAmount() { return lineAmount; }
    public void setLineAmount(BigDecimal lineAmount) { this.lineAmount = lineAmount; }
}
