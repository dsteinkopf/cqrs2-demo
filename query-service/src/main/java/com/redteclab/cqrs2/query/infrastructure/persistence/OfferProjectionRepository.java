package com.redteclab.cqrs2.query.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferProjectionRepository extends JpaRepository<OfferProjectionEntity, String> {
}
