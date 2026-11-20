package vn.edu.restman.model;

import java.math.BigDecimal;

public class Dish4 {
    private int id;
    private String dishCode;
    private String name;
    private String category;
    private String unit;
    private BigDecimal salePrice;
    private String servingStatus;
    private String description;
    private String dishType;

    public Dish4() { }

    public Dish4(int id, String dishCode, String name, String category, String unit,
                 BigDecimal salePrice, String servingStatus, String description) {
        this(id, dishCode, name, category, unit, salePrice, servingStatus, description, "Món lẻ");
    }

    public Dish4(int id, String dishCode, String name, String category, String unit,
                 BigDecimal salePrice, String servingStatus, String description, String dishType) {
        this.id = id;
        this.dishCode = dishCode;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.salePrice = salePrice;
        this.servingStatus = servingStatus;
        this.description = description;
        this.dishType = dishType;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDishCode() { return dishCode; }
    public void setDishCode(String dishCode) { this.dishCode = dishCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public String getServingStatus() { return servingStatus; }
    public void setServingStatus(String servingStatus) { this.servingStatus = servingStatus; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDishType() { return dishType; }
    public void setDishType(String dishType) { this.dishType = dishType; }
}
