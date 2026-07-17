package com.spaza.connect.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDTO {

    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotNull(message = "Single unit price is required")
    @DecimalMin(value = "0.01", message = "Single unit price must be greater than zero")
    private BigDecimal singleUnitPrice;

    @NotNull(message = "Bulk unit price is required")
    @DecimalMin(value = "0.01", message = "Bulk unit price must be greater than zero")
    private BigDecimal bulkUnitPrice;

    @NotNull(message = "Bulk threshold quantity is required")
    @Min(value = 1, message = "Bulk threshold quantity must be at least 1")
    private Integer bulkThresholdQty;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getSingleUnitPrice() { return singleUnitPrice; }
    public void setSingleUnitPrice(BigDecimal singleUnitPrice) { this.singleUnitPrice = singleUnitPrice; }

    public BigDecimal getBulkUnitPrice() { return bulkUnitPrice; }
    public void setBulkUnitPrice(BigDecimal bulkUnitPrice) { this.bulkUnitPrice = bulkUnitPrice; }

    public Integer getBulkThresholdQty() { return bulkThresholdQty; }
    public void setBulkThresholdQty(Integer bulkThresholdQty) { this.bulkThresholdQty = bulkThresholdQty; }
}
