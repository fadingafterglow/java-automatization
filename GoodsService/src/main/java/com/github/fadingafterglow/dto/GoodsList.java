package com.github.fadingafterglow.dto;

import com.github.fadingafterglow.entity.GoodsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GoodsList {

    private long total;
    private List<GoodsEntity> items;
}
