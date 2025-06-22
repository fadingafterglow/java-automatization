package com.github.fadingafterglow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEntity {

    private Integer id;
    private String name;
    private String category;
    private int quantity;
    private BigDecimal price;
}
