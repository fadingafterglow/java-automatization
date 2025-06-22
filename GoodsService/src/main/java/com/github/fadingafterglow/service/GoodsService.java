package com.github.fadingafterglow.service;

import com.github.fadingafterglow.database.transaction.TransactionDelegate;
import com.github.fadingafterglow.dto.GoodsList;
import com.github.fadingafterglow.dto.UpdateGoods;
import com.github.fadingafterglow.entity.GoodsEntity;
import com.github.fadingafterglow.exception.ValidationException;
import com.github.fadingafterglow.filter.GoodsFilter;
import com.github.fadingafterglow.repository.GoodsRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class GoodsService {

    private final GoodsRepository repository;
    private final TransactionDelegate transactionDelegate;
    private final TransactionDelegate readOnlyTransactionDelegate;

    public GoodsService(GoodsRepository repository) {
        this.repository = repository;
        this.transactionDelegate = new TransactionDelegate();
        this.readOnlyTransactionDelegate = new TransactionDelegate(true);
    }

    public GoodsEntity findById(int id) {
        return readOnlyTransactionDelegate.runInTransaction(() -> getOrThrow(id));
    }

    public GoodsList findByFilter(GoodsFilter filter) {
        return readOnlyTransactionDelegate.runInTransaction(() -> {
            List<GoodsEntity> goods = repository.getGoodsByFilter(filter);
            long total = repository.countGoodsByFilter(filter);
            return new GoodsList(total, goods);
        });
    }

    public int create(UpdateGoods dto) {
        return transactionDelegate.runInTransaction(() -> {
            GoodsEntity goodsEntity = new GoodsEntity();
            merge(goodsEntity, dto);
            validateData(goodsEntity);
            return repository.createGoods(goodsEntity);
        });
    }

    public boolean update(int id, UpdateGoods dto) {
        return transactionDelegate.runInTransaction(() -> {
            GoodsEntity goodsEntity = getOrThrow(id);
            merge(goodsEntity, dto);
            validateData(goodsEntity);
            return repository.updateGoods(goodsEntity);
        });
    }

    public boolean delete(int id) {
        return transactionDelegate.runInTransaction(() -> repository.deleteGoods(id));
    }

    private GoodsEntity getOrThrow(int id) {
        return repository.getGoodsById(id).orElseThrow(NoSuchElementException::new);
    }

    private void merge(GoodsEntity goodsEntity, UpdateGoods dto) {
        goodsEntity.setName(dto.getName());
        goodsEntity.setCategory(dto.getCategory());
        goodsEntity.setQuantity(dto.getQuantity());
        goodsEntity.setPrice(dto.getPrice());
    }

    private void validateData(GoodsEntity entity) {
        if (entity.getName() == null || entity.getName().isBlank())
            throw new ValidationException("Goods name cannot be blank");
        if (entity.getName().length() > 255)
            throw new ValidationException("Goods name cannot exceed 255 characters");
        if (entity.getCategory() == null || entity.getCategory().isBlank())
            throw new ValidationException("Goods category cannot be blank");
        if (entity.getCategory().length() > 100)
            throw new ValidationException("Goods category cannot exceed 100 characters");
        if (entity.getQuantity() < 0)
            throw new ValidationException("Goods quantity cannot be negative");
        if (entity.getPrice() == null)
            throw new ValidationException("Goods price must be specified");
        if (entity.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("Goods price cannot be negative");

        boolean duplicateName = repository.findIdByName(entity.getName())
                .map(id -> !id.equals(entity.getId()))
                .orElse(false);
        if (duplicateName)
            throw new ValidationException("Goods with this name already exists");
    }
}
