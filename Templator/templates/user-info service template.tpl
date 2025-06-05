package ua.edu.ukma.fin.userinfo.services.[:main-l-sd:];

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.fin.userinfo.criteria.Criteria;
import ua.edu.ukma.fin.userinfo.criteria.[:main-h-ns:]Criteria;
import ua.edu.ukma.fin.userinfo.domain.entity.[:main-h-ns:]Entity;
import ua.edu.ukma.fin.userinfo.exception.BaseException;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]ListResponse;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]Response;
import ua.edu.ukma.fin.userinfo.model.[:main-l-sd:].[:main-h-ns:]View;
import ua.edu.ukma.fin.userinfo.services.OwnedEntityService;

@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = BaseException.class)
public class [:main-h-ns:]Service extends OwnedEntityService<[:main-h-ns:]Entity, [:main-h-ns:]View,
        [:main-h-ns:]Response, [:main-h-ns:]ListResponse, Integer> {

    public [:main-h-ns:]Service() {
        super([:main-h-ns:]Entity.class, [:main-h-ns:]Entity::new);
    }

    @Override
    public Criteria<[:main-h-ns:]Entity> parse(String restrict) {
        return new [:main-h-ns:]Criteria(restrict);
    }
}