package com.redteclab.cqrs2.query.infrastructure.web;

import com.redteclab.cqrs2.query.application.OfferQueryService;
import com.redteclab.cqrs2.query.infrastructure.persistence.OfferProjectionEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferQueryController {

    private final OfferQueryService queryService;

    public OfferQueryController(OfferQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public List<OfferProjectionEntity> findAll() {
        return queryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferProjectionEntity> findById(@PathVariable String id) {
        return queryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
