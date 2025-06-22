package com.github.fadingafterglow.service;

import com.github.fadingafterglow.BaseTest;
import com.github.fadingafterglow.dto.GoodsList;
import com.github.fadingafterglow.dto.UpdateGoods;
import com.github.fadingafterglow.entity.GoodsEntity;
import com.github.fadingafterglow.exception.ValidationException;
import com.github.fadingafterglow.filter.GoodsFilter;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class GoodsServiceTest extends BaseTest {

    private final GoodsService service;

    GoodsServiceTest() {
        this.service = new GoodsService(goodsRepository);
    }

    @Test
    void ifDataIsValid_shouldCreateGoodsAndReturnId() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(dto);
        assertEntity(id, dto);
    }

    @ParameterizedTest
    @MethodSource("invalidGoods")
    void ifDataIsInvalid_shouldThrowExceptionAndNotCreateGoods(UpdateGoods dto) {
        assertThrows(ValidationException.class, () -> service.create(dto));
        assertCount(0);
    }

    @Test
    void ifNameIsNotUnique_shouldThrowExceptionAndNotCreateGoods() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        service.create(dto);
        assertThrows(ValidationException.class, () -> service.create(dto));
        assertCount(1);
    }

    @Test
    void ifGoodsExists_shouldFindByIdAndReturnGoods() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(dto);
        assertEntity(service.findById(id), dto);
    }

    @Test
    void ifGoodsDoesNotExist_shouldNotFindByIdAndThrowException() {
        final int id = -1;
        Assumptions.assumeFalse(exists(id));
        assertThrows(NoSuchElementException.class, () -> service.findById(id));
    }

    @Test
    void givenFiler_shouldReturnCorrectGoods() {
        Assumptions.assumeTrue(transactionDelegate.runInTransaction(() -> goodsRepository.countAll() == 0));
        Integer[] ids = createGoodsForFilterTest();

        GoodsFilter filter = GoodsFilter.builder()
                .build();
        assertList(service.findByFilter(filter), 4, ids[0], ids[1], ids[2], ids[3]);

        filter = GoodsFilter.builder()
                .name("nonexistent")
                .build();
        assertList(service.findByFilter(filter), 0);

        filter = GoodsFilter.builder()
                .name("mI")
                .build();
        assertList(service.findByFilter(filter), 2, ids[2], ids[3]);

        filter = GoodsFilter.builder()
                .category("KeR")
                .build();
        assertList(service.findByFilter(filter), 2, ids[0], ids[1]);

        filter = GoodsFilter.builder()
                .minPrice(BigDecimal.valueOf(45))
                .build();
        assertList(service.findByFilter(filter), 3, ids[0], ids[1], ids[2]);

        filter = GoodsFilter.builder()
                .maxPrice(BigDecimal.valueOf(45))
                .build();
        assertList(service.findByFilter(filter), 1, ids[3]);

        filter = GoodsFilter.builder()
                .minPrice(BigDecimal.valueOf(45))
                .maxPrice(BigDecimal.valueOf(80))
                .build();
        assertList(service.findByFilter(filter), 1, ids[2]);

        filter = GoodsFilter.builder()
                .minQuantity(5)
                .build();
        assertList(service.findByFilter(filter), 3, ids[0], ids[2], ids[3]);

        filter = GoodsFilter.builder()
                .maxQuantity(5)
                .build();
        assertList(service.findByFilter(filter), 2, ids[0], ids[1]);

        filter = GoodsFilter.builder()
                .minQuantity(7)
                .maxQuantity(50)
                .build();
        assertList(service.findByFilter(filter), 1, ids[2]);

        filter = GoodsFilter.builder()
                .name("br")
                .category("Ba")
                .build();
        assertList(service.findByFilter(filter), 1, ids[0]);

        filter = GoodsFilter.builder()
                .name("i")
                .category("y")
                .maxPrice(BigDecimal.valueOf(70))
                .build();
        assertList(service.findByFilter(filter), 1, ids[2]);

        filter = GoodsFilter.builder()
                .name("i")
                .category("y")
                .maxQuantity(5)
                .build();
        assertList(service.findByFilter(filter), 1, ids[1]);

        filter = GoodsFilter.builder()
                .size(2)
                .build();
        assertList(service.findByFilter(filter), 4, ids[0], ids[1]);

        filter = GoodsFilter.builder()
                .size(2)
                .page(1)
                .build();
        assertList(service.findByFilter(filter), 4, ids[2], ids[3]);

        filter = GoodsFilter.builder()
                .size(2)
                .page(5)
                .build();
        assertList(service.findByFilter(filter), 4);

        filter = GoodsFilter.builder()
                .size(2)
                .descendingOrder(true)
                .build();
        assertList(service.findByFilter(filter), 4, ids[3], ids[2]);

        filter = GoodsFilter.builder()
                .sortBy("quantity")
                .build();
        assertList(service.findByFilter(filter), 4, ids[1], ids[0], ids[2], ids[3]);

        filter = GoodsFilter.builder()
                .category("bakery")
                .size(1)
                .sortBy("name")
                .descendingOrder(true)
                .build();
        assertList(service.findByFilter(filter), 2, ids[0]);

        filter = GoodsFilter.builder()
                .category("y")
                .minQuantity(3)
                .size(1)
                .page(1)
                .sortBy("name")
                .build();
        assertList(service.findByFilter(filter), 2, ids[2]);

        filter = GoodsFilter.builder()
                .category("bakery")
                .size(1)
                .page(1)
                .sortBy("name")
                .descendingOrder(false)
                .build();
        assertList(service.findByFilter(filter), 2, ids[0]);
    }

    @Test
    void ifDataIsValid_shouldUpdateGoodsAndReturnTrue() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(dto);
        dto = UpdateGoods.builder()
                .name("Biscuits")
                .category("Food")
                .price(BigDecimal.valueOf(85))
                .quantity(1)
                .build();
        assertTrue(service.update(id, dto));
        assertEntity(id, dto);
    }

    @ParameterizedTest
    @MethodSource("invalidGoods")
    void ifDataIsInvalid_shouldThrowExceptionAndNotUpdateGoods(UpdateGoods dto) {
        UpdateGoods validDto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(validDto);
        assertThrows(ValidationException.class, () -> service.update(id, dto));
        assertEntity(id, validDto);
    }

    @Test
    void ifNameIsNotChanged_shouldUpdateGoods() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(dto);
        dto.setQuantity(100);
        assertTrue(service.update(id, dto));
        assertEntity(id, dto);
    }

    @Test
    void ifNameIsChangedToExisting_shouldThrowExceptionAndNotUpdateGoods() {
        UpdateGoods dto1 = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        service.create(dto1);
        UpdateGoods dto2 = UpdateGoods.builder()
                .name("Biscuits")
                .category("Food")
                .price(BigDecimal.valueOf(85))
                .quantity(1)
                .build();
        int id2 = service.create(dto2);
        assertThrows(ValidationException.class, () -> service.update(id2, dto1));
        assertEntity(id2, dto2);
    }

    @Test
    void ifGoodsDoesNotExist_shouldThrowExceptionAndNotUpdate() {
        final int id = -1;
        Assumptions.assumeFalse(exists(id));
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        assertThrows(NoSuchElementException.class, () -> service.update(id, dto));
        assertCount(0);
    }

    @Test
    void ifGoodsExists_shouldDeleteGoodsAndReturnTrue() {
        UpdateGoods dto = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        int id = service.create(dto);
        assertTrue(service.delete(id));
        assertCount(0);
    }

    @Test
    void ifGoodsDoesNotExist_shouldNotDeleteAndReturnFalse() {
        final int id = -1;
        Assumptions.assumeFalse(exists(id));
        assertFalse(service.delete(id));
        assertCount(0);
    }

    private static List<UpdateGoods> invalidGoods() {
        return List.of(
            UpdateGoods.builder().name("    ").category("Bakery").price(BigDecimal.valueOf(100)).quantity(5).build(),
            UpdateGoods.builder().name("Bread").category(null).price(BigDecimal.valueOf(100)).quantity(5).build(),
            UpdateGoods.builder().name("Bread").category("Bakery").price(BigDecimal.valueOf(-100)).quantity(5).build(),
            UpdateGoods.builder().name("Bread").category("Bakery").price(null).quantity(5).build(),
            UpdateGoods.builder().name("Bread").category("Bakery").price(BigDecimal.valueOf(100)).quantity(-5).build()
        );
    }

    private Integer[] createGoodsForFilterTest() {
        List<Integer> ids = new ArrayList<>();
        UpdateGoods dto1 = UpdateGoods.builder()
                .name("Bread")
                .category("Bakery")
                .price(BigDecimal.valueOf(99.99))
                .quantity(5)
                .build();
        ids.add(service.create(dto1));
        UpdateGoods dto2 = UpdateGoods.builder()
                .name("Biscuits")
                .category("Bakery")
                .price(BigDecimal.valueOf(85))
                .quantity(1)
                .build();
        ids.add(service.create(dto2));
        UpdateGoods dto3 = UpdateGoods.builder()
                .name("Milk")
                .category("Dairy")
                .price(BigDecimal.valueOf(50))
                .quantity(10)
                .build();
        ids.add(service.create(dto3));
        UpdateGoods dto4 = UpdateGoods.builder()
                .name("Mint")
                .category("Flowers")
                .price(BigDecimal.valueOf(12.5))
                .quantity(100)
                .build();
        ids.add(service.create(dto4));
        return ids.toArray(new Integer[0]);
    }

    private boolean exists(int id) {
        return transactionDelegate.runInTransaction(() -> goodsRepository.getGoodsById(id).isPresent());
    }

    private void assertEntity(int id, UpdateGoods dto) {
        GoodsEntity entity = transactionDelegate.runInTransaction(() -> goodsRepository.getGoodsById(id).orElseThrow());
        assertEntity(entity, dto);
    }

    private void assertEntity(GoodsEntity entity, UpdateGoods dto) {
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getCategory(), entity.getCategory());
        assertEquals(0, dto.getPrice().compareTo(entity.getPrice()));
        assertEquals(dto.getQuantity(), entity.getQuantity());
    }

    private void assertCount(long count) {
        assertEquals(count, transactionDelegate.runInTransaction(goodsRepository::countAll));
    }

    private void assertList(GoodsList list, long expectedTotal, Integer... expectedIds) {
        assertEquals(expectedTotal, list.getTotal());
        assertThat(list.getItems())
                .extracting(GoodsEntity::getId)
                .containsExactly(expectedIds);
    }

    private void assertList(GoodsList list, long expectedTotal) {
        assertEquals(expectedTotal, list.getTotal());
        assertThat(list.getItems()).isEmpty();
    }
}
