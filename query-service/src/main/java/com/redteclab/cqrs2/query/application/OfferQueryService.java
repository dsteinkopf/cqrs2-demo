package com.redteclab.cqrs2.query.application;

import com.redteclab.cqrs2.query.infrastructure.persistence.OfferProjectionEntity;
import com.redteclab.cqrs2.query.infrastructure.persistence.OfferProjectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OfferQueryService {

    private final OfferProjectionRepository repository;

    public OfferQueryService(OfferProjectionRepository repository) {
        this.repository = repository;
    }

    public List<OfferProjectionEntity> findAll() {
        return repository.findAll();
    }

    public Optional<OfferProjectionEntity> findById(String offerId) {
        return repository.findById(offerId);
    }
}
