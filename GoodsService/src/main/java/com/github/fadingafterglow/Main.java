package com.github.fadingafterglow;

import com.github.fadingafterglow.database.context.PersistenceContext;
import com.github.fadingafterglow.database.migration.DefaultMigrationRunner;
import com.github.fadingafterglow.dto.UpdateGoods;
import com.github.fadingafterglow.filter.GoodsFilter;
import com.github.fadingafterglow.repository.GoodsRepository;
import com.github.fadingafterglow.service.GoodsService;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Properties;

public class Main {

    private static final String PROPERTIES_FILE = "/db-connection.properties";

    public static void main(String[] args) {
        PersistenceContext.init(loadProperties());
        new DefaultMigrationRunner().runMigrations();
        GoodsService goodsService = new GoodsService(new GoodsRepository());

        UpdateGoods goods = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = goodsService.create(goods);
        System.out.println(goodsService.findById(id));

        goods.setQuantity(1);
        goods.setPrice(BigDecimal.valueOf(85));
        goodsService.update(id, goods);
        System.out.println(goodsService.findById(id));

        GoodsFilter filter = GoodsFilter.builder()
                .category("Bakery")
                .minPrice(BigDecimal.valueOf(50))
                .maxQuantity(3)
                .build();
        System.out.println(goodsService.findByFilter(filter));

        goodsService.delete(id);
    }

    @SneakyThrows
    private static Properties loadProperties() {
        Properties properties = new Properties();
        properties.load(PersistenceContext.class.getResourceAsStream(PROPERTIES_FILE));
        return properties;
    }
}
