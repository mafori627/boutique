package com.spaza.connect.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PoolOrderDTO {
    @NotNull(message = "Product ID is mandatory")
    private Long productId;

    @Min(value = 1, message = "You must order at least 1 case")
    private Integer quantity;
}
