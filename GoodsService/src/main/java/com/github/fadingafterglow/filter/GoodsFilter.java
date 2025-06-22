package com.github.fadingafterglow.filter;

import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@SuperBuilder
public class GoodsFilter extends BaseFilter {

    private String name;
    private String category;
    private Integer minQuantity;
    private Integer maxQuantity;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

    @Override
    protected List<String> formWhereConditions(Map<String, String> fieldExpressionMap) {
        return new ConditionsBuilder()
                .like(name, fieldExpressionMap.get("name"))
                .like(category, fieldExpressionMap.get("category"))
                .min(minQuantity, fieldExpressionMap.get("quantity"))
                .max(maxQuantity, fieldExpressionMap.get("quantity"))
                .min(minPrice, fieldExpressionMap.get("price"))
                .max(maxPrice, fieldExpressionMap.get("price"))
                .getConditions();
    }

    @Override
    public void setParameters(PreparedStatement st, int parametersIndexOffset) {
        new ParametersSetter(st, parametersIndexOffset)
                .setLikeString(name)
                .setLikeString(category)
                .setInt(minQuantity)
                .setInt(maxQuantity)
                .setBigDecimal(minPrice)
                .setBigDecimal(maxPrice);
    }
}
