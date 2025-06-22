package com.github.fadingafterglow.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateGoods {
    private String name;
    private String category;
    private int quantity;
    private BigDecimal price;
}
